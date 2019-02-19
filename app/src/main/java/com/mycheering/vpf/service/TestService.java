package com.mycheering.vpf.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mycheering.vpf.utils.SystemUtil;
import com.mycheering.vpf.view.TimerTaskHandler;

import java.util.Timer;
import java.util.TimerTask;


public class TestService extends Service {

    /**
     * 綁定服务时调用
     *
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("zdy Service", "onBind");
        //返回代理对象
//        return new MyBinder();
        return null;
    }

    /**
     * 代理类
     */
    class MyBinder extends Binder {
        public void showToast() {
            Log.e("zdy Service", "showToast");
        }

        public void showList() {
            Log.e("zdy Service", "showList");
        }
    }

    /**
     * 解除绑定服务时调用
     *
     * @param intent
     * @return
     */
    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("zdy Service", "onUnbind");
        return super.onUnbind(intent);
    }

    /**
     * 服务创建时调用
     */
    @Override
    public void onCreate() {
        Log.e("zdy Service", "onCreate");

//        func1();

        TimerTaskHandler.getInstance(getApplicationContext()).startTask();

        super.onCreate();
    }

    /**
     * 执行startService时调用
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("zdy Service", "onStartCommand");

        SystemUtil.getHistoryApps(TestService.this);

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 服务被销毁时调用
     */
    @Override
    public void onDestroy() {
        Log.e("zdy Service", "onDestroy");
        super.onDestroy();
    }

    private void func1() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SystemUtil.getTopApp(TestService.this);
            }
        },1000,500);
    }
}
