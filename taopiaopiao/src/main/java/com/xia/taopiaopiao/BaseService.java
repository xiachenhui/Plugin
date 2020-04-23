package com.xia.taopiaopiao;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.xia.pluginstand.PayInterfaceService;

public class BaseService extends Service implements PayInterfaceService {

    public Service mService;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void attach(Service proxyService) {
        mService = proxyService;
    }


}
