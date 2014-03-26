package com.andbase.demo.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ab.activity.AbActivity;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.task.AbTaskObjectListener;
import com.ab.task.AbTaskPool;
import com.ab.task.AbTaskQueue;
import com.ab.task.AbThread;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.global.MyApplication;
/**
 * 异步的使用参照
 * http://www.amengsoft.org/post-133.html
 * @author zhaoqp
 *
 */
public class ThreadControlActivity extends AbActivity {
	
	private MyApplication application;
	private AbTitleBar mAbTitleBar = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAbContentView(R.layout.thread_main);
        application = (MyApplication)abApplication;
        
        mAbTitleBar = this.getTitleBar();
        mAbTitleBar.setTitleText(R.string.thread_name);
        mAbTitleBar.setLogo(R.drawable.button_selector_back);
        mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
        mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
        mAbTitleBar.setLogoLine(R.drawable.line);
        
        initTitleRightLayout();
        
        Button threadBtn  = (Button)this.findViewById(R.id.threadBtn);
        Button queueBtn  = (Button)this.findViewById(R.id.queueBtn);
        Button poolBtn  = (Button)this.findViewById(R.id.poolBtn);
        Button taskBtn1  = (Button)this.findViewById(R.id.taskBtn1);
        Button taskBtn2  = (Button)this.findViewById(R.id.taskBtn2);
        
        //单个线程
        threadBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//显示进度框
				showProgressDialog();
				AbThread mAbThread = new AbThread();
				//定义异步执行的对象
		    	final AbTaskItem item = new AbTaskItem();
				item.setListener(new AbTaskListener() {

					@Override
					public void update() {
						removeProgressDialog();
						showToast("执行完成");
					}

					@Override
					public void get() {
			   		    try {
			   		    	showToastInThread("开始执行");
			   		    	Thread.sleep(3000);
			   		    	//下面写要执行的代码，如下载数据
			   		    } catch (Exception e) {
			   		    }
				  };
				});
				//开始执行
				mAbThread.execute(item);
			}
        	
        });
        
        //线程队列
        queueBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//显示进度框
				showProgressDialog();
				//获取队列
				AbTaskQueue mAbTaskQueue = AbTaskQueue.getInstance();
				//定义异步执行的对象
		    	AbTaskItem item1 = new AbTaskItem();
				item1.setListener(new AbTaskListener() {

					@Override
					public void update() {
						showToast("执行完成1");
					}

					@Override
					public void get() {
			   		    try {
			   		    	showToastInThread("开始执行1");
			   		    	Thread.sleep(2000);
			   		    	//下面写要执行的代码，如下载数据
			   		    } catch (Exception e) {
			   		    }
				  };
				});
				
				AbTaskItem item2 = new AbTaskItem();
				item2.setListener(new AbTaskListener() {

					@Override
					public void update() {
						showToast("执行完成2");
						removeProgressDialog();
					}

					@Override
					public void get() {
			   		    try {
			   		    	showToastInThread("开始执行2");
			   		    	Thread.sleep(3000);
			   		    	//下面写要执行的代码，如下载数据
			   		    } catch (Exception e) {
			   		    }
				  };
				});
				
				//开始执行
				mAbTaskQueue.execute(item1);
				mAbTaskQueue.execute(item2);
				
				//强制停止
				//mAbTaskQueue.quit();
				
				//强制停止前面的请求
				//mAbTaskQueue.execute(item2,true);
			}
        	
        });
        
        
        //线程池
        poolBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//显示进度框
				showProgressDialog();
				AbTaskPool mAbTaskPool = AbTaskPool.getInstance();
				//定义异步执行的对象
		    	final AbTaskItem item = new AbTaskItem();
				item.setListener(new AbTaskListener() {

					@Override
					public void update() {
						removeProgressDialog();
						showToast("执行完成");
					}

					@Override
					public void get() {
			   		    try {
			   		    	showToastInThread("开始执行");
			   		    	Thread.sleep(1000);
			   		    	//下面写要执行的代码，如下载数据
			   		    } catch (Exception e) {
			   		    }
				  };
				});
				//开始执行
				mAbTaskPool.execute(item);
			}
        	
        });
        
        //异步任务
        taskBtn1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showProgressDialog();
				AbTask task = new AbTask();
				//定义异步执行的对象
		    	final AbTaskItem item = new AbTaskItem();
				item.setListener(new AbTaskListener() {

					@Override
					public void update() {
						removeProgressDialog();
						showToast("执行完成");
					}

					@Override
					public void get() {
			   		    try {
			   		    	showToastInThread("开始执行");
			   		    	Thread.sleep(3000);
			   		    	//下面写要执行的代码，如下载数据
			   		    } catch (Exception e) {
			   		    }
				  };
				});
		        task.execute(item);
			}
        	
        });
        
        //异步任务
        taskBtn2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				AbTask task = new AbTask();
				//定义异步执行的对象
		    	final AbTaskItem item = new AbTaskItem();
		    	item.setListener(new AbTaskObjectListener(){

					@Override
					public <T> void update(T entity) {
						Log.d("TAG", "执行完成:"+(String)entity);
						
					}

					@Override
					public void get() {
						try {
			   		    	Log.d("TAG", "开始执行");
			   		    	Thread.sleep(3000);
			   		    	//下面写要执行的代码，如下载数据
			   		    	String result = "hello andbase";
			   		    	item.setResult(result);
			   		    	
			   		    } catch (Exception e) {
			   		    	e.printStackTrace();
			   		    }
					}
		    		
		    	});
				
				//执行
		        task.execute(item);
			}
        	
        });
        
    }
    
    
    private void initTitleRightLayout(){
    	mAbTitleBar.clearRightView();
    }

	@Override
	protected void onResume() {
		super.onResume();
		initTitleRightLayout();
	}
	
	public void onPause() {
		super.onPause();
	}
   
}


