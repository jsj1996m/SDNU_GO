package cn.edu.sdnu.go.data;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by jsj1996m on 2016/9/1.
 */
public class BitmapData implements Serializable{

    Bitmap bitmap;
    String url;

    public BitmapData(Bitmap bitmap,String url){
        this.bitmap = bitmap;
        this.url = url;

    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
