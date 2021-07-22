package com.androidcourse.toktik.activity.videoplay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.androidcourse.toktik.CustomCameraActivity;
import com.androidcourse.toktik.MainActivity;
import com.androidcourse.toktik.R;
import com.androidcourse.toktik.activity.recyclervideo.RecyclerVideoActivity;
import com.androidcourse.toktik.util.ProxyServer;

/**
 * 流式视频播放activity
 */
public class VideoPlayActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private SwipeRefreshLayout swipeRefreshLayout;

    private Button b1;
    private Button b3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        viewPager = findViewById(R.id.pager);
        swipeRefreshLayout = findViewById(R.id.main_srl);
        ProxyServer.getProxy(getApplicationContext());
        VideoFragmentStateAdapter videoFragmentStateAdapter = new VideoFragmentStateAdapter(this, swipeRefreshLayout);
        viewPager.setAdapter(videoFragmentStateAdapter);
        getSupportActionBar().hide();

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0) {
                    videoFragmentStateAdapter.prevAddFragment(viewPager, 1);
                }
                if (position == videoFragmentStateAdapter.getItemCount() - 2) {
                    videoFragmentStateAdapter.lastAddFragment(viewPager, position - 1);
                }
            }
        });

        b1 = findViewById(R.id.vp_b1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(VideoPlayActivity.this, RecyclerVideoActivity.class);
                startActivity(myIntent);
                finish();
            }
        });
        b3 = findViewById(R.id.vp_b3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkPermissionAllGranted(mPermissionsArrays)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(mPermissionsArrays, REQUEST_PERMISSION);
                        if(checkPermissionAllGranted(mPermissionsArrays)){
                            Intent myIntent = new Intent(VideoPlayActivity.this, CustomCameraActivity.class);
                            startActivity(myIntent);
                        }
                    }
                } else {
                    // 打开相机activity
                    //openSystemCamera();
                    Intent myIntent = new Intent(VideoPlayActivity.this, CustomCameraActivity.class);
                    startActivity(myIntent);
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                videoFragmentStateAdapter.flush();
            }
        });
    }

    private String[] mPermissionsArrays = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            // Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
            Manifest.permission.RECORD_AUDIO};

    private final static int REQUEST_PERMISSION = 123;

    private boolean checkPermissionAllGranted(String[] permissions) {
        // 6.0以下不需要
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            // Toast.makeText(this, "已经授权" + Arrays.toString(permissions), Toast.LENGTH_LONG).show();
        }
    }
}