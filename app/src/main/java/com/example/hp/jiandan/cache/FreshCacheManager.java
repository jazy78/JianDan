package com.example.hp.jiandan.cache;

import android.content.Context;
import android.util.Log;

import com.example.hp.jiandan.APP;
import com.example.hp.jiandan.model.FreshNews;
import com.example.hp.jiandan.net.JSONParser;
import com.google.gson.reflect.TypeToken;
import com.source.greendao.FreshNewsCache;
import com.source.greendao.FreshNewsCacheDao;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by hp on 2016/3/9.
 */
public class FreshCacheManager extends BaseCache {

    private static FreshCacheManager instance;
    private static FreshNewsCacheDao mFreshNewsCacheDao;


    public static FreshCacheManager getInstance(Context context){
        if(instance==null){
            synchronized (FreshCacheManager.class){
                if(instance==null){

                    instance=new FreshCacheManager();
                }

            }
            mDaoSession = APP.getDaoSession(context);
            //获取数据库
            mFreshNewsCacheDao = mDaoSession.getFreshNewsCacheDao();
        }
        return  instance;
    }

    @Override
    public void clearAllCache() {
             mFreshNewsCacheDao.deleteAll();
    }

    @Override
    public ArrayList<FreshNews> getCacheByPage(int page) {

       QueryBuilder<FreshNewsCache> queryBuilder=mFreshNewsCacheDao.queryBuilder()
               .where(FreshNewsCacheDao.Properties.Page.eq(""+page));

        if(queryBuilder.list().size()>0){
            try{
                Log.d("DDD","Str="+queryBuilder.list().get(0).getResult());
             //   return  FreshNews.parse(new JSONArray(queryBuilder.list().get(0).getResult()));
                String string=queryBuilder.list().get(0).getResult();
                return  ((ArrayList<FreshNews>) JSONParser.toObject(string,
                        new TypeToken<ArrayList<FreshNews>>(){}.getType()));

            }catch (Exception e){
                e.printStackTrace();
            }

        }else {
            Log.d("DDD","大小为0");
            return  new ArrayList<>();

        }
        return null;
    }

    @Override
    public void addResultCache(String result, int page) {
        Log.d("DDD","result="+result);
        FreshNewsCache freshNewsCache=new FreshNewsCache();
        freshNewsCache.setResult(result);
        freshNewsCache.setPage(page);
        freshNewsCache.setTime(System.currentTimeMillis());
        //插入到数据库
        mFreshNewsCacheDao.insert(freshNewsCache);
    }
}
