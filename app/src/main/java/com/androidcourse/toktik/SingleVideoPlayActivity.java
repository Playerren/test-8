package com.androidcourse.toktik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.androidcourse.toktik.entity.Video;

/**
 * 单视频播放页面(Activity)，需要以intent形式传入6个字段
 * String avatar       name:"avatar"
 * String feedUrl      name:"feedUrl"
 * String description  name:"description"
 * int likeCount       name:"likeCount" default 0
 * String thumbnails   name:"thumbnails"
 * String nickname     name:"nickname"
 */
public class SingleVideoPlayActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_video_play);
        Intent intent = getIntent();
        String avatar = intent.getStringExtra("avatar");
        String feedUrl = intent.getStringExtra("feedUrl");
        String description = intent.getStringExtra("description");
        int likeCount = intent.getIntExtra("likeCount",0);
        String thumbnails = intent.getStringExtra("thumbnails");
        String nickname = intent.getStringExtra("nickname");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Video video = new Video();
        video.setAvatar(avatar);
        video.setDescription(description);
        video.setFeedUrl(feedUrl);
        video.setLikeCount(likeCount);
        video.setNickname(nickname);
        video.setThumbnails(thumbnails);
        VideoFragment videoFragment = new VideoFragment(video);
        transaction.add(R.id.videoFragment,videoFragment);
        transaction.commit();
    }
}