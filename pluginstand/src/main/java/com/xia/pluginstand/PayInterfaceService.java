package com.xia.pluginstand;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;

/**
 * @ author : xia chen hui
 * @ email : 184415359@qq.com
 * @ date : 2020/4/23 22:24
 * @ desc :  抽象的Service
 */
public interface PayInterfaceService {

    void onCreate();

    void onStart(Intent intent, int startId);

    int onStartCommand(Intent intent, int flags, int startId);

    void onDestroy();

    void onConfigurationChanged(Configuration newConfig);

    void onLowMemory();

    void onTrimMemory(int level);

    IBinder onBind(Intent intent);

    boolean onUnbind(Intent intent);

    void onRebind(Intent intent);

    void onTaskRemoved(Intent rootIntent);

    void attach(Service proxyService);
}
