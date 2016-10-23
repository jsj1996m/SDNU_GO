package cn.edu.sdnu.go.web;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;

/**
 * Created by jsj1996m on 2016/8/26.
 */
public class HttpThread extends Thread {

    private String urlString;
    private String method;
    private Handler handler;
    private String cookie;
    private Map<String, String> paramaters;

    public static final int RESULT_OK = 0;
    public static final int RESULT_ERROR = -1;

    public void startThread(String url, Handler handler) {
        this.urlString = url;
        this.method = "GET";
        this.handler = handler;
        this.start();
    }

    public void startThread(String url, String method, Handler handler) {
        this.urlString = url;
        this.method = method;
        this.handler = handler;
        this.start();
    }

    @Override
    public void run() {
        super.run();
        if (method.equals("GET")){
            doGet(this.urlString);
        }else if (method.equals("POST")){

        }


    }







    private void doGet(String url) {
        Log.d("url", url);
        HttpGet httpRequest = new HttpGet(url);
        if (!(this.cookie == null || this.cookie.equals(""))) {
            httpRequest.addHeader("Cookie", cookie);
        }
        HttpParams paramses = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(paramses, 5000); //设置连接超时
        HttpConnectionParams.setSoTimeout(paramses, 10000);
        httpRequest.setParams(paramses);
        //使用DefaultHttpClient类的execute方法发送HTTP GET请求，并返回HttpResponse对象。
        HttpResponse httpResponse = null;//其中HttpGet是HttpUriRequst的子类
        try {
            httpResponse = new DefaultHttpClient().execute(httpRequest);
            Log.d("httpcode", "" + httpResponse.getStatusLine().getStatusCode());
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                Log.d("cookie", httpResponse.getHeaders("Set-Cookie").toString());
                HttpEntity httpEntity = httpResponse.getEntity();
                String result = EntityUtils.toString(httpEntity);//取出应答字符串
                // 一般来说都要删除多余的字符
                result.replaceAll("\r", "");//去掉返回结果中的"\r"字符，否则会在结果字符串后面显示一个小方格
                Log.d("result:", result);
                Message msg = new Message();
                if (result.contains("<html>")){
                    msg.what = RESULT_ERROR;
                    handler.sendMessage(msg);
                    return;
                }
                msg.what = RESULT_OK;
                msg.obj = result;
                handler.sendMessage(msg);

            } else {
                httpRequest.abort();
                Message msg = new Message();
                msg.what = RESULT_ERROR;
                handler.sendMessage(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void doPost(String url) {
        Log.d("url",url);
        HttpPost httpRequest = new HttpPost(url);
        if (!(this.cookie==null||this.cookie.equals(""))){
            httpRequest.addHeader("Cookie",cookie);
        }
        HttpParams paramses = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(paramses, 5000); //设置连接超时
        HttpConnectionParams.setSoTimeout(paramses, 10000);
        httpRequest.setParams(paramses);
        //使用DefaultHttpClient类的execute方法发送HTTP GET请求，并返回HttpResponse对象。
        HttpResponse httpResponse = null;//其中HttpGet是HttpUriRequst的子类
        try {
            httpResponse = new DefaultHttpClient().execute(httpRequest);
            Log.d("httpcode", "" + httpResponse.getStatusLine().getStatusCode());
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                Log.d("cookie", httpResponse.getHeaders("Set-Cookie").toString());
                HttpEntity httpEntity = httpResponse.getEntity();
                String result = EntityUtils.toString(httpEntity);//取出应答字符串
                // 一般来说都要删除多余的字符
                result.replaceAll("\r", "");//去掉返回结果中的"\r"字符，否则会在结果字符串后面显示一个小方格
                Log.d("result:", result);
                Message msg = new Message();
                msg.what=RESULT_OK;
                msg.obj = result;
                handler.sendMessage(msg);

            } else {
                httpRequest.abort();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}
