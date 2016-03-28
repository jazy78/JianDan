package com.example.hp.jiandan.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hp.jiandan.Adapter.JokeAdapter;
import com.example.hp.jiandan.R;
import com.example.hp.jiandan.callback.LoadFinishCallBack;
import com.example.hp.jiandan.callback.LoadMoreListener;
import com.example.hp.jiandan.callback.LoadResultCallBack;
import com.example.hp.jiandan.model.Joke;
import com.example.hp.jiandan.view.AutoLoadRecyclerView;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by hp on 2016/3/8.
 */
public class JokeFragment extends BaseFragment implements LoadResultCallBack{
    @InjectView(R.id.recycler_view)
    AutoLoadRecyclerView recyclerView;
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.loading)
    RotateLoading loading;
    private JokeAdapter adapter;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_auto_load, container, false);
        ButterKnife.inject(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void loadMore() {
            adapter.loadmore();
            }
        });

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                    adapter.laodrefresh();
            }
        });
       adapter=new JokeAdapter(getActivity(),this,recyclerView);
        recyclerView.setAdapter(adapter);
        adapter.loadFirst();
        loading.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onSuccess(int result, Object object) {
           loading.stop();

        if(swipeRefreshLayout.isRefreshing()){

            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onError(int code, String msg) {
        loading.stop();
        if(swipeRefreshLayout.isRefreshing()){

            swipeRefreshLayout.setRefreshing(false);
        }

    }
}
