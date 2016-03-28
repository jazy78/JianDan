package com.example.hp.jiandan.Database;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hp on 2016/3/18.
 */
public class DataBaseManager {

    public static DataBaseManager manager;
    public static SharedPreferences sharedPreferences;

    public DataBaseManager getInstance(Context context) {

        if (manager == null) {

            synchronized (DataBaseManager.class) {


                manager = new DataBaseManager();
                sharedPreferences = context.getSharedPreferences("filename", Context.MODE_PRIVATE);
            }

        }
        return manager;
    }


    public static void SaveDataBase(Context context, String content, String key) {

        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, content);
            editor.commit();
        }

    }


    public static String  queryDataBase(String key){

        if(sharedPreferences!=null){

            String base=sharedPreferences.getString(key,null);
              return base;
        }
      return  null;
    }


}
