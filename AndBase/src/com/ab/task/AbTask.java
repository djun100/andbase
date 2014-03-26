package com.ab.task;

import java.util.List;

import android.os.AsyncTask;
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * 名称：AbAsyncTask.java 
 * 描述：下载数据的任务实现，单次下载
 * @author zhaoqp
 * @date：2013-9-2 下午12:52:13
 * @version v1.0
 */
public class AbTask extends AsyncTask<AbTaskItem, Integer, AbTaskItem> {
	
	private AbTaskListener listener; 
	
	public AbTask() {
		super();
	}

	@Override
	protected AbTaskItem doInBackground(AbTaskItem... items) {
		AbTaskItem item = items[0];
		this.listener = item.getListener();
		if (this.listener != null) { 
			this.listener.get();
        } 
		return item;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(AbTaskItem item) {
		if (this.listener != null) {
			if(this.listener instanceof AbTaskListListener){
        		((AbTaskListListener)this.listener).update((List<?>)item.getResult()); 
        	}else if(this.listener instanceof AbTaskObjectListener){
        		((AbTaskObjectListener)this.listener).update(item.getResult()); 
        	}else{
        		this.listener.update(); 
        	}
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		if (this.listener != null) { 
			this.listener.onProgressUpdate(values);
		}
	}

}
