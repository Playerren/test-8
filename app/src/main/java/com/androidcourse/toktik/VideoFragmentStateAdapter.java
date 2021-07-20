package com.androidcourse.toktik;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VideoFragmentStateAdapter extends FragmentStateAdapter {
    private ArrayList<Fragment> videoFragmentList = new ArrayList<>() ;
    private ArrayList<Long> idList = new ArrayList<>();
    private int mapcount = 0;
    private int maxcount = 5;
    private long videoCount = 0;

    public VideoFragmentStateAdapter(@NonNull  FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public VideoFragmentStateAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public VideoFragmentStateAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public void prevAddFragment(Fragment fragment){
        if(fragment!=null){
            videoFragmentList.add(0,fragment);
            if(mapcount<maxcount){
                idList.add(0,videoCount++);
                mapcount++;
            }else{
                videoFragmentList.remove(idList.size()-1);
                idList.remove(idList.size()-1);
                idList.add(0,videoCount++);
            }
            notifyDataSetChanged();
        }
    }


    public void lastAddFragment(Fragment fragment){
        if(fragment!=null){
            videoFragmentList.add(fragment);
            if(mapcount<maxcount){
                idList.add(videoCount++);
                mapcount++;
            }else{
                videoFragmentList.remove(0);
                idList.remove(0);
                idList.add(videoCount++);
            }
            notifyDataSetChanged();
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
