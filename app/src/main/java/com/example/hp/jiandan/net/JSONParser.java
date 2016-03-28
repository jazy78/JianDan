package com.example.hp.jiandan.net;

import android.util.Log;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by hp on 2016/3/9.
 */
public class JSONParser {



    //将List 转换成String Gson 将Java类和String之间的转换
    protected  static Gson gson=new Gson();

    public  static  String  toString(Object obj) {

        return gson.toJson(obj);

    }

    public static Object toObject(String jsonString,Object type){

        if(type instanceof Type){
            Log.d("DDD","type="+type);
            return  gson.fromJson(jsonString, (Type) type);

        }else if(type instanceof Class<?>){

             return  gson.fromJson(jsonString,(Class<? extends Object>) type);
        }else {
            throw new RuntimeException("只能是Class<?>或者通过TypeToken获取的Type类型");

        }
    }

}
