package com.sfan.lib.app;

import android.app.Application;
import android.content.res.Configuration;

import java.lang.ref.SoftReference;

/**
 * Created by zhazhiyong on 2018/4/4.
 * Application
 */

public class MyApplication extends Application {

    private static SoftReference<Application> mContext;// 强引用，System.gc()不被回收

    public static Application getContext() {
        if (mContext == null || mContext.get() == null) {
            throw new NullPointerException("The Context is Null !");
        }
        return mContext.get();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 在创建应用程序时创建
        mContext = new SoftReference<Application>(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // 当后台程序已经终止资源还匮乏时会调用这个方法。好的应用程序一般会在这个方法里面
        // 释放一些不必要的资源来应付当后台程序已经终止，前台应用程序内存还不够时的情况。
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 配置改变时触发这个方法
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mContext = null;
        // 当终止应用程序对象时调用，不保证一定被调用，当程序是被内核终止以便为其他应用程序释放资源时，
        // 那么将不会提醒，并且不调用应用程序的对象的onTerminate方法而直接终止进程
    }
}
