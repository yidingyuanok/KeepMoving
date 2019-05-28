package com.mycheering.vpf.utils;

import android.os.Environment;
import android.util.Log;

import com.mycheering.vpf.BuildConfig;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Turing on 2017/4/13.
 */

public class L {
    private static final String TAG = "keepfuck";
    private static final String MSG = " -Msg: ";

    private static final String LOG_FILE_NAME = "keepfucklog.txt";
    private static final boolean LOG_TO_FILE = false;


    static {
        if (BuildConfig.DEBUG) {
            if (LOG_TO_FILE) {
                writeLog("--------------------- NEW LOG ------------------------------------\r\n");
            }
        }
    }

    public static void i(String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, localDate() + MSG + msg);
            if (LOG_TO_FILE) {
                writeLog(localDate() + MSG + msg);
            }
        }
    }

    public static void i(String TAG , String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, localDate() + MSG + msg);
            if (LOG_TO_FILE) {
                writeLog(localDate() + MSG + msg);
            }
        }
    }


    public static void e( String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, localDate() + MSG + msg);
            if (LOG_TO_FILE) {
                writeLog(localDate() + MSG + msg);
            }
        }
    }

    public static void e(String TAG ,String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, localDate() + MSG + msg);
            if (LOG_TO_FILE) {
                writeLog(localDate() + MSG + msg);
            }
        }
    }


    static private String localDate() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return fmt.format(new Date());
    }

    static private void writeLog(String msg) {
        if (BuildConfig.DEBUG) {
            try {
                File file = new File(Environment.getExternalStorageDirectory().getPath() + LOG_FILE_NAME);
                RandomAccessFile raf = new RandomAccessFile(file, "rw");
                FileChannel fc = raf.getChannel();
                fc.position(fc.size());
                fc.write(ByteBuffer.wrap(msg.getBytes()));
                fc.close();
                raf.close();
            } catch (Exception e) {

            }
        }
    }
}
