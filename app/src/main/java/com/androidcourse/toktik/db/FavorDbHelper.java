package com.androidcourse.toktik.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class FavorDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "favor.db";

    public FavorDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(FavorContract.SQL_DELETE_ENTRIES);
        sqLiteDatabase.execSQL(FavorContract.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(FavorContract.SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
        if (i == 1 && i1 == 2) {
            sqLiteDatabase.execSQL("");
        }
    }
}
