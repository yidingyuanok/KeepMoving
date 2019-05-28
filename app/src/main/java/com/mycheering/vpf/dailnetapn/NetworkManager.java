package com.mycheering.vpf.dailnetapn;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PowerManager;

import com.mycheering.vpf.BuildConfig;
import com.mycheering.vpf.utils.L;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class NetworkManager {

    private Context mContext = null;
    private PowerManager.WakeLock mWakeLock;

//	public static int TYPE = ConnectivityManager.TYPE_WIFI;
    public static int               TYPE  = -1; // ConnetctivityManaget.TYPE_NONE.
    public static NetworkInfo.State STATE = NetworkInfo.State.DISCONNECTED;
    public static String            APN   = null;


    public NetworkManager(Context context) {
        mContext = context;
        return;
    }

    private ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public int beginConnectivity(int _nSimId) {
//		if (IdeaLog.LOG)
        if (BuildConfig.DEBUG) {
            L.i("NetworkManager::beginConnectivity :: SimId : " + _nSimId);
        }

        try {
            // 6.0以下版本可以强起网络
            final Method method = getConnectivityManager().getClass().getMethod("startUsingNetworkFeature", Integer.TYPE, String.class);

            if (method != null) {
                return (Integer) method.invoke(getConnectivityManager(), ConnectivityManager.TYPE_MOBILE, Phone.FEATURE_ENABLE_MMS);
            }
        }
        catch (final Exception _E) {
            if (BuildConfig.DEBUG) {
                L.i("NetworkManager::beginConnectivity :: Exception : " + _E);
            }
        }

        return PhoneConstants.APN_REQUEST_FAILED;
    }

    public void endConnectivity(int simId) {
//		if (IdeaLog.LOG)
        if (BuildConfig.DEBUG) {
            L.i("NetworkManager::endConnectivity :: SimId : " + simId);
        }

        try {
            final Method method = getConnectivityManager().getClass().getMethod("stopUsingNetworkFeature", Integer.TYPE, String.class);
            if (method != null) {
                method.invoke(getConnectivityManager(), ConnectivityManager.TYPE_MOBILE, Phone.FEATURE_ENABLE_MMS);
            }
        }
        catch (final Exception e) {
//			if (IdeaLog.LOG)
            if (BuildConfig.DEBUG) {
                L.i("NetworkManager::endConnectivity :: Exception : " + e);
            }
        }

        return;
    }

    @SuppressLint("InvalidWakeLockTag")
    private synchronized void createWakeLock() {
        // Create a new wake lock if we haven't made one yet.
        if (null == mWakeLock) {
            PowerManager stPM = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
            mWakeLock = stPM.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
            mWakeLock.setReferenceCounted(false);
        }

        return;
    }

    private void acquireWakeLock() {
        // It's okay to double-acquire this because we are not using it in
        // reference-counted mode.
        mWakeLock.acquire();
        return;
    }

    private void releaseWakeLock() {
        // Don't release the wake lock if it hasn't been created and acquired.
        if (mWakeLock != null && mWakeLock.isHeld()) {
            mWakeLock.release();
        }

        return;
    }

    public boolean dial() throws IOException {
        int          nErr     = PhoneConstants.APN_REQUEST_FAILED;
        IntentFilter stFilter = new IntentFilter();

        createWakeLock();

        nErr = beginConnectivity(0);

        switch (nErr) {
            case PhoneConstants.APN_REQUEST_STARTED:
            case PhoneConstants.APN_ALREADY_ACTIVE:
                acquireWakeLock();

                stFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

                // mReceiver = new Receiver();
                // mContext.registerReceiver(mReceiver, stFilter);

                // add this for time out mechanism
                // setDataConnectionTimer(result);
                return true;
            case PhoneConstants.APN_TYPE_NOT_AVAILABLE:
            case PhoneConstants.APN_REQUEST_FAILED:
                return false;
        }

        throw new IOException("Cannot establish MMS connectivity : nErr : " + nErr);

    }


    public void hungup() {
        try {
            endConnectivity(0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            releaseWakeLock();
        }
    }

//	public class Receiver extends BroadcastReceiver {
//
//		@Override
//		public void onReceive(Context _Context, Intent _Intent) {
//			String stExtraInfo = null;
//			Bundle stBundle = _Intent.getExtras();
//
//			if (null != stBundle) {
//				NetworkInfo stInfo = (NetworkInfo) stBundle.get(ConnectivityManager.EXTRA_NETWORK_INFO);
//
//				if (IdeaLog.LOG)
//					Log.d(IdeaLog.NET_TAG, "---------------------- stInfo :: " + stInfo);
//
//				if (null != stInfo) {
//					stExtraInfo = stInfo.getExtraInfo();
//					if (IdeaLog.LOG)
//						Log.d(IdeaLog.NET_TAG, "---------------------- stExtraInfo :: " + stExtraInfo);
//				}
//			}
//		}
//	}
//
//	private static void APN_FEATURE() {
//		return;
//	}

    private static int lookupHost(String _szHostName) {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(_szHostName);
        }
        catch (UnknownHostException _E) {
            return -1;
        }

        byte[] addrBytes;
        int    addr;

        addrBytes = inetAddress.getAddress();
        addr = ((addrBytes[3] & 0xff) << 24) | ((addrBytes[2] & 0xff) << 16) | ((addrBytes[1] & 0xff) << 8) | (addrBytes[0] & 0xff);

        return addr;
    }

    /**
     * 路由
     *
     * @param context
     * @param _szHOST host, ip
     * @return
     */
    public static boolean ensureRouteToHost(Context context, String _szHOST) {
        int     nInetAddr = -1;
        boolean bDone     = false;
        int     nTYPE     = ConnectivityManager.TYPE_MOBILE_MMS;

//		if (IdeaLog.LOG)
        if (BuildConfig.DEBUG) {
            L.i("IdeaUtils --> ensureRouteToHost : Host : " + _szHOST);
        }

        nInetAddr = NetworkManager.lookupHost(_szHOST);

        if (-1 == nInetAddr) {
        }
        else {
            nTYPE = ConnectivityManager.TYPE_MOBILE_MMS;
            ConnectivityManager cmConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//            bDone = cmConnectivityManager.requestRouteToHost(nTYPE, nInetAddr);
        }

//		if (IdeaLog.LOG)
        if (BuildConfig.DEBUG) {
            L.i("IdeaUtils --> ensureRouteToHost :: bDone :" + bDone);
        }


        if (BuildConfig.DEBUG) {
            L.i("ensureRouteToHost result:" + bDone);
        }
        return bDone;
    }

}
