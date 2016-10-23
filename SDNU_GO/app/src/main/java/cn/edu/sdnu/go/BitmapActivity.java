package cn.edu.sdnu.go;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import cn.edu.sdnu.go.web.GetBitmapThread;

/**
 * Created by jsj1996m on 2016/9/1.
 */
public class BitmapActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView imageView;
    private Bitmap bitmap;
    private String url;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_bitmap);
        imageView = (ImageView) findViewById(R.id.bitmap_dialog_imageView);

        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = wm.getDefaultDisplay().getWidth();
        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
        lp.width = screenWidth;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        imageView.setLayoutParams(lp);

        imageView.setMaxWidth(screenWidth);
        imageView.setMaxHeight(screenWidth * 5);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
//        bitmap = bundle.getParcelable("bitmap");
//        imageView.setImageDrawable(new BitmapDrawable(bitmap));
        url = bundle.getString("url");
        GetBitmapThread getBitmapThread = new GetBitmapThread();
        getBitmapThread.startThread(url, getBitmapXHandler);

        imageView.setOnClickListener(BitmapActivity.this);


    }

    Handler getBitmapXHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what) {
                case 200:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    if (imageView != null) {
                        imageView.setImageDrawable(new BitmapDrawable(bitmap));

                    }
                    break;
            }
            return false;
        }
    });

    @Override
    public void onClick(View v) {
        this.finish();
    }
}
