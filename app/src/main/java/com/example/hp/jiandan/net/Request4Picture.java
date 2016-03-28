package com.example.hp.jiandan.net;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.example.hp.jiandan.model.Picture;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by hp on 2016/3/15.
 */
public class Request4Picture extends com.android.volley.Request<ArrayList<Picture>> {

    private Response.Listener<ArrayList<Picture>> listListener;

    public Request4Picture(String url, Response.Listener<ArrayList<Picture>> listListener1, Response.ErrorListener listener) {
        super(Method.GET,url, listener);
        this.listListener=listListener1;
    }

    @Override
    protected Response<ArrayList<Picture>> parseNetworkResponse(NetworkResponse response) {
        try {

            String jsonStr=new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            String string=new JSONObject(jsonStr).getJSONArray("comments").toString();
            ArrayList<Picture> pictures=(ArrayList<Picture>)JSONParser.toObject(string,new TypeToken<ArrayList<Picture>>(){}.getType());
            Log.d("DDD","pictures.size()="+pictures.size());
            return  Response.success(pictures,HttpHeaderParser.parseCacheHeaders(response));

        }catch (Exception e){
            e.printStackTrace();
            return Response.error(new ParseError(e));

        }

    }

    @Override
    protected void deliverResponse(ArrayList<Picture> response) {
        listListener.onResponse(response);
    }
}
