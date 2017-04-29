package com.beijingnews.Fragement;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.beijingnews.Fragement.base.BaseFragment;
import com.beijingnews.R;
import com.beijingnews.activity.MainActivity;
import com.beijingnews.domain.NewsCenterPagerBean;
import com.beijingnews.pager.NewsCenterPager;
import com.beijingnews.utils.DensityUtil;
import com.beijingnews.utils.LogUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/4/20.
 */

public class LeftMenuFragment extends BaseFragment {

    private List<NewsCenterPagerBean.DataBean> data;
    private ListView listView;
    private int prePosition;//点击的位置
    private LeftMenuFlagmentAdapter leftMenuFlagmentAdapter;

    @Override
    public View initView() {
        LogUtil.e("左侧菜单视图");
        listView = new ListView(context);
        listView.setPadding(0, DensityUtil.dip2px(context, 40),0,0);
        //设置分割线
        listView.setDividerHeight(0);
        //
        listView.setCacheColorHint(Color.TRANSPARENT);
        //设置按下我们的listView的item不变色
        listView.setSelection(android.R.color.transparent);

        //设置item的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //1.记录点击的位置，变成红色
                prePosition = position;
                leftMenuFlagmentAdapter.notifyDataSetChanged();//getCount（） ->getView()
                //2.把左侧菜单关闭
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.getSlidingMenu().toggle();//是开就关，是关就开
                //3.切换到详细页面：新闻详细页面 专题详情页面 等等
                swichPager(prePosition);

            }
        });

        return listView;
    }

    /**
     * 根据位置切换不同的页面
     * @param position
     */
    private void swichPager(int position){
        MainActivity mainActivity = (MainActivity) context;
        ContentFragment contentFragment = mainActivity.getContentFragment();
        NewsCenterPager newsCenterPager = contentFragment.getNewsCenterPager();
        newsCenterPager.swichPager(position);
    }
    @Override
    protected void initData() {
        super.initData();
        LogUtil.e("左侧菜单被初始化了");

    }

    /**
     * 接收数据
     * @param data
     */
    public void setData(List<NewsCenterPagerBean.DataBean> data) {
        this.data = data;
        for(int i = 0; i<data.size(); ++i){
            LogUtil.e("title == " + data.get(i).getTitle());
        }
        //设置适配器
        //这里设置适配器 因为这里有数据
        leftMenuFlagmentAdapter = new LeftMenuFlagmentAdapter();
        listView.setAdapter(leftMenuFlagmentAdapter);
        //设置默认页面
        swichPager(prePosition);
    }
    class LeftMenuFlagmentAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return data.size();
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
            TextView textView = (TextView) View.inflate(context, R.layout.layout_item_leftmenu, null);
            textView.setText(data.get(position).getTitle());
            if(prePosition == position){
                textView.setEnabled(true);
            }else{
                textView.setEnabled(false);
            }
            return textView;
        }
    }
}
