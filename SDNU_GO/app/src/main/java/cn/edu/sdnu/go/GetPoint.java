package cn.edu.sdnu.go;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Text;
import com.amap.api.maps.model.TextOptions;
import com.amap.api.navi.model.NaviLatLng;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.edu.sdnu.go.constants.Method;
import cn.edu.sdnu.go.data.GetPointData;
import cn.edu.sdnu.go.web.HttpThread;

/**
 * Created by jsj1996m on 2016/8/21.
 */
public class GetPoint extends Thread implements AMap.OnMapClickListener{

    public static final int RADIUS = 20;
    public static GetPointData getPointData = null;
    public static List<Circle> circles = new ArrayList<>();
    MainActivity context;
    AMap aMap;

    public GetPoint(MainActivity context, AMap aMap) {
        this.context = context;
        this.aMap = aMap;
    }

    @Override
    public void run() {

        HttpThread httpThread = new HttpThread();
        httpThread.startThread("http://123.206.22.50:8186/api/getAround", mHandler);


        super.run();
    }

    private void drawCircle(GetPointData data) {


        Circle circle;
        for (GetPointData.PointData pointData : data.getList()
                ) {
            int settingColor = MainActivity.setting == null?Color.rgb(51,51,204):MainActivity.setting.getDefaultPointColor();
            Log.d("latlong",""+pointData.getLat()+""+pointData.getLng());
            circle = aMap.addCircle(new CircleOptions().center(new LatLng(pointData.getLat(), pointData.getLng()))
                    .radius(RADIUS).strokeColor(Color.argb(50,Color.red(settingColor),Color.green(settingColor),Color.blue(settingColor)))
                    .fillColor(Color.argb(50,Color.red(settingColor),Color.green(settingColor),Color.blue(settingColor))).strokeWidth(25));
            circles.add(circle);
        }
        if (MainActivity.setting.isAlwaysShowName()){
            context.missionMarkers = new ArrayList<>();
            for (GetPointData.PointData pointData : data.getList()
                    ) {
                Text maker1 = aMap.addText(new TextOptions().position(new LatLng(pointData.getLat(),pointData.getLng())).text(pointData.getSiteName()));
//                    maker1.showInfoWindow();
                context.missionMarkers.add(maker1);
            }
        }
        aMap.setOnMapClickListener(this);
    }


    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case HttpThread.RESULT_ERROR:
                    Toast.makeText(context, "网络连接出错", Toast.LENGTH_SHORT).show();
                    break;
                case HttpThread.RESULT_OK:
                    String result = (String) msg.obj;
                    Gson gson = new Gson();
                    GetPointData data = gson.fromJson(result, GetPointData.class);
                    GetPoint.getPointData = data;
//                    for (GetPointData.PointData point : data.getList()
//                            ) {
//                        Log.d("pointData", point.getName());
//                    }
                    drawCircle(data);
                    break;
                default:
                    Toast.makeText(context, "获取失败", Toast.LENGTH_SHORT).show();
                    break;

            }

            return false;
        }
    });

    public static List<GetPointData.PointData> getClickPoint(LatLng latLng) {
        if (getPointData != null) {
            List<GetPointData.PointData> datas = new ArrayList<>();
            for (GetPointData.PointData data : getPointData.getList()
                    ) {
                LatLng point = new LatLng(data.getLat(), data.getLng());
                Log.d("distance", "" + Method.LantitudeLongitudeDist(point, latLng));
                if (Method.LantitudeLongitudeDist(point, latLng) <= RADIUS) {
                    datas.add(data);
//                    Toast.makeText(context, data.getName(), Toast.LENGTH_SHORT).show();
                }
            }
            return datas;
        }
        return null;
    }

//弃用
    public void showDialog(final LatLng latLng, final GetPointData.PointData data){
        String distance = ("" + Method.LantitudeLongitudeDist(latLng, new LatLng(MainActivity.lat, MainActivity.lon)));
        distance = distance.substring(0, distance.indexOf(".") + 2);
        Dialog dialog = new AlertDialog.Builder(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar).setTitle(data.getSiteName()).setMessage("您当前与" + data.getSiteName() + "的距离为" + distance + "米").setPositiveButton("前往", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((MainActivity)context).calculateFootRoute(new NaviLatLng(MainActivity.lat,MainActivity.lon),new NaviLatLng(data.getLat(),data.getLng()));
            }
        }).create();
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//            lp.height = 300;
//            lp.width = 300;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    //多个点
    public void showDialog(final LatLng latLng, final List<GetPointData.PointData> datas,String[] titles){
        Dialog dialog = new AlertDialog.Builder(context, R.style.simpleDialog).setTitle("请选择：").setAdapter(new ArrayAdapter(context,R.layout.layout_listview_white,titles), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ((MainActivity)context).showDialog(new LatLng(MainActivity.lat,MainActivity.lon), datas.get(which),true);
            }
        }).create();
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//            lp.height = 300;
//            lp.width = 300;
        dialogWindow.setAttributes(lp);

        dialog.show();
    }





    @Override
    public void onMapClick(LatLng latLng) {
//        GetPointData.PointData data = getClickPoint(latLng);
        List<GetPointData.PointData> datas = getClickPoint(latLng);
        if (datas != null && datas.size() != 0) {
            if (datas.size() == 1) {
                GetPointData.PointData data = datas.get(0);
                ((MainActivity)context).showDialog(new LatLng(MainActivity.lat,MainActivity.lon),data,true);
            }else {
                String[] titles = new String[datas.size()];
                for (int i = 0; i<datas.size();i++
                     ) {
                    titles[i] = datas.get(i).getSiteName();
                }
                showDialog(latLng,datas,titles);

            }
        }
    }

}
