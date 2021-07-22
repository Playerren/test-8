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
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class VideoPlayerIJK extends FrameLayout {

    private VideoManager videoManager;
    Runnable flushBar;
    private boolean hasCreateSurfaceView = false;

    private SurfaceView surfaceView;

    private ImageButton playorpauseButton;
    private LinearLayout controlBar;
    private TextView currTime;
    private TextView fullTime;
    private SeekBar videoSeekBar;
    private LinearLayout timeInfo;

    private boolean mDragging = false;
    private int newPosition = 0;

    private boolean firstLoad = true;
    private IMediaPlayer.OnPreparedListener onPreparedListener = new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer iMediaPlayer) {

        }
    };
    private IMediaPlayer.OnCompletionListener onCompletionListener = new IMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(IMediaPlayer iMediaPlayer) {

        }
    };


    private Handler seekBarHandler;
    private PlayerSurfaceCallback callback;

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

    public void setTimeInfo(LinearLayout timeInfo) {
        this.timeInfo = timeInfo;
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

    public void preInit(){
        videoManager = new VideoManager(getContext());
    }

    /**
     * 播放器管理器初始化，设置播放控制行为（播放/停止按钮，进度条与时间显示）
     */
    public void init(){

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
                    newPosition = progress;
                    seekBarHandler.removeCallbacks(VideoPlayerIJK.this.flushBar);
                    currTime.setText(longToTime(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mDragging=true;
                showTimeInfo();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mDragging=false;
                seekBarHandler.postDelayed(flushBar,1000);
                videoManager.seekTo((newPosition));
                hideTimeInfo();
            }
        });

        // 进度条刷新定时任务
        seekBarHandler = new Handler();
        flushBar = new Runnable() {
            @Override
            public void run() {
                videoSeekBar.setMax((int) (videoManager.getDuration()));
                videoSeekBar.setProgress((int)(videoManager.getCurrentPosition()));
                seekBarHandler.postDelayed(this,1000);
                currTime.setText(longToTime(videoManager.getCurrentPosition()));
                fullTime.setText(longToTime(videoManager.getDuration()));
            }
        };

        //默认开始进度条刷新
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

    /**
     * 开始播放：创建播放界面 加载
     */
    public void startPlay(){
        Log.d("video-debug","load resource");
        //先加载资源
        videoManager.load();
        //再创建播放界面
        Log.d("video-debug","create surface");
        createSurfaceView();

        // 设置准备好开始播放的事件：处理显示范围
        videoManager.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                int width = VideoPlayerIJK.this.getWidth();
                int height = VideoPlayerIJK.this.getHeight();
                int vWidth = videoManager.getVideoWidth();
                int vHeight = videoManager.getVideoHeight();
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
                // 调用custom listener
                onPreparedListener.onPrepared(iMediaPlayer);
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
        callback = new PlayerSurfaceCallback();
        surfaceView.getHolder().addCallback(callback);
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
            videoManager.stop();
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

    private void showTimeInfo(){
        timeInfo.setAlpha(1f);
    }

    private void hideTimeInfo(){
        timeInfo.setAlpha(0f);
    }

    /**
     * 开始播放，如果发现视频还无法播放则会先创建播放控件
     */
    public void start() {
        if(videoManager.isPlayable()){
            Log.d("video-debug","video can play, normal play");
            videoManager.start();
        }else{
            Log.d("video-debug","video cannot play");
            this.startPlay();
        }

    }

    /**
     * 释放现有的资源
     */
    public void release() {
        if(surfaceView!=null&&surfaceView.getHolder()!=null){
            surfaceView.getHolder().removeCallback(callback);
            callback=null;
        }
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

    //播放完成
    public void setOnCompletionListener(IMediaPlayer.OnCompletionListener onCompletionListener){
        videoManager.setOnCompletionListener(onCompletionListener);
    }

    //缓冲中
    public void setOnBufferingUpdateListener(IMediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener){
        videoManager.setOnBufferingUpdateListener(onBufferingUpdateListener);
    }

    public void setOnControlMessageListener(IjkMediaPlayer.OnControlMessageListener onControlMessageListener){
        videoManager.setOnControlMessageListener(onControlMessageListener);
    }

    //视频大小发生变化
    public void setOnVideoSizeChangedListener(IMediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener){
        videoManager.setOnVideoSizeChangedListener(onVideoSizeChangedListener);
    }

    public void setOnMediaCodecSelectListener(IjkMediaPlayer.OnMediaCodecSelectListener onMediaCodecSelectListener){
        videoManager.setOnMediaCodecSelectListener(onMediaCodecSelectListener);
    }

    public void setOnNativeInvokeListener(IjkMediaPlayer.OnNativeInvokeListener onNativeInvokeListener){
        videoManager.setOnNativeInvokeListener(onNativeInvokeListener);
    }

    //播放过程中发生了错误
    public void setOnErrorListener(IMediaPlayer.OnErrorListener onErrorListener){
        videoManager.setOnErrorListener(onErrorListener);
    }

    public void setOnSeekCompleteListener(IMediaPlayer.OnSeekCompleteListener onSeekCompleteListener){
        videoManager.setOnSeekCompleteListener(onSeekCompleteListener);
    }

    //信息监听
    public void setOnInfoListener(IMediaPlayer.OnInfoListener onInfoListener){
        videoManager.setOnInfoListener(onInfoListener);
    }

    public void setOnPreparedListener(IMediaPlayer.OnPreparedListener onPreparedListener){
        this.onPreparedListener = onPreparedListener;
    }

}
