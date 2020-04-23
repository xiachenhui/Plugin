package com.xia.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xia.pluginstand.PayInterfaceBroadcast;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @ author : xia chen hui
 * @ email : 184415359@qq.com
 * @ date : 2020/4/23 23:13
 * @ desc : 占位置的BroadCastReceiver
 */
public class ProxyBroadCastReceiver extends BroadcastReceiver {
    //全类名
    private String className;

    private Context mContext;
    private PayInterfaceBroadcast payInterfaceBroadcast;

    public ProxyBroadCastReceiver(String className, Context context) {
        this.className = className;
        this.mContext = context;

        try {
            //获取广播的类
            Class<?> aClass = PluginManager.getInstance().getDexClassLoader().loadClass(className);
            Constructor<?> constructor = aClass.getConstructor(new Class[]{});
            Object object = constructor.newInstance(new Object[]{});
            payInterfaceBroadcast = (PayInterfaceBroadcast) object;
            payInterfaceBroadcast.attach(context);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        payInterfaceBroadcast.onReceive(context, intent);
    }
}
