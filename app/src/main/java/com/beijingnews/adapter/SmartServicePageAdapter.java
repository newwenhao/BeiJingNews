package com.beijingnews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beijingnews.R;
import com.beijingnews.domain.SmartServicePagerBean;
import com.beijingnews.utils.LogUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by Administrator on 2017/4/29.
 * 商城热卖的适配器 传数据上下文
 */

public class SmartServicePageAdapter extends RecyclerView.Adapter<SmartServicePageAdapter.ViewHolder> {
    private final Context context;
    private final List<SmartServicePagerBean.list> datas;

    public SmartServicePageAdapter(Context context, List<SmartServicePagerBean.list> datas){
        this.context = context;
        this.datas = datas;
    }

    /**
     * getView中的创建视图和ViewHolder代码
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_smartservicepager, null);
        LogUtil.e("视图初始化");
        return new ViewHolder(view);
    }

    /**
     * 绑定数据部分代码
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //1.根据位置得到相应的数据
        final SmartServicePagerBean.list list = datas.get(position);

        //2.绑定数据
        Glide.with(context)
                .load(list.getImgUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.govaffair)
                .error(R.drawable.govaffair)
                .into(holder.id_icon);
        holder.tv_name.setText(list.getName());
        holder.tv_price.setText("￥" + list.getPrice());

        //设置点击事件
        holder.btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "price ==" + list.getPrice(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    /**
     * 清除数据
     */
    public void clearData() {
        datas.clear();
        //刷新
        notifyItemRangeChanged(0, datas.size());
    }

    /**
     * 根据指定位置添加数据
     * @param position
     * @param data
     */
    public void addData(int position, List<SmartServicePagerBean.list> data) {
        if(data!=null && data.size() >0){
            datas.addAll(0, data);
            notifyItemRangeChanged(position, datas.size());
        }
    }

    /**
     * 得到总的条数
     * @return
     */
    public int getDataCount() {
        return datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView id_icon;
        private TextView tv_name;
        private TextView tv_price;
        private Button btn_buy;
        public ViewHolder(View itemView) {
            super(itemView);
            id_icon = (ImageView) itemView.findViewById(R.id.id_icon);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            btn_buy = (Button) itemView.findViewById(R.id.btn_buy);
        }
    }
}
