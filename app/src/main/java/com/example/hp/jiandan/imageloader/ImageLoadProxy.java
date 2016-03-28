package com.example.hp.jiandan.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.example.hp.jiandan.BuildConfig;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by hp on 2016/3/7.
 */
public class ImageLoadProxy {

    private static final int MAX_DISK_CACHE = 1024 * 1024 * 50;
    private static final int MAX_MENMORY_CACHE = 1024 * 1024 * 10;
    private static boolean isShowLog = false;

    private static ImageLoader imageLoader;
    private ImageLoaderConfiguration configuration;


    public static ImageLoader getImageLoader() {
        if (imageLoader == null) {
            synchronized (ImageLoadProxy.class) {
                imageLoader = ImageLoader.getInstance();

            }

        }
        return imageLoader;
    }

    public static void initImageLoader(Context context) {

        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(context);
        builder.tasksProcessingOrder(QueueProcessingType.LIFO);//设置图片加载和显示队列处理的类型
        builder.diskCacheSize(MAX_DISK_CACHE);
        builder.memoryCacheSize(MAX_MENMORY_CACHE);
        builder.memoryCache(new LruMemoryCache(MAX_MENMORY_CACHE));//缓存策略
        getImageLoader().init(builder.build());
    }


    public static DisplayImageOptions getOption4PictureList(int loadingResourse){
        return  new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .showImageOnLoading(loadingResourse)
                .showImageForEmptyUri(loadingResourse)
                .showImageOnFail(loadingResourse)
                .build();


    }


    /**
     * 自定义Option
     *
     * @param url
     * @param target
     * @param options
     */
    public static void displayImage(String url, ImageView target, DisplayImageOptions options) {
        imageLoader.displayImage(url, target, options);
    }

    public static void displayImage(String url,ImageView target, int laodResoure){
        imageLoader.displayImage(url,target,getOption4PictureList(laodResoure));
        Log.d("EEE","url="+url);

    }

    public static void displayImageList(String url, ImageView target, int loadResource, SimpleImageLoadingListener simpleBitmapDisplayer,
                                 ImageLoadingProgressListener listener){
        imageLoader.displayImage(url, target, getOption4PictureList(loadResource),simpleBitmapDisplayer,listener);

    }
    public static void displayImage4Detail(String url, ImageView target, SimpleImageLoadingListener loadingListener) {
        imageLoader.displayImage(url, target, getOption4ExactlyType(), loadingListener);
    }
    /**
     * 设置图片放缩类型为模式EXACTLY，用于图片详情页的缩放
     *
     * @return
     */
    public static DisplayImageOptions getOption4ExactlyType() {
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
    }


    public static void loadImageFromLocalCache(String url, SimpleImageLoadingListener loadingListener) {
        imageLoader.loadImage(url, getOption4ExactlyType(), loadingListener);
    }
}
