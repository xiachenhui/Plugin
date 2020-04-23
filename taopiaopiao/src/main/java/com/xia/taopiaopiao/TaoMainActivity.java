package com.xia.taopiaopiao;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class TaoMainActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.image_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mActivity, "插件", Toast.LENGTH_SHORT).show();
                //跳转页面
                startActivity(new Intent(mActivity, SecondActivity.class));
                //开启服务
                startService(new Intent(mActivity, OnService.class));
                //注册广播
                IntentFilter intentFilter =new IntentFilter();
                intentFilter.addAction("com.xia.taopiaopiao.TaoMainActivity");
                registerReceiver(new MyReceiver(),intentFilter);
            }
        });
        //发送广播,插件内部发射广播，不需要在ProxyActivity中重写发送广播
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction("com.xia.taopiaopiao.TaoMainActivity");
                sendBroadcast(intent);
            }
        });
    }

}
