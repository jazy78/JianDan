package com.example.hp.jiandan.Adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alertdialogpro.AlertDialogPro;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.hp.jiandan.Activity.ImageDetailActivity;
import com.example.hp.jiandan.R;
import com.example.hp.jiandan.base.BaseActivity;

import com.example.hp.jiandan.cache.PictureCacheManager;
import com.example.hp.jiandan.callback.LoadFinishCallBack;
import com.example.hp.jiandan.callback.LoadResultCallBack;
import com.example.hp.jiandan.callback.SaveFileCallBack;
import com.example.hp.jiandan.imageloader.ImageLoadProxy;
import com.example.hp.jiandan.model.CommentNumber;
import com.example.hp.jiandan.model.Picture;
import com.example.hp.jiandan.net.JSONParser;
import com.example.hp.jiandan.net.NetWorkUtil;
import com.example.hp.jiandan.net.Request4CommentCounts;
import com.example.hp.jiandan.net.Request4Picture;
import com.example.hp.jiandan.net.RequestManager;
import com.example.hp.jiandan.net.ShareUtil;
import com.example.hp.jiandan.net.String2TimeUtil;
import com.example.hp.jiandan.view.ShowMaxImageView;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.source.greendao.PictureCache;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by hp on 2016/3/15.
 */
