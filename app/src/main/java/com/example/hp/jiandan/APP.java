package com.example.hp.jiandan;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.view.animation.AnimationSet;

import com.example.hp.jiandan.cache.BaseCache;

import com.example.hp.jiandan.imageloader.ImageLoadProxy;
import com.example.hp.jiandan.logger.LogLevel;
import com.facebook.stetho.Stetho;
import com.source.greendao.DaoMaster;
import com.source.greendao.DaoSession;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.logging.Logger;


/**
 * Created by hp on 2016/3/7.
 */
public class APP extends Application {
    public static int COLOR_OF_DIALOG = R.color.primary;
    public static int COLOR_OF_DIALOG_CONTENT = Color.WHITE;
    private static Context mContext;
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();

        refWatcher= LeakCanary.install(this);

        mContext=this;
        ImageLoadProxy.initImageLoader(this);
        if(BuildConfig.DEBUG){
            com.example.hp.jiandan.logger.Logger.init().hideThreadInfo().setMethodCount(1).setLogLevel(LogLevel.FULL);
       }

         Stetho.initialize(Stetho.newInitializerBuilder(this)
        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
        .build());

    }

      public static  Context getmContext(){
          return mContext;

      }
    public static  RefWatcher getRefWatcher(Context context){

        APP app=(APP)context.getApplicationContext();
        return app.refWatcher;
    }

    public static DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {
            //DaoMaster 接受一个SQLitDatabase
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context, BaseCache.DB_NAME, null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }


    public static DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }
}
