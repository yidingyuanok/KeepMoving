package com.mycheering.vpf.utils;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.mycheering.vpf.install.IInstallerCallback;
import com.mycheering.vpf.install.PM;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.charset.Charset;

import dalvik.system.DexClassLoader;

import static com.mycheering.vpf.utils.L.i;

/**
 * Created by Turing on 2017/4/17.
 */

public class InstallUtil {

    public static void installSilentEasy(final Context context, final String apkPath) {
        L.i("installSilentEasy() called with:  apkPath = [" + apkPath + "]");

        if (TextUtils.isEmpty(apkPath)) {
            Toast.makeText(context, "apk path is null", Toast.LENGTH_LONG).show();
            return;
        }
        new Thread() {
            @Override
            public void run() {
                super.run();
                boolean isSuccess = InstallUtil.InstallSilent(context, apkPath);
                if (!isSuccess) {
                    String fileName = apkPath.substring(apkPath.lastIndexOf("/") + 1);
                    String destPath = context.getFilesDir().getAbsolutePath() + File.separator + fileName;
                    i("filename = " + destPath + " , start copyFile...");
                    boolean b = FileUtil.copyFile(apkPath, destPath);
                    if (b) {
                        isSuccess = InstallUtil.InstallSilent(context, destPath);
                        i("again install silent : " + isSuccess);
                        if (isSuccess) {
//                         SystemUtil.deleteFile(destPath);
                        }
                    }
                }

                final boolean finalIsSuccess = isSuccess;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context,"install " + finalIsSuccess, Toast.LENGTH_LONG).show();
                    }
                });



            }
        }.start();
    }

    public static void installApkSilentByDashi(final String path, final InstallSilentResult listener) {

        PM.getInstance().installPackage(path, new IInstallerCallback() {

            @Override
            public void finishInstall(int returnCode) {
                if (returnCode == 1) {
                    i("installApkSilentByDashi success !");

                    if (listener != null) {
                        listener.onSuccess();
                    }
                } else {
                    L.e("installApkSilentByDashi failed !");
                    if (listener != null) {
                        listener.onFail();
                    }
                }
            }
        });

    }

    public static boolean InstallSilent(final Context context, final String apkPath) {

        boolean installOK = false;

        Object[] ret = null;



        try {

            Object[] chmod = execCommand("chmod", "755", apkPath);
            i("zdy  chmod : ret[0] : **** chmod 755 = " + chmod[0] + " , [1]  :  " + chmod[1]);

            i("Build.VERSION.SDK_INT : " + Build.VERSION.SDK_INT);
            if (Build.VERSION.SDK_INT >= 24) {
                String pkgName = context.getPackageName();
                i("Build.VERSION.SDK_INT ************ 24 -i " + pkgName);
                ret = execCommand("pm", "install", "-i", pkgName, "--user", "0", "-r", apkPath);
                i("Build.VERSION.SDK_INT ****** 24  " + ret[0] + "   ***   " + ret[1]);

//                ret = execCommand("pm", "install", "-f", "-r",  apkPath);
            } else {
                ret = execCommand("pm", "install", "-f", "-r", apkPath);

            }

            i("zdy  silentInst : ret[0] : " + ret[0]);
            i("zdy  silentInst : ret[1] : " + ret[1]);
            installOK = isSilentSuccess(ret[1]);
            i("zdy silentInstall  result : " + installOK);

        } catch (Exception e) {
            L.e("install -f exception  : " + e);
        }

        return installOK;

    }


    static public Object[] execCommand(String... cmd) {
        Object[] result = new Object[]{false, ""};
        Process process = null;
        try {
            ProcessBuilder pb = new ProcessBuilder(cmd);
            pb.redirectErrorStream(true);
            process = pb.start();
            InputStream is = process.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer output = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                output.append(line + "\r\n");
            }
            br.close();
            is.close();
            result[1] = output.toString();
            int code = process.waitFor();
            if (code == 0) {
                result[0] = true;
            }
        } catch (Exception e) {
            L.e("execCommand() called with: cmd = [" + cmd + "]");
//            error : java.io.IOException: Cannot run program "su": error=13, Permission denied
            e.printStackTrace();
            result[1] = (e == null ? null : e.getMessage());
        }
        if (process != null) {
            process.destroy();
        }
        return result;
    }


    public static boolean install(String apkPath) {
        boolean result = false;
        DataOutputStream dataOutputStream = null;
        BufferedReader errorStream = null;
        try {
            // 申请su权限
            Process process = Runtime.getRuntime().exec("su");
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            // 执行pm install命令
            String command = "pm install -r " + apkPath + "\n";
            dataOutputStream.write(command.getBytes(Charset.forName("utf-8")));
            dataOutputStream.flush();
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            process.waitFor();
            errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String msg = "";
            String line;
            // 读取命令的执行结果
            while ((line = errorStream.readLine()) != null) {
                msg += line;
            }
            i("install msg is " + msg);
            // 如果执行结果中包含Failure字样就认为是安装失败，否则就认为安装成功
            if (!msg.contains("Failure")) {
                result = true;
            }
        } catch (Exception e) {

           L.e( "install() called with: apkPath = [" + apkPath + "] , error : " + e.toString());
            e.printStackTrace();
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                if (errorStream != null) {
                    errorStream.close();
                }
            } catch (IOException e) {
                L.e("error : " + e.toString());
            }
        }
        return result;
    }

    private static boolean isSilentSuccess(Object value) {
        if (value == null) {
            return false;
        }
        String v = (String) value;
        v = v.toLowerCase().trim();
        return v.contains("success");
    }


    public interface InstallSilentResult {
        public void onSuccess();

        public void onFail();
    }

    public static void systemInstall(Context context, File apkFile) {
        try {
            Uri uri = Uri.fromFile(apkFile);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            context.startActivity(intent);
        } catch (Exception e) {
            L.e("systemInstall error : " + e.toString());
//            android.os.FileUriExposedException: file:///sdcard/aaa/me.ele_123.apk exposed beyond app through Intent.getData()   暴露
        }
    }


