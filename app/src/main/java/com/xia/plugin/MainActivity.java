package com.xia.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button mButton, mButton2, mButton3;
    private static final String TAG = "Plugin MainActivity";
    private static final String ACTION = "com.xia.plugin.Receiver.PLUGIN_ACTION";
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "宿主App收到插件发送的广播", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = findViewById(R.id.button);
        mButton2 = findViewById(R.id.button2);
        mButton3 = findViewById(R.id.button3);
        mButton.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);

        registerReceiver(mReceiver, new IntentFilter(ACTION));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                loadPlugin();
                break;
            case R.id.button2:
                turnActivity();
                break;
            case R.id.button3:
                sendBroadCastReceiver();
                break;
        }
    }

    //发送广播
    private void sendBroadCastReceiver() {
        Intent intent = new Intent();
        intent.setAction("com.xia.taopiaopiao.StaticReceiver");
        sendBroadcast(intent);
    }

    //跳转Activity
    private void turnActivity() {
        //跳转到占位置的Activity
        Intent intent = new Intent(this, ProxyActivity.class);
        //淘票票的清单文件中注册的Activity 从上到下的顺序
        intent.putExtra("className", PluginManager.getInstance().getPackageInfo().activities[0].name);
        startActivity(intent);
    }

    //加载插件
    private void loadPlugin() {
        File filesDir = this.getDir("plugin", Context.MODE_PRIVATE);
        String name = "plugin.apk";
        String filePath = new File(filesDir, name).getAbsolutePath();
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        InputStream is = null;
        FileOutputStream os = null;
        try {
            //需要开启sd卡权限
            Log.i(TAG, "加载插件 " + new File(Environment.getExternalStorageDirectory(), name).getAbsolutePath());
            is = new FileInputStream(new File(Environment.getExternalStorageDirectory(), name));
            os = new FileOutputStream(filePath);
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            File f = new File(filePath);
            if (f.exists()) {
                Toast.makeText(this, "dex overwrite", Toast.LENGTH_SHORT).show();
            }
            PluginManager.getInstance().loadPath(this);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                os.close();
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
}
