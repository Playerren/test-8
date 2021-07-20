package com.androidcourse.toktik.entity;

import com.google.gson.annotations.SerializedName;

public class Video {

    @SerializedName("feedurl")
    private String feedUrl;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("description")
    private String description;
    @SerializedName("likecount")
    private int likeCount;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("thumbnails")
    private String thumbnails;

    public String getFeedUrl() {
        return feedUrl;
    }

    public void setFeedUrl(String feedUrl) {
        this.feedUrl = feedUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(String thumbnails) {
        this.thumbnails = thumbnails;
    }

    @Override
    public String toString() {
        return "Video{" +
                "feedUrl='" + feedUrl + '\'' +
                ", nickname='" + nickname + '\'' +
                ", description='" + description + '\'' +
                ", likeCount=" + likeCount +
                ", avatar='" + avatar + '\'' +
                ", thumbnails='" + thumbnails + '\'' +
                '}';
    }
}
