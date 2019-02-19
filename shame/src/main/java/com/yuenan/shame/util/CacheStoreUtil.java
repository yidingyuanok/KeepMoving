package com.yuenan.shame.util;

import android.content.Context;
import android.graphics.Bitmap;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CacheStoreUtil extends BaseStoreUtil {

    public static File getCacheDir(Context ctx) {
        return getRootDir(ctx, Constants.WENBA_CACHE, false);
    }

    public static File getVideoDir(Context ctx) {
        return getRootDir(ctx, Constants.WENBA_VIDEO, false);
    }

    public static File getImagesDir(Context ctx) {
        return getRootDir(ctx, Constants.WENBA_IMAGES, false);
    }

    public static File getLogsDir(Context ctx) {
        return getRootDir(ctx, Constants.WENBA_LOGS, false);
    }

    public static File getUserEventDir(Context ctx) {
        return getRootDir(ctx, Constants.WENBA_CACHE, false);
    }


    public static long getCacheSize(Context context) throws Exception {
        long cacheSize = 0;
        File cacheDir = getCacheDir(context);
        File imagesDir = getRootDir(context, Constants.WENBA_IMAGES, false);
        File recordFilesDir = getRootDir(context, Constants.WENBA_VIDEO, false);
        if (cacheDir != null) {
            cacheSize += FileUtils.sizeOfDirectory(cacheDir);
        }
        if (imagesDir != null) {
            cacheSize += FileUtils.sizeOfDirectory(imagesDir);
        }
        if (recordFilesDir != null) {
            cacheSize += FileUtils.sizeOfDirectory(recordFilesDir);
        }
        return cacheSize;
    }

    //保存文件到指定路径
    public static File saveBitmap(Context context, Bitmap bmp, String goodsId) {
        // 首先保存图片
        int hashcode = goodsId.hashCode();
        File targetParent = CacheStoreUtil.getImagesDir(context);
        final File target = new File(targetParent, hashcode + ".jpg");
        try {
            FileOutputStream fos = new FileOutputStream(target);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return target;
    }


}
