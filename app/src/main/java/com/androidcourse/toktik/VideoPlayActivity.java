package com.androidcourse.toktik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.androidcourse.toktik.player.ProxyServer;

/**
 * 流式视频播放activity
 */
public class VideoPlayActivity extends AppCompatActivity {

    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        viewPager = findViewById(R.id.pager);
        ProxyServer.getProxy(getApplicationContext());
        VideoFragmentStateAdapter videoFragmentStateAdapter = new VideoFragmentStateAdapter(this);
        viewPager.setAdapter(videoFragmentStateAdapter);
        getSupportActionBar().hide();

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(position==0){
                    videoFragmentStateAdapter.prevAddFragment(viewPager,1);
                }
                if(position==videoFragmentStateAdapter.getItemCount()-2){
                    videoFragmentStateAdapter.lastAddFragment(viewPager,position-1);
                }
            }
        });
    }
}