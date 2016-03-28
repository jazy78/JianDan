package com.example.hp.jiandan.cache;

import android.content.Context;
import android.util.Log;

import com.example.hp.jiandan.APP;
import com.example.hp.jiandan.model.Video;
import com.example.hp.jiandan.net.JSONParser;
import com.google.gson.reflect.TypeToken;
import com.source.greendao.VideoCache;
import com.source.greendao.VideoCacheDao;

import java.util.ArrayList;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by hp on 2016/3/15.
 */
public class VideoCacheManager  extends BaseCache<Video>{

     private static VideoCacheManager instance;
     private static VideoCacheDao videoCacheDao;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public static VideoCacheManager getInstance(Context context){

        synchronized (VideoCacheManager.class){

            if(instance==null){

                instance=new VideoCacheManager();
                mDaoSession= APP.getDaoSession(context);
                videoCacheDao=mDaoSession.getVideoCacheDao();
            }

        }
        return  instance;
    }

    @Override
    public void clearAllCache() {
        videoCacheDao.deleteAll();
    }

    @Override
    public ArrayList<Video> getCacheByPage(int page) {

        QueryBuilder<VideoCache> queryBuilder=videoCacheDao.queryBuilder()
                .where(VideoCacheDao.Properties.Page.eq(""+page));

        if(queryBuilder.list().size()>0){
            String string=queryBuilder.list().get(0).getResult();
            Log.d("DDD","string="+string);
            return  ((ArrayList<Video>) JSONParser.toObject(string,new TypeToken<ArrayList<Video>>(){}.getType()));
        }else {

            return new ArrayList<>();
        }
    }

    @Override
    public void addResultCache(String result, int page) {
        Log.d("DDD","Vidoresult="+result);
        VideoCache cache=new VideoCache();
        cache.setResult(result);
        cache.setPage(page);
        cache.setTime(System.currentTimeMillis());
        videoCacheDao.insert(cache);

    }
}
