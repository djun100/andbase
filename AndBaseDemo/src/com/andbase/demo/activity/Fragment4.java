
package com.andbase.demo.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListListener;
import com.ab.task.AbTaskQueue;
import com.ab.view.listener.AbOnListViewListener;
import com.ab.view.pullview.AbPullListView;
import com.andbase.R;
import com.andbase.demo.adapter.ImageListAdapter;
import com.andbase.global.Constant;
import com.andbase.global.MyApplication;


public class Fragment4 extends Fragment {
	
	private MyApplication application;
	private Activity mActivity = null;
	private List<Map<String, Object>> list = null;
	private AbPullListView mAbPullListView = null;
	private int currentPage = 1;
	private AbTaskQueue mAbTaskQueue = null;
	private ArrayList<String> mPhotoList = new ArrayList<String>();
	private ImageListAdapter myListViewAdapter = null;
	private int total = 50;
    private int pageSize = 5;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) { 
		 mActivity = this.getActivity();
		 application = (MyApplication) mActivity.getApplication();
		 
		 View view = inflater.inflate(R.layout.pull_list, null);
		 for (int i = 0; i < 22; i++) {
	        	mPhotoList.add(Constant.BASEURL+"content/templates/lanye/images/rand/"+i+".jpg");
		 }
		 mAbTaskQueue = AbTaskQueue.getInstance();
	     //获取ListView对象
         mAbPullListView = (AbPullListView)view.findViewById(R.id.mListView);
         //设置进度条的样式
         mAbPullListView.getHeaderView().setHeaderProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular));
         mAbPullListView.getFooterView().setFooterProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular));
         //ListView数据
    	 list = new ArrayList<Map<String, Object>>();
    	
    	 //使用自定义的Adapter
    	 myListViewAdapter = new ImageListAdapter(mActivity, list,R.layout.list_items,
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

		 return view;
	} 
	
	

	@Override
	public void onStart() {
		super.onStart();
		//定义两种查询的事件
        final AbTaskItem item1 = new AbTaskItem();
        item1.setListener(new AbTaskListListener() {
            
            @Override
            public List<?> getList(){
                List<Map<String, Object>> newList = null;
                try {
                    Thread.sleep(1000);
                    currentPage = 1;
                    newList = new ArrayList<Map<String, Object>>();
                    Map<String, Object> map = null;
                    
                    for (int i = 0; i < pageSize; i++) {
                        map = new HashMap<String, Object>();
                        map.put("itemsIcon",mPhotoList.get(new Random().nextInt(mPhotoList.size())));
                        map.put("itemsTitle", "item"+(i+1));
                        map.put("itemsText", "item..."+(i+1));
                        newList.add(map);
                        
                    }
                } catch (Exception e) {
                }
                return newList;
            }

            @Override
            public void update(List<?> paramList){
                List<Map<String, Object>> newList = (List<Map<String, Object>>)paramList;
                list.clear();
                if(newList!=null && newList.size()>0){
                    list.addAll(newList);
                    myListViewAdapter.notifyDataSetChanged();
                    newList.clear();
                }
                mAbPullListView.stopRefresh();
                
            }

            @Override
            public void update() {
                
            }

            @Override
            public void get() {
               
          };
        });
        
        final AbTaskItem item2 = new AbTaskItem();
        item2.setListener(new AbTaskListListener() {

            @Override
            public void update(List<?> paramList){
                List<Map<String, Object>> newList = (List<Map<String, Object>>)paramList;
                if(newList!=null && newList.size()>0){
                    list.addAll(newList);
                    myListViewAdapter.notifyDataSetChanged();
                    newList.clear();
                }
                mAbPullListView.stopLoadMore();
                
            }

            @Override
            public List<?> getList(){
                List<Map<String, Object>> newList = null;
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
                }
                return newList;
          };
        });
		
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



	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

}

