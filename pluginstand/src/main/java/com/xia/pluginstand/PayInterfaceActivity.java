package com.xia.pluginstand;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @ author : xia chen hui
 * @ email : 184415359@qq.com
 * @ date : 2020/4/22 21:43
 * @ desc : 公共模块用于实现的接口Activity
 */
public interface PayInterfaceActivity {

    void attach(Activity proxyActivity);

    /**
     * 生命周期
     */
    void onCreate(Bundle saveInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    void onSaveInstanceState(Bundle outState);

    boolean onTouchEvent(MotionEvent event);

    void onBackPressed();

}
