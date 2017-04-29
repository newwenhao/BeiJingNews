package com.beijingnews.view;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beijingnews.R;

import java.util.Date;

/**
 * Created by Administrator on 2017/4/22.
 */

public class RefreshListView extends ListView {
    /**
     * 里面包含下拉刷新和底部轮播图
     */
    private LinearLayout headView;
    /**
     * 下拉刷新控件
     */
    private LinearLayout ll_pull_down_refresh;
    private ImageView iv_arrow;
    private ProgressBar pb_status;
    private TextView tv_status;
    private TextView tv_time;
    /**
     * 下拉刷新
     */
    private static final int PULL_DOWN_REFRESH = 0;
    /**
     * 手松刷新
     */
    public static final int RELEASE_REFRESH = 1;
    /**
     * 正在刷新
     */
    public static final int REFRESHING = 2;
    /**
     * 当前状态
     */
    private int currentStatus = PULL_DOWN_REFRESH;
    /**
     * 下拉刷新的高
     */
    private int pullDownRefreshHeight;

    private Animation upAnimation;
    private Animation downAnimation;
    /**
     * 加载更多
     */
    private View footerView;
    private int footerViewHeight;//上拉加载更多的高
    private boolean isLoadMore = false;//是否已经加载更多

    public RefreshListView(Context context) {
        this(context, null);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView(context);
        initAnimation();//初始化动画
        initFooterView(context);//加载更多
    }

    private void initFooterView(Context context) {
        footerView = View.inflate(context, R.layout.refresh_footer, null);
        footerView.measure(0, 0);
        /**
         * 得到高度
         */
        footerViewHeight = footerView.getMeasuredHeight();
        /**
         * 隐藏加载跟多默认是不显示
         */
        footerView.setPadding(0, -footerViewHeight, 0, 0);
        /**
         * 添加到listView中去
         */
        this.addFooterView(footerView);
        //监听listView滑到最后一条怎么做
        setOnScrollListener(new MyOnScrollListener());
    }
    class MyOnScrollListener implements OnScrollListener{

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            /**
             * 状态变化， 当禁止或者惯性滚动是 并且是最后一条可见的 回调接口和隐藏
             */
            if(scrollState == OnScrollListener.SCROLL_STATE_IDLE || scrollState == OnScrollListener.SCROLL_STATE_FLING){
                //并且是最后一条
                if(getLastVisiblePosition() >= getCount() - 1){
                    //1.显示加载更多布局
                    footerView.setPadding(8, 8, 8, 8);//应为在布局中padding为8888
                    //2.状态改变
                    isLoadMore = true; //转到上拉加载更多判断
                    //3.回调接口
                    if(mOnRefreshListener != null){
                        mOnRefreshListener.onLoadMore();
                    }
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            /**
             * 正在滚动
             */
        }
    }


