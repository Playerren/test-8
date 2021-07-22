package com.androidcourse.toktik;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class VideoPushActivity extends AppCompatActivity {
    private String videoPath;

    private ImageView imageView;

    private EditText editText;

    private ImageView back, knows, open;

    private Button setToLocal, push, location,settings;

    private RadioGroup share, grade;

    private String lacalPath = "DCIM/Camera";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videopush);
        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        videoPath = bundle.getString("videoPath");
        Log.i("获取到的视频地址为",videoPath);

        initButton();
        initImgView();
    }

    private void initImgView() {
        imageView = findViewById(R.id.imageView);
        imageView.setImageBitmap(createThumbnailAtTime(videoPath, 1));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }

    private void initButton() {
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, CustomCameraActivity.class);
            startActivity(intent);
            finish();
        });

        knows = findViewById(R.id.knows);

        location = findViewById(R.id.location);

        settings = findViewById(R.id.settings);

        setToLocal = findViewById(R.id.setToLocal);
        setToLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // videoPath = /storage/emulated/0/Android/data/com.androidcourse.toktik/files/vid_1626779108060.mp4
                // newPath = /storage/emulated/0/DCIM/Camera/vid_1626779108060.mp4
                String newPath = videoPath;
                newPath = newPath.replace("Android/data/com.androidcourse.toktik/files", "DCIM/Camera");
                copyFile(videoPath, newPath);
            }
        });

        push = findViewById(R.id.push);

        share = findViewById(R.id.share);

        grade = findViewById(R.id.grade);

        open = findViewById(R.id.btn_open);
        open.setOnClickListener(v->{

        });
    }

    private Bitmap createThumbnailAtTime(String filePath, int timeInSeconds){
        MediaMetadataRetriever mMMR = new MediaMetadataRetriever();

        mMMR.setDataSource(filePath);

//api time unit is microseconds

        return mMMR.getFrameAtTime(timeInSeconds*1000000, MediaMetadataRetriever.OPTION_CLOSEST);

    }


    /**
     * 复制单个文件
     *
     * @param oldPath$Name String 原文件路径+文件名 如：data/user/0/com.test/files/abc.txt
     * @param newPath$Name String 复制后路径+文件名 如：data/user/0/com.test/cache/abc.txt
     * @return <code>true</code> if and only if the file was copied;
     *         <code>false</code> otherwise
     */
    public boolean copyFile(String oldPath$Name, String newPath$Name) {
        try {
            File oldFile = new File(oldPath$Name);
            if (!oldFile.exists()) {
                Log.e("--Method--", "copyFile:  oldFile not exist.");
                return false;
            } else if (!oldFile.isFile()) {
                Log.e("--Method--", "copyFile:  oldFile not file.");
                return false;
            } else if (!oldFile.canRead()) {
                Log.e("--Method--", "copyFile:  oldFile cannot read.");
                return false;
            }
            /* 如果不需要打log，可以使用下面的语句
            if (!oldFile.exists() || !oldFile.isFile() || !oldFile.canRead()) {
                return false;
            }
            */
            File newFile = new File(newPath$Name);
            if (!newFile.exists()){
                newFile.createNewFile();
            }
            FileInputStream fileInputStream = new FileInputStream(oldPath$Name);
            FileOutputStream fileOutputStream = new FileOutputStream(newPath$Name);
            byte[] buffer = new byte[1024];
            int byteRead;
            while (-1 != (byteRead = fileInputStream.read(buffer))) {
                fileOutputStream.write(buffer, 0, byteRead);
            }
            fileInputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
