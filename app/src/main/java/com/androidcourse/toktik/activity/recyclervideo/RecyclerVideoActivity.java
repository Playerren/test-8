package com.androidcourse.toktik.activity.recyclervideo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidcourse.toktik.R;
import com.androidcourse.toktik.activity.videoplay.VideoPlayActivity;
import com.androidcourse.toktik.entity.Video;
import com.androidcourse.toktik.network.ApiService;
import com.androidcourse.toktik.util.ProxyServer;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecyclerVideoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerVideoAdapter adapter;
    private ApiService apiService;
    private final String TAG = "RecyclerVideoActivity";

    private Button b2;
    private Button b3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_video);
        ProxyServer.getProxy(getApplicationContext());
        getSupportActionBar().hide();

        List<Video> videos = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerVideoAdapter(videos, getApplicationContext());
        recyclerView.setAdapter(adapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://beiyou.bytedance.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
        apiService.getVideos().enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                if (response.body() != null) {
//                    Toast.makeText(RecyclerVideoActivity.this,
//                            "加载成功", Toast.LENGTH_SHORT).show();
                    videos.addAll(response.body());
                    adapter.refresh(videos);
                    Log.e(TAG, "onResponse: " + videos);
                } else {
                    Toast.makeText(RecyclerVideoActivity.this,
                            "加载失败", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                Toast.makeText(RecyclerVideoActivity.this, "网络失败："
                        + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

        b2 = findViewById(R.id.rv_b2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(RecyclerVideoActivity.this, VideoPlayActivity.class);
                startActivity(myIntent);
                finish();
            }
        });
        b3 = findViewById(R.id.rv_b3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 跳转到拍摄界面
            }
        });
    }

}