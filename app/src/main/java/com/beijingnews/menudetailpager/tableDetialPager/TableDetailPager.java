package com.beijingnews.menudetailpager.tableDetialPager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beijingnews.R;
import com.beijingnews.domain.NewsCenterPagerBean;
import com.beijingnews.domain.TabDetailPagerBean;
import com.beijingnews.menudetailpager.base.MenuDetailBasePager;
import com.beijingnews.utils.CacheUtils;
import com.beijingnews.utils.Constants;
import com.beijingnews.utils.DensityUtil;
import com.beijingnews.utils.LogUtil;
import com.beijingnews.view.HorizontalScrollviewPager;
import com.beijingnews.view.RefreshListView;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2017/4/22.
 * 叶签详情页面 所有都是相同的
 */

public class TableDetailPager extends MenuDetailBasePager {
    private final NewsCenterPagerBean.DataBean.ChildrenBean childerBean;
    /**
     * 顶部轮播图部分的数据
     */
    List<TabDetailPagerBean.Topnews> topnews;
    /**
     * 初始化控件
     */
    private HorizontalScrollviewPager viewpager;
    private TextView tv_title;
    private LinearLayout ll_point_group;
    private RefreshListView  listview;
    /**
     * 之前的那个红点
     */
    private int prePosition;
    /**
     * ListView的数据集合
     */
    private List<TabDetailPagerBean.News> news;
    /**
     * xutils设置默认图片
     */
    private ImageOptions imageOptions;
    /**
     * listView的适配器adapter
     */
    private MyDetailPagerListAdapter adapter;

    String url;

    /**
     * 下一页的联网路径
     */
    public String moreUrl;

    /**
     * 加在更多
     */
    private boolean isLoadMore = false;

    public TableDetailPager(Context context, NewsCenterPagerBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.childerBean = childrenBean;
        /**
         * xutils默认构造方法中
         */
        imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(context, 100), DensityUtil.dip2px(context, 100))
                .setRadius(DensityUtil.dip2px(context, 5))
                            //如果图片的大小不是定义为wrap_content,不要CROP
                .setCrop(true)
                            //加载中或错误图片的ScaleType
                .setImageScaleType(ImageView.ScaleType.FIT_XY)
                .setLoadingDrawableId(R.drawable.icon)
                .setFailureDrawableId(R.drawable.icon)
                .build();
    }

    @Override
    public void initData() {
        super.initData();
        //2联网请求得到数据 创建视图

        /**
         * ViewPager默认创建了两个视图，多以有两个url
         */
        url = Constants.BASE_URL + childerBean.getUrl();
        //把之前缓存的数据取出
        String savaJson = CacheUtils.getString(context, url);
        if(!TextUtils.isEmpty(savaJson)){
            processData(savaJson);
        }
        LogUtil.e("URL == " + url);
        //联网请求数据
        getDataFromNet();
    }

    private void getDataFromNet() {
        LogUtil.e("url地址 == " + url);
        RequestParams params = new RequestParams(url);
        params.setConnectTimeout(4000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //数据缓存
                CacheUtils.putString(context, url, result);
                LogUtil.e(childerBean.getTitle() + "数据页面请求成功" + result);
                //解析和处理显示数据
                processData(result);

                /**
                 * 隐藏下拉刷新控件 成功了更新时间 失败不更新事件
                 */
                listview.OnRefreshFinish(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e(childerBean.getTitle() + "数据页面请求失败" + ex.getMessage());
                /**
                 * 隐藏下拉刷新控件 成功了更新时间 失败不更新事件
                 */
                listview.OnRefreshFinish(false);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e(childerBean.getTitle() + "数据页面请求onCancelled" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e(childerBean.getTitle() + "onfinished");
            }
        });
    }

    private void processData(String json) {
        TabDetailPagerBean bean = parsedJson(json);
        LogUtil.e(childerBean.getTitle() + "解析数据成功 " + bean.getData().getNews().get(0).getTitle());
        /**
         * 加载更多处理
         */
        moreUrl = "";
        if(TextUtils.isEmpty(bean.getData().getMore())){
            moreUrl = "";
        }else{
            /**
             * 不为空 是这个 ""而不是null
             */
            moreUrl = Constants.BASE_URL + bean.getData().getMore();
        }
        LogUtil.e("加载更多的地址 == " + moreUrl);
        //默认和加载更多
        if(!isLoadMore){
            //默认
            //顶部数据集合
            topnews = bean.getData().getTopnews();
            //设置ViewPager的适配器
            viewpager.setAdapter(new MyTabDetailPagerAdapter());

            ll_point_group.removeAllViews();//移除第一次的视图
            for (int i = 0; i<topnews.size(); ++i){
                //创建红点
                ImageView imageView = new ImageView(context);
                imageView.setBackgroundResource(R.drawable.point_selector);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(context, 5), DensityUtil.dip2px(context, 5));
                imageView.setLayoutParams(params);
                if(i == 0){
                    imageView.setEnabled(true);
                }else{
                    imageView.setEnabled(false);
                    params.leftMargin = DensityUtil.dip2px(context, 5);
                }
                ll_point_group.addView(imageView);
            }
            //监听页面的改变
            viewpager.addOnPageChangeListener(new MyTabDetailOnPageChangeListener());
            //默认显示第0个
            tv_title.setText(topnews.get(prePosition).getTitle());

            //准备ListView对应的集合数据
            news = bean.getData().getNews();
            //设置ListView的适配器
            adapter = new MyDetailPagerListAdapter();
            listview.setAdapter(adapter);
        }else{
            //加载更多 回去的数据
            isLoadMore = false;
            /**
             * 第二个页面的数据
             */
            List<TabDetailPagerBean.News> newsIsMore = bean.getData().getNews();
            //添加到原来的集合中
            news.addAll(newsIsMore);
            //刷新适配器
            adapter.notifyDataSetChanged();
        }
    }
    class MyDetailPagerListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return news.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            viewHolder Holder;
            if(convertView == null){
                convertView = View.inflate(context, R.layout.item_tabledetail_pager, null);
                Holder = new viewHolder();
                Holder.iv_icon = (ImageView) convertView.findViewById(R.id.ig_icon);
                Holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                Holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);

                convertView.setTag(Holder);
            }else{
                Holder = (viewHolder) convertView.getTag();
            }

            //根据位置得到数据
            TabDetailPagerBean.News newsData = news.get(position);
            String imageUrl = Constants.BASE_URL + newsData.getListimage();
            //xutils3 图片
            x.image().bind(Holder.iv_icon, imageUrl, imageOptions);
