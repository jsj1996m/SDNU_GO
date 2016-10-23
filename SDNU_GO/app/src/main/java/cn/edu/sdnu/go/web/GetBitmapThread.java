package cn.edu.sdnu.go.web;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by jsj1996m on 2016/8/26.
 */
public class GetBitmapThread extends Thread{

    String url ;
    Handler handler;

    public void startThread(String url,Handler handler){
        this.handler = handler;
        this.url = url;

        this.start();


    }

    @Override
    public void run() {

        super.run();
        try {
            URL url = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            int statusCode = connection.getResponseCode();
            Message message = new Message();
            if (200 == statusCode) {
                message.what = 200;
                InputStream inputStream = connection.getInputStream();

                message.obj = inputStream;
            } else {
                message.what = statusCode;
            }

            decodeHandler.sendMessage(message);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    Handler decodeHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 200:
                    InputStream in = (InputStream) msg.obj;
                    new ToBitmapThread(in,handler).startThread();
                    break;
            }
            return false;
        }
    });


    class ToBitmapThread extends Thread {

        private InputStream in;
        private Handler handler;

        public void startThread() {
            this.start();
        }

        public ToBitmapThread(InputStream in, Handler handler) {
            this.in = in;

            this.handler = handler;


        }


        @Override
        public void run() {
            super.run();
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            Message message = new Message();
            message.what = 200;
            message.obj = bitmap;
            handler.sendMessage(message);


        }
    }


}
