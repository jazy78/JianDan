package com.example.hp.jiandan.okHttp;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.TimeUtils;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

/**
 * Created by hp on 2016/3/14.
 */
public class OkHttpManger {

    public static OkHttpManger manger;
    public OkHttpClient okHttpClient;
    private Handler mDelivery;


 private synchronized  static OkHttpManger getInstance(){

      if(manger==null){

          manger=new OkHttpManger();
      }
     return manger;
 }
    public OkHttpManger() {
        okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(10, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(10, TimeUnit.SECONDS);
        //设置一个缓存管理
        okHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        mDelivery = new Handler(Looper.getMainLooper());//主线程的Handler

    }


    public   void get(String url, final MyCallBack callBack){
             getInstance();
        final Request request=new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {

                mDelivery.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onErrorResponse(e);
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String str=response.body().string();
                Log.d("DDD","String="+str);
                if(!TextUtils.isEmpty(str)){

                   mDelivery.post(new Runnable() {
                       @Override
                       public void run() {
                        callBack.onResponse( str);
                       }
                   });
                }

            }
        });


    }

  public   interface  MyCallBack{

        void  onResponse(String response);
        void  onErrorResponse(IOException e);

    }

}