//            //现在用glide请求图片
//            Glide.with(context)
//                    .load(imageUrl)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)//设置缓存
//                    .into(Holder.iv_icon);
            //标题
            Holder.tv_title.setText(newsData.getTitle());
            //更新时间
            Holder.tv_time.setText(newsData.getPubdate());

            return convertView;
        }
    }
    public static class viewHolder{
        ImageView iv_icon;
        TextView tv_title;
        TextView tv_time;
    }

    class MyTabDetailOnPageChangeListener implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //1.设置文本
            tv_title.setText(topnews.get(position).getTitle());
            //2.设置对应的高亮红点 把之前变成灰 把现在变成红
            ll_point_group.getChildAt(prePosition).setEnabled(false);
            //3.把现在变红
            ll_point_group.getChildAt(position).setEnabled(true);
            prePosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    class MyTabDetailPagerAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setBackgroundResource(R.drawable.pic_item_list_default);
            container.addView(imageView);
            //联网请求图片
            String imageUrl =  Constants.BASE_URL + topnews.get(position).getTopimage();
            //xutile3设置默认图片
            x.image().bind(imageView,imageUrl);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ImageView)object);
        }
    }


    private TabDetailPagerBean parsedJson(String json) {
        return new Gson().fromJson(json, TabDetailPagerBean.class);
    }

    @Override
    public View initView() {

        View view = View.inflate(context, R.layout.tabdetail_pager, null);
        listview = (RefreshListView ) view.findViewById(R.id.listView);

        View topNewsView = View.inflate(context, R.layout.topnews, null);
        viewpager = (HorizontalScrollviewPager) topNewsView.findViewById(R.id.viewpager);
        tv_title = (TextView) topNewsView.findViewById(R.id.tv_title);
        ll_point_group = (LinearLayout) topNewsView.findViewById(R.id.ll_point_group);

        //把顶部轮播图部分视图， 以头的方式添加到ListView中
        listview.addHeaderView(topNewsView);
        //设置监听下拉刷新
        listview.setmOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onPullDownRefresh() {
                /**
                 * 下拉刷新就是重新进行网络请求， 重新解析数据就可以了
                 */
                Toast.makeText(context, "下拉刷新被回调", Toast.LENGTH_LONG).show();
                LogUtil.e("Tag == " + "下拉刷新被回调");
                getDataFromNet();
            }

            @Override
            public void onLoadMore() {
                /**
                 * 加载更多就是重新进行网络请求， 重新解析数据就可以了
                 */

                if(TextUtils.isEmpty(moreUrl)){
                    Toast.makeText(context, "没有更多数据", Toast.LENGTH_SHORT).show();
                    listview.OnRefreshFinish(false);//直接隐藏上拉加载更多框架
                }else{
                    getMoreDataFromNet();//进行加载更多
                }
            }
        });
        return view;

    }

    private void getMoreDataFromNet() {
        RequestParams params = new RequestParams(moreUrl);
        params.setConnectTimeout(4000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                /**
                 * 加载更多不用再要缓存了因为前面已经有啦数据   解析数据
                 */
                LogUtil.e("加载更多联网成功 == " + result);
                isLoadMore = true;//把这个放在前面应为后面要用到

                //解析数据
                processData(result);
                /**
                 * 成功隐藏刷新框架
                 */
                listview.OnRefreshFinish(false);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("加载更多联网失败 == " + ex.getMessage());
                /**
                 * 失败隐藏刷新框架
                 */
                listview.OnRefreshFinish(false);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("加载更多联网onCancelled == " + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("加载更多联网onFinished == ");
            }
        });

    }

}
