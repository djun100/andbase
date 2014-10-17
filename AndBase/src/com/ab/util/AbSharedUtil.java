package com.ab.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.ab.global.AbConstant;

//TODO: Auto-generated Javadoc

/**
* © 2012 amsoft.cn
* 名称：AbSharedUtil.java 
* 描述：保存到 SharedPreferences 的数据.    
*
* @author 还如一梦中
* @version v1.0
* @date：2014-10-09 下午11:52:13
*/
public class AbSharedUtil {

	private static final String SHARED_PATH = AbConstant.SHAREPATH;

	public static SharedPreferences getDefaultSharedPreferences(Context context) {
		return context.getSharedPreferences(SHARED_PATH, Context.MODE_PRIVATE);
	}
	
	public static void putInt(Context context,String key, int value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PATH, Context.MODE_PRIVATE);
		Editor edit = sharedPreferences.edit();
		edit.putInt(key, value);
		edit.commit();
	}

	public static int getInt(Context context,String key) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PATH, Context.MODE_PRIVATE);
		return sharedPreferences.getInt(key, 0);
	}
	
	public static void putString(Context context,String key, String value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PATH, Context.MODE_PRIVATE);
		Editor edit = sharedPreferences.edit();
		edit.putString(key, value);
		edit.commit();
	}

	public static String getString(Context context,String key) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PATH, Context.MODE_PRIVATE);
		return sharedPreferences.getString(key,null);
	}

}
