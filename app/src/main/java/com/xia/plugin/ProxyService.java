package com.xia.plugin;

import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.xia.pluginstand.PayInterfaceService;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @ author : xia chen hui
 * @ email : 184415359@qq.com
 * @ date : 2020/4/23 22:32
 * @ desc : 占位置的Service
 */
public class ProxyService extends Service {

    private String serviceName;
    private PayInterfaceService payInterfaceService;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        init(intent);
        return null;
    }

    private void init(Intent intent) {
        if (intent != null) {
            serviceName = intent.getStringExtra("serviceName");
            //加载Service类
            try {
                Class<?> aClass = getClassLoader().loadClass(serviceName);
                Constructor<?> constructor = aClass.getConstructor(new Class[]{});
                Object o = constructor.newInstance(new Object[]{});
                payInterfaceService = (PayInterfaceService) o;
                payInterfaceService.attach(this);
                Bundle bundle = new Bundle();
                bundle.putInt("from", 1);
                payInterfaceService.onCreate();

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (payInterfaceService == null) {
            init(intent);
        }
        return payInterfaceService.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        payInterfaceService.onUnbind(intent);
        return super.onUnbind(intent);
    }

    @Override
    public ClassLoader getClassLoader() {
        return PluginManager.getInstance().getDexClassLoader();
    }

    @Override
    public Resources getResources() {
        return PluginManager.getInstance().getResources();
    }
}
