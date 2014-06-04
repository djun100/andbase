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

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

// TODO: Auto-generated Javadoc
/**
 * 
 * © 2012 amsoft.cn
 * 名称：AbRequestParams.java 
 * 描述：Http请求参数
 * @author 还如一梦中
 * @date：2013-11-13 上午10:28:55
 * @version v1.0
 */
public class AbRequestParams {

    /** 日志标记. */
    private static final String TAG = "AbRequestParams";
    
    /** url参数. */
    protected ConcurrentHashMap<String, String> urlParams;
    
    /** 文件参数. */
    protected ConcurrentHashMap<String, FileWrapper> fileParams;

    /**
     * 构造一个空的请求参数.
     */
    public AbRequestParams() {
        init();
    }

    /**
     * 用一个map构造请求参数
     * @param source
     */
    public AbRequestParams(Map<String, String> source) {
        init();

        for (Map.Entry<String, String> entry : source.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 用一个key和value构造请求参数
     * @param key
     * @param value
     */
    public AbRequestParams(String key, String value) {
        init();
        put(key, value);
    }
    
    /**
     * 初始化.
     */
    private void init() {
        urlParams = new ConcurrentHashMap<String, String>();
        fileParams = new ConcurrentHashMap<String, FileWrapper>();
    }


    /**
     * 增加一对请求参数.
     * @param key
     * @param value
     */
    public void put(String key, String value) {
        if (key != null && value != null) {
        	urlParams.put(key, value);
        }
    }
    
    /**
     * 增加一个文件域.
     * @param key
     * @param value
     * @param contentType the content type of the file, eg. application/json
     */
    public void put(String key, File file,String contentType) {
        if (key != null && file != null) {
        	fileParams.put(key, new FileWrapper(file,contentType));
        }
    }
    
    /**
     * 增加一个文件域.
     * @param key
     * @param file
     */
    public void put(String key, File file) {
    	put(key,file,"application/octet-stream");
    }
    
    
    /**
     * 删除一个请求参数.
     * @param key.
     */
    public void remove(String key) {
        urlParams.remove(key);
        fileParams.remove(key);
    }

    /**
     * 描述：转换为参数字符串
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
            if (result.length() > 0)
                result.append("&");

            result.append(entry.getKey());
            result.append("=");
            result.append(entry.getValue());
        }

        return result.toString();
    }

    /**
     * 获取参数列表.
     * @return the params list
     */
    public List<BasicNameValuePair> getParamsList() {
        List<BasicNameValuePair> lparams = new LinkedList<BasicNameValuePair>();
        for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
            lparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return lparams;
    }

    
    /**
     * 获取参数字符串.
     * @return the param string
     */
    public String getParamString() {
        return URLEncodedUtils.format(getParamsList(), HTTP.UTF_8);
    }
    
   /**
    * 获取HttpEntity.
    * @param responseListener
    * @return
    * @throws IOException
    */
    public HttpEntity getEntity(AbHttpResponseListener responseListener) throws IOException {
        if (fileParams.isEmpty()) {
            return createFormEntity();
        } else {
            return createMultipartEntity(responseListener);
        }
    }
    
    /**
     * 创建HttpEntity.
     * @return the http entity
     */
    public HttpEntity createFormEntity() {
        try {
            return new UrlEncodedFormEntity(getParamsList(), HTTP.UTF_8);
        } catch (UnsupportedEncodingException e) {
            return null; 
        }
    }
    
    /**
     * 
     * 描述：创建文件域HttpEntity
     * @param responseListener
     * @return
     * @throws IOException
     */
    private HttpEntity createMultipartEntity(AbHttpResponseListener responseListener) throws IOException {
        AbMultipartEntity entity = new AbMultipartEntity(responseListener);

        // Add string params
        for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
            entity.addPart(entry.getKey(), entry.getValue());
        }

        // Add file params
        for (ConcurrentHashMap.Entry<String, FileWrapper> entry : fileParams.entrySet()) {
            FileWrapper fileWrapper = entry.getValue();
            entity.addPart(entry.getKey(), fileWrapper.file, fileWrapper.contentType);
        }

        return entity;
    }

    /**
     * 获取url参数
     * @return
     */
	public ConcurrentHashMap<String, String> getUrlParams() {
		return urlParams;
	}

	/**
	 * 设置url参数
	 * @param urlParams
	 */
	public void setUrlParams(ConcurrentHashMap<String, String> urlParams) {
		this.urlParams = urlParams;
	}
	
	/**
     * 文件类.
     */
    private static class FileWrapper {
        
        /** 文件. */
        public File file;
        
        /** 类型. */
        public String contentType;

        /**
         * 构造.
         * @param file the file
         * @param contentType the content type
         */
        public FileWrapper(File file, String contentType) {
            this.file = file;
            this.contentType = contentType;
        }
    }
    
    
}
