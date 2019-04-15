package com.hxd.study.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * 作 者： Cazaea
 * 日 期： 2018/4/26
 * 邮 箱： wistorm@sina.com
 * <p>
 * 不滑动的ViewPager
 */
public class NoScrollViewPager extends ViewPager {

    private boolean isScroll;

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /*
     * *********************************************************************************************
     * ***********************************禁止ViewPager滑动切换效果***********************************
     * *********************************************************************************************
     */

    /**
     * 1.dispatchTouchEvent一般情况不做处理
     * ,如果修改了默认的返回值,子孩子都无法收到事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        // 不行
        // return true;

        return super.dispatchTouchEvent(ev);
    }

    /**
     * 是否拦截
     * 拦截:会走到自己的onTouchEvent方法里面来
     * 不拦截:事件传递给子孩子
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        // 可行,不拦截事件,
        // return false;

        // 不行,孩子无法处理事件
        // return true;

        // 不行,会有细微移动
        // return super.onInterceptTouchEvent(ev);

        if (isScroll) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }

    /**
     * 是否消费事件
     * 消费:事件就结束
     * 不消费:往父控件传
     */
    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent ev) {

        // 可行,不消费,传给父控件
        // return false;

        // 可行,消费,拦截事件
        // return true;

        // 不行,
        // 虽然onInterceptTouchEvent中拦截了,
        // 但是如果ViewPager里面子控件不是ViewGroup,还是会调用这个方法.
        // super.onTouchEvent(ev);

        if (isScroll) {
            return super.onTouchEvent(ev);
        } else {
            // 可行,消费,拦截事件
            return true;
        }
    }
    /*
     * *********************************************************************************************
     * ***********************************禁止ViewPager滑动切换效果***********************************
     * *********************************************************************************************
     */

    /*
     * *********************************************************************************************
     * **********************************去除页面切换时的滑动翻页效果**********************************
     * *********************************************************************************************
     */
    @Override
    public void setCurrentItem(int item) {
        // TODO Auto-generated method stub  
        super.setCurrentItem(item, false);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        // TODO Auto-generated method stub  
        super.setCurrentItem(item, smoothScroll);
    }
    /*
     * *********************************************************************************************
     * **********************************去除页面切换时的滑动翻页效果**********************************
     * *********************************************************************************************
     */

    /**
     * 设置是否支持滑动
     *
     * @param scroll
     */
    public void setScroll(boolean scroll) {
        isScroll = scroll;
    }
}