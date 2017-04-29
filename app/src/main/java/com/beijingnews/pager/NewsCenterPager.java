package com.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.beijingnews.Fragement.LeftMenuFragment;
import com.beijingnews.activity.MainActivity;
import com.beijingnews.domain.NewsCenterPagerBean;
import com.beijingnews.menudetailpager.InteracMenuDetailPager;
import com.beijingnews.menudetailpager.NewsMenuDetailPager;
import com.beijingnews.menudetailpager.PhotosMenuDetailPager;
import com.beijingnews.menudetailpager.TopicMenuDetailPager;
import com.beijingnews.menudetailpager.base.MenuDetailBasePager;
import com.beijingnews.pager.base.BasePager;
import com.beijingnews.utils.CacheUtils;
import com.beijingnews.utils.Constants;
import com.beijingnews.utils.LogUtil;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/21.
 */

public class NewsCenterPager extends BasePager {

    private List<NewsCenterPagerBean.DataBean> data;
    private ArrayList<MenuDetailBasePager> detailBasePagers;//详情页面的集合
    public NewsCenterPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("新闻中心被初始化了");
        //1设置表题
        titleBar.setText("新闻中心");
        ib_menu.setVisibility(View.VISIBLE);
        //2联网请求得到数据 创建视图
        TextView textView = new TextView(context);
        textView.setTextColor(Color.RED);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        //3.把自视图添加到basepager的fragement中
        fl_content.addView(textView);
        //4.绑定数据
        textView.setText("我是新闻中心");

        //联网请求数据
        getDataFromNet();
    }

    private void getDataFromNet() {
        //使用xUtils3联网请求 初始化 关联
        RequestParams requestParams = new RequestParams(Constants.NEWSCENTER_PAGER_URL);
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("使用xUtils3联网请求成功==" + result);
                //缓存数据 保存数据
                CacheUtils.putString(context, Constants.NEWSCENTER_PAGER_URL, result);
                //获取缓存数据
                String savaJson = CacheUtils.getString(context, Constants.NEWSCENTER_PAGER_URL);
                if(!TextUtils.isEmpty(savaJson)){
                    //不为空就解析里面的数据
                    processData(result);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("使用xUtils3联网请求失败==" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("使用xUtils3 onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("使用xUtils3 onFinished");
            }
        });
    }

    /**
     * json 解析 和显示
     * @param json
     */
    private void processData(String json) {
        NewsCenterPagerBean bean = parsedJson(json);
        String title = bean.getData().get(0).getChildren().get(1).getTitle();
        LogUtil.e("使用Gson解析json数据成功 == " + title);
        //给左侧菜单传递数据
        data = bean.getData();
        //得到左侧菜单
        MainActivity mainActivity = (MainActivity) context;
        LeftMenuFragment leftmenuFragment = mainActivity.getLeftMenuFragment();
        //添加详情页面 这个要在setDate之前实现
        detailBasePagers = new ArrayList<>();
        detailBasePagers.add(new NewsMenuDetailPager(context,data.get(0)));//新闻详情
        detailBasePagers.add(new TopicMenuDetailPager(context));//专题详情
        detailBasePagers.add(new PhotosMenuDetailPager(context));//图组详情
        detailBasePagers.add(new InteracMenuDetailPager(context));//互动详情
        //把数据传递个左侧菜单
        leftmenuFragment.setData(data);

    }

    /**
     * 解析json数据 1.使用系统 2,。第三方解析 例如Gson（google） fastJson（阿里）
     * @param json
     * @return
     */
    private NewsCenterPagerBean parsedJson(String json) {
        Gson gson = new Gson();
        NewsCenterPagerBean bean = gson.fromJson(json, NewsCenterPagerBean.class);
        return bean;
    }

    public void swichPager(int position) {
        /**
         * 根据位置切换详情页面
         */
        //表题
        titleBar.setText(data.get(position).getTitle());
        //移除原来内容
        fl_content.removeAllViews();//移除之前的内容
        //添加新内容
        MenuDetailBasePager detailBasePager = detailBasePagers.get(position);
        View view = detailBasePager.rootView;
        detailBasePager.initData();//初始化数据
        fl_content.addView(view);
    }
}
