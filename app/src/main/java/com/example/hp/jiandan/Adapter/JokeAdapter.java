package com.example.hp.jiandan.Adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alertdialogpro.AlertDialogPro;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.hp.jiandan.R;
import com.example.hp.jiandan.cache.JokeManager;
import com.example.hp.jiandan.callback.LoadFinishCallBack;
import com.example.hp.jiandan.callback.LoadResultCallBack;
import com.example.hp.jiandan.model.CommentNumber;
import com.example.hp.jiandan.model.Joke;
import com.example.hp.jiandan.net.JSONParser;
import com.example.hp.jiandan.net.NetWorkUtil;
import com.example.hp.jiandan.net.Request4CommentCounts;
import com.example.hp.jiandan.net.Request4Joke;
import com.example.hp.jiandan.net.RequestManager;
import com.example.hp.jiandan.net.ShareUtil;
import com.example.hp.jiandan.net.String2TimeUtil;
import com.example.hp.jiandan.net.TextUtil;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by hp on 2016/3/18.
 */
public class JokeAdapter extends RecyclerView.Adapter<JokeAdapter.JokeViewHolder> {



    private int page;
    private LoadResultCallBack mLoadResultCallBack;
    private LoadFinishCallBack mLoadFinisCallBack;
    private ArrayList<Joke> mJokes;
    private Activity mActivity;


    public JokeAdapter(Activity activity,LoadResultCallBack loadResultCallBack,LoadFinishCallBack loadFinishCallBack){
        this.mActivity=activity;
        this.mLoadFinisCallBack=loadFinishCallBack;
        this.mLoadResultCallBack=loadResultCallBack;
        mJokes=new ArrayList<>();


    }

    @Override
    public JokeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_joke,parent,false);
        JokeViewHolder holder=new JokeViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(JokeViewHolder holder, int position) {

        final Joke joke=mJokes.get(position);
        holder.tv_content.setText(joke.getComment_content());
        holder.tv_author.setText(joke.getComment_author());
        holder.tv_time.setText(String2TimeUtil.dateString2GoodExperienceFormat(joke.getComment_date()));
        holder.tv_like.setText(joke.getVote_positive());
        holder.tv_comment_count.setText(joke.getComment_counts());
        holder.tv_unlike.setText(joke.getVote_negative());
        holder.img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogPro.Builder builder=new AlertDialogPro.Builder(mActivity,R.style.Theme_AlertDialogPro_Material_Light);
                builder.setTitle("你想干嘛")
                        .setIcon(R.drawable.wangting)
                        .setItems(R.array.joke_dialog, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                switch (which){
                                    case 0:
                                        ShareUtil.shareText(mActivity, joke.getComment_content().trim());
                                        break;
                                    case 1:

                                        TextUtil.copy(mActivity,joke.getComment_content());
                                        break;

                                }


                            }
                        }).show();


            }
        });


    }

    @Override
    public int getItemCount() {
        return mJokes==null?0:mJokes.size();
    }

    public void loadFirst(){

        page=1;

        loadDataByNetworkType();
    }


    private void loadDataByNetworkType(){

        if(NetWorkUtil.isNetWorkConnected(mActivity)){

            loadData();

        }else {

            loadCache();
        }

    }
    public void loadData(){
        RequestManager.addRequest(new Request4Joke(Joke.getRequestUrl(page), new Response.Listener<ArrayList<Joke>>() {
            @Override
            public void onResponse(ArrayList<Joke> response) {
                getCommentCounts(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mLoadFinisCallBack.loadFinish(null);
                mLoadResultCallBack.onSuccess(1, "下载成功");
                mLoadResultCallBack.onError(0, "下载失败");
            }
        }),mActivity);

    }

    public void loadCache(){
        mLoadFinisCallBack.loadFinish(null);
        mLoadResultCallBack.onSuccess(LoadResultCallBack.SUCCESS_OK, null);
        JokeManager manager=JokeManager.getInstance(mActivity);
        mJokes.addAll(manager.getCacheByPage(page));
        notifyDataSetChanged();

    }



    public void laodrefresh(){
        page++;
        mJokes.clear();
        loadDataByNetworkType();
    }

    public  void loadmore(){

        page++;
        loadDataByNetworkType();
    }
    private  void  getCommentCounts(final  ArrayList<Joke> jokes){

        StringBuilder sb=new StringBuilder();

        for(Joke joke:jokes){

            sb.append("comment-"+joke.getComment_ID()+",");
        }
        String url=sb.toString();
        if(url.endsWith(",")){

            url.substring(0,url.length()-1);
        }


        RequestManager.addRequest(new Request4CommentCounts(CommentNumber.getCommentCountsURL(url),
                new Response.Listener<ArrayList<CommentNumber>>() {
                    @Override
                    public void onResponse(ArrayList<CommentNumber> response) {
                        for (int i = 0; i < jokes.size(); i++) {
                            jokes.get(i).setComment_counts(response.get(i).getComments() + "");
                        }

                           if(page==1){

                               mJokes.clear();
                               JokeManager.getInstance(mActivity).clearAllCache();
                           }
                        JokeManager.getInstance(mActivity).addResultCache(JSONParser.toString(mJokes),page);
                        mJokes.addAll(jokes);
                        notifyDataSetChanged();
                        mLoadFinisCallBack.loadFinish(null);
                        mLoadResultCallBack.onSuccess(LoadResultCallBack.SUCCESS_OK, null);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mLoadFinisCallBack.loadFinish(null);
                mLoadResultCallBack.onSuccess(LoadResultCallBack.SUCCESS_OK, null);
            }
        }),mActivity);
    }
    public static class JokeViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_author;
        private TextView tv_time;
        private TextView tv_content;
        private TextView tv_like;
        private TextView tv_unlike;
        private TextView tv_comment_count;

        private ImageView img_share;
        private CardView card;
        private LinearLayout ll_comment;
        public JokeViewHolder(View contentView) {
            super(contentView);
            ButterKnife.inject(this,itemView);

            tv_author = (TextView) contentView.findViewById(R.id.tv_author);
            tv_content = (TextView) contentView.findViewById(R.id.tv_content);
            tv_time = (TextView) contentView.findViewById(R.id.tv_time);
            tv_like = (TextView) contentView.findViewById(R.id.tv_like);
            tv_unlike = (TextView) contentView.findViewById(R.id.tv_unlike);
            tv_comment_count = (TextView) contentView.findViewById(R.id.tv_comment_count);

            img_share = (ImageView) contentView.findViewById(R.id.img_share);
            card = (CardView) contentView.findViewById(R.id.card);
            ll_comment = (LinearLayout) contentView.findViewById(R.id.ll_comment);
        }
    }

}
