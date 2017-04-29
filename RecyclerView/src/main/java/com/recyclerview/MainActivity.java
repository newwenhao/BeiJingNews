package com.recyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.recyclerview.Adapter.RecyclerViewAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recycleView;
    private ArrayList<String> datas;
    private RecyclerViewAdapter adapter;
    private Button btn_add;
    private Button btn_remove;
    private Button btn_list;
    private Button btn_grid;
    private SwipeRefreshLayout swiperefresh_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initListener();
        initRefresh();
        RefreshData();
    }

    private void RefreshData() {
        for(int i = 0; i<50; ++i){
            datas.add(1, "data" + (50+i));
        }

    }

    private void initRefresh() {
        //设置控制刷新的圈来回的炫
        swiperefresh_layout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_dark);
        //设置刷新空间的背景色
        swiperefresh_layout.setProgressBackgroundColorSchemeColor(getResources().getColor(android.R.color.background_light));
        //设置滑动的距离
        swiperefresh_layout.setDistanceToTriggerSync(50);
        //设置大小模式
        swiperefresh_layout.setSize(SwipeRefreshLayout.DEFAULT);
        //设置下拉刷新的监听
        swiperefresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //这是在主线程
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RefreshData();
                        //刷新数据
                        adapter.notifyItemRangeChanged(0, 50);
                        //定位
                        recycleView.scrollToPosition(0);
                        //刷新状态影藏
                        swiperefresh_layout.setRefreshing(false);
                    }
                },2000);
            }
        });
    }
    private void initListener() {
        //使用switch语句更方便
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "btn_add", Toast.LENGTH_LONG).show();
                adapter.addData(0, "new Data");
                //定位到第0个位置
                recycleView.scrollToPosition(0);
            }
        });
        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "btn_remove", Toast.LENGTH_LONG).show();
                adapter.removeData(0);
                //定位到第0个位置
                recycleView.scrollToPosition(0);

            }
        });
        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "btn_list", Toast.LENGTH_LONG).show();

                //设置布局管理器 第一个参数：上下文， 第二个参数：方向 第三个参数：是否倒序
                recycleView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
            }
        });
        btn_grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "btn_grid", Toast.LENGTH_LONG).show();
                /**
                 * new GridLayoutManager(MainActivity.this, 2, GridLayoutManager.VERTICAL, false)
                 * 第一个参数上下文， 第二个参数是列数， 第三个是方向， 第四个是否倒序
                 */
                recycleView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2, GridLayoutManager.VERTICAL, false));
            }
        });
    }

    private void initData() {
        //添加数据
        datas = new ArrayList<>();
        for(int i = 0; i<50; ++i){
            datas.add("data" + i);
        }
        //设置适配器
        adapter = new RecyclerViewAdapter(this, datas);
        recycleView.setAdapter(adapter);
        //设置布局管理器 第一个参数：上下文， 第二个参数：方向 第三个参数：是否倒序
        recycleView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
        /**
         * new GridLayoutManager(MainActivity.this, 2, GridLayoutManager.VERTICAL, false)
         * 第一个参数上下文， 第二个参数是列数， 第三个是方向， 第四个是否倒序
         */
//        recycleView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2, GridLayoutManager.VERTICAL, false));
        /**
         * new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
         * 第一个参数多少列 第二个参数是方向
         */
//        recycleView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        //设置分割线
        recycleView.addItemDecoration(new MyItemDecoration(MainActivity.this, MyItemDecoration.VERTICAL_LIST));
        //设置点击某一条
        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position, String data) {
                 Toast.makeText(MainActivity.this, "data==" + datas.get(position), Toast.LENGTH_LONG).show();
            }
        });
        //设置动画
        recycleView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initView() {
        recycleView = (RecyclerView) findViewById(R.id.recycleView);
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_remove = (Button) findViewById(R.id.btn_remove);
        btn_list = (Button) findViewById(R.id.btn_list);
        btn_grid = (Button) findViewById(R.id.btn_grid);
        swiperefresh_layout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh_layout);

    }
}
