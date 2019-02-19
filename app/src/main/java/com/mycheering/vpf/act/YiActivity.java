package com.mycheering.vpf.act;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.View;

import com.mycheering.vpf.R;
import com.mycheering.vpf.base.BaseActivity;
import com.mycheering.vpf.utils.L;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by zdy on 17-10-30.
 */

public class YiActivity extends BaseActivity implements View.OnClickListener {


    private Context mContext;
    public static ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private Future mFutureTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yi);

        mContext = this;
        initView();
        //设置事件监听
        setTextViewListener();


    }

    public static void actionActivity(Context context, Class clazz) {
        Intent starter = new Intent(context, clazz);
        context.startActivity(starter);
    }


    private void initView() {

    }

    private void setTextViewListener() {

        findViewById(R.id.tv1).setOnClickListener(this);
        findViewById(R.id.tv2).setOnClickListener(this);
        findViewById(R.id.tv3).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv1:
                actionActivity(YiActivity.this,MemoryLeakAct.class);
                break;
            case R.id.tv2:
                mFutureTask = mExecutor.submit(new Task());
//                mFutureTask = mExecutor.submit(new Task2());
                break;
            case R.id.tv3:
                mFutureTask.cancel(true);
                break;
        }

    }


    public class Task implements Callable {
        @Override
        public Object call() throws Exception {
            int i = 0;
            while (i < 1000) {
                L.i("Task: Test\n");
                Thread.sleep(500);
                i++;
            }
            return null;
        }

    }

    public class Task2 implements Runnable {

        @Override
        public void run() {
            while (true) {
                L.i("Task: Test\n");
                SystemClock.sleep(500);
            }
        }
    }
}
