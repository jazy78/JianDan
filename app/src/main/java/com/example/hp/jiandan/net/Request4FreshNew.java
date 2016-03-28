package com.example.hp.jiandan.net;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.example.hp.jiandan.model.FreshNews;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by hp on 2016/3/9.
 */
public class Request4FreshNew extends Request<ArrayList<FreshNews>> {

    private Response.Listener<ArrayList<FreshNews>> listener;

    public Request4FreshNew(String url, Response.Listener<ArrayList<FreshNews>> listener,
                            Response.ErrorListener errorListener) {
        super(Method.GET,url,errorListener);
        this.listener=listener;
    }

    @Override
    protected Response<ArrayList<FreshNews>> parseNetworkResponse(NetworkResponse networkResponse) {
        Log.d("AAA","networkResponse");
        // networkResponse 是网络请求返回的数据 通过解析返回到 deliverResponse(ArrayList<FreshNews> freshNewses) 中
        try {
            String resultStr = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
            Log.d("AAA","networkResponse="+networkResponse);
            Log.d("AAA","resultStr="+resultStr);
            JSONObject resultObj = new JSONObject(resultStr);
            JSONArray postsArray = resultObj.optJSONArray("posts");
            return Response.success(FreshNews.parse(postsArray), HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(ArrayList<FreshNews> freshNewses) {
        Log.d("AAA","deliverResponse");
        //将数据回调出去
        listener.onResponse(freshNewses);
    }
}
