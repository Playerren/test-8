package com.androidcourse.toktik;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        // TODO 设置图片
        Glide.with(context).load(LoadImage.getResourceId(video.getThumbnails())).into(cover);
        Log.e(TAG, "bind: " + video.getThumbnails());
        title.setText(video.getDescription());
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 跳转到视频播放页面
                Log.e(TAG, "onClick: goto video");
            }
        });
    }

}
