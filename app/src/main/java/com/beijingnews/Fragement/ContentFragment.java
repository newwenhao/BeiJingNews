package com.beijingnews.Fragement;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.beijingnews.Fragement.base.BaseFragment;
import com.beijingnews.R;
import com.beijingnews.activity.MainActivity;
import com.beijingnews.pager.GovaffairPager;
import com.beijingnews.pager.HomePager;
import com.beijingnews.pager.NewsCenterPager;
import com.beijingnews.pager.SettingPager;
import com.beijingnews.pager.SmartServicePager;
import com.beijingnews.pager.base.BasePager;
import com.beijingnews.utils.LogUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/20.
 */

public class ContentFragment extends BaseFragment {
    @ViewInject(R.id.viewpager)
    private ViewPager viewpager;
    @ViewInject(R.id.rg_main)
    private RadioGroup rg_main;
    private View view;

    private ArrayList<BasePager> basePagers;

    @Override
    public View initView() {
        LogUtil.e("正文菜单视图初始化");
        view = View.inflate(context, R.layout.content_fragment, null);
        initFragmentView();
        return this.view;
    }

    private void initFragmentView() {
        //把视图注入到框架中，与view关联,实例化控件
        x.view().inject(ContentFragment.this, view);
    }

    @Override
    protected void initData() {
        super.initData();
        LogUtil.e("正文被初始化了");

        //初始化5个页面 并放入集合中
        basePagers = new ArrayList<>();
        basePagers.add(new HomePager(context));//主页面
        basePagers.add(new NewsCenterPager(context));//新闻
        basePagers.add(new SmartServicePager(context));//智慧
        basePagers.add(new GovaffairPager(context));//政要
        basePagers.add(new SettingPager(context));//设置

        //设置ViewPager适配器 要准备数据
        viewpager.setAdapter(new ContentFragementAdapter());

        //设置radioGroup的选中状态改变
        rg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        //监听某个页面被选中，初始化对应的页面数据
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());

        //设置默认选中首页
        rg_main.check(R.id.rb_home);
        basePagers.get(0).initData();//一定要加上
        isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);//不可以滑动
    }

    public NewsCenterPager getNewsCenterPager() {
        //得到新闻中心
        return (NewsCenterPager) basePagers.get(1);
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{
        /**
         *
         * @param position
         * @param positionOffset 百分比
         * @param positionOffsetPixels 像素
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * 当某个页面被选中的时候回调这个方法
         * @param position 选中页面得位置
         */
        @Override
        public void onPageSelected(int position) {
            //调用被选中的页面得initData()方法  其他页面不被初始化,不进行网络请求
            basePagers.get(position).initData();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{
        /**
         *
         * @param group radioGroup
         * @param checkedId 被选中RadioButton的ID
         */
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch(checkedId){
                case R.id.rb_home://主页面 false 表示下面没有动画 TRUE表示有动画
                    viewpager.setCurrentItem(0,false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_newscenter://新闻中心
                    viewpager.setCurrentItem(1,false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    break;
                case R.id.rb_smartservice://智慧服务RadioButton的ID;
                    viewpager.setCurrentItem(2,false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_govaffair://政要指南
                    viewpager.setCurrentItem(3,false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_setting://设置
                    viewpager.setCurrentItem(4,false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
            }
        }
    }

    /**
     * 根据传入的参数是SlidingMenu是否滑动
     * @param touchmodeFullscreen
     */
    public void isEnableSlidingMenu(int touchmodeFullscreen){
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.getSlidingMenu().setTouchModeAbove(touchmodeFullscreen);

    }
    class ContentFragementAdapter extends PagerAdapter{
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager basePager =  basePagers.get(position); //各个页面得实例
            View rootView = basePager.rootView;//代表各个子页面
//            //调用各个页面得initData()方法 初始化数据
//            basePager.initData();
            container.addView(rootView);
            return rootView;
        }

        @Override
        public int getCount() {
            return basePagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object == view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
