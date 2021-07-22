package com.androidcourse.toktik.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class Utils {

    public static String convertUriToPath(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String imgPathSel = UriUtils.formatUri(context, uri);
            if (!TextUtils.isEmpty(imgPathSel)) {
                return imgPathSel;
            }
        }
        String schema = uri.getScheme();
        if (TextUtils.isEmpty(schema) || ContentResolver.SCHEME_FILE.equals(schema)) {
            return uri.getPath();
        }
        if ("http".equals(schema)) {
            return uri.toString();
        }
        if (ContentResolver.SCHEME_CONTENT.equals(schema)) {
            String[] projection = new String[]{MediaStore.MediaColumns.DATA};
            Cursor cursor = null;
            String filePath = "";
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(0);
                }
                if (null != cursor) {
                    cursor.close();
                }
            } catch (Exception e) {
                // do nothing
            } finally {
                try {
                    if (null != cursor) {
                        cursor.close();
                    }
                } catch (Exception e2) {
                    // do nothing
                }
            }
            if (TextUtils.isEmpty(filePath)) {
                try {
                    ContentResolver contentResolver = context.getContentResolver();
                    String selection = MediaStore.Images.Media._ID + "= ?";
                    String id = uri.getLastPathSegment();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && !TextUtils.isEmpty(id) && id.contains(":")) {
                        id = id.split(":")[1];
                    }
                    String[] selectionArgs = new String[]{id};
                    cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null);
                    if (cursor.moveToFirst()) {
                        filePath = cursor.getString(0);
                    }
                    if (null != cursor) {
                        cursor.close();
                    }

                } catch (Exception e) {
                    // do nothing
                } finally {
                    try {
                        if (cursor != null) {
                            cursor.close();
                        }
                    } catch (Exception e) {
                        // do nothing
                    }
                }
            }
            return filePath;
        }
        return null;
    }


    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private static String path = "/storage/emulated/0/Android/data/com.androidcourse.toktik/files";
    /**
     * Create a File for saving an image or video
     */
    public static String getOutputMediaFile(int type, Context c) {
        File mediaStorageDir = c.getExternalFilesDir("video");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        // Create a media file name
        // videoPath = new File("/storage/emulated/0/DCIM/Camera/", videoName);
        // videoPath = /storage/emulated/0/Android/data/com.androidcourse.toktik/files/vid_1626779108060.mp4
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            String imageName = String.format(Locale.getDefault(), "img_%d.jpg", System.currentTimeMillis());
//            mediaFile = new File(getPath() + File.separator +
//                    imageName);
            mediaFile = new File(mediaStorageDir.getAbsolutePath() + File.separator +
                    imageName);
        } else if (type == MEDIA_TYPE_VIDEO) {
            String videoName = String.format(Locale.getDefault(), "vid_%d.mp4", System.currentTimeMillis());
//            mediaFile = new File(getPath() + File.separator +
//                    videoName);
            mediaFile = new File(mediaStorageDir.getAbsolutePath() + File.separator +
                    videoName);
        } else {
            return null;
        }
        String path = mediaFile.toString();
        return path;
    }

    private static String getPath() {

        return path;
    }


    private static final int NUM_90 = 90;
    private static final int NUM_180 = 180;
    private static final int NUM_270 = 270;

    public static  Bitmap rotateImage(Bitmap bitmap, String path) {
        ExifInterface srcExif = null;
        try {
            srcExif = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
            return bitmap;
        }
        Matrix matrix = new Matrix();
        int angle = 0;
        int orientation = srcExif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                angle = NUM_90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                angle = NUM_180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                angle = NUM_270;
                break;
            default:
                break;
        }
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
