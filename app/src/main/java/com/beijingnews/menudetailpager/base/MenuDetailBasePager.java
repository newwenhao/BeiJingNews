package com.beijingnews.menudetailpager.base;

import android.content.Context;
import android.view.View;

/**
 * Created by Administrator on 2017/4/22.
 * 详情页面的基类
 */

public abstract class MenuDetailBasePager {
    /**
     * 代表各个详情页面的视图，
     */
    public View rootView;
    /**
     * 上下文
     */
    public final Context context;
    public MenuDetailBasePager(Context context){
        this.context = context;
        rootView = initView();
    }

    /**
     * 抽象方法 强制孩子实现该方法，每个页面实现不同的效果
     * @return
     */
    public abstract View initView();

    /**
     * 子页面绑定数据 联网请求数据
     */
    public void initData(){

    }

}
