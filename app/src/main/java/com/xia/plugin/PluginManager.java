package com.xia.plugin;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import dalvik.system.DexClassLoader;

/**
 * @ author : xia chen hui
 * @ email : 184415359@qq.com
 * @ date : 2020/4/22 23:04
 * @ desc : 复制文件到data/data中
 */
public class PluginManager {

    private static final PluginManager ourInstance = new PluginManager();

    private DexClassLoader dexClassLoader;

    private Resources resources;

    private PackageInfo packageInfo;

    public static PluginManager getInstance() {
        return ourInstance;
    }

    public PluginManager() {
    }

    public void loadPath(Context context) {
        File filesDir = context.getDir("plugin", Context.MODE_PRIVATE);
        String name = "plugin.apk";
        String path = new File(filesDir, name).getAbsolutePath();

        PackageManager packageManager = context.getPackageManager();
        packageInfo = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);

        //activity
        File dex = context.getDir("dex", Context.MODE_PRIVATE);
        dexClassLoader = new DexClassLoader(path, dex.getAbsolutePath(), null, context.getClassLoader());

        //resource
        try {
            AssetManager manager = AssetManager.class.newInstance();
            Method addAssetPath = AssetManager.class.getMethod("addAssetPath", String.class);
            addAssetPath.invoke(manager, path);
            resources = new Resources(manager,
                    context.getResources().getDisplayMetrics(),
                    context.getResources().getConfiguration());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //解析Receiver
        parseReceivers(context, path);
    }

    public Resources getResources() {
        return resources;
    }

    public DexClassLoader getDexClassLoader() {
        return dexClassLoader;
    }

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    /**
     * 解析静态广播 ,通过pms中的解析xml文件获取所有的receiver集合，注册广播
     *
     * @param context
     * @param path
     */
    @SuppressLint("PrivateApi")
    public void parseReceivers(Context context, String path) {
        try {
            //获取PackageParser类
            Class<?> packageParserClass = Class.forName("android.content.pm.PackageParser");
            //获取PackageParser中的parsePackage()方法
            Method parsePackageMethod = packageParserClass.getDeclaredMethod("parsePackage", File.class, int.class);
            //创建PackageParser对象
            Object packageParser = packageParserClass.newInstance();
            //调用parsePackage()方法 ，返回Package对象
            Object packageObj = parsePackageMethod.invoke(packageParser, new File(path), PackageManager.GET_ACTIVITIES);
            //通过Package 获取对象中的成员变量receivers
            Field receiverField = packageObj.getClass().getDeclaredField("receivers");
            //获取注册的广播的集合,里面装的是Activity
            List receivers = (List) receiverField.get(packageObj);

            //获取Activity的父类Component中的成员变量IntentFilter集合
            Class<?> componentClass = Class.forName("android.content.pm.PackageParser$Component");
            Field intentField = componentClass.getDeclaredField("intents");
            //调用generateActivityInfo方法，获取PackageParse.Activity
            Class<?> packageParser$ActivityClass = Class.forName("android.content.pm.PackageParser$Activity");
            //获取PackageUserState类
            Class<?> packageUserStateClass = Class.forName("android.content.pm.PackageUserState");
            Object defaultUserState = packageUserStateClass.newInstance();
            //获取generateActivityInfo（）方法
            Method generateActivityInfo = packageParserClass.getDeclaredMethod("generateActivityInfo",
                    packageParser$ActivityClass, int.class, packageUserStateClass, int.class);

            //获取UserId
            Class<?> userHandleClass = Class.forName("android.os.UserHandle");
            Method getCallingUserIdMethod = userHandleClass.getDeclaredMethod("getCallingUserId");
            int userId = (int) getCallingUserIdMethod.invoke(null);

            //获取广播中的IntentFilter
            for (Object activity : receivers) {
                ActivityInfo activityInfo = (ActivityInfo) generateActivityInfo.invoke(packageParser, activity, 0, defaultUserState, userId);
                //获取所有的IntentFilter集合
                List<? extends IntentFilter> intentFilters = (List<? extends IntentFilter>) intentField.get(activity);
                //获取广播
                BroadcastReceiver broadcastReceiver = (BroadcastReceiver) dexClassLoader.loadClass(activityInfo.name).newInstance();
                for (IntentFilter intentFilter : intentFilters) {
                    //注册广播
                    context.registerReceiver(broadcastReceiver, intentFilter);
                }
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
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
