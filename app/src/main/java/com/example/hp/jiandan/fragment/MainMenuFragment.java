package com.example.hp.jiandan.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.hp.jiandan.Activity.SettingActivity;
import com.example.hp.jiandan.Adapter.MenuAdapter;
import com.example.hp.jiandan.MainActivity;
import com.example.hp.jiandan.R;
import com.example.hp.jiandan.model.MenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by hp on 2016/3/8.
 */
public class MainMenuFragment extends BaseFragment implements View.OnClickListener {

    private MainActivity mainActivity;
    private RecyclerView mRecycleView;
    private RelativeLayout rl_container;
    private MenuAdapter mAdapter;
    private ArrayList<MenuItem> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drawer, container, false);
        ButterKnife.inject(this, view);
        mRecycleView = (RecyclerView) view.findViewById(R.id.recycle_view);
        rl_container = (RelativeLayout) view.findViewById(R.id.rl_container);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        rl_container.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addAllMenuItems();
        mAdapter = new MenuAdapter(list,mainActivity);
        mRecycleView.setAdapter(mAdapter);

    }

    //每个fragment必须关联一个Activity,onAttach这个方法在fragment被加入到Activity中时由系统调用
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MainActivity) {
            mainActivity = (MainActivity) activity;
        } else {
            throw new IllegalArgumentException("The activity must be MianActivity");

        }

    }

    private void addAllMenuItems() {
        list = new ArrayList<>();
        list.add(new MenuItem("新鲜事", R.drawable.ic_explore_white_24dp, MenuItem.FragmentType.FreshNews,
                FreshNewsFragment.class));
        list.add(new MenuItem("无聊图", R.drawable.ic_mood_white_24dp, MenuItem.FragmentType.BoringPicture,
                PictureFragment.class));
        list.add(new MenuItem("妹子图", R.drawable.ic_local_florist_white_24dp, MenuItem.FragmentType.Sister,
                SisterFragment.class));
        list.add(new MenuItem("段子", R.drawable.ic_chat_white_24dp, MenuItem.
                FragmentType.Joke, JokeFragment
                .class));
        list.add(new MenuItem("小电影", R.drawable.ic_movie_white_24dp, MenuItem.FragmentType.Video,
                VideoFragment.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_container:

                startActivity(new Intent(mainActivity, SettingActivity.class));
                mainActivity.closeDrawer();
                break;

        }

    }
}
