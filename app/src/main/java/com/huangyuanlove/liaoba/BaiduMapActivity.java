package com.huangyuanlove.liaoba;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.huangyuanlove.liaoba.customui.ElasticListView;
import com.huangyuanlove.liaoba.customui.indris.material.RippleView;

import java.util.ArrayList;
import java.util.List;

public class BaiduMapActivity extends AppCompatActivity {
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private MapView baiduMapView;
    private RippleView location_button;
    private BDLocation mLocation;
    private RoutePlanSearch mSearch;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_baidu_map);
        baiduMapView = (MapView) findViewById(R.id.bmapView);

        location_button = (RippleView) findViewById(R.id.location);
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        initLocation();
        location_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLocationClient.start();
            }
        });

    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 2000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {

        private BaiduMap mBaiduMap;

        @Override
        public void onReceiveLocation(BDLocation location) {

            mLocation = location;
            //Receive Location

            mBaiduMap = baiduMapView.getMap();
            mBaiduMap.clear();
            //定义Maker坐标点
            LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_marka);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap);
            //在地图上添加Marker，并显示
            mBaiduMap.addOverlay(option);
            //定义地图状态
            MapStatus mMapStatus = new MapStatus.Builder()
                    .target(point)
                    .zoom(15)
                    .build();

            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            //改变地图状态
            mBaiduMap.setMapStatus(mMapStatusUpdate);
            //获取定位信息
            StringBuffer sb = new StringBuffer(256);
            sb.append("时间 : ");
            sb.append(location.getTime());

            sb.append("\n纬度 : ");
            sb.append(location.getLatitude());
            sb.append("\n经度 : ");
            sb.append(location.getLongitude());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\n速度 : ");
                sb.append(location.getSpeed()+"公里/小时");// 单位：公里每小时
                sb.append("\n地址 : ");
                sb.append(location.getAddrStr());

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\n位置 : ");
                sb.append(location.getAddrStr());
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\n描述 : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\n描述 : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\n描述 : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\n描述 : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\n位置描述 : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {

                for (Poi p : list) {
                    sb.append("\n附近点 : ");
                    sb.append(p.getName());
                }
            }
            mLocationClient.stop();
            AlertDialog alertDialog = new AlertDialog.Builder(BaiduMapActivity.this)
                    .setTitle("位置信息")
                    .setMessage(sb.toString())
                    .setPositiveButton("公交路线", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showPopupWindowForRoutePlanSearch();
                        }
                    })
                    .setNegativeButton("取消",null)
                    .setCancelable(true)
                    .create();
            alertDialog.show();
        }
    }

    @Override
    protected void onDestroy() {
        baiduMapView.onDestroy();
        super.onDestroy();
    }

    private void showPopupWindowForRoutePlanSearch() {

        View view = LayoutInflater.from(this).inflate(R.layout.popupwindow_for_route_plan_search, null);

        final PopupWindow popupWindow = new PopupWindow(view, ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth(), ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight() / 4);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAsDropDown(location_button, 100, 0);
        RippleView ok = (RippleView) view.findViewById(R.id.popupwindow_ok);

        RippleView reset = (RippleView) view.findViewById(R.id.popupwindow_reset);
        final EditText start = (EditText) view.findViewById(R.id.start_location);
        final EditText stop = (EditText) view.findViewById(R.id.stop_location);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setText("");
                stop.setText("");
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startLocation = start.getText().toString().trim();
                String stopLocation = stop.getText().toString().trim();
                startRoutePlanSearch(startLocation, stopLocation);
                popupWindow.dismiss();
            }
        });
    }

    private void startRoutePlanSearch(String starLocation, String stopLocation) {
        String cityName = mLocation.getCity();
        mSearch = RoutePlanSearch.newInstance();

        OnGetRoutePlanResultListener onGetRoutePlanResultListener = new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
            }

            @Override
            public void onGetTransitRouteResult(final TransitRouteResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(BaiduMapActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                }
                if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                    //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                    //result.getSuggestAddrInfo()
                    Toast.makeText(BaiduMapActivity.this, "抱歉，起终点或途经点地址有岐义", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                    View view = LayoutInflater.from(BaiduMapActivity.this)
                            .inflate(R.layout.alert_route_result, null);
                    ElasticListView listView = (ElasticListView) view.findViewById(R.id.route_option);
                    final TextView routeDetail = (TextView) view.findViewById(R.id.route_detail);
                    List<String> list = new ArrayList<>();
                    for (int i = 0; i < result.getRouteLines().size(); i++) {
                        list.add("线路" + (i + 1));
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(BaiduMapActivity.this, android.R.layout.simple_list_item_1, list);
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            TransitRouteLine transitRouteLine = result.getRouteLines().get(position);
                            int duration = transitRouteLine.getDuration();
                            List<TransitRouteLine.TransitStep> allStep = transitRouteLine.getAllStep();
                            index = position;
                            StringBuffer sb = new StringBuffer();
                            sb.append("花费时间：" + duration + "秒\n");
                            for (int i = 0; i < allStep.size(); i++) {
                                sb.append(allStep.get(i).getInstructions() + "\n");

                            }
                            routeDetail.setText(sb.toString());
                        }
                    });

                    AlertDialog alertDialog = new AlertDialog.Builder(BaiduMapActivity.this)
                            .setTitle("请选择路线：")
                            .setView(view)
                            .setCancelable(true)
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    baiduMapView.getMap().clear();
                                    List<TransitRouteLine.TransitStep> allStep = result.getRouteLines().get(index).getAllStep();
                                    for (int i = 0; i < allStep.size(); i++) {
                                        LatLng location = allStep.get(i).getEntrance().getLocation();
                                        OverlayOptions textOption = new TextOptions()
                                                .bgColor(0X88717171)
                                                .fontSize(50)
                                                .fontColor(0XFFFFFF00)
                                                .text(allStep.get(i).getInstructions())
                                                .position(location);
                                        Log.d("xuanxuan",allStep.get(i).getInstructions());
                                        baiduMapView.getMap().addOverlay(textOption);
                                    }

                                    TransitRouteOverlay overlay = new TransitRouteOverlay(baiduMapView.getMap());
                                    overlay.setData(result.getRouteLines().get(index));
                                    overlay.addToMap();
                                    overlay.zoomToSpan();

                                }
                            })
                            .create();
                    alertDialog.show();
                }
            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
            }
        };
        mSearch.setOnGetRoutePlanResultListener(onGetRoutePlanResultListener);
        PlanNode stNode = PlanNode.withCityNameAndPlaceName(cityName, starLocation);
        PlanNode enNode = PlanNode.withCityNameAndPlaceName(cityName, stopLocation);

        mSearch.transitSearch((new TransitRoutePlanOption())
                .from(stNode)
                .city(cityName)
                .to(enNode));
    }

}