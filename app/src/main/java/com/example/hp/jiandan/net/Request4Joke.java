package com.example.hp.jiandan.net;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.example.hp.jiandan.Activity.ImageDetailActivity;
import com.example.hp.jiandan.model.Joke;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by hp on 2016/3/18.
 */
public class Request4Joke extends Request<ArrayList<Joke>> {

    private Response.Listener<ArrayList<Joke>> listListener;

    public Request4Joke(String url,Response.Listener<ArrayList<Joke>> listListener, Response.ErrorListener listener) {
        super(Method.GET,url, listener);
        this.listListener=listListener;
    }

    @Override
    protected Response<ArrayList<Joke>> parseNetworkResponse(NetworkResponse response) {

        try {
            String string=new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            String jsonObject= new JSONObject(string).getJSONArray("comments").toString();
            return Response.success((ArrayList<Joke>) JSONParser.toObject(jsonObject,new TypeToken<ArrayList<Joke>>(){}.getType()),
                    HttpHeaderParser.parseCacheHeaders(response));

        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }


    }

    @Override
    protected void deliverResponse(ArrayList<Joke> response) {
          listListener.onResponse(response);
    }
}
