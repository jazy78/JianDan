package com.example.hp.jiandan.net;

import android.os.Environment;

/**
 * Created by hp on 2016/3/17.
 */
public class SDCardUtils {

    public static boolean hasSdCard(){
        //判断SDCard的挂载状况
        String status= Environment.getExternalStorageState();

        if(status.equals(Environment.MEDIA_MOUNTED)){

            return true;
        }else {
            return false;
        }

    }

}
