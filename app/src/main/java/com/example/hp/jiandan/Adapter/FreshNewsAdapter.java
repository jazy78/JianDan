package com.example.hp.jiandan.Adapter;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.hp.jiandan.R;
import com.example.hp.jiandan.cache.FreshCacheManager;
import com.example.hp.jiandan.callback.LoadFinishCallBack;
import com.example.hp.jiandan.callback.LoadResultCallBack;
import com.example.hp.jiandan.imageloader.ImageLoadProxy;
import com.example.hp.jiandan.model.FreshNews;
import com.example.hp.jiandan.net.JSONParser;
import com.example.hp.jiandan.net.NetWorkUtil;
import com.example.hp.jiandan.net.Request4FreshNew;
import com.example.hp.jiandan.net.RequestManager;
import com.example.hp.jiandan.net.ShareUtil;
import com.example.hp.jiandan.okHttp.OkHttpManger;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

/**
 * Created by hp on 2016/3/8.
 */
public class FreshNewsAdapter extends RecyclerView.Adapter<FreshNewsAdapter.ViewHolder> {

    private LoadFinishCallBack mLoadFinisCallBack;
    private LoadResultCallBack mLoadResultCallBack;
    private boolean isLargeMode;
    private Activity mActivity;
    private ArrayList<FreshNews> mFreshNews;
    private int page;
    private int lastPosition = -1;
    private DisplayImageOptions options;
    private OkHttpManger okHttpManger;


    public FreshNewsAdapter(Activity activity, LoadFinishCallBack loadFinisCallBack, LoadResultCallBack loadResultCallBack, boolean isLargeMode) {
        this.mActivity = activity;
        this.isLargeMode = isLargeMode;
        this.mLoadFinisCallBack = loadFinisCallBack;
        this.mLoadResultCallBack = loadResultCallBack;
        mFreshNews = new ArrayList<>();

        int loadingResourse = isLargeMode ? R.drawable.ic_loading_large : R.drawable.ic_loading_small;
        options = ImageLoadProxy.getOption4PictureList(loadingResourse);

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = isLargeMode ? R.layout.item_fresh_news : R.layout.item_fresh_news_small;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final FreshNews freshNews = mFreshNews.get(position);
        ImageLoadProxy.displayImage(freshNews.getCustomFields().getThumb_m(), holder.img, options);
        holder.tv_title.setText(freshNews.getTitle());
        holder.tv_info.setText(freshNews.getAuthor().getName() + "@" + freshNews.getTags()
                .getTitle());
        holder.tv_views.setText("浏览" + freshNews.getCustomFields().getViews() + "次");
        holder.tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtil.shareText(mActivity, freshNews.getTitle() + " " + freshNews.getUrl());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mFreshNews == null ? 0 : mFreshNews.size();
    }

    public void loadFrist() {
        page = 1;
        loadDataByNetworkType();
        /*okHttpManger=new OkHttpManger();
        okHttpManger.get(FreshNews.getUrlFreshNews(page), new OkHttpManger.MyCallBack() {
            @Override
            public void onResponse(String response) {
                JSONObject resultObj = null;
                try {
                    resultObj = new JSONObject(response);
                    JSONArray postsArray = resultObj.optJSONArray("posts");
                    Log.d("DDD","response="+response);
                    mFreshNews.addAll(FreshNews.parse(postsArray));
                    Log.d("DDD","mFreshNews="+mFreshNews.size());
                    notifyDataSetChanged();
                    mLoadResultCallBack.onSuccess(LoadResultCallBack.SUCCESS_OK, null);
                    mLoadFinisCallBack.loadFinish(null);
                } catch (JSONException e) {
                    e.printStackTrace();
                    mLoadResultCallBack.onSuccess(LoadResultCallBack.SUCCESS_OK, null);
                    mLoadFinisCallBack.loadFinish(null);
                }

            }

            @Override
            public void onErrorResponse(IOException e) {

            }
        });
*/


    }


    public void loadNextPage() {
        page++;
        loadDataByNetworkType();

    }

    public void loadRefresh() {
        page++;
        mFreshNews.clear();
        loadDataByNetworkType();
    }

    private void loadDataByNetworkType() {

        if (NetWorkUtil.isNetWorkConnected(mActivity)) {
            RequestManager.addRequest(new Request4FreshNew(FreshNews.getUrlFreshNews(page),
                    new Response.Listener<ArrayList<FreshNews>>() {
                        @Override
                        public void onResponse(ArrayList<FreshNews> freshNewses) {
                            Log.d("AAA", "onResponse");
                            mLoadResultCallBack.onSuccess(LoadResultCallBack.SUCCESS_OK, null);
                            mLoadFinisCallBack.loadFinish(null);
                            if (page == 1) {
                                FreshCacheManager.getInstance(mActivity).clearAllCache();

                            }
                            mFreshNews.addAll(freshNewses);

                            //缓存起来 将ArrayList 装换成 JSonArray 的字符串
                            FreshCacheManager.getInstance(mActivity).addResultCache(JSONParser.toString(freshNewses), page);
                            notifyDataSetChanged();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    mLoadResultCallBack.onError(LoadResultCallBack.ERROR_NET, volleyError.getMessage());
                    mLoadFinisCallBack.loadFinish(null);
                    Log.d("AAA", "onErrorResponse");

                }
            }), mActivity);


        } else {

            mLoadResultCallBack.onSuccess(LoadResultCallBack.SUCCESS_OK, null);
            mLoadFinisCallBack.loadFinish(null);
            if (page == 1) {
                mFreshNews.clear();
                Toast.makeText(mActivity, "本地缓冲下载", Toast.LENGTH_LONG).show();
            }

            mFreshNews.addAll(FreshCacheManager.getInstance(mActivity).getCacheByPage(page));
            Log.d("DDD", " mFreshNews.size()=" + mFreshNews.size());
            notifyDataSetChanged();
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_info;
        TextView tv_views;
        TextView tv_share;
        ImageView img;
        CardView card;
        LinearLayout ll_content;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_info = (TextView) itemView.findViewById(R.id.tv_info);
            tv_views = (TextView) itemView.findViewById(R.id.tv_views);
            tv_share = (TextView) itemView.findViewById(R.id.tv_share);
            img = (ImageView) itemView.findViewById(R.id.img);
            card = (CardView) itemView.findViewById(R.id.card);
            ll_content = (LinearLayout) itemView.findViewById(R.id.ll_content);

        }
    }

}
