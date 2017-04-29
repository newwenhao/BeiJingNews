package com.beijingnews.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Window;

import com.beijingnews.Fragement.ContentFragment;
import com.beijingnews.Fragement.LeftMenuFragment;
import com.beijingnews.R;
import com.beijingnews.utils.DensityUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {

    public static final String MAIN_CONTENT_TAG = "main_content_tag";
    public static final String LEFT_MENU_TAG = "left_menu_tag";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //标题影藏
        requestWindowFeature(Window.FEATURE_NO_TITLE);//没有标题
        //1.设置主页面
        setContentView(R.layout.activity_main);
        //2.设置左侧菜单
        setBehindContentView(R.layout.activity_leftmenu);
        //3.设置右侧滑动
        SlidingMenu sliding = getSlidingMenu();
        sliding.setSecondaryMenu(R.layout.activity_rightmenu);
        //4.设置模式  左侧菜单+主页 ，左侧+主页+右侧，等等
        sliding.setMode(SlidingMenu.LEFT);
        //5.设置滑动的模式 滑动边缘 全屏滑动  不可以滑动
        sliding.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //6.设置主页占据的宽度
        sliding.setBehindOffset(DensityUtil.dip2px(MainActivity.this, 200));

        //把Fragment初始化
        initFragment();
    }

    private void initFragment() {
        //1.得到fragment的Manager
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        //2.开启事务
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        //3.替换 把之前隐藏 第一次注意 现在的替换 add show()的区别
        ft.replace(R.id.activity_main, new ContentFragment(), MAIN_CONTENT_TAG);//主页
        ft.replace(R.id.fl_leftmenu, new LeftMenuFragment(), LEFT_MENU_TAG);//左侧菜单
        //4.提交
        ft.commit();
    }

    /**
     * 得到左侧菜单Frgment
     * @return
     */
    public LeftMenuFragment getLeftMenuFragment() {
        FragmentManager fm = getSupportFragmentManager();
        return (LeftMenuFragment) fm.findFragmentByTag(LEFT_MENU_TAG);
    }

    public ContentFragment getContentFragment() {
        //得到正文的fragment
        FragmentManager fm = getSupportFragmentManager();
        return (ContentFragment) fm.findFragmentByTag(MAIN_CONTENT_TAG);
    }
}
