package com.androidcourse.toktik;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.androidcourse.toktik.player.ProxyServer;
import com.androidcourse.toktik.player.VideoPlayerIJK;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

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