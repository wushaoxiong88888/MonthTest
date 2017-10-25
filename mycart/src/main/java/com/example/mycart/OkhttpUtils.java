package com.example.mycart;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by pc on 2017/10/17.
 */

public class OkhttpUtils {

    private static OkhttpUtils okhttpUtils;
    private OkHttpClient okHttpClient;
    private Handler handler = new Handler(Looper.getMainLooper());

    public OkhttpUtils(){
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

    }

    //单例模式
    public static OkhttpUtils getInstance(){
        if(okhttpUtils==null){
            synchronized (OkhttpUtils.class){
                if(okhttpUtils==null){
                    okhttpUtils = new OkhttpUtils();
                }
            }
        }
        return okhttpUtils;
    }

    public void doGet(String url, final Class clazz, final OnNetListener onNetListener){

        Request request = new Request.Builder()
                .url(url)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.e("TAG-------",string);
                final ItemBean itemBean = (ItemBean) new Gson().fromJson(string,clazz);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onNetListener.onSuccess(itemBean);
                    }
                });
            }
        });

    }

}
