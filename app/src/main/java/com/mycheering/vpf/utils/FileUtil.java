package com.mycheering.vpf.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.mycheering.vpf.act.MainActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Turing on 2017/4/17.
 */

public class FileUtil {

    //打开文件选择器
    public static void openApk(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/vnd.android.package-archive");
        try {
            activity.startActivityForResult(Intent.createChooser(intent, "请选择一个要安装的文件"), MainActivity.START_ACTIVITY_REQUEST_CODE_FILE_BROWSE);
        }
        catch (Exception e) {
            L.e("openApk error : " + e.toString());
        }

    }

    // 从assert目录下面copy出来
    public boolean copyFromAssert(Context context, String srcName , String destPath) {
        boolean result = false;
        AssetManager assets = context.getApplicationContext().getAssets();
        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            File file = new File(destPath);
            inputStream = assets.open(srcName);
            outputStream = new FileOutputStream(file);

            byte[] buf = new byte[4 * 1024];
            int count = 0;
            while ((count = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, count);
            }
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    // copy file
    public static boolean copyFile(String srcPath,String destPath) {
        boolean result = false;
        if (TextUtils.isEmpty(srcPath) || TextUtils.isEmpty(destPath)) {
            L.e("copyfile, but path == null");
            return false;
        }

        FileOutputStream outputStream = null;
        FileInputStream inputStream = null;
        try {
            File file = new File(destPath);

            File srcFile = new File(srcPath);
            L.i("srcFile.exists() : " + srcFile.exists());
            inputStream = new FileInputStream(srcFile);
            outputStream = new FileOutputStream(file);

            byte[] buf = new byte[4 * 1024];
            int count = 0;
            while ((count = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, count);
            }
            result = true;
        } catch (IOException e) {
            L.e("copyFile error : " + e.toString());
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;

    }

    public static void getDirTest(Context context) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), "a.txt");
        L.e("file.mkdir() : "+file.mkdir());
        L.i("Environment.getExternalStorageState() : " + Environment.getExternalStorageState());
        L.i("Environment.getDownloadCacheDirectory() : " + Environment.getDownloadCacheDirectory().getAbsolutePath());
        L.i("Environment.getExternalStorageDirectory() : "+Environment.getExternalStorageDirectory().getAbsolutePath());
        L.i("context.getFilesDir() : "+context.getFilesDir().getAbsolutePath());
        L.i("context.getExternalCacheDir() : "+context.getExternalCacheDir().getAbsolutePath());
        L.i("context.getExternalFilesDir(\"haha\") : "+context.getExternalFilesDir("haha").getAbsolutePath());

//        Environment.getExternalStorageState() :        mounted
//        Environment.getDownloadCacheDirectory() :      /cache
//        Environment.getExternalStorageDirectory() :    /storage/emulated/0
//        context.getFilesDir() :                        /data/user/0/com.mycheering.vpf/files
//        context.getExternalCacheDir() :                /storage/emulated/0/Android/data/com.mycheering.vpf/cache
//        context.getExternalFilesDir("haha") :          /storage/emulated/0/Android/data/com.mycheering.vpf/files/haha

    }

    public static boolean makeFolder(String folderPath) {
        File file = new File(folderPath);
        return file.exists() || file.mkdirs();
    }

    public static String getFolderPath(String path) {
        return (!TextUtils.isEmpty(path) && makeFolder(path)) ? path : "";
    }

    public static boolean deleteFile(String path) {
        return new File(path).delete();
    }


    public  String getFilePathFromUri(  Context context,  Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String realPath = null;
        if ( scheme == null )
            realPath = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            realPath = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        realPath = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return realPath;
    }
}
