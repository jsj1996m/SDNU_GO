package cn.edu.sdnu.go.menu;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.Switch;
import com.gc.materialdesign.widgets.ColorSelector;

import cn.edu.sdnu.go.MainActivity;
import cn.edu.sdnu.go.R;

/**
 * Created by jsj1996m on 2016/8/31.
 */
public class SettingActivity extends AppCompatActivity{

    private Switch is2dSwitch;
    private Switch isAlwaysNameSwitch;
    private TextView setting2dHint;
    private RelativeLayout colorShow;
    private Toolbar toolbar;
    private boolean selectIs2d;
    private boolean selectIsAlwaysName;
    private int selectColor;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_menu);
        initView();
        addBackButton();
    }


    private void initView(){

        toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        is2dSwitch = (Switch) findViewById(R.id.setting_is2d_switch);
        isAlwaysNameSwitch = (Switch) findViewById(R.id.setting_isAlwaysShowName_switch);
        setting2dHint = (TextView) findViewById(R.id.setting_is2d_hint);
        colorShow = (RelativeLayout) findViewById(R.id.setting_colorShow);
        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        selectColor = sharedPreferences.getInt("defaultPointColor",Color.parseColor("#66ccff"));
        selectIs2d = sharedPreferences.getBoolean("is2d",true);
        is2dSwitch.setChecked(!selectIs2d);
        selectIsAlwaysName = sharedPreferences.getBoolean("isAlwaysShowName",false);
        isAlwaysNameSwitch.setChecked(selectIsAlwaysName);
        setting2dHint.setText(selectIs2d?"2d视角":"3d视角");
        colorShow.setBackgroundColor(selectColor);

        is2dSwitch.setOncheckListener(new Switch.OnCheckListener() {
            @Override
            public void onCheck(Switch is2dSwitch, boolean b) {
                if (b){
                    setting2dHint.setText("3d视角");
                }else {
                    setting2dHint.setText("2d视角");

                }
            }
        });

        colorShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorSelector colorSelector = new ColorSelector(SettingActivity.this, selectColor, new ColorSelector.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int i) {
                        selectColor = i;
                        colorShow.setBackgroundColor(i);
                        Toast.makeText(SettingActivity.this,"颜色更改需要重启应用后生效",Toast.LENGTH_SHORT).show();

                    }
                });
                colorSelector.show();
            }
        });

    }

    private void addBackButton(){


        //将返回键设置为白色
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(upArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("is2d",!is2dSwitch.isCheck());
                editor.putInt("defaultPointColor",selectColor);
                editor.putBoolean("isAlwaysShowName",isAlwaysNameSwitch.isCheck());

                editor.commit();
                MainActivity.setting.setDefaultPointColor(selectColor);
                MainActivity.setting.setIs2d(!is2dSwitch.isCheck());
                MainActivity.setting.setAlwaysShowName(isAlwaysNameSwitch.isCheck());
                SettingActivity.this.finish();
            }
        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onDestroy() {
        SharedPreferences sharedPreferences = getSharedPreferences("settings",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("is2d",!is2dSwitch.isCheck());
        editor.putInt("defaultPointColor",selectColor);
        editor.putBoolean("isAlwaysShowName",isAlwaysNameSwitch.isCheck());
        editor.commit();
        MainActivity.setting.setDefaultPointColor(selectColor);
        MainActivity.setting.setIs2d(!is2dSwitch.isCheck());
        MainActivity.setting.setAlwaysShowName(isAlwaysNameSwitch.isCheck());
        super.onDestroy();
    }
}
