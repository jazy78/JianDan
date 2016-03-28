package com.example.hp.jiandan.Adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alertdialogpro.AlertDialogPro;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.hp.jiandan.Activity.CommentListActivity;
import com.example.hp.jiandan.Activity.VideoDetailActivity;
import com.example.hp.jiandan.R;
import com.example.hp.jiandan.base.BaseActivity;
import com.example.hp.jiandan.cache.VideoCacheManager;
import com.example.hp.jiandan.callback.LoadFinishCallBack;
import com.example.hp.jiandan.callback.LoadResultCallBack;
import com.example.hp.jiandan.imageloader.ImageLoadProxy;
import com.example.hp.jiandan.model.CommentNumber;
import com.example.hp.jiandan.model.Video;
import com.example.hp.jiandan.net.JSONParser;
import com.example.hp.jiandan.net.NetWorkUtil;
import com.example.hp.jiandan.net.Request4CommentCounts;
import com.example.hp.jiandan.net.Request4Video;
import com.example.hp.jiandan.net.RequestManager;
import com.example.hp.jiandan.net.ShareUtil;
import com.example.hp.jiandan.net.TextUtil;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;

/**
 * Created by hp on 2016/3/10.
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {


    private int page;
    private ArrayList<Video> mVideos;
    private int lastPosition = -1;
    private Activity mActivity;
    private LoadResultCallBack mLoadResultCallBack;
    private LoadFinishCallBack mLoadFinshCallBack;


    public VideoAdapter(Activity activity, LoadResultCallBack loadResultCallBack, LoadFinishCallBack loadFinishCallBack) {
        mActivity = activity;
        mLoadResultCallBack = loadResultCallBack;
        mLoadFinshCallBack = loadFinishCallBack;
        mVideos = new ArrayList<>();
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vido, parent, false);
        VideoViewHolder holder = new VideoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        final Video video = mVideos.get(position);
        holder.tv_title.setText(video.getTitle());
        holder.tv_comment_count.setText(video.getComment_count());
        ImageLoadProxy.displayImage(video.getImgUrl(), holder.img, R.drawable.ic_loading_small);
        holder.tv_like.setText(video.getVote_positive());
        holder.tv_like.setTypeface(Typeface.DEFAULT);
        holder.tv_like.setTextColor(mActivity.getResources().getColor(R.color
                .secondary_text_default_material_light));
        holder.tv_support_des.setTextColor(mActivity.getResources().getColor(R.color
                .secondary_text_default_material_light));

        holder.tv_unlike.setText(video.getVote_negative());
        holder.tv_unlike.setTypeface(Typeface.DEFAULT);
        holder.tv_unlike.setTextColor(mActivity.getResources().getColor(R.color
                .secondary_text_default_material_light));
        holder.tv_un_support_des.setTextColor(mActivity.getResources().getColor(R.color
                .secondary_text_default_material_light));
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, VideoDetailActivity.class);
                intent.putExtra("url", video.getUrl());
                Log.d("DDD", "url=" + video.getUrl());
                mActivity.startActivity(intent);

            }
        });

        holder.img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialogPro.Builder builder = new AlertDialogPro.Builder(mActivity, R.style.Theme_AlertDialogPro_Material_Light);
                builder.setTitle("你想干什么")
                        .setIcon(R.drawable.wangting).setCancelable(true)
                        .setItems(R.array.vido, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        switch (which) {
                                            case 0:
                                                ShareUtil.shareText(mActivity,video.getTitle().trim() + "" + video.getUrl());
                                                break;
                                            case 1:
                                                TextUtil.copy(mActivity, video.getUrl());
                                                break;
                                            case 2:
                                               mActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(video.getUrl())));
                                                break;

                                        }
                                    }
                                }
                        ).show();
            }
        });

        holder.ll_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity, CommentListActivity.class);
                intent.putExtra(BaseActivity.DATA_THREAD_KEY, "comment-" + video.getComment_ID());
                mActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVideos == null ? 0 : mVideos.size();
    }

    public void loadFirst() {
        page = 1;

        loadDataByNetworkType();
    }

    public void loadNextPage() {
        page++;
        loadDataByNetworkType();

    }

    public void loadRefresh() {
        page++;
        mVideos.clear();
        loadDataByNetworkType();

    }

    public void loadDataByNetworkType() {
        if (NetWorkUtil.isNetWorkConnected(mActivity)) {

            loadData();
        } else {

            loadCache();
        }

    }


    public void loadData() {
        RequestManager.addRequest(new Request4Video(Video.getUrlVideos(page), new Response.Listener<ArrayList<Video>>() {
            @Override
            public void onResponse(ArrayList<Video> response) {
                Log.d("AAA", "response.size=" + response.size());//22
                getCommentCounts(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mLoadFinshCallBack.loadFinish(null);
            }
        }), mActivity);

    }

    private void getCommentCounts(final ArrayList<Video> videos) {

        StringBuilder sb = new StringBuilder();
        for (Video video : videos) {
            sb.append("comment-" + video.getComment_ID() + ",");

        }
        Log.d("BBB", "url=" + CommentNumber.getCommentCountsURL(sb.toString()));
        RequestManager.addRequest(new Request4CommentCounts(CommentNumber.getCommentCountsURL(sb.toString()), new Response.Listener<ArrayList<CommentNumber>>() {
            @Override
            public void onResponse(ArrayList<CommentNumber> response) {
                mLoadResultCallBack.onSuccess(LoadResultCallBack.SUCCESS_OK, null);
                mLoadFinshCallBack.loadFinish(null);


                for (int i = 0; i < videos.size(); i++) {
                    videos.get(i).setComment_count(response.get(i).getComments() + "");

                }

                if (page == 1) {
                    mVideos.clear();
                    VideoCacheManager.getInstance(mActivity).clearAllCache();

                }
                mVideos.addAll(videos);
                Log.d("CCC", "mVideos=" + mVideos.size());
                notifyDataSetChanged();
                VideoCacheManager.getInstance(mActivity).addResultCache(JSONParser.toString(videos), page);
                if (mVideos.size() < 10) {


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mLoadFinshCallBack.loadFinish(null);
                mLoadResultCallBack.onError(LoadResultCallBack.ERROR_NET, null);
            }
        }), mActivity);
    }


    public void loadCache() {
        mLoadResultCallBack.onSuccess(LoadResultCallBack.SUCCESS_OK, null);
        mLoadFinshCallBack.loadFinish(null);
        mVideos.addAll(VideoCacheManager.getInstance(mActivity).getCacheByPage(page));
        notifyDataSetChanged();
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {


        private TextView tv_title;
        private TextView tv_like;
        private TextView tv_unlike;
        private TextView tv_comment_count;
        private TextView tv_un_support_des;
        private TextView tv_support_des;
        private ImageView img_share;
        private ImageView img;

        private LinearLayout ll_comment;
        private CardView card;

        public VideoViewHolder(View contentView) {
            super(contentView);

            img = (ImageView) contentView.findViewById(R.id.img);
            tv_title = (TextView) contentView.findViewById(R.id.tv_title);
            tv_like = (TextView) contentView.findViewById(R.id.tv_like);
            tv_unlike = (TextView) contentView.findViewById(R.id.tv_unlike);

            tv_comment_count = (TextView) contentView.findViewById(R.id.tv_comment_count);
            tv_un_support_des = (TextView) contentView.findViewById(R.id.tv_unsupport_des);
            tv_support_des = (TextView) contentView.findViewById(R.id.tv_support_des);

            img_share = (ImageView) contentView.findViewById(R.id.img_share);
            ll_comment = (LinearLayout) contentView.findViewById(R.id.ll_comment);
            card = (CardView) contentView.findViewById(R.id.card);
        }
    }

}
