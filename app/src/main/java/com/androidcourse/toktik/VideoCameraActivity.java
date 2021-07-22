package com.androidcourse.toktik;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VideoCameraActivity extends AppCompatActivity {
    /**
     * 请求编号
     * 123：照相机
     * 1001: 录像机
     */
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private ImageView tabline;
    private List<Fragment> list;// 声明一个list集合存放Fragment（数据源）

    private int tabLineLength;// 1/3屏幕宽
    private int currentPage = 0;// 初始化当前页为0（第一页）

    private final int REQUEST_VIDEO_CAMERA = 1001;
    private final static int REQUEST_CAMERA = 123;
    private File imagePath;
    private File videoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_videocamera);
        // 初始化滑动条1/3
        initTabLine();

        // 初始化界面
        initView();
    }


    private void initTabLine() {
        // 获取显示屏信息
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        // 得到显示屏宽度
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        // 1/3屏幕宽度
        tabLineLength = metrics.widthPixels / 3;
        // 获取控件实例
        tabline = (ImageView) findViewById(R.id.tabline);
        // 控件参数
        ViewGroup.LayoutParams lp = tabline.getLayoutParams();
        lp.width = tabLineLength;
        tabline.setLayoutParams(lp);
    }

    private void initView() {
        // 实例化对象
        // 声明一个viewpager对象
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        list = new ArrayList<Fragment>();

        // 设置数据源



        // 设置适配器
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(
                getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return list.get(arg0);
            }
        };

        // 绑定适配器
        viewPager.setAdapter(adapter);

        // 设置滑动监听
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // 当页面被选择时，先讲3个textview的字体颜色初始化成黑
                tv1.setTextColor(Color.BLACK);
                tv2.setTextColor(Color.BLACK);
                tv3.setTextColor(Color.BLACK);

                // 再改变当前选择页（position）对应的textview颜色
                switch (position) {
                    case 0:
                        tv1.setTextColor(Color.rgb(51, 153, 0));
                        break;
                    case 1:
                        tv2.setTextColor(Color.rgb(51, 153, 0));
                        break;
                    case 2:
                        tv3.setTextColor(Color.rgb(51, 153, 0));
                        break;
                }

                currentPage = position;

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                Log.i("tuzi", arg0 + "," + arg1 + "," + arg2);

                // 取得该控件的实例
                LinearLayout.LayoutParams ll = (android.widget.LinearLayout.LayoutParams) tabline
                        .getLayoutParams();

                if (currentPage == 0 && arg0 == 0) { // 0->1移动(第一页到第二页)
                    ll.leftMargin = (int) (currentPage * tabLineLength + arg1
                            * tabLineLength);
                } else if (currentPage == 1 && arg0 == 1) { // 1->2移动（第二页到第三页）
                    ll.leftMargin = (int) (currentPage * tabLineLength + arg1
                            * tabLineLength);
                } else if (currentPage == 1 && arg0 == 0) { // 1->0移动（第二页到第一页）
                    ll.leftMargin = (int) (currentPage * tabLineLength - ((1 - arg1) * tabLineLength));
                } else if (currentPage == 2 && arg0 == 1) { // 2->1移动（第三页到第二页）
                    ll.leftMargin = (int) (currentPage * tabLineLength - (1 - arg1)
                            * tabLineLength);
                }

                tabline.setLayoutParams(ll);

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        openSystemVideoCamera();
    }
    /**
     * 打开系统相机
     */
    private void openSystemCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String imageName = String.format(Locale.getDefault(), "img_%d.jpg", System.currentTimeMillis());
        imagePath = new File(getExternalFilesDir(""), imageName);
        Uri outUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", imagePath);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
        startActivityForResult(cameraIntent, REQUEST_CAMERA);
    }

    private void openSystemVideoCamera(){
        Intent videoCameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        //质量
        videoCameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
        //限制时长
        videoCameraIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);
        //限制大小
        videoCameraIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 1024*1024*20L);


        String videoName = String.format(Locale.getDefault(), "vid_%d.mp4", System.currentTimeMillis());
        videoPath = new File(getExternalFilesDir(""), videoName);
        // videoPath = new File("/storage/emulated/0/DCIM/Camera/", videoName);
        // videoPath = /storage/emulated/0/Android/data/com.androidcourse.toktik/files/vid_1626779108060.mp4
        Uri outUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", videoPath);
        videoCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
        startActivityForResult(videoCameraIntent, REQUEST_VIDEO_CAMERA);

//        File mediaStorageDir = Environment.getExternalStorageDirectory();
//        this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(mOutputFile.getAbsolutePath()))));
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_VIDEO_CAMERA && resultCode == RESULT_OK){
            toVideoPush();
//        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
//            Glide.with(this).load(imagePath).into(mImageView);
//        }
        }
    }

    /**
     * 跳转到视频上传界面，并将新拍摄的视频的 绝对地址 传递过去
     */
    private void toVideoPush(){
        Intent intent = new Intent(VideoCameraActivity.this, VideoPushActivity.class);
        Bundle bundle=new Bundle();
        //传递name参数为tinyphp
        bundle.putString("videoPath", videoPath.getAbsolutePath());
        intent.putExtras(bundle);

        startActivity(intent);
    }
}
