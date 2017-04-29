package com.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.beijingnews.pager.base.BasePager;
import com.beijingnews.utils.LogUtil;

/**
 * Created by Administrator on 2017/4/21.
 */

public class HomePager extends BasePager {
    public HomePager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("主页面被初始化了");

        //1设置表题
        titleBar.setText("主页面");
        //2联网请求得到数据 创建视图
        TextView textView = new TextView(context);
        textView.setTextColor(Color.RED);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        //3.把自视图添加到basepager的fragement中
        fl_content.addView(textView);
        //4.绑定数据
        textView.setText("我是主页面");
    }
}
