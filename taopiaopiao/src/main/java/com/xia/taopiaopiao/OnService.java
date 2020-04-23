package com.xia.taopiaopiao;

import android.util.Log;
import android.widget.Toast;

public class OnService extends BaseService {
    private static final String TAG = "OnService";
    private int i;

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(mService, "服务开启成功", Toast.LENGTH_SHORT).show();
        new Thread() {
            @Override
            public void run() {
                super.run();
                Log.i(TAG, "run: " + (i++));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
