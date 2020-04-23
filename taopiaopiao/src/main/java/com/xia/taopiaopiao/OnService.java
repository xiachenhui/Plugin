package com.xia.taopiaopiao;

import android.util.Log;

public class OnService extends BaseService {
    private static final String TAG = "OnService";
    private int i;

    @Override
    public void onCreate() {
        super.onCreate();

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
