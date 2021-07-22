package com.androidcourse.toktik.activity.recyclervideo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidcourse.toktik.R;
import com.androidcourse.toktik.activity.recyclervideo.CoverViewHolder;
import com.androidcourse.toktik.entity.Video;

import java.util.ArrayList;
import java.util.List;

public class RecyclerVideoAdapter extends RecyclerView.Adapter<CoverViewHolder> {

    private final List<Video> videos = new ArrayList<>();
    private final String TAG = "RecyclerVideoAdapter";
    private final Context context;

    public RecyclerVideoAdapter(List<Video> videos, Context context) {
        this.videos.addAll(videos);
        this.context = context;
    }

    @NonNull
    @Override
    public CoverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cover_item, parent, false);
        return new CoverViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CoverViewHolder holder, int position) {
        holder.bind(videos.get(position), context);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public void refresh(List<Video> newVideos) {
        videos.clear();
        if (newVideos != null) {
            videos.addAll(newVideos);
        }
        notifyDataSetChanged();
        Log.e(TAG, "refresh: " + videos);
    }

}
