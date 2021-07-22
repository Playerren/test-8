package com.androidcourse.toktik.db;

import android.provider.BaseColumns;

public class FavorContract {

    private FavorContract() {
    }

    public static class FavorEntry implements BaseColumns {

        public static final String TABLE_NAME = "favor";

        public static final String COLUMN_NAME_FEED_URL = "feedUrl";

        public static final String COLUMN_NAME_NICKNAME = "nickname";

        public static final String COLUMN_NAME_DESCRIPTION = "description";

        public static final String COLUMN_NAME_LIKE_COUNT = "likeCount";

        public static final String COLUMN_NAME_AVATAR = "avatar";

        public static final String COLUMN_NAME_THUMBNAILS = "thumbnails";

    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FavorEntry.TABLE_NAME + " (" +
                    FavorEntry._ID + " INTEGER PRIMARY KEY," +
                    FavorEntry.COLUMN_NAME_FEED_URL + " TEXT," +
                    FavorEntry.COLUMN_NAME_NICKNAME + " TEXT," +
                    FavorEntry.COLUMN_NAME_DESCRIPTION + " TEXT," +
                    FavorEntry.COLUMN_NAME_LIKE_COUNT + " INTEGER," +
                    FavorEntry.COLUMN_NAME_AVATAR + " TEXT," +
                    FavorEntry.COLUMN_NAME_THUMBNAILS + " TEXT)";

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + FavorEntry.TABLE_NAME;

}
