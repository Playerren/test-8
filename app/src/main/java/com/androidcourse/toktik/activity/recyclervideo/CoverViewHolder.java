package com.androidcourse.toktik.activity.recyclervideo;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidcourse.toktik.R;
import com.androidcourse.toktik.entity.Video;
import com.androidcourse.toktik.util.LoadImage;
import com.bumptech.glide.Glide;

public class CoverViewHolder extends RecyclerView.ViewHolder {

    private final TextView title;
    private final ImageView cover;
    private final String TAG = "CoverViewHolder";

    public CoverViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.cover_title);
        cover = itemView.findViewById(R.id.cover_image);
    }

    public void bind(Video video, Context context) {
        Glide.with(context).load(LoadImage.getResourceId(video.getThumbnails())).into(cover);
        Log.e(TAG, "bind: " + video.getThumbnails());
        title.setText(video.getDescription());
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), SingleVideoPlayActivity.class);
                myIntent.putExtra("avatar", video.getAvatar());
                myIntent.putExtra("feedUrl", video.getFeedUrl());
                myIntent.putExtra("description", video.getDescription());
                myIntent.putExtra("likeCount", video.getLikeCount());
                myIntent.putExtra("thumbnails", video.getThumbnails());
                myIntent.putExtra("nickname", video.getNickname());
                Log.e(TAG, "onClick: goto video");
                view.getContext().startActivity(myIntent);
            }
        });
    }

}
