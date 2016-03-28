package com.example.hp.jiandan.Activity;

import android.animation.ObjectAnimator;
import android.app.Instrumentation;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;

import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.jiandan.R;
import com.example.hp.jiandan.base.BaseActivity;
import com.example.hp.jiandan.callback.SaveFileCallBack;
import com.example.hp.jiandan.imageloader.ImageLoadProxy;
import com.example.hp.jiandan.net.CacheUtil;
import com.example.hp.jiandan.net.FileUtil;
import com.example.hp.jiandan.net.SDCardUtils;
import com.example.hp.jiandan.net.ScreenSizeUtil;
import com.example.hp.jiandan.net.ShareUtil;
import com.example.hp.jiandan.view.MyWebView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.support.design.widget.Snackbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnLongClick;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by hp on 2016/3/16.
 */
public class ImageDetailActivity extends BaseActivity implements View.OnClickListener, SaveFileCallBack {


    @InjectView(R.id.web_git)
    MyWebView webGit;
    @InjectView(R.id.img)
    PhotoView img;
    @InjectView(R.id.img_back)
    ImageButton imgBack;
    @InjectView(R.id.img_share)
    ImageButton imgShare;
    @InjectView(R.id.rl_top_bar)
    RelativeLayout rlTopBar;
    @InjectView(R.id.progress)
    ProgressBar progress;
    @InjectView(R.id.tv_like)
    TextView tvLike;
    @InjectView(R.id.tv_unlike)
    TextView tvUnlike;
    @InjectView(R.id.img_comment)
    ImageButton imgComment;
    @InjectView(R.id.img_download)
    ImageButton imgDownload;
    @InjectView(R.id.ll_bottom_bar)
    LinearLayout llBottomBar;
    @InjectView(R.id.root)
    RelativeLayout root;
    private String[] img_urls;
    private String threadKey;
    private String imgPath;
    private boolean isNeedWebView;
    private File imgCacheFile;
    private boolean isImgHaveLoad = true;
    private MediaScannerConnection connection;
    private boolean isBarShow = true;
    Bitmap bitmap;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        ButterKnife.inject(this);

