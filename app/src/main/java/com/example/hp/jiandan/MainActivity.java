package com.example.hp.jiandan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.hp.jiandan.base.BaseActivity;
import com.example.hp.jiandan.fragment.FreshNewsFragment;
import com.example.hp.jiandan.fragment.MainMenuFragment;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    private Toolbar toolbar;
    private DrawerLayout mDrawlayout;

    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        initView();
        initData();

    }

    @Override
    protected void initView() {
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mActionBarDrawerToggle=new ActionBarDrawerToggle(this,mDrawlayout,toolbar,R.string.app_name,R.string.app_name){


            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {

                invalidateOptionsMenu();

            }
        };
        mActionBarDrawerToggle.syncState();
        mDrawlayout.setDrawerListener(mActionBarDrawerToggle);
        replaceFragment(R.id.drawer_container,new MainMenuFragment());
        replaceFragment(R.id.frame_container,new FreshNewsFragment());
    }

    @Override
    protected void initData() {

        //注册网络监视广播
          broadcastReceiver=new BroadcastReceiver() {
              @Override
              public void onReceive(Context context, Intent intent) {
                  ConnectivityManager manager=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
                  NetworkInfo mobNetInfo=manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                  NetworkInfo wifiNetInfo=manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                  if(!mobNetInfo.isConnected()&&!wifiNetInfo.isConnected()){
                      Toast.makeText(context, "检测到当前并未连接网络......", Toast.LENGTH_SHORT).show();

                  }else if(wifiNetInfo.isConnected()){
                      Toast.makeText(context, "正在使用wifi连接,请放心使用......", Toast.LENGTH_SHORT).show();
                  }else if(mobNetInfo.isConnected()){
                      Toast.makeText(context, "检测到正在使用移动网络,可能会消耗很多流量,建议在wifi下浏览.....", Toast.LENGTH_SHORT).show();

                  }


              }
          };

        registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public void closeDrawer() {
       mDrawlayout.closeDrawers();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
         unregisterReceiver(broadcastReceiver);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mActionBarDrawerToggle.onOptionsItemSelected(item)){
            return  true;
        }

        return super.onOptionsItemSelected(item);
    }
}
