package com.mycheering.vpf.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;

import com.mycheering.vpf.utils.L;

public class TimerTaskHandler {
	private static TimerTaskHandler mInstance;
	private static Context mContext;
	private static final int  MSG_TIMEUP	= 5550;
//	private static final long PRELOAD_TIMEOUT = 30 * 1000L; // 30秒
//	private Bitmap mBitmap;
	public static final String TAG = "tian";

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(msg.what == MSG_TIMEUP) {
				new AdShowView(mContext, null, null);
//				mHandler.postDelayed(new Runnable() {
//					public void run() {
//							EmobLog.d(TAG, "show view");
//					}
//				}, PRELOAD_TIMEOUT);
			} 
		};
	};
	
	public static synchronized TimerTaskHandler getInstance(Context context) {
		mContext = context.getApplicationContext();
		if (mInstance == null) {
			mInstance = new TimerTaskHandler();
		}
		
		return mInstance;
	}
	
	public void startTask() {
		L.i("tian", "startTask");
		L.i(TAG, "定时任务开启");
		mHandler.removeCallbacks(updateTimeThread);
		mHandler.postDelayed(updateTimeThread, 5000);
	}
	
    Runnable updateTimeThread = new Runnable() {
        public void run() {
			L.i(TAG, "定时任务执行，下次执行时间为1分钟后");
//        	if(Utils.hasActiveNetwork(mContext)){
        		checkAds();
//        	}
//        	mHandler.postDelayed(updateTimeThread,  3 * 60 * 1000L);
//        	mHandler.postDelayed(updateTimeThread,  5 * 1000L);
        	mHandler.postDelayed(updateTimeThread,  10 * 1000L);
        }
    };
    
    private void checkAds() {
    	mHandler.sendEmptyMessage(MSG_TIMEUP);
	}
}
