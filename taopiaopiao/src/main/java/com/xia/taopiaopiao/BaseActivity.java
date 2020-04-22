package com.xia.taopiaopiao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.xia.pluginstand.PayInterfaceActivity;

public class BaseActivity extends Activity implements PayInterfaceActivity {

    public Activity mActivity;

    @Override
    public void attach(Activity proxyActivity) {
        mActivity = proxyActivity;
    }

    @Override
    public void setContentView(View view) {

        if (mActivity != null) {
            mActivity.setContentView(view);
        } else {
            super.setContentView(view);
        }

    }

    @Override
    public void setContentView(int layoutResID) {
        if (mActivity != null) {
            mActivity.setContentView(layoutResID);
        } else {
            super.setContentView(layoutResID);
        }
    }

    @Override
    public <T extends View> T findViewById(int id) {
        return mActivity.findViewById(id);
    }

    @Override
    public void startActivity(Intent intent) {
        Intent it = new Intent();
        it.putExtra("className", intent.getComponent().getClassName());
        mActivity.startActivity(it);
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }
}
