package com.beijingnews.pager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.beijingnews.R;
import com.beijingnews.adapter.SmartServicePageAdapter;
import com.beijingnews.domain.SmartServicePagerBean;
import com.beijingnews.pager.base.BasePager;
import com.beijingnews.utils.CacheUtils;
import com.beijingnews.utils.Constants;
import com.beijingnews.utils.LogUtil;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/4/21.
 */

public class SmartServicePager extends BasePager {
    private MaterialRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private ProgressBar pb_loading;
    private String url;
    private SmartServicePageAdapter adapter;
    /**
     * 默认状态
     */
    private static final int STATE_NORMAL = 1;
    /**
     * 下拉刷新状态
     */
    private static final int STATE_REFRESH = 2;
    /**
     * 加载更多
     */
    private static final int STATE_LOADMORE = 3;
    /**
     * 默认状态
     */
    private int state = STATE_NORMAL;
    /**
     * 商品列表数据
     */
    private List<SmartServicePagerBean.list> datas;

    /**
     * 每页的个数
     * @param context
     */
    private int pageSize = 10;

    /**
     * 当前页
     */
    private int curPage = 1;

    /**
     * 总页数
     * @param context
     */
    private int totalPage = 1;

    public SmartServicePager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("智慧服务被初始化了");
        //1设置表题
        titleBar.setText("商城热卖");
        //2联网请求得到数据 创建视图
        View view = View.inflate(context, R.layout.smartservice_pager,null);
        refreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.refresh);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        pb_loading = (ProgressBar) view.findViewById(R.id.pb_loading);
        //每次点击都要清除原来的视图
        if(fl_content!=null){
            fl_content.removeAllViews();
        }
        //添加到Fragment中
        fl_content.addView(view);
        initRefresh();
        setRequestParams();
        getDataFromNet();
    }

    /**
     * 设置下拉和上拉监听
     */
    private void initRefresh() {
        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            /**
             * 下拉
             * @param materialRefreshLayout
             */
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                state = STATE_REFRESH;
                /**
                 * 重新请求第一页
                 */
                curPage = 1;
                url = Constants.WARES_HOT_URL + pageSize + "&curPage=" + curPage; //curPage发生了改变所以要重新写
                getDataFromNet();
            }
            /**
             * 加载跟多
             */
            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                state = STATE_LOADMORE;
                /**
                 * 重新请求第一页
                 */
                curPage = curPage + 1;//一定要判断
                if(curPage < totalPage){
                    url = Constants.WARES_HOT_URL + pageSize + "&curPage=" + curPage; //curPage发生了改变所以要重新写
                    getDataFromNet();
                }else{
                    Toast.makeText(context, "没有跟多的数据", Toast.LENGTH_SHORT).show();
                    refreshLayout.finishRefreshLoadMore();//把加载跟多的一个ui还原
                }
            }
        });
    }

    private void getDataFromNet() {
        String json = CacheUtils.getString(context, Constants.WARES_HOT_URL);
        if(!TextUtils.isEmpty(json)){
            processData(json);
        }
        //使用okhttp把第三方封装库请求网络
        OkHttpUtils.get()
                .url(url)
                .id(100)
                .build()
                .execute(new MyStringCallback());
    }

    private void setRequestParams() {
        state = STATE_NORMAL;
        curPage = 1;
        url = Constants.WARES_HOT_URL + pageSize + "&curPage=" + curPage;
    }

    private class MyStringCallback extends Callback {
        @Override
        public String parseNetworkResponse(Response response, int i) throws Exception {
            return response.body().string();
        }

        @Override
        public void onError(Call call, Exception e, int i) {
            e.printStackTrace();
            LogUtil.e("使用OKHTTP联网请求失败==" + e.getMessage()) ;

        }

        @Override
        public void onResponse(Object o, int i) {
            LogUtil.e("OnResponse:complete");
            LogUtil.e("使用okhttp联网请求成功==" + o.toString());
            //缓存数据
            CacheUtils.putString(context, url, (String) o);
            processData((String) o);
            //设置适配器
        }
    }

    /**
     * 解析数据和显示数据绑定到适配器中用Gson解析实在不行用手动解析
     * @param json
     */
    private void processData(String json) {
        SmartServicePagerBean bean = parseJson(json);
        LogUtil.e("===" + bean.getPageSize());
        datas = bean.getList();
        curPage = bean.getCurrentPage();
        totalPage = bean.getTotalPage();
        LogUtil.e("curPage ==" + curPage);
        LogUtil.e("totalPage == " + totalPage);
        LogUtil.e("datas == " + datas.get(1).getName());
        showData();
    }

    private void showData() {
        switch(state){
            case STATE_NORMAL: //默认
                adapter = new SmartServicePageAdapter(context, datas);
                /**
                 * 显示数据 设置适配器
                 */
                recyclerView.setAdapter(adapter);
                //布局管理器
                recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                pb_loading.setVisibility(View.GONE);
                break;
            case STATE_REFRESH: //下拉刷新
                //1.把之前的数据清除
                adapter.clearData();
                //2.添加数据
                adapter.addData(0, datas);
                //3.状态还原
                refreshLayout.finishRefresh();
                break;
            case STATE_LOADMORE: //加载更多
                //把新的数据添加在原来数据的末尾 datas已经更新
                adapter.addData(adapter.getDataCount(), datas);
                //把状态还原
                refreshLayout.finishRefreshLoadMore();
                break;
        }

    }

    /**
     * 解析商城热卖
     */
    private SmartServicePagerBean parseJson(String json) {
        return new Gson().fromJson(json, SmartServicePagerBean.class);
    }
}
