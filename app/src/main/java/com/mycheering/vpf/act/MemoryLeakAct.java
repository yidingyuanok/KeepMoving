package com.mycheering.vpf.act;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.mycheering.vpf.R;
import com.mycheering.vpf.base.BaseActivity;
import com.mycheering.vpf.utils.L;

/**
 * Created by zdy on 17-10-30.
 */

public class MemoryLeakAct extends BaseActivity {

//      public static Context mContext;
//    public Context mContext;

    private int i = 0;

//    private Handler mHandler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//
//            L.i("memoryleak......111");
//            return false;
//
//        }
//    });

    private static Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            L.i("memoryleak......111");

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ll_text);
//        mContext = this;
//       L.i("MemoryLeakAct.onCreate()  mContext = [" + mContext + "]");

//        mHandler.postDelayed(new MyRunnable(), 10 * 1000);
//        mHandler.sendEmptyMessageAtTime(0,20 * 1000);

//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                SystemClock.sleep(20 * 1000);
//            }
//        }.start();

        new Thread(new MyRunnable1()).start();

//        UserManger manger = UserManger.getInstance(this);
        L.i("memoryleak......222");

    }


   static class MyRunnable1 implements Runnable {
        @Override
        public void run() {
            SystemClock.sleep(20 * 1000);
            mHandler.sendEmptyMessage(0);

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

//        mHandler.removeCallbacksAndMessages(null);

    }

    public static class UserManger {

        private static UserManger instance;

        private Context context;

        private UserManger(Context context) {
            this.context = context;
        }

        public static UserManger getInstance(Context context) {
            L.i("getInstance() called with: instance = [" + instance + "]");
//            if (instance == null) {
//                instance = new UserManger(context);
//            }
//            return instance;


            instance = new UserManger(context);
            return instance;

        }
    }





    

}

