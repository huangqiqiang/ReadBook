/*
 * Create on 2016-12-14 下午3:54
 * FileName: OkHttpImpl.java
 * Author: huang qiqiang
 * Contact: http://www.huangqiqiang.cn
 * Email 593979591@QQ.com
 *
 */

package com.qq.readbook.net.ok;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * $desc$
 * author  黄其强
 * Created by Administrator on 2016/12/14 15:54.
 */

public class OkHttpImpl implements HttpCompat {

    OkHttpClient mOkHttpClient;
    Handler mHandler;
    public final static int WRITE_TIMEOUT = 60;
    private static String TAG = "OkHttpImpl";

    @Override
    public HttpCompat create() {
        mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//设置写的超时时
                .connectTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .build();
        mHandler = new Handler(Looper.getMainLooper());
        return this;
    }


    @Override
    public void get(String url, ParamsCompat params, final OkNetCallback callback) {
        final String realUrl;
        if (params != null) {
            realUrl = url + '?' + params.paramGet();
        } else {
            realUrl = url;
        }
        Request.Builder request = new Request.Builder().url(realUrl).get();
        mOkHttpClient.newCall(request.build()).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure("001", "网络连接失败", "");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String string = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess("", string);
                    }
                });
            }
        });
    }

    @Override
    public void post(String url, ParamsCompat params, OkNetCallback callback) {
        RequestBody body = params.paramForm();
        Request.Builder request = new Request.Builder().url(url).post(body);
        doRequest(url + '?' + params, request, callback);
    }

    private void doRequest(final String allUrl, Request.Builder request, final OkNetCallback callback) {

        mOkHttpClient.newCall(request.build()).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Log.e(TAG, "请求地址 onFailure: " + allUrl);
                Log.e(TAG, "IOException onFailure: " + e.toString());
                postHandler(mHandler, callback, false, 0, "网络连接失败,请检查网络");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final int code = response.code();
                final String string = response.body().string();
                Log.e(TAG, "请求地址 onResponse: " + allUrl + "\n" + string);
                postHandler(mHandler, callback, response.isSuccessful(), code, string);
            }
        });
    }

    private static void postHandler(Handler handler, final OkNetCallback callback
            , final boolean successful, final int statusCode, final String responseString) {
        handler.post(new Runnable() {
                         @Override
                         public void run() {
                             if (successful) {
                                 callback.onSuccess(statusCode + "", responseString);
                                 return;
                             } else {
                                 callback.onFailure(String.valueOf(statusCode), "网络连接失败,请检查网络", "");
                             }
                         }
                     }

        );
    }

}
