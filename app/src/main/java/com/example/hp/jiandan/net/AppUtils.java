package com.example.hp.jiandan.net;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by hp on 2016/3/18.
 */
public class AppUtils {


    public static String getVersionName(Activity activity) {

        PackageManager packageManager = activity.getPackageManager();
        PackageInfo packageInfo = null;

        try {
            packageInfo = packageManager.getPackageInfo(activity.getPackageName(), 0);
            String version = packageInfo.versionName;
            return  version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "0";
        }

    }
}
