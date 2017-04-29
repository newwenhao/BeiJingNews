package com.beijingnews.pager.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.beijingnews.R;
import com.beijingnews.activity.MainActivity;

/**
 * Created by Administrator on 2017/4/21.
 * 基类
 */

public class BasePager  {
    /**
     * 上下文
     */
    public final Context context;//MainActivity
    /**
     * 显示表题
     */
    public TextView titleBar;
    /**
     * 点击侧滑
     */
    public ImageButton ib_menu;
    /**
     * 帧布局
     */
    public FrameLayout fl_content;

    /**
     * 视图代表各个不同的页面
     */
    public View rootView;
    public BasePager(Context context){
        this.context = context;
        /**
         * 构造函数一执行就初始化了 视图
         */
        this.rootView = initView();
    }

    /**
     * 初始化公共部分视图，并加载子视图的Fragement
     * @return
     */
    private View initView() {
        //基类页面
        View view = View.inflate(context, R.layout.base_pager, null);
        titleBar = (TextView) view.findViewById(R.id.tv_title);
        fl_content = (FrameLayout) view.findViewById(R.id.fl_content);
        ib_menu = (ImageButton) view.findViewById(R.id.ib_menu);
        //点击事件
        ib_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.getSlidingMenu().toggle();//是开就关，是关就开
            }
        });
        return view;
    }

    /**
     * 初始化数据 绑定数据 联网请求
     */
    public void initData(){

    }
}
