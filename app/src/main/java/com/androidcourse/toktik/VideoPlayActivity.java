package com.androidcourse.toktik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

public class VideoPlayActivity extends AppCompatActivity {

    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        viewPager = findViewById(R.id.pager);
        VideoFragmentStateAdapter videoFragmentStateAdapter = new VideoFragmentStateAdapter(this);
        videoFragmentStateAdapter.lastAddFragment(new VideoFragment());
        videoFragmentStateAdapter.lastAddFragment(new VideoFragment());
        videoFragmentStateAdapter.lastAddFragment(new VideoFragment());
        viewPager.setAdapter(videoFragmentStateAdapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(position==0){
                    videoFragmentStateAdapter.prevAddFragment(new VideoFragment());
                }
                if(position==videoFragmentStateAdapter.getItemCount()-2){
                    videoFragmentStateAdapter.lastAddFragment(new VideoFragment());
                    viewPager.setCurrentItem(position-1,false);
                }
            }
        });
    }
}