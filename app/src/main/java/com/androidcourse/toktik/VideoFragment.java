package com.androidcourse.toktik;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.androidcourse.toktik.player.TouchView;
import com.androidcourse.toktik.player.VideoPlayerIJK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoFragment extends Fragment {

    private VideoPlayerIJK videoPlayer;
    private ImageButton playorpauseButton;
    private LinearLayout controlBar;
    private TextView currTime;
    private TextView fullTime;
    private SeekBar videoSeekBar;
    private TouchView surface;
    private boolean firstshow = true;

    private int fadeTime = 3000;

    private GestureDetector gestureDetector;

    public VideoFragment() {

    }

    public static VideoFragment newInstance() {
        VideoFragment fragment = new VideoFragment();
        return fragment;
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
        super.onActivityCreated(savedInstanceState);
        videoPlayer = getView().findViewById(R.id.player);
        playorpauseButton = getView().findViewById(R.id.playorpauseButton);
        controlBar = getView().findViewById(R.id.controlBar);
        currTime = getView().findViewById(R.id.currTime);
        fullTime = getView().findViewById(R.id.fullTime);
        videoSeekBar = getView().findViewById(R.id.videoSeekBar);
        surface = getView().findViewById(R.id.surface);
        videoPlayer.setControlBar(controlBar);
        videoPlayer.setCurrTime(currTime);
        videoPlayer.setFullTime(fullTime);
        videoPlayer.setPlayorpauseButton(playorpauseButton);
        videoPlayer.setVideoSeekBar(videoSeekBar);
        videoPlayer.init();
        surface.setGestureListener(new MyGestureListener());
        Log.d("fragment",this.toString());
    }

    @Override
    public void onResume() {
        super.onResume();
        videoPlayer.setVideoResource(R.raw.bytedance);
        Log.d("video","resume "+videoPlayer.isPlaying());
        videoPlayer.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        videoPlayer.stop();
        videoPlayer.release();
    }


    public void singleClick(){
        if(videoPlayer.isPlaying()){
            videoPlayer.pause();
            playorpauseButton.setAlpha(1f);
            playorpauseButton.setImageDrawable(getContext().getDrawable(R.drawable.pause));
        }else{
            videoPlayer.start();
            playorpauseButton.setAlpha(0f);
            playorpauseButton.setImageDrawable(getContext().getDrawable(R.drawable.play));
        }
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        public MyGestureListener() {
            super();
        }

        String TAG = "gesture";

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.e(TAG, "onDoubleTap");
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.e(TAG, "onSingleTapConfirmed");
            if(VideoFragment.this!=null){
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

