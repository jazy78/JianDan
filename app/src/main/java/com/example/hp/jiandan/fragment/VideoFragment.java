package com.example.hp.jiandan.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.hp.jiandan.Adapter.VideoAdapter;
import com.example.hp.jiandan.R;
import com.example.hp.jiandan.callback.LoadMoreListener;
import com.example.hp.jiandan.callback.LoadResultCallBack;
import com.example.hp.jiandan.view.AutoLoadRecyclerView;
import com.victor.loading.rotate.RotateLoading;

/**
 * Created by hp on 2016/3/8.
 */
public class VideoFragment extends BaseFragment implements LoadResultCallBack {

    private SwipeRefreshLayout swipeRefreshLayout;
    private AutoLoadRecyclerView recyclerView;
    private RotateLoading loading;
    private VideoAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auto_load, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = (AutoLoadRecyclerView) view.findViewById(R.id.recycler_view);
        loading = (RotateLoading) view.findViewById(R.id.loading);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

         recyclerView.setHasFixedSize(false);
         recyclerView.setLoadMoreListener(new LoadMoreListener() {
             @Override
             public void loadMore() {
                   mAdapter.loadNextPage();
             }
         });
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               mAdapter.loadRefresh();
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        mAdapter=new VideoAdapter(getActivity(),this,recyclerView);
        recyclerView.setAdapter(mAdapter);
        mAdapter.loadFirst();
        loading.start();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
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
