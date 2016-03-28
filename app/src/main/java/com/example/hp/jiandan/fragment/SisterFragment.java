package com.example.hp.jiandan.fragment;

import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hp.jiandan.Adapter.PictureAdapter;
import com.example.hp.jiandan.R;
import com.example.hp.jiandan.callback.LoadMoreListener;
import com.example.hp.jiandan.callback.LoadResultCallBack;
import com.example.hp.jiandan.callback.SaveFileCallBack;
import com.example.hp.jiandan.imageloader.ImageLoadProxy;
import com.example.hp.jiandan.model.Picture;
import com.example.hp.jiandan.view.AutoLoadRecyclerView;
import com.victor.loading.rotate.RotateLoading;

import java.io.File;

/**
 * Created by hp on 2016/3/8.
 */
public class SisterFragment extends BaseFragment implements LoadResultCallBack,SaveFileCallBack{

    private AutoLoadRecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private RotateLoading loading;
    private PictureAdapter mAdapter;
    protected Picture.PictureType mType;
    MediaScannerConnection mConnection;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mType = Picture.PictureType.Sister;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View view=inflater.inflate(R.layout.fragment_auto_load,container,false);
        recyclerView=(AutoLoadRecyclerView)view.findViewById(R.id.recycler_view);
        refreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
        loading=(RotateLoading)view.findViewById(R.id.loading);
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
                 mAdapter.laodmore();
            }
        });

        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                      mAdapter.refreshLoad();
            }
        });

        mAdapter=new PictureAdapter(getActivity(),this,recyclerView,mType);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setmSaveFileCallBack(this);
        mAdapter.loadFirst();
        loading.start();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ImageLoadProxy.getImageLoader().clearMemoryCache();
    }

    @Override
    public void onSuccess(int result, Object object) {
        loading.stop();
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onError(int code, String msg) {
        loading.stop();
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void SaveFinsh(Object obj) {
        Bundle bundle = (Bundle) obj;
        boolean isSmallPic = bundle.getBoolean(DATA_IS_SIAMLL_PIC);
        String filePath = bundle.getString(DATA_FILE_PATH);
        final File newFile = new File(filePath);
           // 通知 MediaProvider 更新了数据

        MediaScannerConnection.MediaScannerConnectionClient client=new MediaScannerConnection.MediaScannerConnectionClient() {
            @Override
            public void onMediaScannerConnected() {
                mConnection.scanFile(newFile.getAbsolutePath(),null);
            }

            @Override
            public void onScanCompleted(String path, Uri uri) {
                Looper.prepare();
                Looper.loop();
            }
        };
        mConnection =new MediaScannerConnection(getContext(),client);
        mConnection.connect();
        Toast.makeText(getContext(),"保存成功",Toast.LENGTH_LONG).show();
    }
}
