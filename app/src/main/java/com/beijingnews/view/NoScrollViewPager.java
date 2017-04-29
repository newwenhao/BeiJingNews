package com.beijingnews.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/4/21.
 * 自定义不可滑动的ViewPager
 */

public class NoScrollViewPager extends ViewPager {
    /**
     * 通常在代码中实例化的时候使用
     * @param context
     */
    public NoScrollViewPager(Context context) {
        super(context);
    }

    /**
     * 在布局文件中使用该类的时候 实例化该类用该类构造方法 这个方法不能少 少了就会崩溃 系统规定
     * @param context
     * @param attrs
     */
    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        /**
         * true是不可滑动
         */
        return true;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev){
        /**
         * 下层viewPager的触摸不传到上层的viewPAGER 事件不处理交给孩子
         */
        return false;
    }

}
