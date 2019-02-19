package com.mycheering.vpf.dailnetapn;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.text.TextUtils;

public class APNUtils {

    public static EYAPN.APN getAPN(Context context) {

        // 如果是wifi，直接返回null�?
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo         ni = cm.getActiveNetworkInfo();
        if (ni != null) {
            int type = ni.getType();
            if (type == ConnectivityManager.TYPE_WIFI) {
                NetworkManager.APN = null;
                return null;
            }

            // 用户自己打开手机网络
            if (type == ConnectivityManager.TYPE_MOBILE || type == ConnectivityManager.TYPE_MOBILE_MMS) {
                State state = ni.getState();
                if (state == State.CONNECTED) {
                    String extraInfo = ni.getExtraInfo();
                    NetworkManager.TYPE = type;
                    NetworkManager.STATE = state;
                    NetworkManager.APN = extraInfo;
                }
            }
        }

        // 得到索引
        int subIdIndex = SimIndexUtils.getDefaultDataSubId(context);
        if (subIdIndex <= -1) {
            return null;
        }

        // 得到apn name
        String apnName = NetworkManager.APN;
        if (TextUtils.isEmpty(apnName)) {
            return null;
        }

        // 得到代理
        EYAPN.APN apn = EYAPN.load(context, NetworkManager.APN, subIdIndex);

        return apn;
    }
}
