package com.beijingnews.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/4/22.
 * 水平方向滑动的viewPager
 */

public class HorizontalScrollviewPager extends ViewPager {
    /**
     * 起始坐标
     */
    private float startX;
    private float startY;
    public HorizontalScrollviewPager(Context context) {
        super(context);
    }

    public HorizontalScrollviewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        //请求父层视图不拦截，当前控件的事件

        switch(ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);//都把事件穿个当前控件
                startX = ev.getX();
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                /**
                 * 来到新的坐标
                 */
                float endX = ev.getX();
                float endY = ev.getY();
                //3.计算偏移量
                float distanceX = endX - startX;
                float distanceY = endY - startY;
                //判断滑动方向

                if(Math.abs(distanceX) > Math.abs(distanceY)){
                    /**
                     * 水平方向滑动
                     * 1.当滑动到ViewPager的第0个页面，并且是从左向右滑动
                     * getParent().requestDisallowInterceptTouchEvent(false);//都把事件穿个当前控件
                     * 2.当滑动到ViewPager的最后一个页面，并且是从右向左滑动
                     * getParent().requestDisallowInterceptTouchEvent(false);//都把事件穿个当前控件
                     * 3.其他
                     * getParent().requestDisallowInterceptTouchEvent(true);//都把事件穿个当前控件
                     */
                    if(getCurrentItem() == 0 && distanceX > 0){
                        getParent().requestDisallowInterceptTouchEvent(false);//都把事件穿个当前控件
                    }else if((getCurrentItem() == getAdapter().getCount() - 1) && distanceX < 0){
                        getParent().requestDisallowInterceptTouchEvent(false);//都把事件穿个当前控件
                    }else{
                        getParent().requestDisallowInterceptTouchEvent(true);//都把事件穿个当前控件
                    }
                }else{
                    //竖直方向滑动
                    getParent().requestDisallowInterceptTouchEvent(false);//都把事件穿个当前控件
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;

        }
        return super.dispatchTouchEvent(ev);
    }
}
