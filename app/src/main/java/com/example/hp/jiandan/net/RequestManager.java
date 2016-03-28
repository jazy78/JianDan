package com.example.hp.jiandan.net;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.hp.jiandan.APP;

/**
 * Created by hp on 2016/3/8.
 */
public class RequestManager {

    public static final int OUT_TIME = 10000;
    public static final int TIMES_OF_RETRY = 1;
    public static RequestQueue mRequeue = Volley.newRequestQueue(APP.getmContext());

    public static void addRequest(Request<?> request,Object tag){

        if(tag!=null){
            request.setTag(tag);
        }
        //给每个请求重设超时、重试次数,防止程序崩溃
        request.setRetryPolicy(new DefaultRetryPolicy(
                OUT_TIME,TIMES_OF_RETRY,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        mRequeue.add(request);
    }


    public static void canceAll(Object tag) {

        mRequeue.cancelAll(tag);
    }


}


