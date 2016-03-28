package com.example.hp.jiandan.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hp.jiandan.Adapter.FreshNewsAdapter;
import com.example.hp.jiandan.R;
import com.example.hp.jiandan.callback.LoadMoreListener;
import com.example.hp.jiandan.callback.LoadResultCallBack;
import com.example.hp.jiandan.view.AutoLoadRecyclerView;
import com.victor.loading.rotate.RotateLoading;

import butterknife.ButterKnife;

/**
 * Created by hp on 2016/3/8.
 */
public class FreshNewsFragment extends BaseFragment implements LoadResultCallBack {

    private AutoLoadRecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RotateLoading loading;

    private FreshNewsAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auto_load, container, false);
        mRecyclerView = (AutoLoadRecyclerView) view.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        loading = (RotateLoading) view.findViewById(R.id.loading);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void loadMore() {
                mAdapter.loadNextPage();
            }
        });

        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                    mAdapter.loadRefresh();
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setOnPauseListenerParams(false, true);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean isLargeMode = sharedPreferences.getBoolean(SettingFragment.ENABLE_FRESH_BIG, true);
        mAdapter = new FreshNewsAdapter(getActivity(), mRecyclerView, this, isLargeMode);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.loadFrist();
        loading.start();

    }

    @Override
    public void onSuccess(int result, Object object) {
        loading.stop();
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);

        }
    }

    @Override
    public void onError(int code, String msg) {
        loading.stop();
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
