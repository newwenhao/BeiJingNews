package com.beijingnews.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/4/20.
 * 缓存软件的一些参数和数据
 */

public class CacheUtils {
    public static boolean getBoolean(Context context, String key){
        SharedPreferences sp = context.getSharedPreferences("liu", Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }
    /**
     * 保存软件的参数
     * @param context
     * @param key
     * @param values
     */
    public static void putBoolean(Context context, String key, boolean values){
        SharedPreferences sp = context.getSharedPreferences("liu", Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, values).commit();

    }
    /**
     * 把联网请求的数据保存到本地中来 缓存文本数据
     */
    public static void putString(Context context, String key, String values){
        SharedPreferences sp = context.getSharedPreferences("liu", Context.MODE_PRIVATE);
        sp.edit().putString(key, values).commit();

    }
    /**
     * 取得缓存的文本信息
     */
    public static String getString(Context context, String key){
        SharedPreferences sp = context.getSharedPreferences("liu", Context.MODE_PRIVATE);
        LogUtil.e("result == " + sp.getString(key, ""));
        return sp.getString(key, "");

    }
}
