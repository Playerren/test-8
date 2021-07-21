package com.androidcourse.toktik;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.androidcourse.toktik.entity.Video;
import com.androidcourse.toktik.player.VideoSourceLoadFinishedCallback;
import com.androidcourse.toktik.player.VideoSourceProvider;

import java.util.ArrayList;

/**
 * viewpager2 adapter
 */
public class VideoFragmentStateAdapter extends FragmentStateAdapter {
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<Fragment> videoFragmentList = new ArrayList<>() ;
    private ArrayList<Long> idList = new ArrayList<>();
    private int mapcount = 0;
    private int maxcount = 5;
    private long videoCount = 0;

    public VideoFragmentStateAdapter(@NonNull  FragmentActivity fragmentActivity, SwipeRefreshLayout swipeRefreshLayout) {
        super(fragmentActivity);
        this.swipeRefreshLayout = swipeRefreshLayout;
        swipeRefreshLayout.setRefreshing(true);
        init();
    }


    public VideoFragmentStateAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public VideoFragmentStateAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public void flush(){
        VideoSourceProvider.getINSTANCE(null).flush();
        swipeRefreshLayout.setRefreshing(true);
        mapcount = 0;
        maxcount = 5;
        videoCount = 0;
        videoFragmentList.clear();
        idList.clear();
        notifyDataSetChanged();
        init();
    }

    public void init(){
        for(int i=0;i<3;i++){
            VideoSourceProvider.getINSTANCE(null).endAcquire(new VideoSourceLoadFinishedCallback() {
                @Override
                public void onVideoSourceLoadFinished(Video video) {
                    if(video!=null){
                        Fragment fragment = new VideoFragment(video);
                        lastAdd(fragment);
                    }
                    notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }

    public void prevAddFragment(ViewPager2 vi, int item){
        VideoSourceProvider.getINSTANCE(vi.getContext()).prevAcquire(new VideoSourceLoadFinishedCallback() {
            @Override
            public void onVideoSourceLoadFinished(Video video) {
                if(video!=null){
                    Fragment fragment = new VideoFragment(video);
                    prevAdd(fragment);
                    if(vi!=null){
                        vi.setCurrentItem(item,false);
                    }
                    notifyDataSetChanged();
                }
            }
        });


    }

    private void prevAdd(Fragment fragment){
        videoFragmentList.add(0,fragment);
        if(mapcount<maxcount){
            idList.add(0,videoCount++);
            mapcount++;
        }else{
            videoFragmentList.remove(idList.size()-1);
            idList.remove(idList.size()-1);
            idList.add(0,videoCount++);
        }
    }

    public void lastAddFragment( ViewPager2 vi, int item){
        VideoSourceProvider.getINSTANCE(vi.getContext()).endAcquire(new VideoSourceLoadFinishedCallback() {
            @Override
            public void onVideoSourceLoadFinished(Video video) {
                if(video!=null){
                    Fragment fragment = new VideoFragment(video);
                    lastAdd(fragment);
                    if(vi!=null){
                        vi.setCurrentItem(item,false);
                    }
                    notifyDataSetChanged();
                }
            }
        });
    }

    private void lastAdd(Fragment fragment){
        videoFragmentList.add(fragment);
        if(mapcount<maxcount){
            idList.add(videoCount++);
            mapcount++;
        }else{
            videoFragmentList.remove(0);
            idList.remove(0);
            idList.add(videoCount++);
        }
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return videoFragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return videoFragmentList.size();
    }

    @Override
    public long getItemId(int position) {
        return idList.get(position);
    }

    @Override
    public boolean containsItem(long itemId) {
        for(int i=0;i<idList.size();i++){
            if(idList.get(i)==itemId)return true;
        }
        return false;
    }
}
