package com.example.hp.jiandan.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by hp on 2016/3/16.
 */
public class FileUtil {


    /**
     * 复制文件
     *
     * @param src 源文件
     * @param dst 目标文件
     * @return
     */
    public static boolean copyTo(File src, File dst) {

        FileInputStream inputStream=null;
        FileOutputStream outputStream=null;
        FileChannel in = null;
        FileChannel out = null;

        try {
            inputStream = new FileInputStream(src);
            outputStream = new FileOutputStream(dst);
            in = inputStream.getChannel();
            out = outputStream.getChannel();
            in.transferTo(0, in.size(), out);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }finally {

            try {

                if (inputStream != null) {
                    inputStream.close();
                }

                if (in != null) {
                    in.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }

                if (out != null) {
                    out.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }


    }

    public static double getDirSize(File file){

        if(file.exists()){
            //如果是目录则递归计算其内容的总大小
            if(file.isDirectory()){
                File[] childen=file.listFiles();
                double size=0;
                for(File f:childen){

                    size+=getDirSize(f);
                }

                return size;
            }else {

                return (double)file.length();
            }
        }else {

            return 0.0;
        }

    }

}
