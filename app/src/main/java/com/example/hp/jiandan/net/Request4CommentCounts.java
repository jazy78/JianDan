package com.example.hp.jiandan.net;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.example.hp.jiandan.model.CommentNumber;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by hp on 2016/3/10.
 */
public class Request4CommentCounts extends Request<ArrayList<CommentNumber>> {


    Response.Listener<ArrayList<CommentNumber>> listListener;
    public Request4CommentCounts(String url, Response.Listener<ArrayList<CommentNumber>> listListener,Response.ErrorListener errorListener) {
        super(Method.GET,url, errorListener);
        this.listListener=listListener;
    }

    @Override
    protected Response<ArrayList<CommentNumber>> parseNetworkResponse(NetworkResponse response) {

        Log.d("QQQ","NetworkResponse="+response);
        Log.d("QQQ","response.headers="+response.headers);
        Log.d("QQQ","paraset"+HttpHeaderParser.parseCharset(response.headers));

        try{
            String stringStr=new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            JSONObject jsonObject=new JSONObject(stringStr).getJSONObject("response");

            String[] comment_IDS=getUrl().split("\\=")[1].split("\\,");
          for(int i=0;i<comment_IDS.length;i++){

              Log.d("CCC","IDS="+i+comment_IDS[i]);
          }
            ArrayList<CommentNumber> commentNumbers = new ArrayList<>();
           for(String comment_ID:comment_IDS){

               if(!jsonObject.isNull(comment_ID)){
                   CommentNumber commentNumber=new CommentNumber();
                   commentNumber.setComments(jsonObject.getJSONObject(comment_ID).getInt(CommentNumber.COMMENTS));
                   commentNumber.setThread_id(jsonObject.getJSONObject(comment_ID).optString(CommentNumber.THREAD_ID));
                   commentNumber.setThread_key(jsonObject.getJSONObject(comment_ID).getString(CommentNumber.THREAD_KEY));
                   commentNumbers.add(commentNumber);
               }else {
                   //可能会出现没有对应id的数据的情况，为了保证条数一致，添加默认数据
                   commentNumbers.add(new CommentNumber("0", "0", 0));

               }

           }
            Log.d("CCC","commentNumbers="+commentNumbers.size());
            return Response.success(commentNumbers,HttpHeaderParser.parseCacheHeaders(response));

        }catch (Exception e){

            e.printStackTrace();
            return  Response.success(new ArrayList<CommentNumber>(),HttpHeaderParser.parseCacheHeaders(response));
        }

    }

    @Override
    protected void deliverResponse(ArrayList<CommentNumber> response) {
              listListener.onResponse(response);
    }
}
