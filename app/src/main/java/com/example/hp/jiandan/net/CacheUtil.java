package com.example.hp.jiandan.net;

import android.os.Environment;

import java.io.File;
import java.util.Random;

/**
 * Created by hp on 2016/3/16.
 */
public class CacheUtil {


    public static final String FILE_SAVE = "JianDan";

    public static String getSharePicName(File cacheFile, String[] urls) {
        return cacheFile.getAbsolutePath() + new Random().nextInt(100000) + "." + urls[urls
                .length - 1];
    }
    public static String getWallPicName(File cacheFile, String[] urls) {
        return  new Random().nextInt(100000) + "." + urls[urls
                .length - 1];
    }
    /**
     * 获取图片保存文件夹路径
     *
     * @return
     */
    public static String getSaveDirPath() {
        return Environment
                .getExternalStorageDirectory().getAbsolutePath() + File.separator + FILE_SAVE;
    }
    public static String getWallSaveDirPath() {
        return Environment
                .getExternalStorageDirectory().getAbsolutePath() + File.separator + "wallJianDan";
    }
    public static String getSavePicName(File cacheFile, String[] urls) {
        return "jiandan_" + cacheFile.getName().substring(0, 8) + "." +
                urls[urls.length - 1];
    }
}
