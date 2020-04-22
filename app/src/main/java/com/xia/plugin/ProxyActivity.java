package com.xia.plugin;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;

import com.xia.pluginstand.PayInterfaceActivity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @ author : xia chen hui
 * @ email : 184415359@qq.com
 * @ date : 2020/4/22 21:47
 * @ desc : 宿主APP用于占位置的Activity，展示其他Module的Activity
 */
public class ProxyActivity extends Activity {
    //全类名
    private String className;
    private PayInterfaceActivity payInterfaceActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //通过其他Activity传递过来的Intent 获取全类名
        Intent intent = getIntent();
        if (intent != null) {
            className = intent.getStringExtra("className");
        }
        //通过全类名反射获取类
        try {
            if (!TextUtils.isEmpty(className)) {
                Class<?> aClass = getClassLoader().loadClass(className);
                Constructor<?> constructor = aClass.getConstructor(new Class[]{});
                Object object = constructor.newInstance(new Object[]{});
                //强转成封装的接口类
                payInterfaceActivity = (PayInterfaceActivity) object;
                payInterfaceActivity.attach(this);
                //可以通过Bundle 传入一些需要的数据，比如登录等，然后在淘票票中进行操作
                Bundle bundle = new Bundle();
                //可以调用封装的生命周期
                payInterfaceActivity.onCreate(bundle);
            }
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


    @Override
    public ClassLoader getClassLoader() {
        //重写这个类，加载插件中的类
        return PluginManager.getInstance().getDexClassLoader();
    }

    @Override
    public Resources getResources() {
        //重写加载资源
        return PluginManager.getInstance().getResources();
    }

    @Override
    public void startActivity(Intent intent) {
        String className = intent.getStringExtra("className");
        Intent intent1 = new Intent(this, ProxyActivity.class);
        intent1.putExtra("className", className);
        super.startActivity(intent1);
    }



}
