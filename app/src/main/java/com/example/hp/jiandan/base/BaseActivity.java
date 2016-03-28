package com.example.hp.jiandan.base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.example.hp.jiandan.APP;
import com.example.hp.jiandan.BuildConfig;
import com.example.hp.jiandan.R;
import com.example.hp.jiandan.logger.LogLevel;
import com.example.hp.jiandan.logger.Logger;
import com.example.hp.jiandan.net.RequestManager;

/**
 * Created by hp on 2016/3/7.
 */
public abstract class BaseActivity extends AppCompatActivity implements ConstantString {

    protected Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        if (BuildConfig.DEBUG) {
            Logger.init(getClass().getSimpleName()).setLogLevel(LogLevel.FULL).hideThreadInfo();
        } else {
            Logger.init(getClass().getSimpleName()).setLogLevel(LogLevel.NONE).hideThreadInfo();
        }

    }
    protected abstract void initView();

    protected abstract void initData();

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_none,R.anim.trans_center_2_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        APP.getRefWatcher(this).watch(this);
        RequestManager.canceAll(this);
    }

    public void executeRequest(Request<?> request){
        RequestManager.addRequest(request,this);

    }

    public void replaceFragment(int id_content, Fragment fragment){

        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(id_content,fragment);
        transaction.commit();
    }
}