        initView();
        initData();
    }

    @Override
    protected void initView() {
        ButterKnife.inject(this);
        imgBack.setOnClickListener(this);
        imgDownload.setOnClickListener(this);
        imgShare.setOnClickListener(this);
        imgDownload.setOnClickListener(this);


    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        img_urls = intent.getStringArrayExtra(DATA_IMAGE_URL);
        threadKey = intent.getStringExtra(DATA_THREAD_KEY);
        isNeedWebView = intent.getBooleanExtra(DATA_IS_NEED_WEBVIEW, false);
        //用webview显示动画
        if (isNeedWebView) {
            webGit.getSettings().setJavaScriptEnabled(true);
            webGit.addJavascriptInterface(this, "external");
            webGit.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    view.loadUrl(url);

                    return true;
                }
            });
            webGit.setWebChromeClient(new WebChromeClient() {

                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                    if (newProgress >= 99) {
                        progress.setVisibility(View.GONE);
                    }
                }
            });


            webGit.setBackgroundColor(Color.BLACK);
            webGit.setVisibility(View.VISIBLE);
            img.setVisibility(View.GONE);
            ImageLoadProxy.displayImage4Detail(img_urls[0], img,
                    new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                            progress.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            progress.setVisibility(View.GONE);
                            imgCacheFile = ImageLoader.getInstance().getDiskCache().get(img_urls[0]);
                            if (imgCacheFile != null) {
                                imgPath = "file://" + imgCacheFile.getAbsolutePath();
                                isImgHaveLoad = true;
                                showImgInWebView(imgPath);
                            }
                        }
                    });
        } else {
            Log.d("HHH", "imageview");
            Log.d("HHH", "url=" + img_urls[0]);

            ImageLoadProxy.displayImage4Detail(img_urls[0], img, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    progress.setVisibility(View.GONE);
                    Log.d("HHH", "onLoadingFailed");
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    progress.setVisibility(View.GONE);
                    bitmap = loadedImage;
                    if (loadedImage.getHeight() > ScreenSizeUtil.getScreenWidth(ImageDetailActivity.this)) {
                        imgCacheFile = ImageLoader.getInstance().getDiskCache().get(img_urls[0]);
                        if (imageUri != null) {
                            imgPath = "file://" + imgCacheFile.getAbsolutePath();
                            img.setVisibility(View.GONE);
                            isImgHaveLoad = true;
                            webGit.setVisibility(View.VISIBLE);
                            img.setVisibility(View.GONE);
                            showImgInWebView(imgPath);
                        } else {
                            webGit.setVisibility(View.GONE);
                            img.setVisibility(View.VISIBLE);
                            img.setImageBitmap(loadedImage);
                            isImgHaveLoad = true;
                        }

                    }
                }

                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    progress.setVisibility(View.VISIBLE);
                }
            });
        }

        //设置点击事件
        img.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float v, float v1) {
                toggleBar();
                Log.d("GGG", "onViewTap");

            }
        });

        img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Log.d("PPP", "onLongClick");

                try {
                    setImageToWallpaper();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return true;
            }
        });
    }


    private void showImgInWebView(final String s) {
        if (webGit != null) {

            webGit.loadDataWithBaseURL("",
                    "<!doctype html> <html lang=\"en\"> <head> <meta charset=\"UTF-8\"> <title></title><style type=\"text/css\"> html,body{width:100%;height:100%;margin:0;padding:0;background-color:black;} *{ -webkit-tap-highlight-color: rgba(0, 0, 0, 0);}#box{ width:100%;height:100%; display:table; text-align:center; background-color:black;} body{-webkit-user-select: none;user-select: none;-khtml-user-select: none;}#box span{ display:table-cell; vertical-align:middle;} #box img{  width:100%;} </style> </head> <body> <div id=\"box\"><span><img src=\"img_url\" alt=\"\"></span></div> <script type=\"text/javascript\" >document.body.onclick=function(e){window.external.onClick();e.preventDefault(); };function load_img(){var url=document.getElementsByTagName(\"img\")[0];url=url.getAttribute(\"src\");var img=new Image();img.src=url;if(img.complete){\twindow.external.img_has_loaded();\treturn;};img.onload=function(){window.external.img_has_loaded();};img.onerror=function(){\twindow.external.img_loaded_error();};};load_img();</script></body> </html>".replace("img_url", s),
                    "text/html",
                    "utf-8",
                    "");

        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                Log.d("HHH", "img_back");
                finish();
                break;
            case R.id.img_download:
                toggleBar();
                Log.d("HHH", "img_download");
                ShareUtil.savePicture(ImageDetailActivity.this, img_urls[0], this);
                break;
            case R.id.img_share:
                Log.d("HHH", "img_share");
                try {
                    ShareUtil.sharePicture(ImageDetailActivity.this, img_urls[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.web_git:

                Log.d("HHH", "web");
                toggleBar();
                break;

        }

    }


    final public class JavascriptInterfaceDemo {


        public JavascriptInterfaceDemo() {


        }

        public void myOnCliclk() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("HHH", "myOnCliclk");
                    toggleBar();
                }
            });
        }

        public void onClick() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("HHH", "onClick");
                    toggleBar();
                }
            });

        }
    }

    @JavascriptInterface
    public void onClick() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("RRR", "JavascriptInterface");
                toggleBar();
            }
        });

    }


    public void setImageToWallpaper() throws IOException {

        Log.d("PPP", "setImageToWallpaper");
        if (SDCardUtils.hasSdCard()) {

            saveImageToLoacal();

        } else {

            Snackbar.make(img, "没有SDCard", Snackbar.LENGTH_LONG).setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }).show();
        }

    }


    public void checkFileAndSetWallPaper(File file) {
        Log.d("PPP", "checkFileAndSetWallPaper");
        if (file != null && file.exists()) {
            //提供文件的provider 让其他应用程序能访问本APP的文件 在Xml里面注册了
            Uri contentUri = FileProvider.getUriForFile(getApplication(), "wall", file);//获取本APP提供文件的Uri
            Intent intent = WallpaperManager.getInstance(this).getCropAndSetWallpaperIntent(contentUri);
            startActivityForResult(intent, 1);
        } else {

            Log.d("PPP", "文件夹为空");
        }


    }

    public void saveImageToLoacal() throws IOException {


        String[] urlSplits = img_urls[0].split("\\.");
        File cacheFile = ImageLoader.getInstance().getDiskCache().get(img_urls[0]);
        File picDir = new File(getCacheDir() + File.separator + "wallJianDan");

        if (!picDir.exists()) {
            picDir.mkdir();
        }

        //不能保存在根目录上
        File newFile = new File(picDir, CacheUtil.getWallPicName
                (cacheFile, urlSplits));

        if (!newFile.exists()) {

            newFile.createNewFile();
        }

        if (saveBitmap(bitmap, newFile)) {

            checkFileAndSetWallPaper(newFile);
        } else {
            Log.d("PPP", "复制不成功");

        }


    }

    public boolean saveBitmap(Bitmap bitmap, File file) {

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {

            Log.d("PPP", "Exception");
        }

        return false;

    }

    @Override
    public void SaveFinsh(Object obj) {

        Bundle bundle = (Bundle) obj;
        boolean isSmallPic = bundle.getBoolean(DATA_IS_SIAMLL_PIC);
        String filePath = bundle.getString(DATA_FILE_PATH);
        final File newFile = new File(filePath);
        MediaScannerConnection.MediaScannerConnectionClient client = new MediaScannerConnection.MediaScannerConnectionClient() {
            @Override
            public void onMediaScannerConnected() {
                connection.scanFile(newFile.getAbsolutePath(), null);
            }

            @Override
            public void onScanCompleted(String path, Uri uri) {
                Looper.prepare();
                Looper.loop();
            }
        };
        connection = new MediaScannerConnection(this, client);
        connection.connect();
    }

    //onWindowFocusChanged
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d("HHH", "onWindowFocusChanged");
        toggleBar();

    }


    private void toggleBar() {

        if (isImgHaveLoad) {
            //隐藏
            if (isBarShow) {
                Log.d("HHH", "隐藏");
                isBarShow = false;
                ObjectAnimator.ofFloat(llBottomBar, "translationY", 0, llBottomBar.getHeight())
                        .setDuration(400)
                        .start();

                ObjectAnimator.ofFloat(rlTopBar, "translationY", 0, -rlTopBar.getHeight())
                        .setDuration(400)
                        .start();
            } else {
                Log.d("HHH", "显示");
                //显示
                isBarShow = true;
                ObjectAnimator
                        .ofFloat(llBottomBar, "translationY", llBottomBar.getHeight(), 0)
                        .setDuration(400)
                        .start();
                ObjectAnimator
                        .ofFloat(rlTopBar, "translationY", -rlTopBar.getHeight(), 0)
                        .setDuration(400)
                        .start();
            }


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            Toast.makeText(getApplication(), "壁纸设置成功", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (connection != null && connection.isConnected()) {
            connection.disconnect();
        }

        if (img.getVisibility() == View.VISIBLE) {

            ImageLoadProxy.getImageLoader().cancelDisplayTask(img);
        }
    }
}
