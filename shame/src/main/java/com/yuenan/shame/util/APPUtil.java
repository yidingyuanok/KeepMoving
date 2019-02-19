package com.yuenan.shame.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class APPUtil {
    private static Toast toast = null;

    public static int dpToPx(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (dipValue * scale + 0.5f);
    }

    public static int spToPx(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        return width;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int height = dm.heightPixels;
        return height;
    }

    private static Toast getToast(Context context) {
        if (toast != null) {
            return toast;
        }
        if (context == null) {
            return null;
        }
        toast = new Toast(context.getApplicationContext());
        return toast;
    }







    public static final void cancelToast(Context context) {
        Toast toastStart = getToast(context);
        if (toastStart != null) {
            toastStart.cancel();
        }
    }

    /**
     * 获得NotificationManager
     *
     * @param context
     * @return
     */
    public static NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }



    /**
     * 获得手机安装应用的所有包名
     *
     * @param context
     * @return
     */
    public static ArrayList<String> getInstalledAppcations(Context context) {
        ArrayList<String> packageList = null;
        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        if (installedApps != null) {
            packageList = new ArrayList<String>();
            for (ApplicationInfo appInfo : installedApps) {
                packageList.add(appInfo.packageName);
            }
        }
        return packageList;
    }

    /**
     * 根据packageName 获得应用的名称
     *
     * @param context
     * @param packageName
     * @return
     */
    public static String getApplicationName(Context context, String packageName) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            return packageManager.getApplicationLabel(applicationInfo).toString();
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        return null;
    }

    /**
     * 获得手机当前打开的应用包名
     *
     * @param context
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String getTopApplication(final Context context) {
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<RunningTaskInfo> tasks = am.getRunningTasks(1);
            if (tasks != null && !tasks.isEmpty()) {
                ComponentName topActivity = tasks.get(0).topActivity;
                if (topActivity != null) {
                    return topActivity.getPackageName();
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static Intent getSystemCameraIntent(Context context, String picPath) {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);

        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> infos = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo info : infos) {

            ApplicationInfo appInfo = info.activityInfo.applicationInfo;
            if (appInfo == null) {
                continue;
            }

            try {
                Field field = ResolveInfo.class.getDeclaredField("system");

                boolean isSystem = field.getBoolean(info);
                if (isSystem) {
                    intent.setPackage(appInfo.packageName);
                    break;
                }

            } catch (Exception e) {
                if (appInfo.publicSourceDir != null && appInfo.publicSourceDir.contains("/system")) {
                    intent.setPackage(appInfo.packageName);
                }
            }
        }

        if (picPath != null) {
            Uri takeCameraOutUri = Uri.fromFile(new File(picPath));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, takeCameraOutUri);
        }
        return intent;
    }

    /**
     * 获得手机型号
     *
     * @return
     */
    public static String getPhoneType() {
        return android.os.Build.BRAND + "_" + android.os.Build.MODEL;
    }

    /**
     * 添加快捷方式
     */
    public static void creatShortCut(Activity activity, String shortcutName, int resourceId) {
        Intent intent = new Intent();
        intent.setClass(activity, activity.getClass());
        /* 以下两句是为了在卸载应用的时候同时删除桌面快捷方式 */
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        // 不允许重复创建
        shortcutintent.putExtra("duplicate", false);
        // 需要现实的名称
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);
        // 快捷图片
        Parcelable icon = Intent.ShortcutIconResource.fromContext(activity.getApplicationContext(), resourceId);
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        // 点击快捷图片，运行的程序主入口
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        // 发送广播。OK
        activity.sendBroadcast(shortcutintent);
    }

    /**
     * 删除快捷方式
     */
    public static void deleteShortCut(Activity activity, String shortcutName) {
        Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        // 快捷方式的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);
        // 在网上看到到的基本都是一下几句，测试的时候发现并不能删除快捷方式。
        // String appClass = activity.getPackageName()+"."+
        // activity.getLocalClassName();
        // ComponentName comp = new ComponentName( activity.getPackageName(),
        // appClass);
        // shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new
        // Intent(Intent.ACTION_MAIN).setComponent(comp));
        /** 改成以下方式能够成功删除，估计是删除和创建需要对应才能找到快捷方式并成功删除 **/
        Intent intent = new Intent();
        intent.setClass(activity, activity.getClass());
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        activity.sendBroadcast(shortcut);
    }

    /**
     * 判断是否存在快捷方式
     */
    public static boolean hasShortcut(Activity activity, String shortcutName) {
        String url = "";
        /* 大于8的时候在com.android.launcher2.settings 里查询（未测试） */
        if (android.os.Build.VERSION.SDK_INT < 8) {
            url = "content://com.android.launcher.settings/favorites?notify=true";
        } else {
            url = "content://com.android.launcher2.settings/favorites?notify=true";
        }
        ContentResolver resolver = activity.getContentResolver();
        Cursor cursor = resolver.query(Uri.parse(url), null, "title=?", new String[]{shortcutName}, null);
        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        return false;
    }

    /**
     * 获取当前进程名
     *
     * @param context
     * @return 进程名
     */
    public static final String getProcessName(Context context) {
        String processName = null;

        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));

        while (true) {
            for (ActivityManager.RunningAppProcessInfo info : am.getRunningAppProcesses()) {
                if (info.pid == android.os.Process.myPid()) {
                    processName = info.processName;
                    break;
                }
            }

            // go home
            if (!TextUtils.isEmpty(processName)) {
                return processName;
            }

            // take a rest and again
            try {
                Thread.sleep(100L);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static boolean fileIsExists(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isSupportRecord() {
        boolean isSupport = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            isSupport = true;
        }
        return isSupport;
    }




    public static String stringToMD5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }
}
