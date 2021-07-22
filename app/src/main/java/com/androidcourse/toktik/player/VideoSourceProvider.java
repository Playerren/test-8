package com.androidcourse.toktik.player;

import android.content.Context;
import android.util.Log;

import com.androidcourse.toktik.entity.Video;
import com.androidcourse.toktik.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VideoSourceProvider {
    private static VideoSourceProvider INSTANCE;

    public static VideoSourceProvider getINSTANCE(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new VideoSourceProvider();
            INSTANCE.context = context;
        }
        return INSTANCE;
    }

    private int startbound = 0;
    private int endbound = -1;
    private ApiService apiService;
    private Context context;

    private ArrayList<Video> videoList = new ArrayList<>();

    public void prevAcquire(VideoSourceLoadFinishedCallback videoSourceLoadFinishedCallback) {
        if (startbound == 0) {
            videoSourceLoadFinishedCallback.onVideoSourceLoadFinished(null);
        } else {
            if (endbound - startbound >= 5) {
                endbound--;
            }
            videoSourceLoadFinishedCallback.onVideoSourceLoadFinished(videoList.get(--startbound));
        }
    }

    public void endAcquire(VideoSourceLoadFinishedCallback videoSourceLoadFinishedCallback) {
        if (videoList.size() <= endbound + 1) {
            List<Video> acquired = acquireNewVideo(new Callback() {
                @Override
                public void loadsuccessCallback(List<Video> videos) {
                    videoList.addAll(videos);
                    for (int i = 0; i < videoList.size(); i++) {
                        Log.d("network", videoList.get(i).toString());
                    }
                    if (videoList.size() <= endbound + 1) {
                        videoSourceLoadFinishedCallback.onVideoSourceLoadFinished(null);
                    }
                    if (endbound - startbound >= 5) {
                        startbound++;
                    }
                    endbound++;
                    videoSourceLoadFinishedCallback.onVideoSourceLoadFinished(videoList.get(endbound));
                }
            });
        } else {
            if (endbound - startbound >= 5) {
                startbound++;
            }
            endbound++;
            videoSourceLoadFinishedCallback.onVideoSourceLoadFinished(videoList.get(endbound));
        }
    }

    private List<Video> acquireNewVideo(Callback callback) {
        List<Video> videos = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://beiyou.bytedance.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
        apiService.getVideos().enqueue(new retrofit2.Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                if (response.body() != null) {
                    videos.addAll(response.body());
                    callback.loadsuccessCallback(videos);
                }
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {

            }
        });

        return videos;
    }

    interface Callback {
        void loadsuccessCallback(List<Video> videos);
    }
}
