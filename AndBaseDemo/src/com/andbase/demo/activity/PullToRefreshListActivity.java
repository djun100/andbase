package com.andbase.demo.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.ab.activity.AbActivity;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.task.AbTaskQueue;
import com.ab.view.listener.AbOnListViewListener;
import com.ab.view.pullview.AbPullListView;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.demo.adapter.ImageListAdapter;
import com.andbase.global.MyApplication;

public class PullToRefreshListActivity extends AbActivity {
	
	private MyApplication application;
	private List<Map<String, Object>> list = null;
	private List<Map<String, Object>> newList = null;
	private AbPullListView mAbPullListView = null;
	private int currentPage = 1;
	private AbTaskQueue mAbTaskQueue = null;
	private ArrayList<String> mPhotoList = new ArrayList<String>();
	private AbTitleBar mAbTitleBar = null;
	private ImageListAdapter myListViewAdapter = null;
	private int total = 50;
	private int pageSize = 15;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAbContentView(R.layout.pull_list);
        application = (MyApplication)abApplication;
        
        mAbTitleBar = this.getTitleBar();
        mAbTitleBar.setTitleText(R.string.pull_list_name);
        mAbTitleBar.setLogo(R.drawable.button_selector_back);
        mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
        mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
        mAbTitleBar.setLogoLine(R.drawable.line);
        
        mPhotoList.add("http://img01.taobaocdn.com/bao/uploaded/i3/13215035600700175/T1C2mzXthaXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");  
		mPhotoList.add("http://img01.taobaocdn.com/bao/uploaded/i2/13215025617307680/T1AQqAXqpeXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
		mPhotoList.add("http://img01.taobaocdn.com/bao/uploaded/i1/13215035569460099/T16GuzXs0cXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
		mPhotoList.add("http://img01.taobaocdn.com/bao/uploaded/i2/13215023694438773/T1lImmXElhXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
		mPhotoList.add("http://img01.taobaocdn.com/bao/uploaded/i3/13215023521330093/T1BWuzXrhcXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");  
		mPhotoList.add("http://img01.taobaocdn.com/bao/uploaded/i4/13215035563144015/T1Q.eyXsldXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");  
		mPhotoList.add("http://img01.taobaocdn.com/bao/uploaded/i3/13215023749568975/T1UKWCXvpXXXXXXXXX_!!0-item_pic.jpg_230x230.jpg"); 
		mAbTaskQueue = AbTaskQueue.getInstance();
	    //获取ListView对象
        mAbPullListView = (AbPullListView)this.findViewById(R.id.mListView);
        
        //打开关闭下拉刷新加载更多功能
        mAbPullListView.setPullRefreshEnable(true); 
        mAbPullListView.setPullLoadEnable(true);
        
        //设置进度条的样式
        mAbPullListView.getHeaderView().setHeaderProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular));
        mAbPullListView.getFooterView().setFooterProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular));
        //mAbPullListView.getHeaderView().setHeaderProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular2));
        //mAbPullListView.getFooterView().setFooterProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular2));
         
        //ListView数据
    	list = new ArrayList<Map<String, Object>>();
    	
    	//使用自定义的Adapter
    	myListViewAdapter = new ImageListAdapter(this, list,R.layout.list_items,
				new String[] { "itemsIcon", "itemsTitle","itemsText" }, new int[] { R.id.itemsIcon,
						R.id.itemsTitle,R.id.itemsText });
    	mAbPullListView.setAdapter(myListViewAdapter);
    	//item被点击事件
    	mAbPullListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
    	});
    	
    	showProgressDialog();

    	//定义两种查询的事件
    	final AbTaskItem item1 = new AbTaskItem();
		item1.listener = new AbTaskListener() {

			@Override
			public void update() {
				removeProgressDialog();
				list.clear();
				if(newList!=null && newList.size()>0){
	                list.addAll(newList);
	                myListViewAdapter.notifyDataSetChanged();
	                newList.clear();
   		    	}
				mAbPullListView.stopRefresh();
			}

			@Override
			public void get() {
	   		    try {
	   		    	Thread.sleep(1000);
	   		    	currentPage = 1;
	   		    	newList = new ArrayList<Map<String, Object>>();
	   		    	Map<String, Object> map = null;
	   		    	
	   		    	for (int i = 0; i < 2; i++) {
	   		    		map = new HashMap<String, Object>();
	   					map.put("itemsIcon",mPhotoList.get(new Random().nextInt(mPhotoList.size())));
		   		    	map.put("itemsTitle", "item"+(i+1));
		   		    	map.put("itemsText", "item..."+(i+1));
		   		    	newList.add(map);
		   		    	
	   				}
	   		    } catch (Exception e) {
	   		    }
		  };
		};
		
		final AbTaskItem item2 = new AbTaskItem();
		item2.listener = new AbTaskListener() {

			@Override
			public void update() {
				if(newList!=null && newList.size()>0){
					list.addAll(newList);
					myListViewAdapter.notifyDataSetChanged();
					newList.clear();
                }
				mAbPullListView.stopLoadMore();
				
			}

			@Override
			public void get() {
	   		    try {
	   		    	currentPage++;
	   		    	Thread.sleep(1000);
	   		    	newList = new ArrayList<Map<String, Object>>();
	   		    	Map<String, Object> map = null;
	   		    	
	   		    	for (int i = 0; i < pageSize; i++) {
	   		    		map = new HashMap<String, Object>();
	   					map.put("itemsIcon",mPhotoList.get(new Random().nextInt(mPhotoList.size())));
		   		    	map.put("itemsTitle", "item上拉"+((currentPage-1)*pageSize+(i+1)));
		   		    	map.put("itemsText", "item上拉..."+((currentPage-1)*pageSize+(i+1)));
		   		    	if((list.size()+newList.size()) < total){
		   		    		newList.add(map);
		   		    	}
	   				}
	   		    	
	   		    } catch (Exception e) {
	   		    	currentPage--;
	   		    	newList.clear();
	   		    	showToastInThread(e.getMessage());
	   		    }
		  };
		};
		
		mAbPullListView.setAbOnListViewListener(new AbOnListViewListener(){

			@Override
			public void onRefresh() {
				mAbTaskQueue.execute(item1);
			}

			@Override
			public void onLoadMore() {
				mAbTaskQueue.execute(item2);
			}
			
		});
		
    	//第一次下载数据
		mAbTaskQueue.execute(item1);
		
    }
    

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	public void onPause() {
		super.onPause();
	}
	
   
}


