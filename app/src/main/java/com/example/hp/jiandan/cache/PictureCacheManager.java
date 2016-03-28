package com.example.hp.jiandan.cache;

import android.content.Context;

import com.example.hp.jiandan.APP;
import com.example.hp.jiandan.model.Picture;
import com.example.hp.jiandan.net.JSONParser;
import com.google.gson.reflect.TypeToken;
import com.source.greendao.DaoSession;
import com.source.greendao.PictureCache;
import com.source.greendao.PictureCacheDao;

import java.util.ArrayList;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by hp on 2016/3/15.
 */
public class PictureCacheManager extends BaseCache<Picture> {

    private static PictureCacheManager instance;
    private static PictureCacheDao pictureCacheDao;


    public static PictureCacheManager getInstance(Context context) {

            synchronized (PictureCacheManager.class){
                if(instance==null){

                    instance=new PictureCacheManager();

                    mDaoSession= APP.getDaoSession(context);
                    pictureCacheDao=mDaoSession.getPictureCacheDao();
                }

            }
       return  instance;
    }


    @Override
    public void clearAllCache() {
        pictureCacheDao.deleteAll();
    }

    @Override
    public ArrayList<Picture> getCacheByPage(int page) {
        QueryBuilder<PictureCache> query=pictureCacheDao.queryBuilder().where(
                PictureCacheDao.Properties.Page.eq(""+page)
        );

          if(query.list().size()>0){

            String jsonStr=query.list().get(0).getResult();
            ArrayList<Picture> pictures= (ArrayList<Picture>) JSONParser.toObject(jsonStr,new TypeToken<ArrayList<Picture>>(){}.getType());
             return  pictures;
        }


        return new ArrayList<>();
    }

    @Override
    public void addResultCache(String result, int page) {

        PictureCache cahce=new PictureCache();
        cahce.setResult(result);
        cahce.setPage(page);
        cahce.setTime(System.currentTimeMillis());
        pictureCacheDao.insert(cahce);

    }
}