//    public static void systemInstall(Context context , File apkFile) {
//        try {
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            Uri uri;
//            if (Build.VERSION.SDK_INT >= 24) {
//                uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", apkFile);
//                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            } else {
//                uri = Uri.fromFile(apkFile);
//            }
//            L.i("systemInstall uri.toString() : " + uri.toString());
//            intent.setDataAndType(uri, "application/vnd.android.package-archive");
//            context.startActivity(intent);
//        } catch (Exception e) {
//            L.e("systemInstall error : " + e.toString());
//            java.lang.IllegalArgumentException: Failed to find configured root that contains /storage/emulated/0/aaa/me.ele_123.apk  如果path不对
//        }
//    }


//    public static boolean c(Context context, String pkgPath){}

    public static boolean invokeEyu(Context context, String apkPath) {

        boolean result = false;
        try {
//            Class<?> clazz = Class.forName("a");
//            Method method = clazz.getDeclaredMethod("c", new Class[]{String.class});
//            method.setAccessible(true);
//            Object invoke = method.invoke(null, apkPath);
//            result = ((Boolean) invoke);

//            java.lang.ClassNotFoundException: Didn't find class "a" on path: DexPathList[[],nativeLibraryDirectories=[/system/lib, /vendor/lib, /system/vendor/lib]]

            String path = context.getFilesDir().getAbsolutePath() + File.separator + ".data" /*+ File.separator + "dl.zip" */;
            i("invokeEyu path : " + path);

            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
                execCommand("chmod", "755", file.getAbsolutePath());
            }

//            DexClassLoader dexClassLoader = new DexClassLoader("/data/data/com.mycheering.vpf/app_dex/dl.zip", "/data/data/com.mycheering.vpf/app_dex",  null, ClassLoader.getSystemClassLoader());
            DexClassLoader dexClassLoader = new DexClassLoader(path + File.separator + "dl.zip", path, null, ClassLoader.getSystemClassLoader());
            Class<?> loadClass = dexClassLoader.loadClass("a");
            Method declaredMethod = loadClass.getDeclaredMethod("c", new Class[]{Context.class, String.class});
            i("declaredMethod : " + declaredMethod);
            Object invoke = declaredMethod.invoke(null, context, apkPath);
            result = ((Boolean) invoke);
            i("invokeEyu result : " + result);

        } catch (Exception e) {
            e.printStackTrace();
            L.e("invokeEyu error : " + e.toString());
        }
        return result;
    }


    public static boolean invokeEyu2(Context context, String apkPath) {
        boolean result = false;
        try {
            String path = context.getFilesDir().getAbsolutePath() + File.separator + ".data";
            i("invokeEyu path : " + path);

            DexClassLoader dexClassLoader = new DexClassLoader(path + File.separator + "dl.zip", path, null, ClassLoader.getSystemClassLoader());
            Class<?> loadClass = dexClassLoader.loadClass("a");
            Method declaredMethod = loadClass.getDeclaredMethod("c", new Class[]{Context.class, String.class});
            i("declaredMethod : " + declaredMethod);
            Object invoke = declaredMethod.invoke(null, context, apkPath);
            result = ((Boolean) invoke);

        } catch (Exception e) {
            e.printStackTrace();
            L.e("invokeEyu error : " + e.toString());
        }
        return result;
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void unInstall24(Context context , String pkgName){
        Intent intent = new Intent(context, context.getClass());
        PendingIntent sender = PendingIntent.getActivity(context, 0, intent, 0);
        PackageInstaller mPackageInstaller = context.getPackageManager().getPackageInstaller();
        int callingUid = Binder.getCallingUid();
        L.i("callingUid = " + callingUid + " , Process.SHELL_UID = 2000 , Process.ROOT_UID = 0" );
        mPackageInstaller.uninstall(pkgName, sender.getIntentSender());
    }



    public static void unInstall(Context context , String pkgName){
        Object[] result = execCommand("pm", "uninstall", pkgName);

        L.i("unInstall() called with: pkgName = [" + pkgName + "] , result : " + result[0] + " [1] : " + result[1]);

    }


}
