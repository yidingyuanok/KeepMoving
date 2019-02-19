package com.mycheering.vpf;

import android.os.SystemClock;

/**
 * Created by zdy on 17-11-17.
 */

public  class MyRunnable implements Runnable {
    @Override
    public void run() {

        SystemClock.sleep(20 * 1000);

    }
}
