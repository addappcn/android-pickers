package cn.addapp.androidpickers;

import android.app.Application;

import cn.addapp.framework.AppConfig;
import cn.addapp.framework.BuildConfig;
import cn.addapp.framework.util.LogUtils;

/**
 * Author:matt : addapp.cn
 * DateTime:2016/7/20 20:28
 */
public class PickerApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.setIsDebug(BuildConfig.DEBUG);
        if (!LogUtils.isDebug()) {
            android.util.Log.d(AppConfig.DEBUG_TAG, "logcat is disabled");
        }
    }

}
