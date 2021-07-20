package com.androidcourse.toktik.network;

import com.androidcourse.toktik.entity.Video;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("api/invoke/video/invoke/video")
    Call<List<Video>> getVideos();

}
