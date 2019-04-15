package com.hxd.study.utils;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.ALARM_SERVICE;

/**
 * 作 者： Cazaea
 * 日 期： 2019/4/11
 * 邮 箱： wistorm@sina.com
 */
public class LocationUtil implements AMapLocationListener {

    /**
     * 基础定位
     */
    private Context mContext;
    private static LocationUtil mInstance;

    // 声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    // 声明AMapLocationClientOption对象
    private AMapLocationClientOption mLocationOption = null;

    //
    private Intent mAlarmIntent = null;
    // 手机长时间息屏，每隔一段时间唤醒一次CPU
    private AlarmManager mAlarmManager = null;
    private PendingIntent mAlarmPendingIntent = null;

    /**
     * 获取实例,方便操作方法
     *
     * @return LocationUtil Instance
     */
    public static LocationUtil getInstance() {
        if (mInstance == null) {
            synchronized (LocationUtil.class) {
                if (mInstance == null) {
                    mInstance = new LocationUtil();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化
     *
     * @param pContext 全局Application
     */
    public void init(Context pContext) {
        this.mContext = pContext;
        initLocation();
        initWakeUpService();
    }

    /**
     * 叫醒休眠的CPU
     */
    private void initWakeUpService() {
        // 创建Intent对象，action为LOCATION
        mAlarmIntent = new Intent();
        mAlarmIntent.setAction("LOCATION");
        IntentFilter ift = new IntentFilter();
        // 定义一个PendingIntent对象，PendingIntent.getBroadcast包含了sendBroadcast的动作。
        // 也就是发送了action 为"LOCATION"的intent
        mAlarmPendingIntent = PendingIntent.getBroadcast(mContext, 0, mAlarmIntent, 0);
        // AlarmManager对象,注意这里并不是new一个对象，AlarmManager为系统级服务
        mAlarmManager = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);

        // 动态注册一个广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("LOCATION");
        mContext.registerReceiver(alarmReceiver, filter);
    }

    /**
     * 高德定位初始化
     */
    private void initLocation() {
        // 初始化定位
        mLocationClient = new AMapLocationClient(mContext);
        // 设置定位回调监听
        mLocationClient.setLocationListener(this);
        // 初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        // 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);

        // 获取一次定位结果：
        // 该方法默认为false。
//        mLocationOption.setOnceLocation(true);

        // 获取最近3s内精度最高的一次定位结果：
        // 设置setOnceLocationLatest(boolean b)接口为true，
        // 启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，
        // setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);

        // 设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(2000);

        // 设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        // 设置是否允许模拟位置,默认为true，允许模拟位置
        mLocationOption.setMockEnable(true);
        // 单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        // 关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);

    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                // 定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息

                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                amapLocation.getCountry();//国家信息
                amapLocation.getProvince();//省份信息
                amapLocation.getCity();//城市信息
                amapLocation.getDistrict();//城区信息
                amapLocation.getStreet();//街道信息
                amapLocation.getStreetNum();//街道门牌号信息
                amapLocation.getCityCode();//城市编码
                amapLocation.getAdCode();//地区编码
                amapLocation.getAoiName();//获取当前定位点的AOI信息
                amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
                amapLocation.getFloor();//获取当前室内定位的楼层
                amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态

                @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间

                // 定位信息
                Log.d("AmapSuccess",
                        "\n"
                                + "▨▨▨▨▨▨▨▨▨▨▨▨▨▨▨<==location==>▧▧▧▧▧▨▨▨▨▨▨▨▨▨▨"
                                + "\n"
                                + "纬度：" + amapLocation.getLatitude() + "\n"
                                + "经度：" + amapLocation.getLongitude() + "\n"
                                + "精度：" + amapLocation.getAccuracy() + "\n"
                                + "地址：" + amapLocation.getAddress() + "\n"
                                + "国家：" + amapLocation.getCountry() + "\n"
                                + "省份：" + amapLocation.getProvince() + "\n"
                                + "城市：" + amapLocation.getCity() + "\n"
                                + "城区：" + amapLocation.getDistrict() + "\n"
                                + "街道：" + amapLocation.getStreet() + "\n"
                                + "门牌号：" + amapLocation.getStreetNum() + "\n"
                                + "城市编码：" + amapLocation.getCityCode() + "\n"
                                + "地区编码：" + amapLocation.getAdCode() + "\n"
                                + "AOI信息：" + amapLocation.getAoiName() + "\n"
                                + "建筑物Id：" + amapLocation.getBuildingId() + "\n"
                                + "室内楼层：" + amapLocation.getFloor() + "\n"
                                + "GPS状态：" + amapLocation.getGpsAccuracyStatus() + "\n"
                );

            } else {
                //显示错误信息：ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError",
                        "location Error, "
                                + "ErrCode:"
                                + amapLocation.getErrorCode()
                                + ", "
                                + "errInfo:"
                                + amapLocation.getErrorInfo());
            }
        }
    }

    /**
     * 闹钟广播
     */
    private BroadcastReceiver alarmReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("LOCATION".equals(intent.getAction())) {
                if (null != mLocationClient) {
                    mLocationClient.startLocation();
                }
            }
        }
    };

    /**
     * 开始定位
     */
    @SuppressLint("ShortAlarm")
    public void startLocation() {
        // 给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        // 启动定位
        mLocationClient.startLocation();
        // 设置一个闹钟，2秒之后每隔一段时间执行启动一次定位程序
        if (null != mAlarmManager) {
            int alarmInterval = 5;
            mAlarmManager.setRepeating(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + 2 * 1000,
                    alarmInterval * 1000, mAlarmPendingIntent
            );
        }
    }

    /**
     * 停止定位
     */
    public void stopLocation() {
        if (null != mLocationClient) {
            // 停止定位后，本地定位服务并不会被销毁
            mLocationClient.stopLocation();
            // 销毁定位客户端，同时销毁本地定位服务。
            mLocationClient.onDestroy();
        }
        //停止定位的时候取消闹钟
        if (null != mAlarmManager) {
            mAlarmManager.cancel(mAlarmPendingIntent);
        }
        //停止定位的时候取消广播
        if (null != alarmReceiver) {
            mContext.unregisterReceiver(alarmReceiver);
        }
    }

}
