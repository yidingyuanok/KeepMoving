package com.mycheering.vpf.dailnetapn;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

public class DialUtils {

    public static final String PREF_KEY_DIAL = "DIAL";

    public static void dialAndSetFlag(Context context) {
        try {
            NetworkManager nmManager = new NetworkManager(context);
            nmManager.dial();

            PrefUtils.set(context, PREF_KEY_DIAL, true); // 干完后要置为true
        }
        catch (IOException e) {

            e.printStackTrace();
        }
    }

    public static void hangupAndResetFlag(Context context) {
        try {
            NetworkManager nmManager = new NetworkManager(context);
            nmManager.hungup();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        PrefUtils.set(context, PREF_KEY_DIAL, false); // 重置回false
    }

    public static boolean isDial(Context context) {
        return (Boolean) PrefUtils.get(context, PREF_KEY_DIAL, PrefUtils.PREF_TYPE_BOOLEAN, false);
    }

    /**
     * 如果网络确实可用或�?�之前拨号了。返回true�?
     *
     * @param context
     * @return
     */
    public static boolean isNetActiveOrDialer(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo         ni = cm.getActiveNetworkInfo();
        if (ni != null) {
            return true;
        }

        if (isDial(context)) {
            return true;
        }

        return false;
    }

}
