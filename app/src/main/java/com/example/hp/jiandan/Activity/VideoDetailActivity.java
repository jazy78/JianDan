package com.example.hp.jiandan.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.alertdialogpro.AlertDialogPro;
import com.example.hp.jiandan.R;
import com.example.hp.jiandan.net.ShareUtil;
import com.example.hp.jiandan.net.TextUtil;
import com.victor.loading.rotate.RotateLoading;

/**
 * Created by hp on 2016/3/14.
 */
public class VideoDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private WebView webView;
    private WebSettings settings;

    private Toolbar mToolbar;
    private ImageButton imgBtn_back;
    private ImageButton imgBtn_forward;
    private ImageButton imgBtn_control;
    private RotateLoading loading;

    private String url;

    private boolean isLoadFinish = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
        initView();
        initData();
    }


    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        webView = (WebView) findViewById(R.id.webview);
        imgBtn_back = (ImageButton) findViewById(R.id.imgBtn_back);
        imgBtn_forward = (ImageButton) findViewById(R.id.imgBtn_forward);
        imgBtn_control = (ImageButton) findViewById(R.id.imgBtn_control);
        loading = (RotateLoading) findViewById(R.id.loading);
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.loading);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        imgBtn_back.setOnClickListener(this);
        imgBtn_forward.setOnClickListener(this);
        imgBtn_control.setOnClickListener(this);
        settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        loading.start();
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {

                    loading.stop();

                } else {

                    loading.start();

                }


                super.onProgressChanged(view, newProgress);
            }
        });


        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                imgBtn_control.setImageResource(R.drawable.ic_action_refresh);
                isLoadFinish = true;
                mToolbar.setTitle(view.getTitle());
            }
        });
    }

    public void initData() {
        url = getIntent().getStringExtra("url");
        webView.loadUrl(url);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_back:
                if (webView.canGoBack()) {
                    webView.goBack();
                }
                break;
            case R.id.imgBtn_forward:
                if (webView.canGoForward()) {
                    webView.goForward();
                }
                break;
            case R.id.imgBtn_control:

                if (isLoadFinish) {
                    webView.reload();
                    isLoadFinish = false;
                } else {
                    webView.stopLoading();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_video_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_more:
                AlertDialogPro.Builder builder = new AlertDialogPro.Builder(this, R.style.Theme_AlertDialogPro_Material_Light);


                builder.setTitle("你想干什么")
                        .setIcon(R.drawable.wangting).setCancelable(true)
                        .setItems(R.array.vido, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        switch (which){
                                            case 0:
                                                ShareUtil.shareText(VideoDetailActivity.this,mToolbar.getTitle()+""+url);
                                                break;
                                            case 1:
                                                TextUtil.copy(VideoDetailActivity.this,url);
                                                break;
                                            case 2:
                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                                                break;

                                        }
                                    }
                                }
                        ).show();
        }
                return super.onOptionsItemSelected(item);

    }
    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) {
            webView.onResume();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null) {

            webView.onPause();
        }
    }
}
