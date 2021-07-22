package com.androidcourse.toktik.activity.videoplay;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.androidcourse.toktik.R;
import com.androidcourse.toktik.entity.Video;
import com.androidcourse.toktik.player.TouchView;
import com.androidcourse.toktik.player.VideoPlayerIJK;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * 视频播放Fragment,构造时传入video即可
 */
public class VideoFragment extends Fragment {

    private ImageView coverPic; // 封面图显示imageView
    private VideoPlayerIJK videoPlayer; // 视频播放控件
    private ImageButton playorpauseButton; //开始暂停按钮
    private LinearLayout controlBar; //播放控制条
    private TextView currTime; // 当前播放进度
    private TextView fullTime; // 全视频长度
    private SeekBar videoSeekBar; // 视频进度条
    private TouchView surface; // 触摸界面，用来支持单击/双击等动作检测

    private LinearLayout timeInfo;

    private TextView nicknameTextView;
    private TextView descriptionTextView;

    private ImageView avatarImageView;
    private TextView likeNumTextView;
    private ImageView likeButton;
    private ImageView share;

    private long lastDoubleClick = 0; // 上一次double click的timestamp
    private long doubleClickDelta = 500; //双击识别的最大时间
    private String imageLink = ""; // 视频的封面图连接
    private String videoLink = ""; // 视频链接，若无视频连接则默认使用默认链接
    private String avatarLink = "";
    private String nickname = "匿名用户";
    private String description = "";
    private int likeNum = 0;
    private boolean liked = false;

    public VideoFragment() {
    }

    public VideoFragment(Video video) {
        this.videoLink = video.getFeedUrl();
        this.imageLink = video.getThumbnails();
        this.avatarLink = video.getAvatar();
        this.nickname = video.getNickname();
        this.description = video.getDescription();
        this.likeNum = video.getLikeCount();
    }

    public VideoFragment(String videoLink, String imageLink) {
        this.videoLink = videoLink;
        this.imageLink = imageLink;
    }

