package com.example.hp.jiandan.net;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by hp on 2016/3/15.
 */
public class TextUtil {

    public static void copy(Activity activity,String copytext){
        ClipboardManager clip=(ClipboardManager)activity.getSystemService(Context.CLIPBOARD_SERVICE);
        clip.setPrimaryClip(ClipData.newPlainText(null,copytext));
        Toast.makeText(activity,"复制成功",Toast.LENGTH_LONG).show();
    }
}
