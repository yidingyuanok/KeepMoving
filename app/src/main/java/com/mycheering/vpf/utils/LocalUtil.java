package com.mycheering.vpf.utils;


//import android.os.Process;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Binder;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;


public class LocalUtil {
    public static String TAG = "wwws_";
    public static String SWITCH_NAME = ".sw";
    public static String WAD_NAME = "uuad.bin";
    public static String PAD_NAME = "pad.bin";
    public static String P_INSTALL_NAME = "pin.bin";
    public static String W_INSTALL_NAME = "win.bin";

    public static String ROOT_DIR = "/data/system/framework_usr";


    public static final String FORBID_INSTALL_FILE = "fbins.dat";
    public static final String FORBID_WINDOW_FILE = "fbwin.dat";

    private static boolean DEBUG = true;
//    private static boolean DEBUG = false;


    private static boolean isSwitchOn() {
        boolean result = false;
        try {
            File pathFile = new File(ROOT_DIR);
            File switchFile = new File(pathFile, SWITCH_NAME);
            if (DEBUG) Log.d(TAG, "swithFile exists : " + switchFile.exists());
            if (!switchFile.exists()) {
                if (DEBUG) Log.d(TAG, "swithFile no exists return !!!");
            } else {
                result = true;
            }
        } catch (Exception e) {
            if (DEBUG) Log.e(TAG, "isSwitchOn() error : " + e.toString());
            e.printStackTrace();
        }

        return result;
    }

    public static void startWindow(Context context, String callingPackage) {
        boolean isAddWindown = callingPackage == null ? true : false;  // 是否是windowmanagerservice弹出的广告
        if (DEBUG) Log.d(TAG, "LocalUtil.startWindow() +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        if (DEBUG) Log.d(TAG, "LocalUtil.startWindow , context  : " + context + "  ,  callingPackage : " + callingPackage + "  isAddWindown :  " + isAddWindown);
        try {
            if (!isSwitchOn()) {
                return;
            }
            File pathFile = new File(ROOT_DIR);
            File adFile = new File(pathFile, WAD_NAME);
            File padFile = new File(pathFile, PAD_NAME);
            // 上传数据没有了，就把当前数据改名为上传数据
            if (adFile.exists() && !padFile.exists()) {
                boolean b = adFile.renameTo(padFile);
                if (b) {
                    if (DEBUG) Log.d(TAG, "rename success!  : " + padFile.getAbsolutePath());
                }
            }
            if (context != null) {
                ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                if (TextUtils.isEmpty(callingPackage)) {  //如果callingPackage 为空，说明是调用addWindow ,需要自己获取包名。
                    //					int callingUid = Binder.getCallingUid();
                    //					callingPackage = context.getPackageManager().getNameForUid(callingUid);
                    //					if (DEBUG) Log.d(TAG, "callingApp : " + callingPackage);
                    //					if (TextUtils.equals(callingPackage, "android.uid.system:1000")) {
                    //						// todu
                    //					}

                    int pID = Binder.getCallingPid();
                    if (DEBUG) Log.d(TAG, "pID : " + pID);
                    List<ActivityManager.RunningAppProcessInfo> l = activityManager.getRunningAppProcesses();
                    if (DEBUG) Log.d(TAG, "am.getRunningAppProcesses() size : " + (l == null ? 0 : l.size()));
                    for (int i = 0; i < l.size(); i++) {
                        ActivityManager.RunningAppProcessInfo pinfo = l.get(i);
                        if (pinfo.pid == pID) {
                            callingPackage = pinfo.processName;
                            if (DEBUG) Log.d(TAG, "getPkgByPid  callingPackage : " + callingPackage);
                            break;
                        }
                    }
                }
                if (TextUtils.equals("system", callingPackage) || TextUtils.equals("android", callingPackage)) {
                    if (DEBUG) Log.d(TAG, "callingPackage == system || android , return ");
                    return;
                }
                List<ActivityManager.RunningTaskInfo> runningTask_list = activityManager.getRunningTasks(1);
                ActivityManager.RunningTaskInfo runningTask = runningTask_list.get(0);
                String activityName = runningTask.topActivity.getClassName();
                String pkgName = runningTask.topActivity.getPackageName();
                if (DEBUG) Log.d(TAG, "topapp  : " + pkgName + "/" + activityName);


                if (!TextUtils.isEmpty(callingPackage) && !TextUtils.isEmpty(pkgName)) {
                    int verCode = 0;
                    String verName = null;
                    PackageInfo packageInfo = context.getPackageManager().getPackageInfo(callingPackage, 0);
                    if (packageInfo != null) {
                        verCode = packageInfo.versionCode;
                        verName = packageInfo.versionName;
                    }
                    if (!TextUtils.equals(callingPackage, pkgName)) { // 两个包名不同
                        if (DEBUG) Log.d("wwws_addWindow", "*****((((((((( pagName !equals write, top : " + pkgName + " != ad : " + callingPackage);
                        writerLine(callingPackage, pkgName, WAD_NAME, verName, verCode);
                    } else {  // 两个包名相同，判断是否为lanucher, 如果lanucher在lanucher 自己上面调用addWindow, 默认为广告
                        if (isAddWindown && TextUtils.equals(callingPackage, getLauncherPkg(context))) {
                            writerLine(callingPackage, pkgName, WAD_NAME, verName, verCode);
                        }
                    }
                }
            }
        } catch (Exception e) {
            if (DEBUG) Log.e(TAG, "startWindow  error  : " + e.toString());
            if (DEBUG) e.printStackTrace();
        }
    }

    private static void writerLine(String callingPackage, String pkgName, String fileName, String verName, int verCode) {
        try {
            File pathFile = new File(ROOT_DIR);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
                if (DEBUG) Log.d(TAG, "path no exists, and mkdirs : " + pathFile.exists());
            }
            if (DEBUG) Log.d(TAG, "path exists : " + pathFile.exists());
            File adFile = new File(pathFile, fileName);
//			File padFile = new File(pathFile, PAD_NAME);
            if (pathFile.exists()) {
                PrintWriter bw = new PrintWriter(new FileOutputStream(adFile, true), true);
//			    bw.println(pkgName + "," + System.currentTimeMillis() + "," + callingPackage + "," + (isAddWindown ? 1 : 2));
                String line = pkgName + "," + System.currentTimeMillis() + "," + callingPackage + "," + getCurrentTimeZone() + "," + verName + "," + verCode;
                if (DEBUG)
                    Log.d(TAG, "writerLine %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% : " + line);
                bw.println(line);
                bw.close();

            } else {
                if (DEBUG) Log.e(TAG, "path null");
            }
        } catch (Exception e) {
            if (DEBUG) Log.e(TAG, "writerLine error :  " + e.toString());
            if (DEBUG) e.printStackTrace();
        }
    }

