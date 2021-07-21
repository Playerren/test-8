package com.androidcourse.toktik.player;

import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;

public class ProxyServer {
    private static HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy(Context context) {
        return proxy==null?(proxy = newProxy(context)):proxy;
    }

    private static HttpProxyCacheServer newProxy(Context context) {
        return new HttpProxyCacheServer(context);
    }
}
