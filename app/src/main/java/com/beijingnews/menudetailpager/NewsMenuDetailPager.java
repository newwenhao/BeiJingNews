package com.beijingnews.menudetailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.beijingnews.R;
import com.beijingnews.activity.MainActivity;
import com.beijingnews.domain.NewsCenterPagerBean;
import com.beijingnews.menudetailpager.base.MenuDetailBasePager;
import com.beijingnews.menudetailpager.tableDetialPager.TableDetailPager;
import com.beijingnews.utils.LogUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/22.
 */

public class NewsMenuDetailPager extends MenuDetailBasePager{
    /**
     * 初始化控件
     */
    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;
    @ViewInject(R.id.tablePageIndicator)
    private TabPageIndicator tablePageIndicator;
    @ViewInject(R.id.ib_tab_next)
    private ImageButton ib_tab_next;
    /**
     * 装详情页面的集合 数据
     */
    private ArrayList<TableDetailPager> tableDetailPagers;

    /**
     * 叶签页面的数据集合 页面
     */
    private List<NewsCenterPagerBean.DataBean.ChildrenBean> dataBeanChildren;

    public NewsMenuDetailPager(Context context, NewsCenterPagerBean.DataBean dataBean) {
        super(context);
        dataBeanChildren = dataBean.getChildren();
    }

    @Override
    public View initView() {
        //2联网请求得到数据 创建视图
        View view = View.inflate(context, R.layout.newsmenu_detile_pager, null);
        x.view().inject(NewsMenuDetailPager.this, view);
        //设置点击事件
        ib_tab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);//不用担心越界， 内部处理了
            }
        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();

        LogUtil.e("新闻详情页面被初始化了");
        //准备新闻详情页面的数据
        tableDetailPagers = new ArrayList<>();
        for(int i = 0; i<dataBeanChildren.size(); ++i){
            tableDetailPagers.add(new TableDetailPager(context, dataBeanChildren.get(i)));
        }
        //设置ViewPager的适配器
        viewPager.setAdapter(new MyNewsMenuPagerAdapter());
        //ViewPager和TabPagerIndicator关联
        tablePageIndicator.setViewPager(viewPager);  //设置样式头上的样式
        //注意以后监听事件页面的变化， TabPagerIndicator 监听页面的变化
        tablePageIndicator.setOnPageChangeListener(new MyOnPageChangeListener());
    }
    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //如果是第0个页面
            if(position == 0){
                //slidingMenu可以全屏滑动
                isEnableSlidingMenu(SlidingMenu.TOUCHMODE_FULLSCREEN);//可以全屏滑动
            }else{
                //slidingMenu不可以滑动
                isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);//不可以滑动
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

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
    class MyNewsMenuPagerAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return tableDetailPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //得到表头
            return dataBeanChildren.get(position).getTitle();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TableDetailPager tableDetailPager = tableDetailPagers.get(position);
            View view = tableDetailPager.rootView;
            tableDetailPager.initData();//初始化数据
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);

        }
    }

}
