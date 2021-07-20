package com.androidcourse.toktik.player;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.androidcourse.toktik.R;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public class VideoPlayerIJK extends FrameLayout {

    private VideoManager videoManager;

    private boolean hasCreateSurfaceView = false;

    private SurfaceView surfaceView;

    private ImageButton playorpauseButton;
    private LinearLayout controlBar;
    private TextView currTime;
    private TextView fullTime;
    private SeekBar videoSeekBar;

    private Handler seekBarHandler;

    public void setPlayorpauseButton(ImageButton playorpauseButton) {
        this.playorpauseButton = playorpauseButton;
    }

    public void setControlBar(LinearLayout controlBar) {
        this.controlBar = controlBar;
    }

    public void setCurrTime(TextView currTime) {
        this.currTime = currTime;
    }

    public void setFullTime(TextView fullTime) {
        this.fullTime = fullTime;
    }

    public void setVideoSeekBar(SeekBar videoSeekBar) {
        this.videoSeekBar = videoSeekBar;
    }

    private VideoTouchListener videoTouchListener;

    public VideoPlayerIJK(@NonNull Context context) {
        super(context);
    }

    public VideoPlayerIJK(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoPlayerIJK(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VideoPlayerIJK(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init(){
        videoManager = new VideoManager(getContext());
        this.videoTouchListener = new VideoTouchListener() {
            @Override
            public void onSingleTouch() { }
            @Override
            public void onDoubleTouch() { }
        };

        playorpauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(videoManager.isPlaying()){
                    playorpauseButton.setImageDrawable(getContext().getDrawable(R.drawable.pause));
                    videoManager.pause();
                }else{
                    playorpauseButton.setImageDrawable(getContext().getDrawable(R.drawable.play));
                    videoManager.start();
                }
            }
        });

        videoSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    videoManager.seekTo(progress);
                    if(!videoManager.isPlaying()){
                        videoManager.start();
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarHandler = new Handler();
        Runnable flushBar = new Runnable() {
            @Override
            public void run() {
                videoSeekBar.setMax((int) (videoManager.getDuration()));
                videoSeekBar.setProgress((int)(videoManager.getCurrentPosition()));
                seekBarHandler.postDelayed(this,1000);
                currTime.setText(longToTime(videoManager.getCurrentPosition()));
                fullTime.setText(longToTime(videoManager.getDuration()));
            }
        };
        seekBarHandler.removeCallbacks(flushBar);
        seekBarHandler.postDelayed(flushBar,1000);
    }

    /**
     * 设置视频地址。
     * 根据是否第一次播放视频，做不同的操作。
     *
     * @param path the path of the video.
     */
    public void setVideoPath(String path) {
        videoManager.setVideoPath(path);
    }

    public void setVideoResource(int resourceId) {
        videoManager.setVideoResourceId(resourceId);
    }

    public void setVideoUri(Uri uri) {
        videoManager.setVideoUri(uri);
    }

    public void startPlay(){
        createSurfaceView();
        videoManager.load();
        videoManager.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                int w = VideoPlayerIJK.this.getWidth();
                int h = VideoPlayerIJK.this.getHeight();
                Log.d("size-changed","w:"+w+" h:"+h);
                int vWidth = videoManager.getVideoWidth();
                int vHeight = videoManager.getVideoHeight();
                int width = w;
                int height= h;
                if(height==0||vHeight==0){
                    return;
                }
                int screenRate = width/height;
                int videoRate = vWidth/vHeight;
                ViewGroup.LayoutParams layoutParams = VideoPlayerIJK.this.getLayoutParams();
                if(screenRate<videoRate){
                    VideoPlayerIJK.this.setSize(width,(width*vHeight)/vWidth);
                }else{
                    VideoPlayerIJK.this.setSize((height*vWidth)/vHeight,height);
                }
            }
        });

        videoManager.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {

            }
        });
    }

    public void setVideoTouchListener(VideoTouchListener videoTouchListener){
        if(videoTouchListener!=null){
            this.videoTouchListener = videoTouchListener;
        }
    }

    /**
     * 新建一个surfaceview
     */
    private void createSurfaceView() {
        //生成一个新的surface view
        surfaceView = new SurfaceView(getContext());
        surfaceView.getHolder().addCallback(new PlayerSurfaceCallback());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT);
        surfaceView.setLayoutParams(layoutParams);
        this.addView(surfaceView);
        hasCreateSurfaceView = true;
    }

    /**
     * surfaceView的监听器
     */
    private class PlayerSurfaceCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            //surfaceview创建成功后，加载视频
            //给mediaPlayer设置视图
            videoManager.setDisplay(holder);
            videoManager.prepareAsync();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            videoManager.setDisplay(holder);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            videoManager.release();
        }
    }

    private void setSize(int width, int height){
        ViewGroup.LayoutParams layoutParams = surfaceView.getLayoutParams();
        ViewGroup.LayoutParams thislayout = this.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        thislayout.width = width;
        thislayout.height = height;
        surfaceView.setLayoutParams(layoutParams);
        this.setLayoutParams(thislayout);
    }

    /**
     * 开始播放
     */
    public void start() {
        if(videoManager.isPlayable()){
            videoManager.start();
        }else{
            this.startPlay();
        }

    }

    /**
     * 释放现有的资源
     */
    public void release() {
        videoManager.release();
    }

    /**
     * 暂停播放
     */
    public void pause() {
        videoManager.pause();
    }

    /**
     * 停止播放
     */
    public void stop() {
        videoManager.stop();
    }

    /**
     * 重置播放
     */
    public void reset() {
        videoManager.reset();
    }

    /**
     * 获取视频全长
     * @return
     */
    public long getDuration() {
        return videoManager.getDuration();
    }

    /**
     * 获取当前位置
     * @return
     */
    public long getCurrentPosition() {
        return videoManager.getCurrentPosition();
    }

    /**
     * 获取视频是否在播放
     * @return
     */
    public boolean isPlaying() {
        return videoManager.isPlaying();
    }

    /**
     * 跳到某一位置
     * @param l
     */
    public void seekTo(long l) {
        videoManager.seekTo(l);
    }

    private String longToTime(long t){
        t = t/1000;
        String min = (int)(t/60)+"";
        String sec = (int)(t%60)+"";
        if(min.length()<2){
            min = "0"+min;
        }
        if(sec.length()<2){
            sec = "0"+sec;
        }
        return min+":"+sec;
    }

}
