package com.xia.taopiaopiao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class StaticReceiver extends BroadcastReceiver {
    private static final String ACTION = "com.xia.plugin.Receiver.PLUGIN_ACTION";
    private static final String TAG = "StaticReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "插件中的静态广播接收到宿主的消息");
        //回传消息,给宿主App发送广播
        context.sendBroadcast(new Intent(ACTION));
    }
}