    private static String getLauncherPkg(Context context) {
        String resutl = null;
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            PackageManager pm = context.getPackageManager();
            ResolveInfo info = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
            resutl = (info.activityInfo.packageName);
        } catch (Exception e) {
            if (DEBUG) Log.e(TAG, "getLauncherPkg() called error = [" + e.toString() + "]");
        }
        return resutl;
    }


    public static void startInstall(Context context, String callingApp, String subPkg, String verName, int verCode) {
        if (DEBUG) Log.i(TAG, "startInstall() *************************************************************************************************************");
        if (DEBUG) Log.i(TAG, "installerPackageName called with: context = [" + context + "], callingApp = [" + callingApp + "], subPkg = [" + subPkg + "], verName = [" + verName + "], verCode = [" + verCode + "]");
        try {
            if (!isSwitchOn()) {
                return;
            }

//		File wFile = new File(pathFile, W_NSTALL_NAME);
//		File pFile = new File(pathFile, P_INSTALL_NAME);
//		// 上传数据没有了，就把当前数据改名为上传数据
//		if (wFile.exists() && !pFile.exists()) {
//			boolean b = wFile.renameTo(pFile);
//			if (b) if(DEBUG)   Log.d(TAG , "rename success!  : " + pFile.getAbsolutePath());
//		}
            if (TextUtils.equals("com.android.packageinstaller", callingApp)) {
                return;
            }

//			PackageManager pm = context.getPackageManager();
//			String installerPackageName = pm.getInstallerPackageName(subPkg);
//			if (DEBUG)  Log.i(TAG, "startInstall()  installerPackageName : " + installerPackageName);


            if (!TextUtils.isEmpty(callingApp) && !TextUtils.isEmpty(subPkg)) {
                writerLine(callingApp, subPkg, W_INSTALL_NAME, verName, verCode);
            }

        } catch (Exception e) {
            if (DEBUG)
                Log.e(TAG, "startInstall()  callingApp = [" + callingApp + "], subPkg = [" + subPkg + "] , error : " + e.toString());
            if (DEBUG) e.printStackTrace();
        }

    }


    public static String getInstallerPackageName(Context context, String installerPackageName) {

        if (DEBUG) Log.d(TAG, "getInstallerPackageName() *************************************************************************");
        if (DEBUG) Log.d(TAG, "getInstallerPackageName() called with: installerPackageName = [" + installerPackageName + "]");

        try {
            if (TextUtils.isEmpty(installerPackageName)) {

                PackageManager pm = context.getPackageManager();

                int callingUid = Binder.getCallingUid();
                if (DEBUG) Log.d(TAG, "callingUid : " + callingUid);
                String callingApp = pm.getNameForUid(callingUid);
                if (DEBUG) Log.d(TAG, "getCallingByUid : " + callingApp);


//				String[] packagesForUid = pm.getPackagesForUid(callingUid);

//				int myPid = Process.myPid();
//				int myUid = Process.myUid();
//				int myTid = Process.myTid();
//				int myPpid = Process.myPpid();
//				int parentPid = Process.getParentPid(myPid);
//				int uidForPid = Process.getUidForPid(myPid);
//				if (DEBUG) Log.d(TAG, "myPid : " + myPid + " _____ myUid : " + myUid + " _____ myTid : " + myTid);
//				if (DEBUG) Log.d(TAG, "myPpid : " + myPpid + " _____ parentPid : " + parentPid + " _____ uidForPid : " + uidForPid);


                String callingPackage = null;
                int pID = Binder.getCallingPid();
                if (DEBUG) Log.i(TAG, "pID : " + pID);

                int parentPid2 = getParentPid(pID);
                if (DEBUG) Log.d(TAG, "pID : " + pID + " _____ parentPid2 : " + parentPid2);
                callingPackage = getCallingPkgFromPid(parentPid2, context);

                if (TextUtils.isEmpty(callingPackage)) {
                    if (DEBUG) Log.d(TAG, "getCallingPkgFromPid callingPackage ====== zygote");
                    callingPackage = getCallingPkgFromPid(pID, context);
                }


                if (DEBUG)
                    Log.i(TAG, "result callingPackage : " + callingPackage + " , callingApp : " + callingApp);
                if (!TextUtils.isEmpty(callingPackage)) {
                    return callingPackage;
                }
                if (!TextUtils.isEmpty(callingApp)) {
                    return callingApp;
                }
            }
        } catch (Exception e) {
            if (DEBUG) Log.e(TAG, "getInstallerPackageName() installerPackageName = [" + installerPackageName + "] , error : " + e.toString());
            if (DEBUG) e.printStackTrace();
        }
        return null;
    }


    private static String getCallingPkgName(Context context) {

        if (DEBUG) Log.d(TAG, "getCallingPkgName() *************************************************************************");

        String callingPackage = null;
        try {
            PackageManager pm = context.getPackageManager();
            int callingUid = Binder.getCallingUid();
            String callingApp = pm.getNameForUid(callingUid);
            int pID = Binder.getCallingPid();
            if (DEBUG) Log.i(TAG, "pID : " + pID);
            int parentPid2 = getParentPid(pID);
            if (DEBUG) Log.d(TAG, "pID : " + pID + " _____ parentPid2 : " + parentPid2);
            callingPackage = getCallingPkgFromPid(parentPid2, context);
            if (TextUtils.isEmpty(callingPackage)) {
                callingPackage = getCallingPkgFromPid(pID, context);
            }
            if (DEBUG) Log.i(TAG, "getCallingPkgName() result : callingPackage : " + callingPackage + " , callingApp : " + callingApp);
            if (!TextUtils.isEmpty(callingPackage)) {
                return callingPackage;
            }
            if (!TextUtils.isEmpty(callingApp)) {
                return callingApp;
            }
        } catch (Exception e) {
            if (DEBUG) Log.e(TAG, "getCallingPkgName()  error : " + e.toString());
            if (DEBUG) e.printStackTrace();
        }
        return callingPackage;
    }

    private static int getParentPid(int pID) {
        // TODO: 17-10-10  just for compile
        int pPid = 0;
        try {
//            pPid = Process.getParentPid(pID);
        } catch (Exception e) {
        }
        return pPid;
    }


    private static String getCallingPkgFromPid(int pid, Context context) {
        if (DEBUG) Log.d(TAG, "getCallingPkgFromPid() called with: pid = [" + pid + "]");
        String callingPackage = null;
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> l = activityManager.getRunningAppProcesses();
            if (DEBUG)
                Log.i(TAG, "am.getRunningAppProcesses() size : " + (l == null ? 0 : l.size()));
            for (int i = 0; i < l.size(); i++) {
                ActivityManager.RunningAppProcessInfo pinfo = l.get(i);
                if (DEBUG) Log.d(TAG, "pinfo.pid : " + pinfo.pid + " _____ pkg : " + pinfo.processName);
                if (pinfo.pid == pid) {
                    callingPackage = pinfo.processName;
                    if (DEBUG) Log.i(TAG, "getCallingByPid  callingPackage : " + callingPackage);
                    break;
                }
            }
        } catch (Exception e) {
            if (DEBUG) Log.e(TAG, "getCallingPkgFromPid() called with: pid = [" + pid + "], error : " + e.toString());
            if (DEBUG) e.printStackTrace();
        }
        return callingPackage;
    }


    public static boolean forbidPermisson(Context context, String permissonName) {

        boolean result = false;
        try {
            if (permissonName != null && permissonName.equals("android.permission.INSTALL_PACKAGES")) { // 静默权限
                result = isForbidApp(context, permissonName, FORBID_INSTALL_FILE);
            } else if (permissonName != null && permissonName.equals("android.permission.SYSTEM_ALERT_WINDOW")) { // 弹窗权限
                result = isForbidApp(context, permissonName, FORBID_WINDOW_FILE);
            }
        } catch (Exception e) {
            if (DEBUG) Log.e(TAG, "forbidPermisson() called with:  permissonName = [" + permissonName + "]  error : " + e.toString());
            if (DEBUG) e.printStackTrace();
        }
        return result;
    }


    private static boolean isForbidApp(Context context, String permissonName, String fileName) {
        if (DEBUG) Log.d(TAG, "isForbidApp()  " + permissonName + "  ####################################################### ");

        if (!isSwitchOn()) {
            return false;
        }

        try {
            String callingPkg = getCallingPkgName(context);
            if (DEBUG) Log.d(TAG, "isForbidApp() callingPkg = [" + callingPkg + "]");
            if (!TextUtils.isEmpty(callingPkg)) {
                List<String> pkgs = readStr2List(fileName);
                if (DEBUG)
                    Log.d(TAG, "isForbidApp() readStr2List size = [" + (pkgs == null ? 0 : pkgs.size()) + "]");

                if (pkgs != null && pkgs.contains(callingPkg)) {
                    if (DEBUG) Log.d(TAG, "isForbidApp() !!! fileName = [" + fileName + "], #############----block----block----############## permissonName = [" + permissonName + "]");
                    return true;
                } else {
                    if (DEBUG) Log.d(TAG, "isForbidApp() callingPkg = [" + callingPkg + "] not contains!!!");
                }
            }
        } catch (Exception e) {
            if (DEBUG) Log.d(TAG, "isForbidApp() called with:  permissonName = [" + permissonName + "], fileName = [" + fileName + "] , error : " + e.toString());
            if (DEBUG) e.printStackTrace();
        }
        return false;
    }


    // 总共读取一行，获得集合
    private static synchronized List<String> readStr2List(String fileName) {
        if (DEBUG) Log.d(TAG, "readStr2List() called with: fileName = [" + fileName + "]");
        BufferedReader br = null;
        List<String> list = new ArrayList<String>();
        try {
            File dataDir = new File(ROOT_DIR);
            if (!dataDir.exists()) {
                return list;
            }
            fileName = dataDir.getAbsolutePath() + "/" + fileName;
            File file = new File(fileName);
            if (!file.exists()) {
                if (DEBUG) Log.e(TAG, "readStr2List() called with: fileName = [" + fileName + "] no exist!!!");
                return list;
            }
            br = new BufferedReader(new FileReader(file));
            String line;

            if ((line = br.readLine()) != null) {
                if (DEBUG) Log.d(TAG, "readStr2List() -------------------------- called with: fileName = [" + fileName + "] , read line : " + line);
                String[] strs = line.split(",");
                list = Arrays.asList(strs);
            }
            return list;

        } catch (Exception e) {
            if (DEBUG) Log.e(TAG, "readStr2List() error : " + e.toString());
            if (DEBUG) e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }


    /**
     * 获取当前时区
     */
    private static String getCurrentTimeZone() {
        String result = "";
        try {
            TimeZone tz = TimeZone.getDefault();
            result = tz.getDisplayName(false, TimeZone.SHORT);
        } catch (Exception e) {
            if (DEBUG) Log.e(TAG, "getCurrentTimeZone() error : " + e.toString());
        }
//		if (DEBUG) Log.d(TAG, "getCurrentTimeZone() result : " + result);
        return result;
    }


}
