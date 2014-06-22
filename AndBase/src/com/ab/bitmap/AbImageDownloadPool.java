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
package com.ab.bitmap;

import java.util.concurrent.Executor;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ab.global.AbAppData;
import com.ab.task.AbThreadFactory;
import com.ab.util.AbFileUtil;
import com.ab.util.AbStrUtil;
// TODO: Auto-generated Javadoc
/**
 * © 2012 amsoft.cn
 * 名称：AbImageDownloadPool.java 
 * 描述：线程池图片下载,用andbase线程池
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-5-23 上午10:10:53
 */

public class AbImageDownloadPool{
	
	/** 日志标记. */
	private static String TAG = "AbImageDownloadPool";
	
	/** 日志标记开关. */
	private static final boolean D = AbAppData.DEBUG;
	
	/** The image download. 单例对象*/
	private static AbImageDownloadPool imageDownload = null; 

	/** 线程执行器. */
	public static Executor mExecutorService = null;
	
	/** 下载完成后的消息句柄. */
    private static Handler handler = new Handler() { 
        @Override 
        public void handleMessage(Message msg) { 
            AbImageDownloadItem item = (AbImageDownloadItem)msg.obj; 
            item.getListener().update(item.bitmap, item.imageUrl); 
        } 
    }; 
	
	/**
	 * 构造图片下载器.
	 */
    protected AbImageDownloadPool() {
    	mExecutorService = AbThreadFactory.getExecutorService();
    } 
	
	/**
	 * 单例构造图片下载器.
	 *
	 * @return single instance of AbImageDownloadPool
	 */
    public static AbImageDownloadPool getInstance() { 
        if (imageDownload == null) { 
        	imageDownload = new AbImageDownloadPool(); 
        } 
        return imageDownload;
    } 
    
    /**
     * 执行下载.
     * @param item the item
     */
    public void execute(final AbImageDownloadItem item) {    
    	String imageUrl = item.imageUrl;
    	if(AbStrUtil.isEmpty(imageUrl)){
    		if(D)Log.d(TAG, "图片URL为空，请先判断");
    	}else{
    		imageUrl = imageUrl.trim();
    	}
    	
		//从缓存中获取图片
    	final String cacheKey = AbImageCache.getCacheKey(imageUrl, item.width, item.height, item.type);
		item.bitmap =  AbImageCache.getBitmapFromCache(cacheKey);
    	
    	if(item.bitmap == null){
			// 缓存中没有图像，则从网络上取出数据，并将取出的数据缓存到内存中
    		mExecutorService.execute(new Runnable() { 
	    		public void run() {
	    			try {
	    				//逻辑：判断这个任务是否有其他线程再执行，如果有等待，直到下载完成唤醒显示
	    	    		Runnable runnable = AbImageCache.getRunRunnableFromCache(cacheKey);
	    				if(runnable != null){
	    					
	    	            	//线程等待通知后显示
    	                	//if(D) Log.d(TAG, "线程等待:"+item.imageUrl+","+cacheKey);
    	                	AbImageCache.addToWaitRunnableCache(cacheKey, this);
    	                	synchronized(this){
    	                		this.wait();
    	                	}
    	                	//if(D) Log.d(TAG, "线程被唤醒:"+item.imageUrl);
    	    				//直接获取
    	    				item.bitmap =  AbImageCache.getBitmapFromCache(cacheKey);
	    				}else{
	    					//增加下载中的线程记录
	    					//if(D) Log.d(TAG, "从内存取或者下载:"+item.imageUrl+","+cacheKey);
	    					AbImageCache.addToRunRunnableCache(cacheKey, this);
    	    				item.bitmap = AbFileUtil.getBitmapFromSDCache(item.imageUrl,item.type,item.width,item.height);
    	    				//增加到下载完成的缓存，删除下载中的记录和等待的记录，同时唤醒所有等待列表中key与其key相同的线程
        	    			AbImageCache.addBitmapToCache(cacheKey,item.bitmap);     
    	    				
	    				}
	    				
	    			} catch (Exception e) { 
	    				if(D) Log.d(TAG, "error:"+item.imageUrl);
	    				e.printStackTrace();
	    			} finally{ 
		    			if (item.getListener() != null) { 
	    	                Message msg = handler.obtainMessage(); 
	    	                msg.obj = item; 
	    	                handler.sendMessage(msg); 
	    	            } 
	    			}
	    		}                 
	    	});  
    		
    	}else{
    		//if(D) Log.d(TAG, "从内存缓存中得到图片:"+cacheKey+","+item.bitmap);
    		if (item.getListener() != null) { 
                Message msg = handler.obtainMessage(); 
                msg.obj = item; 
                handler.sendMessage(msg); 
            } 
    	}
    	
    }
    
}
