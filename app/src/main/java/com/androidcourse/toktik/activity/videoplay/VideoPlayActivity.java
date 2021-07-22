package com.androidcourse.toktik.activity.videoplay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.androidcourse.toktik.R;
import com.androidcourse.toktik.activity.recyclervideo.RecyclerVideoActivity;
import com.androidcourse.toktik.util.ProxyServer;

/**
 * 流式视频播放activity
 */
public class VideoPlayActivity extends AppCompatActivity {

    private ViewPager2 viewPager;

    private Button b1;
    private Button b3;

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
                if (position == 0) {
                    videoFragmentStateAdapter.prevAddFragment(viewPager, 1);
                }
                if (position == videoFragmentStateAdapter.getItemCount() - 2) {
                    videoFragmentStateAdapter.lastAddFragment(viewPager, position - 1);
                }
            }
        });

        b1 = findViewById(R.id.vp_b1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(VideoPlayActivity.this, RecyclerVideoActivity.class);
                startActivity(myIntent);
                finish();
            }
        });
        b3 = findViewById(R.id.vp_b3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 跳转到拍摄界面
            }
        });
    }
}