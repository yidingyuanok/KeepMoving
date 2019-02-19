package com.mycheering.vpf.dailnetapn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.util.Log;

import com.mycheering.vpf.BuildConfig;
import com.mycheering.vpf.utils.L;

import java.text.SimpleDateFormat;
import java.util.Date;

public class abak {
    public static final String TAG = "abak";

    private static abak instance = new abak();

    private abak() {
    }

    public static abak getInstance() {
        return instance;
    }

    NReceiver receiver = null;

    public void entrance(Context context) {

    	//没网才执行！   另辟蹊径?
        if (isNetworkEnabled(context)) {
            return;
        }


        register(context);
        dial(context); // APN网络

        if (BuildConfig.DEBUG) {
            L.i(TAG, "before wait, time:" + formatTime(System.currentTimeMillis()));
        }

        synchronized (receiver) {
            try {
                receiver.wait(1000 * 60); //
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (BuildConfig.DEBUG) {
            L.i(TAG, "after wait, time:" + formatTime(System.currentTimeMillis()));
        }
    }

    public void end(Context context) {
    	try {
			unregister(context);			
			if (DialUtils.isDial(context)) {
			    hungup(context);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    private BroadcastReceiver register(Context context) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "register receiver");
        }

        receiver = new NReceiver();
        context.registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        return receiver;
    }

    private void unregister(Context context) {
        context.unregisterReceiver(receiver);
    }

    private void dial(Context context) {
        DialUtils.dialAndSetFlag(context);
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "dial and set flag");
        }
    }

    private void hungup(Context context) {
        DialUtils.hangupAndResetFlag(context);
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "hungup and reset flag");
        }
    }

    class NReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle bundle = intent.getExtras();
            if (null != bundle) {
                NetworkInfo nInfo = (NetworkInfo) bundle.get(ConnectivityManager.EXTRA_NETWORK_INFO);

                String extraInfo = nInfo.getExtraInfo(); // cmwap. 10.0.0.172:80
                NetworkManager.STATE = nInfo.getState();
                NetworkManager.TYPE = nInfo.getType();
                String typeName    = nInfo.getTypeName();
                int    subType     = nInfo.getSubtype();
                String subtypeName = nInfo.getSubtypeName();

                if (BuildConfig.DEBUG) {
                    Log.i(TAG, "extraInfo:" + extraInfo + "_state:" + NetworkManager.STATE + "_type:" + NetworkManager.TYPE + "_typeName:" + typeName + "_subType:" + subType + "_subtypeName:" + subtypeName);
                }

                NetworkManager.APN = null;
//				NetworkManager.TYPE = ConnectivityManager.TYPE_WIFI;
//				NetworkManager.STATE = NetworkInfo.State.DISCONNECTED;

                if (NetworkManager.TYPE == ConnectivityManager.TYPE_MOBILE || NetworkManager.TYPE == ConnectivityManager.TYPE_MOBILE_MMS) {
                    if (NetworkManager.STATE == State.CONNECTED) {  // 移动�? 连接�?
                    	if (BuildConfig.DEBUG) {
                            Log.i(TAG, "NetworkManager.STATE == State.CONNECTED");
                        }
//						NetworkManager.TYPE = type;
//						NetworkManager.STATE = state;
                        NetworkManager.APN = extraInfo;

                        boolean dail = DialUtils.isDial(context);
                        if (dail) {
                            EYAPN.APN apn = APNUtils.getAPN(context);
                            if (apn != null) {
                                NetworkManager.ensureRouteToHost(context, apn.mProxyAddress);
                            }
                        }

                        try {
                            synchronized (this) {
                                if (BuildConfig.DEBUG) {
                                    Log.i(TAG, "NReveiver before notify");
                                }
                                this.notify();  // 唤醒
                                if (BuildConfig.DEBUG) {
                                    Log.i(TAG, "NReceiver after notify");
                                }
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private static String formatTime(long firstInstallTime) {
        SimpleDateFormat formatter  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String           dateString = formatter.format(new Date(firstInstallTime));
        return dateString;
    }

    private static boolean isNetworkEnabled(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (connMgr.getActiveNetworkInfo() != null);
    }
}
