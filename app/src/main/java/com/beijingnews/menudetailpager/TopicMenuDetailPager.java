package com.beijingnews.menudetailpager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.beijingnews.menudetailpager.base.MenuDetailBasePager;
import com.beijingnews.utils.LogUtil;

/**
 * Created by Administrator on 2017/4/22.
 */

public class TopicMenuDetailPager extends MenuDetailBasePager {
    private TextView textView;
    public TopicMenuDetailPager(Context context) {
        super(context);
    }
    @Override
    public View initView() {
        //2联网请求得到数据 创建视图
        textView = new TextView(context);
        textView.setTextColor(Color.RED);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();

        LogUtil.e("专题详情页面被初始化了");
        textView.setText("专题详情页面");
    }


}
