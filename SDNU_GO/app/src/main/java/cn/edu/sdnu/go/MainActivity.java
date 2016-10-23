package cn.edu.sdnu.go;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.Text;
import com.amap.api.maps.model.TextOptions;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.autonavi.amap.mapcore.AMapNativeRenderer;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.gc.materialdesign.views.ButtonFlat;
import com.github.amlcurran.showcaseview.ShowcaseDrawer;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;
import com.yalantis.guillotine.animation.GuillotineAnimation;
import com.yalantis.guillotine.util.GuillotineInterpolator;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import cn.edu.sdnu.go.constants.Constants;
import cn.edu.sdnu.go.constants.Method;
import cn.edu.sdnu.go.constants.ViewPagerAdapter;
import cn.edu.sdnu.go.data.BitmapData;
import cn.edu.sdnu.go.data.GetMissionData;
import cn.edu.sdnu.go.data.GetPointData;
import cn.edu.sdnu.go.data.GetSiteInfoData;
import cn.edu.sdnu.go.data.SettingData;
import cn.edu.sdnu.go.fab.Fab;
import cn.edu.sdnu.go.menu.AboutUsActivity;
import cn.edu.sdnu.go.menu.SettingActivity;
import cn.edu.sdnu.go.web.GetBitmapThread;
import cn.edu.sdnu.go.web.GetViewPagerThread;
import cn.edu.sdnu.go.web.HttpThread;

public class MainActivity extends AppCompatActivity implements LocationSource, AMapLocationListener, AMapNaviListener, View.OnClickListener {
    private MapView mapView;
    private AMap aMap;
    private UiSettings mUiSetting;

    public static List<LatLng> missionPoints;
    //当前地点的经纬度
    public static double lat;
    public static double lon;
    public TextView dialogTextView;
    public ImageView dialogImageView;
    public ViewPager dialogViewPager;
    public ShowcaseView helpPage1;
    public ShowcaseView helpPage2;
    public ShowcaseView helpPage3;

    private MaterialSheetFab materialSheetFab;
    private MaterialSheetFab materialSheetMission;
    private int statusBarColor;

    //菜单项
    private LinearLayout menuSetting;
    private LinearLayout menuHelp;
    private LinearLayout menuActivity;
    private LinearLayout menuAboutUs;
    private LinearLayout menuLogout;
    //任务地点marker
    public List<Text> missionMarkers;


    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private RadioGroup mGPSModeGroup;
    private Dialog pointDitalDialog;

    private EditText ed;

    //BitmapDialog用Imagview
    private ImageView bitmapDialogImageView;
    //任务用Listview
    ListView missionListView;
    public static SettingData setting;

    //上部菜单
    GuillotineAnimation guillotineAnimation;

    //神奇的菜单是否显示
    private boolean isShownMenu = false;
    // 规划线路
    private RouteOverLay mRouteOverLay;
    private AMapNavi aMapNavi;


    //第一次定位成功后重设地图
    private boolean flag = true;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//初始化导航
        setContentView(R.layout.activity_main);
        aMapNavi = AMapNavi.getInstance(getApplicationContext());
        aMapNavi.addAMapNaviListener(this);
        aMapNavi.setEmulatorNaviSpeed(150);

        SharedPreferences sharedPreferences = getSharedPreferences("point", Context.MODE_PRIVATE);
        String point = sharedPreferences.getString("point", "");
        Log.d("points", point);

//初始化上部菜单
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final View guillotineMenu = LayoutInflater.from(this).inflate(R.layout.menu_layout, null);
        menuAboutUs = (LinearLayout) guillotineMenu.findViewById(R.id.aboutus_group);
        menuActivity = (LinearLayout) guillotineMenu.findViewById(R.id.activity_group);
        menuHelp = (LinearLayout) guillotineMenu.findViewById(R.id.help_group);
        menuLogout = (LinearLayout) guillotineMenu.findViewById(R.id.logout_group);
        menuSetting = (LinearLayout) guillotineMenu.findViewById(R.id.settings_group);
        menuAboutUs.setOnClickListener(this);
        menuActivity.setOnClickListener(this);
        menuHelp.setOnClickListener(this);
        menuLogout.setOnClickListener(this);
        menuSetting.setOnClickListener(this);


