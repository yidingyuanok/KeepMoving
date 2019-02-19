package com.yuenan.shame.util;

/**
 * 常量配置类
 * Created by liuhuacheng
 * Created on 17/10/20
 */
public class Constants {
    /**
     * 线上环境
     */
    public static final int MODE_ONLINE = 0;
    /**
     * QA环境
     */
    public static final int MODE_QA = 1;

    /**
     * API环境
     */
    public static final int API_MODE = MODE_ONLINE;

    public static final boolean IS_DEBUG = true;//是否开始debug log调试

    public static final boolean LEAK_CANARY = false;//是否开启内存检测LeakCanary

    public static final int LOGOUT_NOTIFY_ID = 1;

    /**
     * 存储目录/文件
     **/
    public static final String WENBA_DIR = "/note";
    public static final String WENBA_CACHE = WENBA_DIR + "/cache";
    public static final String WENBA_IMAGES = WENBA_DIR + "/images";
    public static final String WENBA_VIDEO = WENBA_DIR + "/video";
    public static final String WENBA_LOGS = WENBA_DIR + "/logs";
    public static final String WENBA_EVENTS = WENBA_DIR + "/events";
    public static final String APK_DOWNLOAD_PATH = "/download";// 应用更新apk下载位置

    // 应用更新类型
    public static final int UPGRADE_BY_SELF = 1;// 应用内更新
    public static final int UPGRADE_BY_BROWSER = 2;// 跳浏览器更新ø

    public static final String BASE_URL_QA = "http://123.59.40.14:8099"; //测试环境
    public static final String BASE_URL_ONLINE = "http://123.59.40.14:8099";//线上环境

    public static String getDomainBaseUrl() {
        String baseUrl = null;
        switch (Constants.API_MODE) {
            case Constants.MODE_ONLINE:
                baseUrl = BASE_URL_ONLINE;
                break;
            case Constants.MODE_QA:
                baseUrl = BASE_URL_QA;
                break;
        }
        return baseUrl;
    }

    public static final String BROADCAST_USER_UNLOGIN = "com.wenba.note.unlogin";//用户未登录

    //用户
    public static final String USER_LOGIN_URL = "/cmcc/login";//登录
    public static final String DOCUMENT_LIST_URL = "/cmcc/listDemo";//文档列表
    public static final String DOCUMENT_ADD_URL = "/cmcc/adddemo";//增加文档
    public static final String DOCUMENT_UPDATE_URL = "/cmcc/update";//修改文档
    public static final String DOCUMENT_MERGE_URL = "/cmcc/dataMerge";//合并文档
    public static final String OCR_RECOGNITION_URL = "/feed/cmccOcr";//手写识别
    public static final String DOCUMENT_WEB_URL = "/index.html#/?uid=";//文档列表

}