    public VideoFragment(String imageLink) {
        this.imageLink = imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_video, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d("video-debug", "activity created");
        super.onActivityCreated(savedInstanceState);
        //组件获取
        coverPic = getView().findViewById(R.id.cover_image);
        videoPlayer = getView().findViewById(R.id.player);
        playorpauseButton = getView().findViewById(R.id.playorpauseButton);
        controlBar = getView().findViewById(R.id.controlBar);
        currTime = getView().findViewById(R.id.currTime);
        fullTime = getView().findViewById(R.id.fullTime);
        videoSeekBar = getView().findViewById(R.id.videoSeekBar);
        surface = getView().findViewById(R.id.surface);
        share = getView().findViewById(R.id.share);

        nicknameTextView = getView().findViewById(R.id.nickname);
        descriptionTextView = getView().findViewById(R.id.description);
        avatarImageView = getView().findViewById(R.id.avatar);
        likeNumTextView = getView().findViewById(R.id.likenum);
        likeButton = getView().findViewById(R.id.like);
        timeInfo = getView().findViewById(R.id.timeInfo);

        // 封面图初始化
        if (imageLink != null && !imageLink.isEmpty()) {

            Glide.with(getContext()).load(imageLink).into(coverPic);
        }
        nicknameTextView.setText("@" + nickname);
        descriptionTextView.setText(description);
        if (avatarLink != null && !avatarLink.isEmpty()) {
            RequestOptions cropOptions = new RequestOptions();
            cropOptions.centerCrop().circleCrop();
            Glide.with(getContext()).load(avatarLink)
                    .placeholder(R.drawable.user_avatar)
                    .error(R.drawable.user_avatar)
                    .fallback(R.drawable.user_avatar)
                    .apply(cropOptions).into(avatarImageView);
        }
        if (liked) {
            likeButton.setImageDrawable(getContext().getDrawable(R.drawable.lovered));
        } else {
            likeButton.setImageDrawable(getContext().getDrawable(R.drawable.lovewhite));
        }
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (liked) {
                    setUnlike();
                } else {
                    setLike();
                }
            }
        });
        likeNumTextView.setText(convertLikeNum(likeNum));

        // 分享功能实现
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareMsg = "好友邀请你来看【" + description + "】视频，快点击链接查看吧：" + videoLink;
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, shareMsg);
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, getResources().getText(R.string.app_name)));
            }
        });

        //传递播放控件
        videoPlayer.setControlBar(controlBar);
        videoPlayer.setCurrTime(currTime);
        videoPlayer.setFullTime(fullTime);
        videoPlayer.setPlayorpauseButton(playorpauseButton);
        videoPlayer.setVideoSeekBar(videoSeekBar);
        videoPlayer.setTimeInfo(timeInfo);
        //播放器初始化

        //surface触摸初始化
        surface.setGestureListener(new MyGestureListener());

        // 设置封面图行为
        videoPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                hideCoverPic();
            }
        });
        videoPlayer.preInit();
        videoPlayer.init();
        // 准备视频资源
        if (videoLink != null && !videoLink.isEmpty()) {
            videoPlayer.setVideoPath(videoLink);
        } else {
            if (Math.random() < 0.5) {
                videoPlayer.setVideoResource(R.raw.bytedance);
            } else {
                videoPlayer.setVideoResource(R.raw.hourse);
            }

        }
        Log.d("fragment", this.toString());
    }

    @Override
    public void onResume() {
        super.onResume();
        //开始播放音频
        Log.d("video-debug", "fragment resume");
        videoPlayer.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        //停止视频播放，不释放资源
        videoPlayer.stop();
        videoPlayer.release();
        showCoverPic();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        videoPlayer.release();
    }

    public void singleClick() {
        long currTime = System.currentTimeMillis();
        if (currTime - lastDoubleClick > doubleClickDelta) {
            if (videoPlayer.isPlaying()) {
                videoPlayer.pause();
                playorpauseButton.setAlpha(1f);
                playorpauseButton.setImageDrawable(getContext().getDrawable(R.drawable.pause));
            } else {
                videoPlayer.start();
                playorpauseButton.setAlpha(0f);
                playorpauseButton.setImageDrawable(getContext().getDrawable(R.drawable.play));
            }
        }
    }

    private void showCoverPic() {
        coverPic.setAlpha(1f);
    }

    private void hideCoverPic() {
        coverPic.setAlpha(0f);
    }

    private void setLike() {
        liked = true;
        likeButton.setImageDrawable(getContext().getDrawable(R.drawable.lovered));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scale(likeButton, "scaleX", 2f, 1f, 100, 0))
                .with(scale(likeButton, "scaleY", 2f, 1f, 100, 0))
                .with(scale(likeButton, "scaleX", 0.9f, 1, 50, 150))
                .with(scale(likeButton, "scaleY", 0.9f, 1, 50, 150));
        animatorSet.start();
    }


    private void setUnlike() {
        liked = false;
        likeButton.setImageDrawable(getContext().getDrawable(R.drawable.lovewhite));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scale(likeButton, "scaleX", 2f, 1f, 100, 0))
                .with(scale(likeButton, "scaleY", 2f, 1f, 100, 0))
                .with(scale(likeButton, "scaleX", 0.9f, 1, 50, 150))
                .with(scale(likeButton, "scaleY", 0.9f, 1, 50, 150));
        animatorSet.start();
    }


    public static ObjectAnimator scale(View view, String propertyName, float from, float to, long time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , propertyName
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }

    public static ObjectAnimator translationX(View view, float from, float to, long time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , "translationX"
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }

    public static ObjectAnimator alpha(View view, float from, float to, long time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , "alpha"
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }

    public static ObjectAnimator translationY(View view, float from, float to, long time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , "translationY"
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }

    private String convertLikeNum(int num) {
        if (num < 10000) {
            return num + "";
        } else {
            return num / 10000 + "." + (num % 10000) / 1000 + "w";
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("video-debug", isVisibleToUser + ":visible?");
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        public MyGestureListener() {
            super();
        }

        String TAG = "gesture";

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            lastDoubleClick = System.currentTimeMillis();
            Log.e(TAG, "onDoubleTap");
            VideoFragment.this.surface.drawLovePic(e);
            setLike();
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.e(TAG, "onSingleTapConfirmed");
            if (VideoFragment.this != null) {
                VideoFragment.this.singleClick();
            }

            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.e(TAG, "onScroll");
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            //Log.e(TAG, "onLongPress");
        }
    }

}