        FrameLayout root = (FrameLayout) findViewById(R.id.root);
        root.addView(guillotineMenu);
        View contentHamburger = findViewById(R.id.content_hamburger);

        guillotineAnimation = new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger)
                .setActionBarViewForAnimation(toolbar)
                .build();
        guillotineAnimation.close();


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }


//重新定位及检查点按钮


//--------------------------
        mapView = (MapView) findViewById(R.id.main_mapView);
        mapView.onCreate(savedInstanceState);

        initView();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initView() {

        setting = getSetting();

        setUpFab();

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setUpMap();
//                aMap.animateCamera(new CameraUpdateFactory().newLatLngZoom(new LatLng(lat,lon),18));
//                aMap.animateCamera(new CameraUpdateFactory().changeTilt(0));
//                List<GetPointData.PointData> datas = GetPoint.getClickPoint(new LatLng(lat, lon));
//                if ((lat != 0 && lon != 0) && datas != null && datas.size() > 0) {
//                    if (datas.size() == 1) {
//
//                        if (missionPoints != null) {
////                            removeLatLng(new LatLng(datas.get(0).getLat(), datas.get(0).getLng()));
//
//                        }
//                        showDialog(new LatLng(lat, lon), datas.get(0));
//
//                    } else {
//                        String[] titles = new String[datas.size()];
//                        for (int i = 0; i < datas.size(); i++
//                                ) {
//                            titles[i] = datas.get(i).getSiteName();
//                        }
//                        showDialog(new LatLng(lat, lon), datas, titles);
//                    }
//                }
//                SharedPreferences sharedPreferences = getSharedPreferences("point", Context.MODE_PRIVATE);
//                String point = sharedPreferences.getString("point", "");
//                Log.d("points", point);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString("point",point +ed.getText().toString() + ":" + lat + "_" + lon + "\n");
//                editor.commit();
//                SharedPreferences sharedPreferences2 = getSharedPreferences("point", Context.MODE_PRIVATE);
//                String point2 = sharedPreferences2.getString("point", "");
//                Toast.makeText(MainActivity.this,point2,Toast.LENGTH_LONG).show();


//            }
//        });
//任务按钮

        missionListView = (ListView) findViewById(R.id.mission_listView);


        setUpMission();
//        misson.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setUpMap();
//                HttpThread httpThread = new HttpThread();
//                httpThread.startThread("http://123.206.22.50/api/getTaskList?TypeID=0", getMissionListHandler);
//
//            }
//        });

        ed = (EditText) findViewById(R.id.ed);

        if (aMap == null) {

            aMap = mapView.getMap();
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Constants.SDNU, 18));
            mRouteOverLay = new RouteOverLay(aMap, null, getApplicationContext());
            mUiSetting = aMap.getUiSettings();
            //隐藏比例尺
            mUiSetting.setScaleControlsEnabled(false);
            mUiSetting.setZoomControlsEnabled(false);
            mUiSetting.setZoomGesturesEnabled(true);

        }
        setUpMap();
        GetPoint getPoint = new GetPoint(this, aMap);
        getPoint.start();
        SharedPreferences sharedPreferences = getSharedPreferences("help", Context.MODE_PRIVATE);
        boolean isShownHelpPage = sharedPreferences.getBoolean("isShownHelpPage", false);

        if (!isShownHelpPage) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isShownHelpPage", true);
            editor.commit();
            showHelp();
        }


    }


    private SettingData getSetting() {
        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        boolean is2d = sharedPreferences.getBoolean("is2d", true);
        boolean isAlwaysShowName = sharedPreferences.getBoolean("isAlwaysShowName", true);
        int color = sharedPreferences.getInt("defaultPointColor", Color.parseColor("#66ccff"));
        SharedPreferences.Editor editor = sharedPreferences.edit();
        SettingData settingData = new SettingData(is2d, color, isAlwaysShowName);
        editor.putBoolean("is2d", settingData.is2d());
        editor.putBoolean("isAlwaysShowName", false);
        editor.putInt("defaultPointColor", settingData.getDefaultPointColor());
        return settingData;
    }


    //将任务点设置为红色

    public void makeMissionCircle(List<GetPointData.PointData> missionPoints) {

        if (!setting.isAlwaysShowName()) {
            if (missionMarkers == null) {
                missionMarkers = new ArrayList<>();
            }
            if (missionMarkers.size() != 0) {
                for (Text marker : missionMarkers
                        ) {
                    marker.remove();
                }
                missionMarkers = new ArrayList<>();
            }
        }
        for (Circle circle1 : GetPoint.circles
                ) {

            int color = setting.getDefaultPointColor();
            circle1.setStrokeColor(Color.argb(50, Color.red(color), Color.green(color), Color.blue(color)));
            circle1.setFillColor(Color.argb(50, Color.red(color), Color.green(color), Color.blue(color)));
            circle1.setStrokeWidth(25);
        }
        for (Circle circle : GetPoint.circles
                ) {
            for (GetPointData.PointData data : missionPoints
                    ) {
                if (Method.LatlngEquals(circle.getCenter(), new LatLng(data.getLat(), data.getLng()))) {
                    circle.setStrokeColor(Color.argb(75, 255, 0, 0));
                    circle.setFillColor(Color.argb(75, 255, 0, 0));
                    circle.setStrokeWidth(50);
                    if (!setting.isAlwaysShowName()) {
//                    Marker maker1 = aMap.addMarker(new MarkerOptions().position(new LatLng(data.getLat(),data.getLng())).title(data.getSiteName()).draggable(false));
                        Text maker1 = aMap.addText(new TextOptions().position(new LatLng(data.getLat(), data.getLng())).text(data.getSiteName()));
//                    maker1.showInfoWindow();
                        missionMarkers.add(maker1);
                    }

                }
            }
        }
    }


    //显示详细信息Dialog
    public void showDialog(LatLng latLng, final GetPointData.PointData data, boolean isFromGetPoint) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.simpleDialog);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_view, null);
        dialogViewPager = (ViewPager) dialogView.findViewById(R.id.dialog_viewPager);
        List<View> list = new ArrayList<>();
        ImageView imageView = new ImageView(this);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.background));
        list.add(imageView);
        dialogViewPager.setAdapter(new ViewPagerAdapter(list, this));
        dialogImageView = (ImageView) dialogView.findViewById(R.id.dialog_imageView);
        dialogTextView = (TextView) dialogView.findViewById(R.id.dialog_textView);
        GetBitmapThread getBitmapThread = new GetBitmapThread();
