package com.example.hp.jiandan.net;

import android.app.Activity;

/**
 * Created by hp on 2016/3/15.
 */
public class ScreenSizeUtil {

    public static int getScreenWidth(Activity activity){

        return  activity.getWindowManager().getDefaultDisplay().getWidth();
    }


    public static  int getScreenHeight(Activity activity){

        return  activity.getWindowManager().getDefaultDisplay().getHeight();
    }
}
