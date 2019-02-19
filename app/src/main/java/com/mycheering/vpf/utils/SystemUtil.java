package com.mycheering.vpf.utils;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;

import dalvik.system.DexClassLoader;

import static com.mycheering.vpf.utils.L.i;

/**
 * Created by Turing on 2017/4/13.
 */

public class SystemUtil {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void getTopApp(Context context) {
        UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);//usagestats
        long time = System.currentTimeMillis();
        List<UsageStats> usageStatsList = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, time - 2000, time);

        if (usageStatsList != null && !usageStatsList.isEmpty()) {

            i("usageStatsList.size : " + usageStatsList.size());
            SortedMap<Long, UsageStats> usageStatsMap = new TreeMap<>();
            for (UsageStats usageStats : usageStatsList) {
                usageStatsMap.put(usageStats.getLastTimeUsed(), usageStats);
            }
            if (!usageStatsMap.isEmpty()) {
                String topPackageName = usageStatsMap.get(usageStatsMap.lastKey()).getPackageName();

                if ( getLauncherPackageName(context).equals(topPackageName) || context.getPackageName().equals(topPackageName)) {
                    return;
                }

                i("TopPackage Name : "+ topPackageName);

//                //模拟home键点击
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.addCategory(Intent.CATEGORY_HOME);
//                context.startActivity(intent);
//
//                //启动提示页面
//                Intent intent1 = new Intent(context, DragViewActivity.class);
//                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent1);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void getHistoryApps(Context context) {

        // 获得了过去一年手机上使用过的app的包名集合（其中包括系统级别的）
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.YEAR, -1);
        long startTime = calendar.getTimeInMillis();

        UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> usageStatsList = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_YEARLY, startTime, endTime);

        if (usageStatsList != null && !usageStatsList.isEmpty()) {
            L.e("usageStatsList.size : " + usageStatsList.size());
            HashSet<String> set = new HashSet<>();
            for (UsageStats usageStats : usageStatsList) {
                set.add(usageStats.getPackageName());
            }

            if (!set.isEmpty()) {
                Log.e("size", set.size() + "");
            }
        }
    }




    // 获取桌面包名
    public static String getLauncherPackageName(Context context) {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        final ResolveInfo res = context.getPackageManager().resolveActivity(intent, 0);
        if (res.activityInfo == null) {
            // should not happen. A home is always installed, isn't it?
            return null;
        }
        if (res.activityInfo.packageName.equals("android")) {
            // 有多个桌面程序存在，且未指定默认项时；
            return null;
        } else {
            return res.activityInfo.packageName;
        }
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean checkUsageStatsPermission(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow("android:get_usage_stats", android.os.Process.myUid(), context.getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }


    /**
     * 判断是否是MIUI系统
     * 因为判断是否为小米系统是有一个读文件的过程，所以不每次都读取判断，只第一次才判断
     */
    public static boolean mIsJudgementBefore = false;
    public static boolean mIsMIUIOS = false;
    public static boolean isMIUI() {
        try {
            if (!mIsJudgementBefore) {
                mIsJudgementBefore = true;
                Properties properties = new Properties();
                properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
                mIsMIUIOS = properties.getProperty("ro.miui.ui.version.code", null) != null ||
                        properties.getProperty("ro.miui.ui.version.name", null) != null ||
                        properties.getProperty("ro.miui.internal.storage", null) != null;
            }
        } catch (Exception e) {
        }
        return mIsMIUIOS;
    }




    /**
     * 设置默认浏览器，优先级更高
     * @param context
     * @param pkgName
     * @param actName
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean setDefaultBrowser(Context context, String pkgName, String actName){
        boolean result = false;
        if (! isAppInstalled(context ,pkgName)) return false;
        try {
            int userId = getUserId();
            L.i("userId : " + userId);   // 0
            PackageManager packageManager = context.getPackageManager();
//        packageManager.setDefaultBrowserPackageNameAsUser(pkgName , userId);

            Class clazz = PackageManager.class;

            Method method  = clazz.getDeclaredMethod("setDefaultBrowserPackageNameAsUser", new Class[]{String.class, int.class});
            method.setAccessible(true);
            method.invoke(packageManager,pkgName,userId);

            L.i("setDefaultBrowser() called with:  pkgName = [" + pkgName + "]  success !");
            result = true;
        } catch ( Exception e) {
            L.e("setDefaultBrowser error : " + e.toString());
            setDefaultBrowser2(context,pkgName, actName);
        }
            return result;

    }

    public static void testDefaultBrowser(Context context) {
        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
//            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            Uri content_url = Uri.parse("http://www.baidu.com");
            intent.setData(content_url);
//            intent.setClassName("com.qihoo.browser","com.qihoo.browser.BrowserActivity");
//            intent.setClassName("com.UCMobile","com.UCMobile.main.UCMobile");
//             com.baidu.searchbox   com.baidu.searchbox.BoxBrowserActivity
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  // 此坑一个小时才爬出来，android.content.ActivityNotFoundException
            context.startActivity(intent);
        } catch (Exception e) {
            L.e("testDefaultBrowser error : " + e.toString());
        }
    }


    //系统设置中修改默认Launcher的代码
//    com.qihoo360.launcher  com.qihoo360.launcher.Launcher

//    ComponentName component = ComponentName.unflattenFromString(value);
//    getContext().getPackageManager().replacePreferredActivity(mHomeFilter,
//                                                              IntentFilter.MATCH_CATEGORY_EMPTY,
//                                                              mAllHomeComponents.toArray(new ComponentName[0]),
//                                                               component);

    public static boolean setDefaultLauncher(Context context , String pkgName ) {

        boolean result = false;
        if (!isAppInstalled(context, pkgName))
            return false;
        try {
            PackageManager pm = context.getPackageManager();
            ArrayList<ResolveInfo> homeActivities = new ArrayList<ResolveInfo>();
            // 5.0 该方法被hide, 用反射兼容
            // ComponentName currentDefaultHome = pm.getHomeActivities(homeActivities);
            Method method = pm.getClass().getDeclaredMethod("getHomeActivities", new Class[] { List.class });
            method.setAccessible(true);
            ComponentName currentDefaultHome = (ComponentName) method.invoke(pm, homeActivities);

            IntentFilter mHomeFilter = new IntentFilter(Intent.ACTION_MAIN);
            mHomeFilter.addCategory(Intent.CATEGORY_HOME);
            mHomeFilter.addCategory(Intent.CATEGORY_DEFAULT);
            ComponentName[] mHomeComponentSet = new ComponentName[homeActivities.size()];

            for (int i = 0; i < homeActivities.size(); i++) {
                final ResolveInfo candidate = homeActivities.get(i);
                final ActivityInfo info = candidate.activityInfo;
                ComponentName activityName = new ComponentName(info.packageName, info.name); // 遍历的每个桌面应用
                mHomeComponentSet[i] = activityName;
                if (info.packageName.equals(pkgName)) {
                    currentDefaultHome = activityName;
                }
            }
            // 5.0 该方法被hide, 用反射兼容 HomeSettings.java
            // pm.replacePreferredActivity(mHomeFilter,
            // IntentFilter.MATCH_CATEGORY_EMPTY, mHomeComponentSet,
            // currentDefaultHome);

            Method method2 = pm.getClass().getDeclaredMethod("replacePreferredActivity",
                    new Class[] { IntentFilter.class, int.class, ComponentName[].class, ComponentName.class });
            method2.setAccessible(true);
            method2.invoke(pm, mHomeFilter, IntentFilter.MATCH_CATEGORY_EMPTY, mHomeComponentSet, currentDefaultHome);


                L.i("setDefaultLauncher() called with:  pkgName = [" + pkgName + "] over!!!");
            result = true;
        } catch (Exception e) {
            L.e("setDefaultLauncher() called with:  pkgName = [" + pkgName + "] error : " + e.toString());

        }

        return result;

    }

    /**
     * Sets the specified package as the default SMS/MMS application.
     * The caller of this method needs to have permission to set AppOps and write to secure settings.
     */

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean setDefaultSms(Context context, String pkgName){
        boolean result = false;
        if (! isAppInstalled(context ,pkgName)) return false;

//        com.android.internal.telephony.SmsApplication
//        SmsApplication.setDefaultApplication(pkgName, context);

        try {

            Class clazz = Class.forName("com.android.internal.telephony.SmsApplication");

            Method method = clazz.getDeclaredMethod("setDefaultApplication" , new Class[]{String.class, Context.class});
            method.setAccessible(true);
            method.invoke(null , pkgName, context);
            result = true;
        } catch ( Exception e) {
            e.printStackTrace();
            L.e( "setDefaultSms() called with: pkgName = [" + pkgName + "] error : " + e.toString());
//            java.lang.reflect.InvocationTargetException
//            java.lang.SecurityException: uid 10086 does not have android.permission.UPDATE_APP_OPS_STATS.
        }
        return  result;
    }


    public static boolean isAppInstalled(Context context, String pkgName) {

        boolean installed = false;
        try {
            context.getPackageManager().getApplicationInfo(pkgName, 0);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    /**
     * 设置默认浏览器
     * @param context
     * @param pkgName
     * @param actName
     */
    public static void setDefaultBrowser2(Context context, String pkgName, String actName) {
        if (! isAppInstalled(context ,pkgName)) return;
        try {
            PackageManager packageManager = context.getPackageManager();
            String str1 = "android.intent.category.DEFAULT";
            String str2 = "android.intent.category.BROWSABLE";
            String str3 = "android.intent.action.VIEW";

            // 设置默认项的必须参数
            IntentFilter filter = new IntentFilter(str3);
            filter.addCategory(str1);
            filter.addCategory(str2);
            filter.addDataScheme("http");
            // 设置浏览页面用的Activity
            ComponentName newComponent = new ComponentName(pkgName, actName);

            Intent intent = new Intent(str3);
            intent.addCategory(str2);
            intent.addCategory(str1);
            Uri uri = Uri.parse("http://");
            intent.setDataAndType(uri, null);

            // 找出手机当前安装的所有浏览器程序
            List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, PackageManager.GET_INTENT_FILTERS);

            int size = resolveInfoList.size();
            ComponentName[] arrayOfComponentName = new ComponentName[size];
            i("queryIntentActivities size : " + size);
            for (int i = 0; i < size; i++) {
                ActivityInfo activityInfo = resolveInfoList.get(i).activityInfo;
                String packageName = activityInfo.packageName;
                String className = activityInfo.name;

                // 清除之前的默认设置
                packageManager.clearPackagePreferredActivities(packageName);
                ComponentName componentName = new ComponentName(packageName, className);
                arrayOfComponentName[i] = componentName;
            }
            packageManager.addPreferredActivity(filter, IntentFilter.MATCH_CATEGORY_SCHEME, arrayOfComponentName, newComponent);
//            java.lang.SecurityException: Neither user 10069 nor current process has android.permission.SET_PREFERRED_APPLICATIONS.
            L.i( "setDefaultBrowser2() called with:  pkgName = [" + pkgName + "], actName = [" + actName + "] is ok !!!");
        } catch (Exception e) {
            L.e("setDefaultBrowser2 error : " + e.toString());

        }
    }

    /**
     *
     * @param context
     * @param pkgName
     * @param actName
     * com.qiyi.video    org.iqiyi.video.activity.PlayerActivity
     * <intent-filter>
        <action android:name="android.intent.action.VIEW"/>
        <category android:name="android.intent.category.LAUNCHER"/>
        <category android:name="android.intent.category.DEFAULT"/>
        <category android:name="android.intent.category.BROWSABLE"/>
        <data android:mimeType="video/*" android:scheme="http"/>
        <data android:mimeType="video/*" android:scheme="rtsp"/>
        <data android:mimeType="video/*" android:scheme="file"/>
        <data android:mimeType="video/*" android:scheme="content"/>
        <data android:mimeType="video/*" android:scheme="mms"/>
       </intent-filter>

    com.android.gallery3d ___ com.android.gallery3d.app.MovieActivity
    com.google.android.apps.photos ___ com.google.android.apps.photos.pager.HostPhotoPagerActivity
    com.UCMobile ___ com.UCMobile.video

    com.qiyi.video ___ org.iqiyi.video.activity.PlayerActivity
    com.tencent.qqlive ___ com.tencent.qqlive.open.LocalVideoOpenActivity
    com.tencent.research.drop ___ com.tencent.research.drop.player.activity.PlayerActivity


     */
    public static boolean setDefaultPlayer(Context context,  String pkgName, String actName) {
        boolean result = false;
        try {
            IntentFilter filter =  new IntentFilter("android.intent.action.VIEW");
            filter.addCategory("android.intent.category.DEFAULT");
            filter.addDataScheme("file");
            filter.addDataType("video/*");

            PackageManager packageManager = context.getPackageManager();
            Intent localIntent = new Intent("android.intent.action.VIEW");
            localIntent.addCategory("android.intent.category.DEFAULT");
            localIntent.setDataAndType(Uri.parse("file://"), "video/*");
            List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(localIntent,  PackageManager.GET_INTENT_FILTERS);
            int size = resolveInfoList.size();
            ComponentName[] arrayOfComponentName = new ComponentName[size];
            for (int i = 0; i < size; i++) {
                ActivityInfo activityInfo = resolveInfoList.get(i).activityInfo;
                String packageName = activityInfo.packageName;
                String className = activityInfo.name;

                i( "packageName : " + packageName +  " ___ " + className);

                // 清除之前的默认设置
                packageManager.clearPackagePreferredActivities(packageName);
                ComponentName componentName = new ComponentName(packageName, className);
                arrayOfComponentName[i] = componentName;
            }
//        packageManager.addPreferredActivity(filter, IntentFilter.MATCH_CATEGORY_SCHEME, arrayOfComponentName, newComponent);
            packageManager.addPreferredActivity(filter, IntentFilter.MATCH_CATEGORY_TYPE, arrayOfComponentName, new ComponentName(pkgName , actName));
//            packageManager.addPreferredActivity(filter, IntentFilter.MATCH_CATEGORY_SCHEME, arrayOfComponentName, new ComponentName(pkgName , actName));
//            packageManager.addPreferredActivity(filter, IntentFilter.MATCH_CATEGORY_PATH, arrayOfComponentName, new ComponentName(pkgName , actName));
            L.i("setDefaultPlayer() called with:  pkgName = [" + pkgName + "], actName = [" + actName + "] is over !");
            result = true;
        } catch ( Exception e) {
            e.printStackTrace();
            L.e("setDefaultPlayer() called with:  pkgName = [" + pkgName + "], actName = [" + actName + "] error : " + e.toString());
        }

        return result;

    }

    //	com.cootek.smartdialer 触宝电话
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean setDefaultPhone(Context context, String pkgName) {
        boolean result = false;
        if (!isAppInstalled(context, pkgName))
            return false;
        try {
            // int userId = UserHandle.myUserId();
            int userId = getUserId();

            // 为了在6.0以下版本编译过，用反射， 5.0版本还没有找到方法
            // DefaultDialerManager.setDefaultDialerApplication(context, pkgName, userId);
            Class<?> dialerClazz = Class.forName("android.telecom.DefaultDialerManager");
            Method method = dialerClazz.getDeclaredMethod("setDefaultDialerApplication", new Class[] { Context.class, String.class, int.class });
            method.setAccessible(true);
            method.invoke(null, context, pkgName, userId);
            result = true;
        } catch (Exception e) {
            L.e("setDefaultPhone() called with: pkgName = [" + pkgName + "] error : " + e.toString());
            // java.lang.SecurityException: Permission denial: writing to
            // settings requires:android.permission.WRITE_SECURE_SETTINGS

        }

        return result;
    }

    public static int getUserId() throws Exception {
        // 5.0版本 改方法hide,反射兼容
        // int userId = UserHandle.myUserId();

        Class<?> userHandleClazz = Class.forName("android.os.UserHandle");
        Method method2 = userHandleClazz.getMethod("myUserId");
        method2.setAccessible(true);
        int userId = (Integer) method2.invoke(null);
        
        return userId;
    }


   public static final String dexName = "dl.zip";
   public static void invokeA(Context context ,String className ,String methodName ){


		String filePath = context.getFilesDir().getAbsolutePath();
//            String filePath = context.getDir("dex", 0).getAbsolutePath();
            //   /data/user/0/com.turing.count/app_dex
            L.i( "file dex : " + filePath + File.separator + dexName);
//		if (dexClassLoader == null) {			
//			dexClassLoader = new DexClassLoader(filePath + File.separator + dexName, filePath, null,
//					context.getClassLoader());	
//			Log.e(TAG, "dexClassLoader : "+ dexClassLoader);
//		}

         Object invoke = null;
         try {
            DexClassLoader dexClassLoader = new DexClassLoader(filePath + File.separator + dexName, filePath, null, context.getClassLoader());

            Class<?> loadClass = dexClassLoader.loadClass(className);
                Method declaredMethod = loadClass.getDeclaredMethod(methodName, new Class[]{Context.class});
                L.i( "declaredMethod : "+ declaredMethod);
                invoke = declaredMethod.invoke(null, context);
            } catch (Exception e) {
             L.e( "invokeA ERROR : " + e.toString());
                e.printStackTrace();
            }
            L.i( "invokeA over invoke:  " + invoke);
        }

    /**
     * 获取当前时区
     */
    public  static String getCurrentTimeZone() {
        String result = "";
        try {
            TimeZone tz = TimeZone.getDefault();
            result = tz.getDisplayName(false, TimeZone.SHORT);
        } catch (Exception e) {
             L.e("getCurrentTimeZone() error : " + e.toString());
        }
        L.i("getCurrentTimeZone() result : " + result);
        return result;

    }

    /**
     * 获取当前系统语言格式
     */
    public static String getCurrentLanguage(Context mContext){
        Locale locale =mContext.getResources().getConfiguration().locale;
        String language=locale.getLanguage();
        String country = locale.getCountry();
        String lc=language+"_"+country;
        return lc;
    }


    public static String getMetaData(Context context) {
        String appKey = null;
        try {
            ApplicationInfo appInfo = context.getPackageManager().
                    getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            appKey = appInfo.metaData.getString("JPUSH_APPKEY");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appKey;
    }


}
