package com.beijingnews.Fragement.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/4/20.
 * 基本的Fragment继承他
 */

public abstract class  BaseFragment extends Fragment {
    public Activity context;//获得上下文， 就是MainActivity
    /**
     * 当我们的Fragment创建时
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    /**
     * 当创建view是回调 创建了视图
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView();
    }
    /**
     * 让孩子实现自己的视图，达到自己的效果
     */
    public abstract View initView();

    /**
     * 当Activity创建之后被回调
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }
    /**
     * 1.如果页面没有数据，联网请求数据
     * 2.有数据直接绑定
     */
    protected void initData(){

    }

}
