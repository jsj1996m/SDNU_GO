package cn.edu.sdnu.go.menu;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import cn.edu.sdnu.go.MainActivity;
import cn.edu.sdnu.go.R;

/**
 * Created by jsj1996m on 2016/9/1.
 */
public class AboutUsActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_about_us);
        initView();
        addBackButton();


    }
    private void initView(){
        toolbar = (Toolbar) findViewById(R.id.aboutUs_toolbar);
    }


    private void addBackButton(){


        //将返回键设置为白色
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(upArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutUsActivity.this.finish();
            }
        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

}
