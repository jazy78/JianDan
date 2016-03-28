package com.example.hp.jiandan.net;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.hp.jiandan.R;
import com.example.hp.jiandan.base.BaseActivity;
import com.example.hp.jiandan.callback.SaveFileCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.IOException;

/**
 * Created by hp on 2016/3/15.
 */
public class ShareUtil {

    public static void shareText(Activity activity, String shareText) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, shareText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(Intent.createChooser(intent, activity.getResources().getString(R.string.app_name)));

    }

    public static void sharePicture(Activity activity, String url) throws IOException {

        String[] urlSplits = url.split("\\.");
        File cacheFile = ImageLoader.getInstance().getDiskCache().get(url);
        Log.d("GGG", " url=" + url);

        File newFile = new File(CacheUtil.getSharePicName
                (cacheFile, urlSplits));

        if (newFile.exists()) {

            newFile.delete();
        }
        newFile.createNewFile();

        if (FileUtil.copyTo(cacheFile, newFile)) {

            ShareUtil.sharePicture(activity, newFile.getAbsolutePath(), "煎蛋" + url);

        } else {

            Toast.makeText(activity, "分享失败", Toast.LENGTH_LONG).show();

        }

    }

    public static void sharePicture(Activity activity, final String imgPath, String shareText) {

        Intent intent = new Intent(Intent.ACTION_SEND);


        File f = new File(imgPath);

        if (f.exists() && f != null && f.isFile()) {

            intent.setType("image/*");
            //输出途径
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
        } else {

            Toast.makeText(activity, "分享图片不存在哦", Toast.LENGTH_LONG).show();
            return;
        }

        //GIF图片指明出处url，其他图片指向项目地址
        intent.putExtra(Intent.EXTRA_TEXT, shareText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(Intent.createChooser(intent, activity.getResources().getString(R
                .string.app_name)));
    }

    public static void savePicture(Activity activity, String picUrl, SaveFileCallBack saveFileCallBack){

        boolean isSmallPic = false;
        String[] urls = picUrl.split("\\.");
        File cacheFile = ImageLoader.getInstance().getDiskCache().get(picUrl);

        File picDir = new File(CacheUtil.getSaveDirPath());

        if (!picDir.exists()) {
            picDir.mkdir();
        }

        final File newFile = new File(picDir, CacheUtil.getSavePicName(cacheFile, urls));

        if(FileUtil.copyTo(cacheFile,newFile)){

            Bundle bundle = new Bundle();
            bundle.putBoolean(BaseActivity.DATA_IS_SIAMLL_PIC, isSmallPic);
            bundle.putString(BaseActivity.DATA_FILE_PATH, newFile.getAbsolutePath());
            saveFileCallBack.SaveFinsh(bundle);
        }else {

            Toast.makeText(activity,"保存失败",Toast.LENGTH_SHORT).show();
        }

    }


}
