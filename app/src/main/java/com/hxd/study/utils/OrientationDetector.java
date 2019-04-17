package com.hxd.study.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.view.Display;
import android.view.OrientationEventListener;
import android.view.WindowManager;

/**
 * 作 者： Cazaea
 * 日 期： 2019/4/17
 * 邮 箱： wistorm@sina.com
 */
public class OrientationDetector extends OrientationEventListener {

    private Activity mActivity;
    private int mLastOrientation = 0;
    private WindowManager mWindowManager;

    /**
     * Creates a new OrientationEventListener.
     *
     * @param context for the OrientationEventListener.
     */
    public OrientationDetector(Context context) {
        super(context);
        initWindowManager(context);
        this.mActivity = (Activity) context;
    }

    private void initWindowManager(Context context) {
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    public boolean isWideScreen() {
        Display display = mWindowManager.getDefaultDisplay();
        Point pt = new Point();
        display.getSize(pt);
        return pt.x > pt.y;
    }

    /**
     * Called when the orientation of the device has changed.
     * orientation parameter is in degrees, ranging from 0 to 359.
     * orientation is 0 degrees when the device is oriented in its natural position,
     * 90 degrees when its left side is at the top, 180 degrees when it is upside down,
     * and 270 degrees when its right side is to the top.
     * {@link #ORIENTATION_UNKNOWN} is returned when the device is close to flat
     * and the orientation cannot be determined.
     *
     * @param orientation The new orientation of the device.
     * @see #ORIENTATION_UNKNOWN
     */
    @Override
    public void onOrientationChanged(int orientation) {
        int value = getCurrentOrientationEx(orientation);
        if (value != mLastOrientation) {
            mLastOrientation = value;
            int current = mActivity.getRequestedOrientation();
            if (current == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT || current == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            }
        }
    }

    private int getCurrentOrientationEx(int orientation) {
        int value = -1;
        if (orientation >= 315 || orientation < 45) {
            // 0度
            value = 0;
        }
        if (orientation >= 45 && orientation < 135) {
            // 90度
            value = 90;
        }
        if (orientation >= 135 && orientation < 225) {
            // 180度
            value = 180;
        }
        if (orientation >= 225 && orientation < 315) {
            // 270度
            value = 270;
        }
        return value;
    }

}
