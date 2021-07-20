package com.androidcourse.toktik;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

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
    private ConstraintLayout surface;
    private boolean firstshow = true;

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
}