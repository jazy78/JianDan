package com.example.hp.jiandan.cache;

import android.content.Context;

import com.example.hp.jiandan.APP;
import com.example.hp.jiandan.model.Joke;
import com.example.hp.jiandan.net.JSONParser;
import com.google.gson.reflect.TypeToken;
import com.source.greendao.JokeCache;
import com.source.greendao.JokeCacheDao;

import java.util.ArrayList;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by hp on 2016/3/18.
 */
public class JokeManager extends BaseCache<Joke> {
    public static JokeManager manager;
    public  static JokeCacheDao jokeCacheDao;

    public static JokeManager getInstance(Context context){

        if(manager==null){

            synchronized (JokeManager.class){

                manager=new JokeManager();
                mDaoSession= APP.getDaoSession(context);
                jokeCacheDao=mDaoSession.getJokeCacheDao();

            }

        }
        return manager;
    }

    @Override
    public void clearAllCache() {
        jokeCacheDao.deleteAll();
    }

    @Override
    public ArrayList<Joke> getCacheByPage(int page) {

        QueryBuilder<JokeCache> query=jokeCacheDao.queryBuilder().where(
                JokeCacheDao.Properties.Page.eq(""+page));

        if(query.list().size()>0){

            String content=query.list().get(0).getResult();

            return ((ArrayList<Joke>) JSONParser.toObject(content,new TypeToken<ArrayList<Joke>>(){}.getType()));

        }else {

            return new ArrayList<>();
        }

    }

    @Override
    public void addResultCache(String result, int page) {

        JokeCache cache =new JokeCache();
        cache.setResult(result);
        cache.setPage(page);
        cache.setTime(System.currentTimeMillis());

        jokeCacheDao.insert(cache);

    }
}