public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.pictureViewHolder> {


    private ArrayList<Picture> pictures;
    private int page;
    private LoadFinishCallBack mLoadFinisCallBack;
    private LoadResultCallBack mLoadResultCallBack;
    private SaveFileCallBack mSaveFileCallBack;
    private Activity mActivity;
    private Picture.PictureType mType;

    public PictureAdapter(Activity activity, LoadResultCallBack loadResultCallBack, LoadFinishCallBack loadFinisCallBack, Picture.PictureType type) {
        mActivity = activity;
        mLoadFinisCallBack = loadFinisCallBack;
        mLoadResultCallBack = loadResultCallBack;
        pictures = new ArrayList<>();
        mType = type;
    }

    @Override
    public pictureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pic, parent, false);

        pictureViewHolder holder = new pictureViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final pictureViewHolder holder, final int position) {

        final Picture picture = pictures.get(position);
        String picUrl = picture.getPics()[0];

        if (picUrl.endsWith(".gif")) {
            holder.img_gif.setVisibility(View.VISIBLE);
            //非WIFI网络情况下，GIF图只加载缩略图，详情页才加载真实图片
            if (!NetWorkUtil.isNetWorkConnected(mActivity)) {
                picUrl = picUrl.replace("mw600", "small").replace("mw1200", "small").replace
                        ("large", "small");
            }
        } else {
            holder.img_gif.setVisibility(View.GONE);
        }

      //  holder.progress.setProgress(0);
        holder.progress.setVisibility(View.VISIBLE);

        ImageLoadProxy.displayImageList(picUrl, holder.img, R.drawable.ic_loading_large,
                new SimpleImageLoadingListener() {

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        holder.progress.setVisibility(View.GONE);
                    }
                }, new ImageLoadingProgressListener() {

                    @Override
                    public void onProgressUpdate(String s, View view, int i, int i1) {
                       // holder.progress.setProgress((int) (i * 100f / i1));
                    }
                });
        if (TextUtils.isEmpty(picture.getText_content().trim())) {
            holder.tv_content.setVisibility(View.GONE);

        } else {
            holder.tv_content.setVisibility(View.VISIBLE);
            holder.tv_content.setText(picture.getText_content().trim());
        }

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ImageDetailActivity.class);
                intent.putExtra(BaseActivity.DATA_IMAGE_AUTHOR, picture.getComment_author());
                intent.putExtra(BaseActivity.DATA_IMAGE_URL, picture.getPics());
                intent.putExtra(BaseActivity.DATA_IMAGE_ID, picture.getComment_ID());
                intent.putExtra(BaseActivity.DATA_THREAD_KEY, "comment-" + picture.getComment_ID());
                if (picture.getPics()[0].endsWith(".gif")) {
                    intent.putExtra(BaseActivity.DATA_IS_NEED_WEBVIEW, true);
                }
                mActivity.startActivity(intent);
            }
        });
        holder.tv_author.setText(picture.getComment_author());
        holder.tv_time.setText(String2TimeUtil.dateString2GoodExperienceFormat(picture.getComment_date()));
        holder.tv_like.setText(picture.getVote_positive());
        holder.tv_comment_count.setText(picture.getComment_counts());

        //用于恢复默认的文字
        holder.tv_like.setTypeface(Typeface.DEFAULT);
        holder.tv_like.setTextColor(mActivity.getResources().getColor(R.color
                .secondary_text_default_material_light));
        holder.tv_support_des.setTextColor(mActivity.getResources().getColor(R.color
                .secondary_text_default_material_light));

        holder.tv_unlike.setText(picture.getVote_negative());
        holder.tv_unlike.setTypeface(Typeface.DEFAULT);
        holder.tv_unlike.setTextColor(mActivity.getResources().getColor(R.color
                .secondary_text_default_material_light));
        holder.tv_un_support_des.setTextColor(mActivity.getResources().getColor(R.color
                .secondary_text_default_material_light));

        holder.img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogPro.Builder builder=new AlertDialogPro.Builder(mActivity,R.style.Theme_AlertDialogPro_Material_Light);
                builder.setTitle("你想干什么");
                builder.setIcon(R.drawable.wangting);
                builder.setCancelable(true);
                builder.setItems(R.array.picture, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case 0:

                                try {
                                    ShareUtil.sharePicture(mActivity, picture.getPics()[0]);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;

                            case 1:

                                ShareUtil.savePicture(mActivity, picture.getPics()[0], mSaveFileCallBack);
                                break;

                        }

                    }
                });
                builder.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return pictures == null ? 0 : pictures.size();
    }


    public void setmSaveFileCallBack(SaveFileCallBack mSaveFileCallBack) {
        this.mSaveFileCallBack = mSaveFileCallBack;


    }

    public void loadFirst() {
        page = 1;
        loadDataByNetworkType();
    }

    public void laodmore() {
        page++;
        loadDataByNetworkType();

    }

    public void refreshLoad(){
        page++;
        pictures.clear();
        loadDataByNetworkType();

    }

    public void loadDataByNetworkType() {

        if (NetWorkUtil.isNetWorkConnected(mActivity)) {

            loadDate();
        } else {
            loadCache();

        }
    }

    public void loadDate() {

        RequestManager.addRequest(new Request4Picture(Picture.getRequestUrl(mType, page), new Response.Listener<ArrayList<Picture>>() {
            @Override
            public void onResponse(ArrayList<Picture> response) {
                Log.d("FFF", "response11=" + response.size());
                getCommentCounts(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("FFF", "onErrorResponse");
                mLoadResultCallBack.onError(LoadResultCallBack.ERROR_NET, error.getMessage());
                mLoadFinisCallBack.loadFinish(null);
            }
        }), mActivity);
    }


    private void getCommentCounts(final ArrayList<Picture> pictures) {

        StringBuilder sb = new StringBuilder();
        for (Picture joke : pictures) {
            sb.append("comment-" + joke.getComment_ID() + ",");
        }

        RequestManager.addRequest(new Request4CommentCounts(CommentNumber.getCommentCountsURL(sb.toString()), new Response
                .Listener<ArrayList<CommentNumber>>() {

            @Override
            public void onResponse(ArrayList<CommentNumber> response) {
                Log.d("FFF", "response.size()=" + response.size());

                mLoadResultCallBack.onSuccess(LoadResultCallBack.SUCCESS_OK, null);
                mLoadFinisCallBack.loadFinish(null);

                for (int i = 0; i < pictures.size(); i++) {
                    pictures.get(i).setComment_counts(response.get(i).getComments() + "");
                }
                if (page == 1) {
                    PictureAdapter.this.pictures.clear();
                    PictureCacheManager.getInstance(mActivity).clearAllCache();
                }
                PictureAdapter.this.pictures.addAll(pictures);
                notifyDataSetChanged();
                //加载完毕后缓存
                PictureCacheManager.getInstance(mActivity).addResultCache(JSONParser.toString
                        (pictures), page);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  ShowToast.Short(ConstantString.LOAD_FAILED);
                mLoadFinisCallBack.loadFinish(null);
                mLoadResultCallBack.onError(LoadResultCallBack.ERROR_NET, error.getMessage());
            }
        }
        ), mActivity);

    }

    public void loadCache() {
        mLoadResultCallBack.onSuccess(LoadResultCallBack.SUCCESS_OK, null);
        mLoadFinisCallBack.loadFinish(null);
        pictures.addAll(PictureCacheManager.getInstance(mActivity).getCacheByPage(page));
        notifyDataSetChanged();

    }

    public static class pictureViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.tv_author)
        TextView tv_author;
        @InjectView(R.id.tv_time)
        TextView tv_time;
        @InjectView(R.id.tv_content)
        TextView tv_content;
        @InjectView(R.id.tv_like)
        TextView tv_like;
        @InjectView(R.id.tv_unlike)
        TextView tv_unlike;
        @InjectView(R.id.tv_comment_count)
        TextView tv_comment_count;
        @InjectView(R.id.tv_unsupport_des)
        TextView tv_un_support_des;
        @InjectView(R.id.tv_support_des)
        TextView tv_support_des;

        @InjectView(R.id.img_share)
        ImageView img_share;
        @InjectView(R.id.img_gif)
        ImageView img_gif;
        @InjectView(R.id.img)
        ShowMaxImageView img;

        @InjectView(R.id.ll_comment)
        LinearLayout ll_comment;
        @InjectView(R.id.progress)
        ProgressBar progress;
        @InjectView(R.id.card)
        CardView card;

        public pictureViewHolder(View contentView) {
            super(contentView);
            ButterKnife.inject(this, contentView);
        }
    }


}