//        getBitmapThread.startThread("http://ocloa3w1p.bkt.clouddn.com/20160828115455.jpg_test", getBitmapHandler);
        HttpThread getSiteThread = new HttpThread();
        getSiteThread.startThread("http://123.206.22.50:8186/api/getSiteInfo?SiteID=" + data.getSiteID(), getSiteInfoHandler);
//                imageView.setImageBitmap();
//                viewPager.setAdapter(imageAdapter);

        builder.setView(dialogView);
        builder.setTitle(data.getSiteName());
        if (isFromGetPoint) {
            ButtonFlat btn = (ButtonFlat) dialogView.findViewById(R.id.dialog_btnGo);
            btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    calculateFootRoute(new NaviLatLng(MainActivity.lat, MainActivity.lon), new NaviLatLng(data.getLat(), data.getLng()));
                    pointDitalDialog.dismiss();
                }

            });

        }
        ButtonFlat btnOut = (ButtonFlat) dialogView.findViewById(R.id.dialog_btnOut);
        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pointDitalDialog.dismiss();
            }
        });

        pointDitalDialog = builder.create();
        pointDitalDialog.show();
    }

    //显示重叠地点时listDialog
    public void showDialog(final LatLng latLng, final List<GetPointData.PointData> datas, String[] titles) {
        Dialog dialog = new AlertDialog.Builder(this, R.style.simpleDialog).setTitle("请选择：").setItems(titles, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (missionPoints != null) {

//                    removeLatLng(new LatLng(datas.get(which).getLat(), datas.get(which).getLng()));
                }
                showDialog(latLng, datas.get(which), false);
            }
        }).create();
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    //显示任务listDialog
    public void showDialog(final List<GetMissionData.MissionData> datas, String[] titles) {
        Dialog dialog = new AlertDialog.Builder(this, R.style.simpleDialog).setTitle("任务列表").setItems(titles, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GetMissionData.MissionData data = datas.get(which);
//                HttpThread httpThread = new HttpThread();
//                httpThread.startThread("http://123.206.22.50/api/getTaskArea?TaskID="+data.getTaskID()+"&TypeID=0",getMissionDetailHandler);
                String missionListStr = data.getSiteList();
                String[] missionIdStrs = missionListStr.split(",");
                int[] missionIds = new int[missionIdStrs.length];
                for (int i = 0; i < missionIdStrs.length; i++) {
                    missionIds[i] = Integer.parseInt(missionIdStrs[i]);
                }
                creatMission(missionIds);

            }
        }).create();
        dialog.show();

    }

    // 获取到任务列表后处理
    public void creatMission(int[] ids) {

        List<GetPointData.PointData> points = new ArrayList<>();
        missionPoints = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            for (GetPointData.PointData data : GetPoint.getPointData.getList()
                    ) {
                if (ids[i] == data.getSiteID()) {
                    points.add(data);
                    missionPoints.add(new LatLng(data.getLat(), data.getLng()));

                }
            }
        }


        makeMissionCircle(points);
