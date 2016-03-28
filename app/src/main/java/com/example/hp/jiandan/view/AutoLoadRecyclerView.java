package com.example.hp.jiandan.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.example.hp.jiandan.callback.LoadFinishCallBack;
import com.example.hp.jiandan.callback.LoadMoreListener;
import com.example.hp.jiandan.callback.LoadResultCallBack;
import com.example.hp.jiandan.imageloader.ImageLoadProxy;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by hp on 2016/3/8.
 */
public class AutoLoadRecyclerView extends RecyclerView implements LoadFinishCallBack {

    private LoadMoreListener loadMoreListener;
    private boolean isLoadingMore;

    public AutoLoadRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        isLoadingMore = false;
        this.addOnScrollListener(new AutoLoadScrollListener(null,true,true));
    }


    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public void setOnPauseListenerParams(boolean pauseOnScroll, boolean pauseOnFling) {
        addOnScrollListener(new AutoLoadScrollListener(ImageLoadProxy.getImageLoader(), pauseOnScroll, pauseOnFling));
    }

    @Override
    public void loadFinish(Object obj) {
           isLoadingMore=false;
    }


    /**
     * 滑动自动加载监听器
     */

    private class AutoLoadScrollListener extends OnScrollListener {
        private ImageLoader imageLoader;
        private final boolean pauseOnScroll;
        private final boolean pauseOnFling;

        public AutoLoadScrollListener(ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnFling) {

            this.pauseOnScroll = pauseOnScroll;
            this.pauseOnFling = pauseOnFling;
            this.imageLoader = imageLoader;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            //由于GridLayoutManager是LinearLayoutManager子类，所以也适
            if(getLayoutManager() instanceof LinearLayoutManager){

                //最后一个可见的item
                int lastitem=((LinearLayoutManager)getLayoutManager()).findLastVisibleItemPosition();

                int totalitemCount=getAdapter().getItemCount();
                if(loadMoreListener!=null&& !isLoadingMore&&lastitem>=totalitemCount-2
                        && dy>0){

                    loadMoreListener.loadMore();
                    isLoadingMore=true;
                }

            }

        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }
    }

}
