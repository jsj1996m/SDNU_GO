package cn.edu.sdnu.go.web;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.edu.sdnu.go.data.BitmapData;
import cn.edu.sdnu.go.data.GetSiteInfoData;

/**
 * Created by jsj1996m on 2016/8/28.
 */
public class GetViewPagerThread extends Thread {

    private Handler handler;
    private List<String> urls;
    private List<String> urlXs;
    private List<GetSiteInfoData.ImageInfoData> images;
    private boolean isForX = false;

    private int size;
    private ArrayList<BitmapData> result;

    public GetViewPagerThread(List<GetSiteInfoData.ImageInfoData> images) {
        this.images = images;
        this.size = images.size();
        result = new ArrayList<>();
        urls = new ArrayList<>();
        urlXs = new ArrayList<>();
        for (GetSiteInfoData.ImageInfoData data : images
                ) {
            urls.add(data.getContent());
            urlXs.add(data.getContentX());
        }

    }


    public void startThread(Handler handler) {
        this.handler = handler;
        isForX = false;
        this.run();
    }

    public void startThreadForX(Handler handler) {
        this.handler = handler;
        isForX = true;
        this.run();
    }

    @Override
    public void run() {
        super.run();
        List<String> url;
//        if (isForX) {
//            url = this.urlXs;
//        } else {
//            url = this.urls;
//        }

        for (GetSiteInfoData.ImageInfoData data : images
                ) {
            GetBitmapThread getBitmapThread = new GetBitmapThread();
            getBitmapThread.startThread(data.getContent(), new ViewPagerHandler(data.getContentX()));
        }

    }

//    Handler getBitmapHandler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//            switch (msg.what) {
//                case 200:
//                    Bitmap bitmap = (Bitmap) msg.obj;
//                    result.add(bitmap);
//                    if (result.size() == size) {
//                        Message messsage = new Message();
//                        messsage.what = 200;
//                        messsage.obj = result;
//                        handler.sendMessage(messsage);
//
//                    }
//                    break;
//                default:
//                    break;
//            }
//
//            return false;
//        }
//    });



    class ViewPagerHandler extends Handler{
        String url;
        public ViewPagerHandler(String url){
            this.url = url;
        }

        @Override
        public void handleMessage(Message msg) {
            Bitmap bitmap = (Bitmap) msg.obj;
            result.add(new BitmapData(bitmap,url));
            if (result.size() == size) {
                Message messsage = new Message();
                messsage.what = 200;
                messsage.obj = result;
                handler.sendMessage(messsage);

            }
        }
    }
}
