/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ab.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.ab.global.AbAppData;

// TODO: Auto-generated Javadoc
/**
 * © 2012 amsoft.cn
 * 名称：AbLogUtil.java 
 * 描述：日志工具类.
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2014-06-26 下午11:52:13
 */
public class AbLogUtil {
	
    /** debug开关. */
	private static final boolean D = AbAppData.DEBUG;
    

	/**
	 * debug日志
	 * @param tag
	 * @param msg
	 */
	public static void d(String tag,String msg) {
		if(D) Log.d(tag, msg);
	}
	
	/**
	 * info日志
	 * @param tag
	 * @param msg
	 */
	public static void i(String tag,String msg) {
		Log.i(tag, msg);
	}
	
	/**
	 * error日志
	 * @param tag
	 * @param msg
	 */
	public static void e(String tag,String msg) {
		Log.e(tag, msg);
	}
	
    

}
