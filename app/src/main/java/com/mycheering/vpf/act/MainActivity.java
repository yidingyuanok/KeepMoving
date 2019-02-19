package com.mycheering.vpf.act;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mycheering.vpf.R;
import com.mycheering.vpf.base.BaseActivity;
import com.mycheering.vpf.dailnetapn.DialUtils;
import com.mycheering.vpf.service.TestService;
import com.mycheering.vpf.utils.FileUtil;
import com.mycheering.vpf.utils.InstallUtil;
import com.mycheering.vpf.utils.L;
import com.mycheering.vpf.utils.PermissionUtil;
import com.mycheering.vpf.utils.SystemUtil;
import com.mycheering.vpf.view.AdShowView;

import java.io.File;

import static com.mycheering.vpf.utils.SystemUtil.checkUsageStatsPermission;

/**
 * Created by zdyok on 2016/11/17.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;
    public static final int PERMISSION_REQUEST_CODE = 201;
    public static final int PERMISSIONS_REQUEST_CODE = 202;
    public static final int START_ACTIVITY_REQUEST_CODE_USAGE = 203;
    public static final int START_ACTIVITY_REQUEST_CODE_FILE_BROWSE = 204;
    private EditText et_apkPath;

    // test

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mContext = this;
        initView();
        //设置事件监听
        setTextViewListener();

//        applyPermission();
        Toast.makeText(mContext,mContext.getPackageName(), Toast.LENGTH_SHORT).show();


    }

    private void change(String a) {
        a = "fuck";
    }

    private void initView() {
        findViewById(R.id.tv28).setOnClickListener(this);
        findViewById(R.id.tv27).setOnClickListener(this);
        findViewById(R.id.tv26).setOnClickListener(this);
        findViewById(R.id.tv25).setOnClickListener(this);
        findViewById(R.id.tv24).setOnClickListener(this);
        findViewById(R.id.tv23).setOnClickListener(this);
        findViewById(R.id.tv22).setOnClickListener(this);

        findViewById(R.id.tv21).setOnClickListener(this);
        findViewById(R.id.tv20).setOnClickListener(this);

        et_apkPath = (EditText) findViewById(R.id.et1);
        findViewById(R.id.tv1).setOnClickListener(this);
        findViewById(R.id.tv2).setOnClickListener(this);
        findViewById(R.id.tv3).setOnClickListener(this);
        findViewById(R.id.tv4).setOnClickListener(this);
        findViewById(R.id.tv5).setOnClickListener(this);
        findViewById(R.id.tv6).setOnClickListener(this);
        findViewById(R.id.tv7).setOnClickListener(this);
        findViewById(R.id.tv8).setOnClickListener(this);
        findViewById(R.id.tv9).setOnClickListener(this);
        findViewById(R.id.tv10).setOnClickListener(this);
        findViewById(R.id.tv11).setOnClickListener(this);
        findViewById(R.id.tv12).setOnClickListener(this);
        findViewById(R.id.tv13).setOnClickListener(this);
        findViewById(R.id.tv14).setOnClickListener(this);
        findViewById(R.id.tv15).setOnClickListener(this);
        findViewById(R.id.tv16).setOnClickListener(this);
        findViewById(R.id.tv17).setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    private void setTextViewListener() {

    }

    public static void actionActivity(Context context, Class clazz) {
        Intent starter = new Intent(context, clazz);
        context.startActivity(starter);
    }


    Handler mHandler = new Handler();


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        L.i("requestCode = [" + requestCode + "], permissions = [" + permissions.length + "], grantResults = [" + grantResults.length + "]");
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    L.i("permission grant !!!");
                } else {
                    L.e("permission no no grant !!!");
                }
                break;
            case PERMISSIONS_REQUEST_CODE:
                for (int i = 0; i < grantResults.length; i++) {

                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        L.i(permissions[i] + " grant !!!");
                    } else {
                        L.e(permissions[i] + " no no grant !!!");
                    }
                }


                break;
        }
    }

    private void applyPermission() {
        PermissionUtil.needPermission(this, PERMISSIONS_REQUEST_CODE, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.CAMERA,
                },
                new PermissionUtil.IPermissionListener() {
                    @Override
                    public void onPermissionAlreadyGranted() {
                        Toast.makeText(mContext, "申请的权限早已经开启了", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void onClick(View v) {
        L.e("onClick : " + v.getId());
        Toast.makeText(mContext, v.getId()+"" ,Toast.LENGTH_SHORT).show();

        switch (v.getId()) {
            case R.id.tv28:
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), ServiceAct.class);
                        startActivity(intent);
                    }
                }, 5 * 1000);
                break;

            case R.id.tv27:
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       L.i("run() called");
                        new AdShowView(getApplicationContext(), null, null);

//                Intent intent = new Intent(getApplicationContext(), TestService.class);
//                startService(intent);
//
                    }
                }, 5 * 1000);
                break;

            case R.id.tv26:
                //卸载今日头条极速版
                InstallUtil.unInstall(getApplicationContext(),"com.ss.android.article.lite");

                break;

            case R.id.tv25:
                final String apkPath3 = et_apkPath.getText().toString().trim();
                new Thread(){
                    @Override
                    public void run() {
                        InstallUtil.invokeEyu(mContext ,apkPath3);
                    }
                }.start();
                break;
            case R.id.tv24:
                new Thread(){
                    @Override
                    public void run() {
                        SystemUtil.invokeA(mContext, "a", "a" );
                    }
                }.start();

                break;
            case R.id.tv23:
                actionActivity(MainActivity.this, AnimationActivity.class);
                break;
            case R.id.tv22:
//                com.android.gallery3d ___ com.android.gallery3d.app.MovieActivity
//                com.google.android.apps.photos ___ com.google.android.apps.photos.pager.HostPhotoPagerActivity
//                com.UCMobile ___ com.UCMobile.video
//
//                com.qiyi.video ___ org.iqiyi.video.activity.PlayerActivity
//                com.tencent.qqlive ___ com.tencent.qqlive.open.LocalVideoOpenActivity  腾讯视频
//                com.tencent.research.drop ___ com.tencent.research.drop.player.activity.PlayerActivity  QQ影音

//                SystemUtil.setDefaultPlayer(mContext,"com.qiyi.video" , "org.iqiyi.video.activity.PlayerActivity" );
                SystemUtil.setDefaultPlayer(mContext,"com.tencent.qqlive" , "com.tencent.qqlive.open.LocalVideoOpenActivity" );

                break;
            case R.id.tv21:
                SystemUtil.setDefaultSms(mContext,"com.jb.gosms");  // go短信
                break;
            case R.id.tv20:

                SystemUtil.setDefaultPhone(mContext , "com.cootek.smartdialer");    // 触宝电话
                break;

            case R.id.tv17:
                SystemUtil.setDefaultLauncher(mContext ,"com.qihoo360.launcher");
                break;
            case R.id.tv16:
                SystemUtil.testDefaultBrowser(mContext);
                break;
            case R.id.tv15:
                //   intent.setClassName("com.qihoo.browser","com.qihoo.browser.BrowserActivity");
//                intent.setClassName("com.UCMobile","com.UCMobile.main.UCMobile");
                SystemUtil.setDefaultBrowser(mContext, "com.qihoo.browser", "com.qihoo.browser.BrowserActivity");
//                SystemUtil.setDefaultBrowser(mContext, "com.UCMobile","com.UCMobile.main.UCMobile");
                break;
            case R.id.tv1:
//         Viewpager+RecyclerView+fragment
                actionActivity(MainActivity.this, RVActivity.class);
                break;
            case R.id.tv2:
//        Viewpager+ListView+fragment
                actionActivity(MainActivity.this, LVActivity.class);
                break;
            case R.id.tv3:
//        RecyclerView头部显示或隐藏
                actionActivity(MainActivity.this, RVHeaderActivity.class);
                break;
            case R.id.tv4:
//        服务的生命周期
                actionActivity(MainActivity.this, ServiceAct.class);
                break;
            case R.id.tv5:
//        外warp内match
                actionActivity(MainActivity.this, DragViewActivity.class);
                break;
            case R.id.tv6:
//                SystemUtil.getTopApp(MainActivity.this);
//                开启服务，监控用户点击的activity
                Intent intent = new Intent(MainActivity.this, TestService.class);
                intent.setPackage(mContext.getPackageName());
                mContext.startService(intent);
                break;
            case R.id.tv7:
//          有权查看使用情况的应用
                if (!checkUsageStatsPermission(mContext)) {
                    Intent intent7 = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                    startActivityForResult(intent7, START_ACTIVITY_REQUEST_CODE_USAGE);
                } else {
                    Toast.makeText(this, "权限已开启！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv8:
//                打开网络， 6.0以下版本可以强起网络
                DialUtils.dialAndSetFlag(mContext);
                break;
            case R.id.tv9:
//                关闭网络
                DialUtils.hangupAndResetFlag(mContext);
                break;
            case R.id.tv10:
//                动态代理
                try {
                    new InvocationHandlerTest().testInvocationHandler();
                } catch (Exception e) {
                    L.e("InvocationHandlerTest error : " + e.toString());
                }
                break;
            case R.id.tv11:
                // 打开文件浏览
                FileUtil.openApk(((Activity) mContext));
                break;
            case R.id.tv12:
//                静默安装，小米note6.0手机 选择sd卡上的apk，安装失败；自己的私有目录可以安装成功！,最后发现，copy到私有目录还是安装失败！
//                但是从assert目录copy就可以安装成功！
                //  java.lang.SecurityException: Neither user 10080 nor current process has android.permission.INSTALL_PACKAGES.  // data分区下
// 小米note 6.0  java.lang.SecurityException: You either need MANAGE_USERS or CREATE_USERS permission to: query users  // data分区下
                 final String apkPath = et_apkPath.getText().toString().trim();

//                apkPath = mContext.getFilesDir().getAbsolutePath() + File.separator + "360-store.apk";

//                InstallUtil.installSilentEasy(mContext, apkPath);


                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        InstallUtil.installSilentEasy(mContext, apkPath);
                    }
                } , 5 * 1000);


//                InstallUtil.InstallSilent(mContext, apkPath);
//                InstallUtil.install( apkPath);




                break;
            case R.id.tv13:
                // 大师的安装方法，
// 小米note 6.0   java.lang.SecurityException: Neither user 10175 nor current process has android.permission.INSTALL_PACKAGES.
                String apkPath1 = et_apkPath.getText().toString().trim();
                if (TextUtils.isEmpty(apkPath1)) {
                    Toast.makeText(mContext, "apk path is null", Toast.LENGTH_LONG).show();
                    return;
                }
                InstallUtil.installApkSilentByDashi(apkPath1, null);
                break;
            case R.id.tv14:
                // TODO: 2017/6/7 还么有测试
//                content://com.mycheering.vpf.fileprovider/external_storage_path_me/aaa/me.ele_123.apk
//                external_storage_path_me 表示 对外暴露的路径的 代名词，具体代表什么，决定于xml文件中的path
//                真实路径： /sdcard/aaa/me.ele_123.apk
//                由于忘记申请sdcard权限，掉坑里去了，读取不到apk，解析包错误！
                String apkpath2 = et_apkPath.getText().toString().trim();
                File apkFile = new File(apkpath2);
                L.i("new File(apkpath2).exists() : " + apkFile.exists());
                apkFile = apkFile.getAbsoluteFile();
                if (!TextUtils.isEmpty(apkpath2)) {
                    InstallUtil.systemInstall(mContext, apkFile);
                }
                break;


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case START_ACTIVITY_REQUEST_CODE_USAGE:
                if (!checkUsageStatsPermission(mContext)) {
                    Toast.makeText(this, "权限未开启！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "权限已开启！", Toast.LENGTH_SHORT).show();
                }
                break;
            case START_ACTIVITY_REQUEST_CODE_FILE_BROWSE:
                if (data == null) {
                    L.e("START_ACTIVITY_REQUEST_CODE_FILE_BROWSE data == null");
                    return;
                }
                Uri uri = data.getData();
                String path = uri.getPath();
                L.i("apk path : " + path);
                //  TODO: 2017/6/7 apk路径
                String fileName = path.substring(path.lastIndexOf(":") + 1);

//                path = "sdcard/aaa" + fileName;
                path = "sdcard/" + fileName;
//                path = mContext.getFilesDir().getAbsolutePath()  + fileName; // 如果是私有目录就不用FileProvider

                et_apkPath.setText(path);

                break;
        }

    }


}
