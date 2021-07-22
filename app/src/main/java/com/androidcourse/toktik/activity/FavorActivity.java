package com.androidcourse.toktik.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidcourse.toktik.CustomCameraActivity;
import com.androidcourse.toktik.R;
import com.androidcourse.toktik.activity.recyclervideo.RecyclerVideoActivity;
import com.androidcourse.toktik.activity.recyclervideo.RecyclerVideoAdapter;
import com.androidcourse.toktik.activity.videoplay.VideoPlayActivity;
import com.androidcourse.toktik.db.FavorContract;
import com.androidcourse.toktik.db.FavorDbHelper;
import com.androidcourse.toktik.entity.Video;
import com.androidcourse.toktik.network.ApiService;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FavorActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerVideoAdapter adapter;
    private ApiService apiService;
    private final String TAG = "RecyclerVideoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favor);
        List<Video> videos = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerVideoAdapter(videos, getApplicationContext());
        recyclerView.setAdapter(adapter);

        FavorDbHelper dbHelper = new FavorDbHelper(FavorActivity.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] proj = {
                BaseColumns._ID,
                FavorContract.FavorEntry.COLUMN_NAME_AVATAR,
                FavorContract.FavorEntry.COLUMN_NAME_DESCRIPTION,
                FavorContract.FavorEntry.COLUMN_NAME_FEED_URL,
                FavorContract.FavorEntry.COLUMN_NAME_LIKE_COUNT,
                FavorContract.FavorEntry.COLUMN_NAME_NICKNAME,
                FavorContract.FavorEntry.COLUMN_NAME_THUMBNAILS
        };
        Cursor cursor = db.query(FavorContract.FavorEntry.TABLE_NAME,
                proj,
                null,null,null,null,null);

        while(cursor.moveToNext()){
            long id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
            String avatar = cursor.getString(cursor.getColumnIndexOrThrow(FavorContract.FavorEntry.COLUMN_NAME_AVATAR));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(FavorContract.FavorEntry.COLUMN_NAME_DESCRIPTION));;
            String feedURL = cursor.getString(cursor.getColumnIndexOrThrow(FavorContract.FavorEntry.COLUMN_NAME_FEED_URL));
            int like_count = cursor.getInt(cursor.getColumnIndexOrThrow(FavorContract.FavorEntry.COLUMN_NAME_FEED_URL));
            String nickname = cursor.getString(cursor.getColumnIndexOrThrow(FavorContract.FavorEntry.COLUMN_NAME_NICKNAME));
            String thumbnails = cursor.getString(cursor.getColumnIndexOrThrow(FavorContract.FavorEntry.COLUMN_NAME_THUMBNAILS));

            Video video = new Video();
            video.setAvatar(avatar);
            video.setDescription(description);
            video.setFeedUrl(feedURL);
            video.setLikeCount(like_count);
            video.setNickname(nickname);
            video.setThumbnails(thumbnails);
            videos.add(video);
        }
        adapter.refresh(videos);
    }
}