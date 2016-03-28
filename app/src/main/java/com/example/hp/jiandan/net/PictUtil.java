/*
 *    Copyright 2015 TedXiong <xiong-wei@hotmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package com.example.hp.jiandan.net;

import android.graphics.Bitmap;
import android.os.Environment;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Ted on 2015/8/30.
 */
public class PictUtil {

    public static String getImageFileName(String url){
        String fileName = url.substring(url.lastIndexOf('/') + 1, url.length()) + (url.endsWith(".jpg")?"":".jpg");

        return fileName;
    }

    public static String getImageFilePath(String imageUrl){
        String savePath = getSavePath().getAbsolutePath();
        String fileName = getImageFileName(imageUrl);
        return savePath+File.separator+fileName;
    }

    public static void saveToFile(File file,Bitmap bmp)throws IOException{
        FileOutputStream out = new FileOutputStream(file);
        //把压缩后的图片存放到out里面
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
        out.flush();//把缓冲的东西写入到文件
        out.close();
    }

    public static boolean hasSDCard() {
        //判断是否有DDCARD
        String status = Environment.getExternalStorageState();//根目录
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    public static String getSDCardPath() {
        File path = Environment.getExternalStorageDirectory();
        return path.getAbsolutePath();
    }

    public static File getSavePath() {
        File path;
        if (hasSDCard()) { // SD card
            path = new File(getSDCardPath() + "/GankDownload");
            path.mkdir();
        } else {
            path = Environment.getDataDirectory();
        }
        return path;
    }
}