    private void initAnimation() {
        upAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        upAnimation.setDuration(500);//事件
        upAnimation.setFillAfter(true);//自动圈

        downAnimation = new RotateAnimation(-180, -360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        downAnimation.setDuration(500);//事件
        downAnimation.setFillAfter(true);//自动圈
    }

    private void initHeaderView(Context context) {
        headView = (LinearLayout) View.inflate(context, R.layout.refresh_header, null);
        ll_pull_down_refresh = (LinearLayout) headView.findViewById(R.id.ll_pull_down_refresh);
        iv_arrow = (ImageView) headView.findViewById(R.id.iv_arrow);
        pb_status = (ProgressBar) headView.findViewById(R.id.pb_status);
        tv_status = (TextView) headView.findViewById(R.id.tv_status);
        tv_time = (TextView) headView.findViewById(R.id.tv_time);
        /**
         * 测量高宽 0， 0 只是代表参数没有意义
         */
        ll_pull_down_refresh.measure(0, 0);
        pullDownRefreshHeight = ll_pull_down_refresh.getMeasuredHeight();
        /**
         * 默认隐藏下拉控件 (原理)
         * View.setPadding(0, -控件高, 0, 0)//完全影藏
         * View.setPadding(0, 0, 0, 0)//完全显示
         */
        ll_pull_down_refresh.setPadding(0, -pullDownRefreshHeight, 0, 0);

        //添加头
        RefreshListView.this.addHeaderView(headView);
    }
    /**
     * 重写onTouchEvent
     */
    private float startY = -1;
    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                /**
                 * 记录起始坐标
                 */
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(startY != -1){
                    //没有初始化
                    startY = event.getY();
                }
                /**
                 * 如果正在刷新就只让他刷新一次
                 */
                if(currentStatus == REFRESHING){
                    break;
                }
                /**
                 * 来到新的坐标
                 */
                float endY = event.getY();
                /**
                 * 计算滑动距离
                 */
                float distanceY = endY - startY;
                if(distanceY > 0) {
                    /**
                     * 下拉
                     * View.setPadding(0, -控件高, 0, 0)//完全影藏
                     * View.setPadding(0, 0, 0, 0)//完全显示
                     */
                    int paddingTop = (int) (-pullDownRefreshHeight + distanceY);
                    if(paddingTop < 0 && currentStatus != PULL_DOWN_REFRESH){
                        //下拉刷新状态
                        currentStatus = PULL_DOWN_REFRESH;
                        //更新状态
                        refreshViewState();
                    }else if(paddingTop > 0 && currentStatus != RELEASE_REFRESH){
                        //下拉刷新状态
                        currentStatus = RELEASE_REFRESH;
                        //更新状态
                        refreshViewState();
                    }
                    ll_pull_down_refresh.setPadding(0, paddingTop, 0, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                startY = -1;
                if(currentStatus == PULL_DOWN_REFRESH){
                    //完全隐藏
                    ll_pull_down_refresh.setPadding(0, -pullDownRefreshHeight, 0, 0);
                }else if(currentStatus == RELEASE_REFRESH){
                    //设置状态正在刷新
                    currentStatus = REFRESHING;
                    //回调接口
                    /**
                     * mOnRefreshListener != null 判断是够有动作让他下拉刷新
                     */
                    if(mOnRefreshListener != null){
                        mOnRefreshListener.onPullDownRefresh();
                    }

                    //完全显示
                    refreshViewState();
                    ll_pull_down_refresh.setPadding(0, 0, 0, 0);

                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private void refreshViewState() {
        switch (currentStatus){
            case PULL_DOWN_REFRESH://下拉刷新状态
                iv_arrow.startAnimation(downAnimation);
                tv_status.setText("下拉刷新...");
                break;
            case RELEASE_REFRESH://手松刷新状态
                iv_arrow.startAnimation(upAnimation);
                tv_status.setText("手松刷新...");
                break;
            case REFRESHING://正在刷新状态
                tv_status.setText("正在刷新...");
                iv_arrow.clearAnimation();
                iv_arrow.setVisibility(GONE);//影藏
                pb_status.setVisibility(VISIBLE);
                break;
            default:
                break;
        }
    }

    /**
     * 定义下拉刷新实现接口
     */
    public interface OnRefreshListener{
        /**
         * 下拉刷新是实现这个方法
         */
        public void onPullDownRefresh();
        /**
         * 当加载更多是回调这个接口
         */
        public void onLoadMore();
    }
    private OnRefreshListener mOnRefreshListener;
    /**
     * 设置监听事件 由外界设置
     */
    public void setmOnRefreshListener(OnRefreshListener l){
        this.mOnRefreshListener = l;
    }
    /**
     * 隐藏或显示下拉属性 当联网和失败的时候回调该方法
     * 用户刷新状态的还原
     */
    public void OnRefreshFinish(boolean sucess){
        if(isLoadMore){
            //加载更多
            isLoadMore = false;
            //隐藏加载更多的布局
            footerView.setPadding(0, -footerViewHeight, 0, 0);
        }else{
            //下拉刷新
            tv_status.setText("下拉刷新...");
            currentStatus = PULL_DOWN_REFRESH;
            iv_arrow.clearAnimation();
            iv_arrow.setVisibility(VISIBLE);//影藏
            pb_status.setVisibility(GONE);
            //影藏下拉刷新控件
            ll_pull_down_refresh.setPadding(0,-pullDownRefreshHeight, 0, 0);
            if(sucess){
                /**
                 * 成功
                 */
                tv_time.setText("上次更新时间：" + getSystemTime());
            }else{
                /**
                 * 失败
                 */
            }
        }

    }
    private String getSystemTime() {
        /**
         * 得到当前Android系统的时间
         */
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//格式化日期
        return format.format(new Date());
    }
}
