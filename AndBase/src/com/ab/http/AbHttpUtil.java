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
package com.ab.http;

import android.content.Context;

// TODO: Auto-generated Javadoc
/**
 * © 2012 amsoft.cn
 * 名称：AbHttpUtil.java 
 * 描述：Http执行工具类，可处理get，post，以及异步处理文件的上传下载
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-10-22 下午4:15:52
 */
public class AbHttpUtil {
	
	/** 上下文. */
	private Context mContext;

	/** 实例话对象. */
	private AbHttpClient client = null;
	
	/** 工具类. */
	private static AbHttpUtil mAbHttpUtil = null;
	
	/** 成功. */
    public static final int SUCCESS_MESSAGE = 0;
    
    /** 失败. */
    public static final int FAILURE_MESSAGE = 1;
    
    /** 开始. */
    public static final int START_MESSAGE = 2;
    
    /** 完成. */
    public static final int FINISH_MESSAGE = 3;
    
    /** 进行中. */
    public static final int PROGRESS_MESSAGE = 4;
    
    /** 重试. */
    public static final int RETRY_MESSAGE = 5;
	
	/**
	 * 描述：获取实例.
	 *
	 * @param context the context
	 * @return single instance of AbHttpUtil
	 */
	public static AbHttpUtil getInstance(Context context){
	    if (null == mAbHttpUtil){
	    	mAbHttpUtil = new AbHttpUtil(context);
	    }
	    
	    return mAbHttpUtil;
	}
	
	/**
	 * 初始化AbHttpUtil.
	 *
	 * @param context the context
	 */
	private AbHttpUtil(Context context) {
		super();
		this.mContext = context;
		this.client = new AbHttpClient(this.mContext);
	}
	

	/**
	 * 描述：无参数的get请求.
	 *
	 * @param url the url
	 * @param responseListener the response listener
	 */
	public void get(String url, AbHttpResponseListener responseListener) {
		client.get(url, responseListener);
	}

	/**
	 * 描述：带参数的get请求.
	 *
	 * @param url the url
	 * @param params the params
	 * @param responseListener the response listener
	 */
	public void get(String url, AbRequestParams params,
			AbHttpResponseListener responseListener) {
		client.get(url, params, responseListener);
	}
	
	/**
	 *  
	 * 描述：下载数据使用，会返回byte数据(下载文件或图片).
	 *
	 * @param url the url
	 * @param responseListener the response listener
	 */
	public void get(String url, AbBinaryHttpResponseListener responseListener) {
		client.get(url, responseListener);
	}
	
	/**
	 * 描述：文件下载的get.
	 *
	 * @param url the url
	 * @param params the params
	 * @param responseListener the response listener
	 */
	public void get(String url, AbRequestParams params,
			AbFileHttpResponseListener responseListener) {
		client.get(url, params, responseListener);
	}
	
	
	/**
	 * 描述：无参数的post请求.
	 *
	 * @param url the url
	 * @param responseListener the response listener
	 */
	public void post(String url, AbHttpResponseListener responseListener) {
		client.post(url, responseListener);
	}
	
	
	/**
	 * 描述：带参数的post请求.
	 *
	 * @param url the url
	 * @param params the params
	 * @param responseListener the response listener
	 */
	public void post(String url, AbRequestParams params,
			AbHttpResponseListener responseListener) {
		client.post(url, params, responseListener);
	}
	
	
	/**
	 * 描述：文件下载的post.
	 *
	 * @param url the url
	 * @param params the params
	 * @param responseListener the response listener
	 */
	public void post(String url, AbRequestParams params,
			AbFileHttpResponseListener responseListener) {
		client.post(url, params, responseListener);
	}
	
	/**
	 * 描述：设置连接超时时间(第一次请求前设置).
	 *
	 * @param timeout 毫秒
	 */
	public void setTimeout(int timeout) {
		client.setTimeout(timeout);
	}
	
	/**
	 * 描述：设置调试模式(第一次请求前设置).
	 *
	 * @param debug 开关
	 */
	public void setDebug(boolean debug) {
		client.setDebug(debug);
	}
	

    /**
     * 打开ssl 自签名(第一次请求前设置).
     * @param enabled
     */
    public void setEasySSLEnabled(boolean enabled){
        client.setOpenEasySSL(enabled);
    }

    /**
	 * 设置编码(第一次请求前设置).
	 * @param encode
	 */
	public void setEncode(String encode) {
		client.setEncode(encode);
	}
	
	/**
     * 设置用户代理(第一次请求前设置).
     * @param userAgent
     */
	public void setUserAgent(String userAgent) {
		client.setUserAgent(userAgent);
	}
	
	/**
	 * 关闭HttpClient
	 * 当HttpClient实例不再需要是，确保关闭connection manager，以释放其系统资源  
	 */
	private void shutdownHttpClient(){
	    if(client != null){
	    	client.shutdown();
	    }
	}
	
}
