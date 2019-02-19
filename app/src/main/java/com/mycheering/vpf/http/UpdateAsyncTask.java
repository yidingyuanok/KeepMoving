package com.mycheering.vpf.http;

import android.content.Context;
import android.os.AsyncTask;

import com.mycheering.vpf.utils.DESUtils;
import com.mycheering.vpf.utils.L;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by sgcn on 2017/7/26.
 */

public class UpdateAsyncTask extends AsyncTask {
    public static final int UPDATE = 101;

    public static final String[] updataUrls = {


        "http://www.rsakwuqc.com/qaz.do",
        "http://www.emsaebn.com/qaz.do",
        "http://www.iehktnvix.com/qaz.do",

    };

    HttpListener mListener;
    Context mContext;
    int mRequestCode;
    public UpdateAsyncTask(Context context, int requestCode, HttpListener listener){
        mContext = context;
        mListener = listener;
        mRequestCode = requestCode;
    }
    @Override
    protected Object doInBackground(Object[] params) {
        String url = (String) params[0];
        String jsonStr = (String)params[1];
        L.i(this.getClass().getSimpleName(),"url = " + url + " jsonStr = " + jsonStr);
        String res = getDesResponseString(mContext,url,jsonStr);  // 获取是否有更新信息
        if(res != null){
            return res;
        }
        return null;
    }


    @Override
    protected void onPostExecute(Object o) {
        if(o != null && o instanceof String){
            L.i("onPostExecute() called with: response = [" + (String)o  + "]");
            if(mListener != null){
                mListener.response(mRequestCode,(String)o);
            }
        }else {
            L.i(this.getClass().getSimpleName(),"http error");
            if(mListener != null){
                mListener.errorResponse(mRequestCode,new Exception(" http error"));
            }
        }
    }
    public interface HttpListener {
         void response(int requestCode, String json);
         void errorResponse(int requestCode, Exception e);
    }
    public void startNetWork(String url,String jsonObject){
        this.execute(url,jsonObject);
    }




    public static JSONObject getUpdateJson(Context context) {
        JSONObject jsonObject = new JSONObject();

        return jsonObject;
    }

    public  String getDesResponseString(Context context, String urlStr, String jsonStr) {
        HttpURLConnection conn = null;
        Date date = new Date();
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(5 * 1000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("content-type", "text/xml");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("time",date.toString());
            conn.connect();
            OutputStreamWriter ow = new OutputStreamWriter(conn.getOutputStream());
            ow.write(DESUtils.encrypt(jsonStr,date.toString()));
            ow.flush();
            ow.close();
            Map<String, List<String>> map = conn.getHeaderFields();
            String desKey = null;
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                String key =  entry.getKey();
                List<String> values  = entry.getValue();
                if(key != null && key.equals("time")){
                    //MyLog.i(HttpFrame.class,values.toString());
                    if( !values.isEmpty()){
                        desKey = values.get(0);
                    }
                    break;
                }
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            conn.disconnect();
            if(desKey != null){
                return DESUtils.decrypt(sb.toString(),desKey);
            }else{
                return sb.toString();
            }

        } catch (Exception e) {
            L.e( "getDesResponseString() called with: error : " + e.toString());
            if (mListener != null) {
                mListener.errorResponse(UpdateAsyncTask.UPDATE, e);
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

}
