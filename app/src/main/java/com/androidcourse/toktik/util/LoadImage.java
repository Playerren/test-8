package com.androidcourse.toktik.util;

import android.graphics.drawable.Drawable;

import com.androidcourse.toktik.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadImage {

    private LoadImage() {
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        InputStream is;
        Drawable d;
        try {
            is = (InputStream) new URL(url).getContent();
            d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    public static InputStream getImageViewInputStream(String urlString) throws IOException {
        InputStream inputStream = null;
        URL url = new URL(urlString);                    //服务器地址
        //打开连接
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setConnectTimeout(3000);//设置网络连接超时的时间为3秒
        httpURLConnection.setRequestMethod("GET");        //设置请求方法为GET
        httpURLConnection.setDoInput(true);                //打开输入流
        int responseCode = httpURLConnection.getResponseCode();    // 获取服务器响应值
        if (responseCode == HttpURLConnection.HTTP_OK) {        //正常连接
            inputStream = httpURLConnection.getInputStream();        //获取输入流
        }
        return inputStream;
    }

    public static Integer getResourceId(String url) {
        switch (url) {
            case "http://8.136.101.204/v/%E9%A5%BA%E5%AD%90%E5%A5%BD%E5%A6%88%E5%A6%88.jpg":
                return R.raw.th_01;
            case "http://8.136.101.204/v/%E9%A5%BA%E5%AD%90%E8%BF%98%E5%B9%B4%E8%BD%BB.jpg":
                return R.raw.th_02;
            case "http://8.136.101.204/v/%E9%A5%BA%E5%AD%90%E6%8C%BA%E4%BD%8F.jpg":
                return R.raw.th_03;
            case "http://8.136.101.204/v/%E9%A5%BA%E5%AD%90%E5%8F%AF%E4%BB%A5.jpg":
                return R.raw.th_04;
            case "http://8.136.101.204/v/%E9%A5%BA%E5%AD%90%E6%83%B3%E5%90%AC.jpg":
                return R.raw.th_05;
            case "http://8.136.101.204/v/%E9%A5%BA%E5%AD%90%E7%9C%9F%E4%BC%9A.jpg":
                return R.raw.th_06;
            case "http://8.136.101.204/v/%E9%A5%BA%E5%AD%90%E7%9C%9F%E8%90%8C.jpg":
                return R.raw.th_07;
            default:
                return -1;
        }
    }

}