//        drawLine(points, new LatLng(lat, lon));


    }


    private int getStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getWindow().getStatusBarColor();
        }
        return 0;
    }

    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
    }


    private void setUpFab() {
        Fab fab = (Fab) findViewById(R.id.fab);
        View sheetView = findViewById(R.id.fab_sheet);
        View overlay = findViewById(R.id.overlay);
        int sheetColor = getResources().getColor(R.color.background_card);
        int fabColor = getResources().getColor(R.color.theme_accent);
        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay, sheetColor, fabColor);

        // Set material sheet event listener
        materialSheetFab.setEventListener(new MaterialSheetFabEventListener() {
            @Override
            public void onShowSheet() {
                // Save current status bar color
                statusBarColor = getStatusBarColor();

                if (materialSheetMission.isSheetVisible()) {
                    materialSheetMission.hideSheet();
                }

                // 重置地图视角
                reSetMap();
//                aMap.animateCamera(new CameraUpdateFactory().newLatLngZoom(new LatLng(lat, lon), 20));
//                aMap.animateCamera(new CameraUpdateFactory().changeTilt(setting.is2d()?0:45));
                final List<GetPointData.PointData> datas = GetPoint.getClickPoint(new LatLng(lat, lon));


                        setStatusBarColor(getResources().getColor(R.color.theme_primary_dark2));
                ListView listView = (ListView) findViewById(R.id.fab_listView);

                if ((lat != 0 && lon != 0) && datas != null &&datas.size() > 0) {
                    String[] titles = new String[datas.size()];
                    for (int i = 0; i < datas.size(); i++
                            ) {
                        titles[i] = datas.get(i).getSiteName();
                    }
                    ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, titles);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            showDialog(new LatLng(lat, lon), datas.get(position), false);
                            materialSheetFab.hideSheet();
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "请前往附近的地标！", Toast.LENGTH_SHORT).show();
                    materialSheetFab.hideSheet();
                }
            }

            @Override
            public void onHideSheet() {
                // Restore status bar color
                setStatusBarColor(statusBarColor);

            }
        });

    }


    private void setUpMission() {
        Fab mission = (Fab) findViewById(R.id.mission);
        View sheetView = findViewById(R.id.mission_sheet);
        View overlay = findViewById(R.id.mission_overlay);
        int sheetColor = getResources().getColor(R.color.background_card);
        int fabColor = getResources().getColor(R.color.theme_accent);
        materialSheetMission = new MaterialSheetFab<>(mission, sheetView, overlay, sheetColor, fabColor);
        materialSheetMission.setEventListener(new MaterialSheetFabEventListener() {

            @Override
            public void onShowSheet() {
                super.onShowSheet();
//                重置视角
                if (materialSheetFab.isSheetVisible()) {
                    materialSheetFab.hideSheet();
                }
                reSetMap();
//                aMap.animateCamera(new CameraUpdateFactory().newLatLngZoom(new LatLng(lat, lon), 20));
//                aMap.animateCamera(new CameraUpdateFactory().changeTilt(setting.is2d()?0:45));

                HttpThread httpThread = new HttpThread();
                httpThread.startThread("http://123.206.22.50:8186/api/getTaskList?TypeID=0", getMissionListHandler);
                missionListView.setAdapter(new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, new String[]{"载入中"}));

            }

            @Override
            public void onHideSheet() {
                super.onHideSheet();
            }
        });

    }


    private void showHelp() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final FloatingActionButton misson = (FloatingActionButton) findViewById(R.id.mission);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
        lp.setMargins(margin, margin, margin, margin);

        helpPage1 = new ShowcaseView.Builder(this).setTarget(new ViewTarget(fab))
                .setContentTitle("探索")
                .setContentText("点击此处可以查看附近地标的详细信息")
                .setStyle(R.style.CustomShowcaseTheme2)
                .blockAllTouches()
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        helpPage1.hide();
                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                        int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
                        lp.setMargins(margin, margin, margin, margin);

                        helpPage2 = new ShowcaseView.Builder(MainActivity.this).setTarget(new ViewTarget(misson))
                                .setContentTitle("分类")
                                .setContentText("点击此处可以将特殊类别的地标标红")
                                .blockAllTouches()

                                .setStyle(R.style.CustomShowcaseTheme2).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        helpPage2.hide();
                                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                                        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                        int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
                                        lp.setMargins(margin, margin, margin, margin);
                                        helpPage3 = new ShowcaseView.Builder(MainActivity.this).setTarget(new ViewTarget(findViewById(R.id.content_hamburger)))
                                                .setContentTitle("菜单")
                                                .setContentText("点击此处以展示菜单")
                                                .blockAllTouches()
                                                .setStyle(R.style.CustomShowcaseTheme2)
                                                .build();
                                        helpPage3.setButtonPosition(lp);

                                    }
                                })
                                .build();
                        helpPage2.setButtonPosition(lp);
                    }
                })
                .build();
        helpPage1.setButtonPosition(lp);
    }

    private void reSetMap() {
        aMap.setMyLocationType(setting.is2d() ? AMap.LOCATION_TYPE_LOCATE : AMap.LOCATION_TYPE_MAP_ROTATE);
        aMap.animateCamera(new CameraUpdateFactory().newLatLngZoom(new LatLng(lat, lon), 20));
        aMap.animateCamera(new CameraUpdateFactory().changeTilt(setting.is2d() ? 0 : 45));


    }

    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);
        aMap.setMyLocationType(setting.is2d() ? AMap.LOCATION_TYPE_LOCATE : AMap.LOCATION_TYPE_MAP_ROTATE);
        aMap.animateCamera(new CameraUpdateFactory().newLatLngZoom(new LatLng(lat, lon), 20));
        aMap.animateCamera(new CameraUpdateFactory().changeTilt(setting.is2d() ? 0 : 45));


        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(),
                        R.drawable.location_marker)));
        myLocationStyle.strokeColor(Color.argb(0, 255, 255, 255));
        myLocationStyle.radiusFillColor(Color.argb(0, 255, 255, 255));
        aMap.setMyLocationStyle(myLocationStyle);
    }


    //计算步行路线
    public void calculateFootRoute(NaviLatLng mNaviStart, NaviLatLng mNaviEnd) {
        boolean isSuccess = aMapNavi.calculateWalkRoute(mNaviStart, mNaviEnd);
        if (!isSuccess) {
            Toast.makeText(this, "路线计算失败,检查参数情况", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();

        }

    }

    @Override
    public void onBackPressed() {
        if (materialSheetFab.isSheetVisible()) {
            materialSheetFab.hideSheet();
            return;
        }
        if (materialSheetMission.isSheetVisible()) {
            materialSheetMission.hideSheet();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;

    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
//                mLocationErrText.setVisibility(View.GONE);

//                Marker marker = aMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
//                        .decodeResource(getResources(),
//                                R.drawable.location_marker))));
//                marker.setPosition(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()));
//                marker.setRotateAngle(amapLocation.getBearing());

                lon = amapLocation.getLongitude();
                lat = amapLocation.getLatitude();
//                if (missionPoints != null) {
//                    ed.setText("" + missionPoints.size());
//                    clearLines();
//                    drawLine(missionPoints, new LatLng(lat, lon));
//                }


                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
//                LatLng thisLatLng = new LatLng(amapLocation.getLatitude();
//                Toast.makeText(this, Method.LantitudeLongitudeDist(thisLatLng, Constants.SDNU) + "", Toast.LENGTH_SHORT).show();
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    //    显示大图
    private void showBitmapDialog(Bitmap bitmap, String url) {
        Intent intent = new Intent();
        intent.setClass(this, BitmapActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("url", url);
//        bundle.putParcelable("bitmap",bitmap);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //    初始化ViewPager
    public void initViewPager(List<BitmapData> images) {
        if (dialogViewPager != null) {
            List<View> views = new ArrayList<>();
            for (final BitmapData bitmapData : images
                    ) {
                ImageView imageView = new ImageView(this);
                imageView.setImageBitmap(bitmapData.getBitmap());
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        im
                        showBitmapDialog(bitmapData.getBitmap(), bitmapData.getUrl());
                    }
                });
                views.add(imageView);
            }
            ViewPagerAdapter adapter = new ViewPagerAdapter(views, this);

            dialogViewPager.setAdapter(adapter);

        }
    }


    //    获取任务列表
    Handler getMissionListHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what) {
                case HttpThread.RESULT_OK:
                    String result = (String) msg.obj;
                    Gson gson = new Gson();
                    GetMissionData data = gson.fromJson(result, GetMissionData.class);
                    final List<GetMissionData.MissionData> datas = data.getList();
                    String[] titles = new String[datas.size()];
                    for (int i = 0; i < datas.size(); i++) {
                        titles[i] = datas.get(i).getName();
                    }
//                    showDialog(datas, titles);
                    ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, titles);
                    missionListView.setAdapter(adapter);
                    missionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            GetMissionData.MissionData data = datas.get(position);
                            String missionListStr = data.getSiteList();
                            String[] missionIdStrs = missionListStr.split(",");
                            int[] missionIds = new int[missionIdStrs.length];
                            for (int i = 0; i < missionIdStrs.length; i++) {
                                missionIds[i] = Integer.parseInt(missionIdStrs[i]);
                            }
                            creatMission(missionIds);
                            materialSheetMission.hideSheet();
                        }
                    });

                    break;
                case HttpThread.RESULT_ERROR:
                default:
                    break;
            }

            return false;
        }
    });


    //获取imgaes list
    Handler getViewPagerHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 200:
                    List<BitmapData> images = (List<BitmapData>) msg.obj;
                    initViewPager(images);
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    //获取单一地点的详细数据
    Handler getSiteInfoHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case HttpThread.RESULT_OK:
                    String result = (String) msg.obj;
                    Gson gson = new Gson();
                    GetSiteInfoData data = gson.fromJson(result, GetSiteInfoData.class);
                    List<GetSiteInfoData.ImageInfoData> images = data.getImageInfo();
                    GetViewPagerThread getViewPagerThread = new GetViewPagerThread(images);
                    getViewPagerThread.startThread(getViewPagerHandler);
                    dialogTextView.setText(data.getSiteDescription());
                    break;
                case HttpThread.RESULT_ERROR:
                default:
                    break;


            }

            return false;
        }
    });

    //获取左上略缩图
    Handler getBitmapHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 200:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    if (dialogImageView != null) {
                        dialogImageView.setImageBitmap(bitmap);
//                        dialogImageView.setBackground(new BitmapDrawable(bitmap));
                    }
                    break;
            }
            return false;
        }
    });

    @Override
    public void onCalculateRouteSuccess() {
        AMapNaviPath naviPath = aMapNavi.getNaviPath();
        if (naviPath == null) {
            return;
        }
        // 获取路径规划线路，显示到地图上
        mRouteOverLay.setAMapNaviPath(naviPath);
        mRouteOverLay.addToMap();

    }

    @Override
    public void onCalculateRouteFailure(int i) {

        Toast.makeText(this, "路径规划出错 " + i, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.settings_group:
                Intent intent = new Intent();
                intent.setClass(this, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.aboutus_group:
                Intent intent1 = new Intent();
                intent1.setClass(this, AboutUsActivity.class);
                startActivity(intent1);
                break;
            case R.id.logout_group:
                this.finish();
                this.onDestroy();
                Process.killProcess(Process.myPid());
                System.exit(0);
                break;
            case R.id.help_group:
                showHelp();
                break;
        }
        guillotineAnimation.close();

    }


    //导航接口所实现的方法

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }


    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateMultipleRoutesSuccess(int[] ints) {

    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    //以下为高德地图的重写方法


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
//        setUpMap();
        reSetMap();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://cn.edu.sdnu.go/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://cn.edu.sdnu.go/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
