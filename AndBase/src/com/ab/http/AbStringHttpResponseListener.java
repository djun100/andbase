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

// TODO: Auto-generated Javadoc
/**
 * 
 * © 2012 amsoft.cn
 * 名称：AbStringHttpResponseListener.java 
 * 描述：Http字符串响应监听器
 * @author 还如一梦中
 * @date：2013-11-13 上午9:00:52
 * @version v1.0
 */
public class AbStringHttpResponseListener extends AbHttpResponseListener{
	
    /** 日志标记. */
    private static final String TAG = "AbStringHttpResponseListener";
	
    /**
     * 构造
     */
	public AbStringHttpResponseListener() {
		super();
	}

	/**
	 * 描述：获取数据成功会调用这里.
	 */
    public void onSuccess(int statusCode,String content) {};
    
    
    /**
     * 成功消息.
     */
    public void sendSuccessMessage(int statusCode,String content){
    	sendMessage(obtainMessage(AbHttpUtil.SUCCESS_MESSAGE, new Object[]{statusCode, content}));
    }
		

}
