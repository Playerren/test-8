package com.androidcourse.toktik.db;

import com.androidcourse.toktik.entity.Video;

import java.util.List;

public interface FavorOperator {

    int insertFavor(Video video);

    int deleteFavor(int _id);

    List<Video> selectAll();

}
