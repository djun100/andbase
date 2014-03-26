/*
 * Copyright (C) 2015 www.amengsoft.org
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
package com.ab.task;

import java.util.List;

import android.os.Handler;
import android.os.Message;

// TODO: Auto-generated Javadoc
/**
 * 下载线程.
 *
 * @author zhaoqp
 * @date 2011-11-10
 * @version v1.0
 */
public class AbThread extends Thread { 
	
	/** 下载单位. */
	public AbTaskItem item = null;
	
	/** 下载完成后的消息句柄. */
    private static Handler handler = new Handler() { 
        @Override 
        public void handleMessage(Message msg) { 
        	AbTaskItem item = (AbTaskItem)msg.obj; 
        	if(item.getListener() instanceof AbTaskListListener){
        		((AbTaskListListener)item.getListener()).update((List<?>)item.getResult()); 
        	}else if(item.getListener() instanceof AbTaskObjectListener){
        		((AbTaskObjectListener)item.getListener()).update(item.getResult()); 
        	}else{
        		item.getListener().update(); 
        	}
        } 
    }; 
    
    /**
     * 构造下载线程队列.
     */
    public AbThread() {
    }
      
    /**
     * 开始一个下载任务.
     *
     * @param item 下载单位
     */
    public void execute(AbTaskItem item) { 
    	 this.item = item;
         this.start();
    } 
 
    /**
     * 描述：线程运行.
     *
     * @see java.lang.Thread#run()
     */
    @Override 
    public void run() { 
            if(item!=null) { 
            	//定义了回调
                if (item.getListener() != null) { 
                	item.getListener().get();
                	//交由UI线程处理 
                    Message msg = handler.obtainMessage(); 
                    msg.obj = item; 
                    handler.sendMessage(msg); 
                } 
        } 
    } 

}

