package com.example.hp.jiandan.fragment;


import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import android.widget.Toast;

import com.example.hp.jiandan.R;
import com.example.hp.jiandan.net.AppUtils;
import com.example.hp.jiandan.net.FileUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Created by hp on 2016/3/8.
 */
public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    public static final String CLEAR_CACHE = "clear_cache";
    public static final String ABOUT_APP = "about_app";
    public static final String APP_VERSION = "app_version";
    public static final String ENABLE_SISTER = "enable_sister";
    public static final String ENABLE_FRESH_BIG = "enable_fresh_big";

    private Preference clearCache;
    private Preference aboutApp;
    private Preference appVersion;
    private CheckBoxPreference enableSister;
    private CheckBoxPreference enableBig;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

        clearCache = findPreference(CLEAR_CACHE);
        aboutApp = findPreference(ABOUT_APP);
        appVersion = findPreference(APP_VERSION);
        enableSister = (CheckBoxPreference) findPreference(ENABLE_SISTER);
        enableBig = (CheckBoxPreference) findPreference(ENABLE_FRESH_BIG);

        appVersion.setSummary(AppUtils.getVersionName(getActivity()));
        File cacheFile = ImageLoader.getInstance().getDiskCache().getDirectory();
        //进行数字格式化
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        clearCache.setSummary("缓存大小：" + decimalFormat.format(FileUtil.getDirSize(cacheFile)));

        clearCache.setOnPreferenceClickListener(this);
        aboutApp.setOnPreferenceClickListener(this);


    }

    @Override
    public boolean onPreferenceClick(Preference preference) {

        switch (preference.getKey()){
            case CLEAR_CACHE:
                ImageLoader.getInstance().clearDiskCache();
                Toast.makeText(getActivity(),"清除缓存成功",Toast.LENGTH_SHORT).show();
                clearCache.setSummary("缓存大小：0.00M");
                break;
            case ABOUT_APP:
                break;

        }

        return true;
    }
}
