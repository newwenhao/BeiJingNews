package com.beijingnews;

import android.app.Application;

import org.xutils.x;


/**
 * Created by Administrator on 2017/4/20.
 * 代表整个软件
 */

public class BeiJingNewsApplication extends Application{
    /**
     * 所有主键创建之前执行
     * 在AndroidMainjingfest.xml中配置
     */
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
    }
}
