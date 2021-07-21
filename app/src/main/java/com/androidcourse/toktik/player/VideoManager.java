package com.androidcourse.toktik.player;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class VideoManager {

    private static boolean firstLoad = true;
    private IjkMediaPlayer mMediaPlayer = null;

    private Context context;


    private MediaSourceType mediaSourceType;

    private String videoPath = "";
    private Uri videoUri = null;
    private int videoResourceId = 0;

    private boolean cycle = true;

    public VideoManager(Context context){
        if(firstLoad){
            try {
                IjkMediaPlayer.loadLibrariesOnce(null);
                IjkMediaPlayer.native_profileBegin("libijkplayer.so");
                firstLoad = false;
            } catch (Exception e) {

            }
        }
        init(context);
    };


    private void init(Context context){
        this.context = context;
    }

    public void setVideoPath(String videoPath) {
        String path = ProxyServer.getProxy(context).getProxyUrl(videoPath);
        this.mediaSourceType = MediaSourceType.LINK;
        this.videoPath = path;
        Log.d("download",path);
    }

    public void setVideoUri(Uri videoUri) {
        this.mediaSourceType = MediaSourceType.URI;
        this.videoUri = videoUri;
    }

    public void setVideoResourceId(int videoResourceId) {
        this.mediaSourceType = MediaSourceType.NATIVE_RESOURCE;
        this.videoResourceId = videoResourceId;
    }

    public void prepareAsync(){
        mMediaPlayer.prepareAsync();
    }

    public void setDisplay(SurfaceHolder sh){
        if(mMediaPlayer==null){
            createPlayer();
        }
        mMediaPlayer.setDisplay(sh);
    }

    public void load(){
        createPlayer();
        mMediaPlayer.setLooping(cycle);
        try{
            switch (this.mediaSourceType){
                case URI:
                    mMediaPlayer.setDataSource(context,videoUri);
                    break;
                case LINK:
                    mMediaPlayer.setDataSource(videoPath);
                    break;
                case NATIVE_RESOURCE:
                    AssetFileDescriptor fileDescriptor = context.getResources().openRawResourceFd(videoResourceId);
                    RawDataSourceProvider provider = new RawDataSourceProvider(fileDescriptor);
                    mMediaPlayer.setDataSource(provider);
                    break;
                default: break;
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * 创建一个新的player
     */
    private void createPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.setDisplay(null);
            mMediaPlayer.release();
        }
        IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();
        //开启硬解码
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
        //设置精准seek
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1);
        mMediaPlayer = ijkMediaPlayer;
        mMediaPlayer.setSpeed(1f);
    }

    /**
     * 开始播放
     */
    public void start() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    /**
     * 释放现有的资源
     */
    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    /**
     * 停止播放
     */
    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
    }

    /**
     * 重置播放
     */
    public void reset() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
        }
    }

    /**
     * 获取视频全长
     * @return
     */
    public long getDuration() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getDuration();
        } else {
            return 0;
        }
    }

    /**
     * 获取当前位置
     * @return
     */
    public long getCurrentPosition() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

    /**
     * 获取视频是否在播放
     * @return
     */
    public boolean isPlaying() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    /**
     * 跳到某一位置
     * @param l
     */
    public void seekTo(long l) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(l);
        }
    }

    public boolean isPlayable(){
        if(mMediaPlayer!=null){
            return mMediaPlayer.isPlayable();
        }
        return false;
    }

    public int getVideoWidth(){
        return mMediaPlayer.getVideoWidth();
    }

    public int getVideoHeight(){
        return mMediaPlayer.getVideoHeight();
    }

    //播放完成
    public void setOnCompletionListener(IMediaPlayer.OnCompletionListener onCompletionListener){
        mMediaPlayer.setOnCompletionListener(onCompletionListener);
    }

    //缓冲中
    public void setOnBufferingUpdateListener(IMediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener){
        mMediaPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);
    }

    public void setOnControlMessageListener(IjkMediaPlayer.OnControlMessageListener onControlMessageListener){
        mMediaPlayer.setOnControlMessageListener(onControlMessageListener);
    }

    //视频大小发生变化
    public void setOnVideoSizeChangedListener(IMediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener){
        mMediaPlayer.setOnVideoSizeChangedListener(onVideoSizeChangedListener);
    }

    public void setOnMediaCodecSelectListener(IjkMediaPlayer.OnMediaCodecSelectListener onMediaCodecSelectListener){
        mMediaPlayer.setOnMediaCodecSelectListener(onMediaCodecSelectListener);
    }

    public void setOnNativeInvokeListener(IjkMediaPlayer.OnNativeInvokeListener onNativeInvokeListener){
        mMediaPlayer.setOnNativeInvokeListener(onNativeInvokeListener);
    }

    //播放过程中发生了错误
    public void setOnErrorListener(IMediaPlayer.OnErrorListener onErrorListener){
        mMediaPlayer.setOnErrorListener(onErrorListener);
    }

    public void setOnSeekCompleteListener(IMediaPlayer.OnSeekCompleteListener onSeekCompleteListener){
        mMediaPlayer.setOnSeekCompleteListener(onSeekCompleteListener);
    }

    //信息监听
    public void setOnInfoListener(IMediaPlayer.OnInfoListener onInfoListener){
        mMediaPlayer.setOnInfoListener(onInfoListener);
    }

    /**
     * 必须调用来初始化加载完成自动播放行为
     * @param onPreparedListener
     */
    public void setOnPreparedListener(IMediaPlayer.OnPreparedListener onPreparedListener){
        mMediaPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                VideoManager.this.start();
                onPreparedListener.onPrepared(iMediaPlayer);
            }
        });
    }



}
