package com.beijingnews.pager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;

import com.beijingnews.R;
import com.beijingnews.domain.SmartServicePagerBean;
import com.beijingnews.pager.base.BasePager;
import com.beijingnews.utils.CacheUtils;
import com.beijingnews.utils.Constants;
import com.beijingnews.utils.LogUtil;
import com.cjj.MaterialRefreshLayout;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

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

    /**
     * 每页的个数
     * @param context
     */
    private int pageSize = 10;

    /**
     * 当前页
     */
    private int curPage = 1;

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
        //添加到Fragment中
        fl_content.addView(view);

        setRequestParams();
        getDataFromNet();
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
    }
    /**
     * 解析商城热卖
     */
    private SmartServicePagerBean parseJson(String json) {
        return new Gson().fromJson(json, SmartServicePagerBean.class);
    }
}
