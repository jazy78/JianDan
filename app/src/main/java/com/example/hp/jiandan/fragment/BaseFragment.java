package com.example.hp.jiandan.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.android.volley.Request;
import com.example.hp.jiandan.APP;
import com.example.hp.jiandan.BuildConfig;
import com.example.hp.jiandan.base.ConstantString;
import com.example.hp.jiandan.imageloader.ImageLoadProxy;
import com.example.hp.jiandan.logger.LogLevel;
import com.example.hp.jiandan.logger.Logger;
import com.example.hp.jiandan.net.RequestManager;

/**
 * Created by hp on 2016/3/8.
 */
public class BaseFragment extends Fragment implements ConstantString {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BuildConfig.DEBUG) {
            Logger.init(getClass().getSimpleName()).setLogLevel(LogLevel.FULL).hideThreadInfo();
        } else {
            Logger.init(getClass().getSimpleName()).setLogLevel(LogLevel.NONE).hideThreadInfo();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        APP.getRefWatcher(getContext()).watch(this);
        RequestManager.canceAll(this);
        ImageLoadProxy.getImageLoader().clearMemoryCache();

    }

    protected void executeRequest(Request request){

        RequestManager.addRequest(request,this);
    }
}
