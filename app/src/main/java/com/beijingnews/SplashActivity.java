package com.beijingnews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.beijingnews.activity.GuideAcitity;
import com.beijingnews.activity.MainActivity;
import com.beijingnews.utils.CacheUtils;

public class SplashActivity extends AppCompatActivity {
    //静态常量
    public static final String START_MAIN = "startMain";
    private LinearLayout activity_main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        //渐变动画
        AlphaAnimation aa = new AlphaAnimation(0, 1);//从看不见到看的见
        aa.setDuration(500);//设置时长，持续播放时间
        aa.setFillAfter(true);
        //缩放动画
        ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(500);
        sa.setFillAfter(true);
        //旋转动画
        RotateAnimation ra = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(500);
        ra.setFillAfter(true);

        AnimationSet set = new AnimationSet(false);
        //添加动画没有循序 便于同时播放
        set.addAnimation(aa);
        set.addAnimation(ra);
        set.addAnimation(sa);
        activity_main.startAnimation(set);
        set.setDuration(2000);
        //动画自身事件监听
        set.setAnimationListener(new MyAnimationListener());

    }

    private void initView() {
        activity_main = (LinearLayout) findViewById(R.id.activity_main);
    }

    class MyAnimationListener implements Animation.AnimationListener{
        /**
         * 开始播放 回调
         * @param animation
         */
        @Override
        public void onAnimationStart(Animation animation) {

        }
        /**
         * 播放结束 回调
         * @param animation
         */
        @Override
        public void onAnimationEnd(Animation animation) {
            //判断是否进入过主页面，如果进入过主页面，直接进入主页面。如果没有进入过主页面就进入引导页面
            boolean isStartMain =  CacheUtils.getBoolean(SplashActivity.this, START_MAIN);
            if(isStartMain){
                //进入过主页面
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            }else{
                //没有进入过主页面
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this, GuideAcitity.class);
                startActivity(intent);
            }
            //关闭页面
            finish();
            Toast.makeText(SplashActivity.this, "引导界面", Toast.LENGTH_LONG).show();
        }
        /**
         * 重播 回调
         * @param animation
         */
        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
