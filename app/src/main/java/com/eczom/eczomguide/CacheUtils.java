package com.eczom.eczomguide;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SP缓存
 */

public class CacheUtils {

    public static void putBoolean(Context context,String key,boolean value){
        SharedPreferences sp = context.getSharedPreferences("eczom",Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).commit();
    }

    public static boolean getBoolean(Context context,String key){
        SharedPreferences sp = context.getSharedPreferences("eczom",Context.MODE_PRIVATE);
        return sp.getBoolean(key,false);
    }
}
