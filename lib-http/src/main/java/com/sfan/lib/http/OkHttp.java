package com.sfan.lib.http;

import android.os.Environment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by zhazhiyong on 2018/4/4.
 * OkHttp 网络Http请求工具
 */

public class OkHttp {

    private static OkHttpClient okHttpClient = null;

    private OkHttp() {
    }

    // 通过单例模式获取实例
    private static OkHttpClient getInstance() {
        if (okHttpClient == null) {
            // 同步代码
            synchronized (OkHttp.class) {
                // 缓存路径
                File sdCache = new File(Environment.getExternalStorageDirectory(), "/file/okhttp/request/cache");
                // 最大缓存
                int cacheSize = 10 * 1024 * 1024;// 10M
                okHttpClient = new OkHttpClient.Builder()
                        .addInterceptor(new CacheInterceptor())
                        // 缓存配置
                        .cache(new Cache(sdCache.getAbsoluteFile(), cacheSize))
                        // 连接超时20s
                        .connectTimeout(20, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();
            }
        }
        return okHttpClient;
    }

    /**
     * GET请求
     *
     * @param url      请求地址
     * @param callback 回调接口
     */
    public static void get(String url, Callback callback) {
        // 创建Request
        Request request = new Request.Builder().url(url).build();
        // 执行异步请求
        getInstance().newCall(request).enqueue(callback);
    }

    /**
     * POST请求
     *
     * @param url      请求地址
     * @param params   请求体
     * @param callback 接口回调
     */
    public static void post(String url, Map<String, String> params, Callback callback) {
        // 遍历map集合给请求体添加值
        Set<String> keys = params.keySet();
        JSONObject jsonObj = new JSONObject();
        try {
            for (String key : keys) {
                jsonObj.put(key, params.get(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // JSON字符
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObj.toString());
        // 创建Request
        Request request = new Request.Builder().url(url).post(requestBody).build();
        // 异步请求
        getInstance().newCall(request).enqueue(callback);
    }

}
