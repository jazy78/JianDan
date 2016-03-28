package com.example.hp.jiandan.Activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.hp.jiandan.R;
import com.example.hp.jiandan.fragment.SettingFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by hp on 2016/3/18.
 */
public class SettingActivity extends AppCompatActivity {

    @InjectView(R.id.toolbar)
    Toolbar  mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seting);
        ButterKnife.inject(this);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.setting, new SettingFragment()).commit();
        ButterKnife.inject(this);
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.title_activity_setting);
        mToolbar.setNavigationIcon(R.drawable.ic_actionbar_back);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
