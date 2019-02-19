package com.mycheering.vpf.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Turing on 2017/4/14.
 */

public class PermissionUtil {

    public static void needPermission(Activity activity, int requestCode, String[] permissions, IPermissionListener listener) {
        requestPermissions(activity, requestCode, permissions, listener);
    }

    @TargetApi(value = Build.VERSION_CODES.M)
    private static void requestPermissions(Object object, int requestCode, String[] permissions, IPermissionListener listener) {
        Activity activity = null;
        if (Build.VERSION.SDK_INT < 23) {
            if (listener != null) {
                listener.onPermissionAlreadyGranted();
            }
            return;
        }

        if (object instanceof Activity) {
            activity = ((Activity) object);
        } else if (object instanceof Fragment) {
            activity = ((Fragment) object).getActivity();

        }

        List<String> deniedPermissions = findDeniedPermissions(activity, permissions);

        if (deniedPermissions.size() > 0) {
            if (object instanceof Activity) {
                ((Activity) object).requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
            } else if (object instanceof Fragment) {
                ((Fragment) object).requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
            } else {
                throw new IllegalArgumentException(object.getClass().getName() + " is not supported");
            }
        } else {
            if (listener != null) {
                listener.onPermissionAlreadyGranted();
            }
        }
    }

    @TargetApi(value = Build.VERSION_CODES.M)
    public static List<String> findDeniedPermissions(Activity activity, String... permission) {
        List<String> denyPermissions = new ArrayList<>();
        for (String value : permission) {
            if (activity.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED) {
                denyPermissions.add(value);
            }
        }
        return denyPermissions;
    }

    public static void getPermission(Context context , String permission , int requestcode) {

        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            //主要用于给用户一个申请权限的解释，该方法只有在用户上一次已经拒绝过你的这个权限申请后，返回true
            //也就是说，用户已经拒绝一次之后每次调用该方法都返回true，你需要给用户一个解释，为什么要授权，则使用该方法。
            L.i("shouldShowRequestPermissionRationale : "+ ActivityCompat.shouldShowRequestPermissionRationale(((Activity) context), permission));
//            if (ActivityCompat.shouldShowRequestPermissionRationale(((YiActivity) context), permission)) {
//
//            } else {
//                ActivityCompat.requestPermissions(((YiActivity) context), new String[]{permission}, requestcode);
//            }
            ActivityCompat.requestPermissions(((Activity) context), new String[]{permission}, requestcode);
        }
    }


  public  interface IPermissionListener {
        void onPermissionAlreadyGranted();
    }
}
