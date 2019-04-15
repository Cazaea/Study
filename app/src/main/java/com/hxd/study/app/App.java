package com.hxd.study.app;

import android.util.Log;

import com.hxd.study.utils.LocationUtil;
import com.tencent.smtt.sdk.QbSdk;

import androidx.multidex.MultiDexApplication;
import cn.jpush.android.api.JPushInterface;

/*
 * 作 者： Cazaea
 * 日 期： 2019/4/2
 * 邮 箱： wistorm@sina.com
 * 描 述： Inherit MultiDexApplication to implement subcontract processing
 *
 *                        ___====-_  _-====___
 *                  _--^^^#####//      \\#####^^^--_
 *               _-^##########// (    ) \\##########^-_
 *              -############//  |\^^/|  \\############-
 *            _/############//   (@::@)   \\############\_
 *           /#############((     \\//     ))#############\
 *          -###############\\    (oo)    //###############-
 *         -#################\\  / VV \  //#################-
 *        -###################\\/      \//###################-
 *       _#/|##########/\######(   /\   )######/\##########|\#_
 *       |/ |#/\#/\#/\/  \#/\##\  |  |  /##/\#/  \/\#/\#/\#| \|
 *       `  |/  V  V  `   V  \#\| |  | |/#/  V   '  V  V  \|  '
 *          `   `  `      `   / | |  | | \   '      '  '   '
 *                           (  | |  | |  )
 *                          __\ | |  | | /__
 *                         (vvv(VVV)(VVV)vvv)
 *
 *                           HERE BE DRAGONS
 *
 */
public class App extends MultiDexApplication {

    private static final String TAG = "Application";

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize JPush
        initJPush();
        // Initialize TBS Browser Kernel
        initTBSX5();
        // Initialize AMap Map Location
        initLocation();
    }

    /**
     * Init JPush
     */
    private void initJPush() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    /**
     * 初始化TBS浏览服务X5内核
     */
    private void initTBSX5() {
        // 非wifi条件下允许下载X5内核
        QbSdk.setDownloadWithoutWifi(true);
        // 搜集本地tbs内核信息并上报服务器，服务器返回结果决定是用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean status) {
                // x5内核初始化完成的回调，为true表示x5内核加载成功
                // 否则表示x5内核加载失败会自动切换到系统内核。
                Log.i(TAG, "TBS浏览服务X5内核是否加载成功-->" + status);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        // X5内核初始化接口
        QbSdk.initX5Environment(this, cb);
    }

    /**
     * 初始化高德地图定位
     */
    private void initLocation(){
        LocationUtil.getInstance().init(this);
    }

}
