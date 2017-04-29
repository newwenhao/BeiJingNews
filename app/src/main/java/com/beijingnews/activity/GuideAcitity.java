package com.beijingnews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.beijingnews.R;
import com.beijingnews.SplashActivity;
import com.beijingnews.utils.CacheUtils;
import com.beijingnews.utils.DensityUtil;

import java.util.ArrayList;


public class GuideAcitity extends AppCompatActivity {
    private ViewPager viewpage;
    private Button btn_start_main;
    private LinearLayout ll_point_group;
    ArrayList<ImageView> imageViews;
    private ImageView iv_red_point;
    private int leftmax;
    private int widthdip;
    private int heighdip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_acitity);
        initView();
        initData();
    }

    private void initData() {
        //准备数据
        int[] ads = new int[]{
                R.drawable.guide_1,
                R.drawable.guide_2,
                R.drawable.guide_3
        };
        widthdip = DensityUtil.dip2px(this, 10);
        heighdip = DensityUtil.dip2px(this, 10);

        imageViews = new ArrayList<>();
        for(int i = 0; i<ads.length; ++i){
            ImageView imageview = new ImageView(this);
            //设置背景
            imageview.setBackgroundResource(ads[i]);
            //添加到集合中
            imageViews.add(imageview);
            //创建点 添加到线性布局里面去
            ImageView point= new ImageView(this);
            point.setBackgroundResource(R.drawable.point_normal);
            /**
             * 像素 把单位当做dp 转成像素
             */
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthdip, heighdip);
            if(i != 0){
                /**
                 * 不设置第一个点
                 */
                params.leftMargin = 10;
            }
            point.setLayoutParams(params);
            ll_point_group.addView(point);
//            point.setBackgroundResource(R.drawable.point_red);
            //根据View的生命周期，当视图执行到onDraw或者onLayout的时候， 视图的高和宽，边距都有了，视图树
            iv_red_point.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener());
            //得到屏幕滑动的百分比
            viewpage.addOnPageChangeListener(new MyOnPageChangeListener());
        }
        //设置ViewPager的适配器
        viewpage.setAdapter(new MyVeiwPager());
        btn_start_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.保存记录参数 进去过
                CacheUtils.putBoolean(GuideAcitity.this, SplashActivity.START_MAIN, true);
                //2.跳转到主页面
                Intent intent = new Intent();
                intent.setClass(GuideAcitity.this, MainActivity.class);
                startActivity(intent);
                //3.关闭引导页面
                finish();
            }
        });
    }
    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{
        /**
         * 当页面滑动
         * @param position 当前页面滑动的页面
         * @param positionOffset 页面滑动的百分比
         * @param positionOffsetPixels 滑动的像素
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //两点间移动的距离 = 屏幕滑动的百分比 * 间距
            int leftMargin =(int) (position * leftmax + positionOffset * leftmax);
            Log.e("Tag" , "position == " + position + " positionOffset == " + positionOffset + "positionOffsetPixels == " + positionOffsetPixels);
            //两点间滑动距离对应的坐标 = 原来坐标 + 两点间移动的距离
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)iv_red_point.getLayoutParams();
            params.leftMargin = leftMargin;
            iv_red_point.setLayoutParams(params);
            //params.leftMargin = 两点间滑动距离对应的坐标

        }

        /**
         * 页面被选中是回调
         * @param position 选中对应的位置
         */
        @Override
        public void onPageSelected(int position) {
            if(position == imageViews.size()-1){
                /**
                 * 最后一个页面
                 */
                btn_start_main.setVisibility(View.VISIBLE);
            }else{
                /**
                 * 其他页面
                 */
                btn_start_main.setVisibility(View.GONE);
            }
        }
        /**
         * 页面状态发生变化是
         * @param state 拖， 静止， 放
         */
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener{
        @Override
        public void onGlobalLayout() {
            //执行不止一次
            iv_red_point.getViewTreeObserver().removeOnGlobalLayoutListener(this);//过时
            //两点的间距
            leftmax = ll_point_group.getChildAt(1).getLeft() - ll_point_group.getChildAt(0).getLeft();
        }
    }
    private void initView() {
        viewpage = (ViewPager) findViewById(R.id.viewpage);
        btn_start_main = (Button) findViewById(R.id.btn_start_main);
        ll_point_group = (LinearLayout) findViewById(R.id.ll_point_group);
        iv_red_point = (ImageView) findViewById(R.id.iv_red_point);
    }
    class MyVeiwPager extends PagerAdapter{

        public MyVeiwPager() {
            super();
        }
        /**
         * 放回页面总个数
         * @return
         */
        @Override
        public int getCount() {
            return imageViews.size();
        }
        /**
         *
         * @param container ViewPager
         * @param position 要创建页面得位置
         * @return 放回和创建当前页面右关系的值
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViews.get(position);
            container.addView(imageView);
//            return position;
            return imageView;
        }
        /**
         *
         * @param container ViewPager
         * @param position 位置
         * @param object 页面
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ImageView) object);
        }
        /**
         *
         * @param view 创建当前视图
         * @param object 上面instantiateItem放回的结果值
         * @return
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
//            return view == imageViews.get(Integer.parseInt((String) object);
            return view == object;
        }
    }

}
