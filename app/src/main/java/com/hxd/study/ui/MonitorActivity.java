package com.hxd.study.ui;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.ezvizuikit.open.EZUIError;
import com.ezvizuikit.open.EZUIKit;
import com.ezvizuikit.open.EZUIPlayer;
import com.hxd.study.R;
import com.hxd.study.app.App;
import com.hxd.study.app.Constants;
import com.hxd.study.utils.OrientationDetector;
import com.hxd.study.utils.WindowSizeChangeNotifier;

import java.util.Calendar;

public class MonitorActivity extends Activity implements WindowSizeChangeNotifier.OnWindowSizeChangedListener, EZUIPlayer.EZUIPlayerCallBack {

    private EZUIPlayer mEZUIPlayer;
    private final String TAG = "Monitor";
    private OrientationDetector mOrientationDetector;

    /**
     * onResume()时是否恢复播放
     */
    private boolean isResumePlay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);

        mOrientationDetector = new OrientationDetector(this);
        new WindowSizeChangeNotifier(this, this);

        //获取EZUIPlayer实例
        mEZUIPlayer = findViewById(R.id.player_ui);
        //设置加载需要显示的View
        mEZUIPlayer.setLoadingView(initProgressBar());
        // 准备播放资源文件
        preparePlay();
        setSurfaceSize();
    }

    /**
     * 准备播放资源参数
     */
    private void preparePlay() {
        //设置debug模式，输出log信息
        EZUIKit.setDebug(true);
        //初始化EZUIKit
        EZUIKit.initWithAppKey(App.getInstance(), Constants.EZUI_APP_KEY);
        //设置授权AccessToken
        EZUIKit.setAccessToken(Constants.EZUI_ACCESS_TOKEN);
        //设置播放资源参数
        mEZUIPlayer.setCallBack(this);
        mEZUIPlayer.setUrl(Constants.EZUI_EZOPEN_URL_HD);
    }

    /**
     * 创建加载中View
     */
    private View initProgressBar() {

        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setBackgroundColor(Color.parseColor("#000000"));
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        relativeLayout.setLayoutParams(lp);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        rlp.addRule(RelativeLayout.CENTER_IN_PARENT);//addRule参数对应RelativeLayout XML布局的属性
        ProgressBar mProgressBar = new ProgressBar(this);
        mProgressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress));
        relativeLayout.addView(mProgressBar, rlp);
        return relativeLayout;


//        //创建loadingView
//        ProgressBar mLoadView = new ProgressBar(this);
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
//        mLoadView.setLayoutParams(lp);
//        //设置loadingView
//        mPlayer.setLoadingView(mLoadView);
    }

    /**
     * 屏幕旋转时调用此方法
     */
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged");
        setSurfaceSize();
    }

    private void setSurfaceSize() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        boolean isWideScreen = mOrientationDetector.isWideScreen();
        //竖屏
        if (!isWideScreen) {
            //竖屏调整播放区域大小，宽全屏，高根据视频分辨率自适应
            mEZUIPlayer.setSurfaceSize(dm.widthPixels, 0);
        } else {
            //横屏屏调整播放区域大小，宽、高均全屏，播放区域根据视频分辨率自适应
            mEZUIPlayer.setSurfaceSize(dm.widthPixels, dm.heightPixels);
        }
    }

    @Override
    public void onWindowSizeChanged(int w, int h, int oldW, int oldH) {
        if (mEZUIPlayer != null) {
            setSurfaceSize();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mOrientationDetector.enable();
        //界面stop时，如果在播放，那isResumePlay标志位置为true，resume时恢复播放
        if (isResumePlay) {
            isResumePlay = false;
            mEZUIPlayer.startPlay();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mOrientationDetector.disable();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop + " + mEZUIPlayer.getStatus());
        //界面stop时，如果在播放，那isResumePlay标志位置为true，以便resume时恢复播放
        if (mEZUIPlayer.getStatus() != EZUIPlayer.STATUS_STOP) {
            isResumePlay = true;
        }
        //停止播放
        mEZUIPlayer.stopPlay();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放资源
        mEZUIPlayer.releasePlayer();
    }

    @Override
    public void onPlaySuccess() {
        Log.d(TAG, "onPlaySuccess: 播放成功！");
    }

    @Override
    public void onPlayFail(EZUIError pEZUIError) {
        Log.e(TAG, "onPlayFail: 播放失败！" + pEZUIError.getErrorString());
    }

    @Override
    public void onVideoSizeChange(int width, int height) {
        Log.d(TAG, "onVideoSizeChange: 播放视频分辨率回调! width =" + width + "   height = " + height);
    }

    @Override
    public void onPrepared() {
        //播放
        mEZUIPlayer.startPlay();
    }

    @Override
    public void onPlayTime(Calendar calendar) {
        if (calendar != null) {
            Log.d(TAG, "onPlayTime: 当前播放时间 = " + calendar.getTime().toString());
        }
    }

    @Override
    public void onPlayFinish() {

    }

}
