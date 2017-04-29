package com.recyclerview.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.recyclerview.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/19.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private final Context context;
    private final ArrayList<String> datas;

    public RecyclerViewAdapter(Context context, ArrayList<String> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {//绑定
        //1.根据位置得到数据
        String data = datas.get(position);
        //2.绑定数据
        holder.tv_text.setText(data);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {//创建
        View itemView;
        itemView = View.inflate(context, R.layout.item, null);
        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemCount() {//总的数目
        return datas.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void addData(int position, String s) {
        datas.add(position, s);
        //刷新添加
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        datas.remove(position);
        //移除刷新
        notifyItemRemoved(0);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_icon;
        private TextView tv_text;
        public MyViewHolder(View itemView) {
            super(itemView);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            tv_text = (TextView) itemView.findViewById(R.id.tv_title);
            //设置点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemListener != null){
                        onItemListener.OnItemClick(v, getLayoutPosition(), datas.get(getLayoutPosition()));
                    }

                }
            });
        }
    }
    public interface OnItemClickListener{
        /**
         * 当点击某条的时候回调方法
         * @param view
         * @param position
         * @param data
         */
        public void OnItemClick(View view, int position, String data);
    }
    private OnItemClickListener onItemListener;
    /**
     * 设置Item的点击事件
     */
    public void setOnItemClickListener(OnItemClickListener l){
        this.onItemListener = l;
    }

}
