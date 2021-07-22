package com.androidcourse.toktik.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.androidcourse.toktik.R;
import com.androidcourse.toktik.player.VideoPlayerIJK;

public class MainActivity extends AppCompatActivity {

    private VideoPlayerIJK player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ProxyServer.getProxy(getApplicationContext()); 启动类需要加上这一行做播放器代理的初始化
        player = findViewById(R.id.player);
        player.setVideoResource(R.raw.bytedance);
        player.startPlay();
    }
}