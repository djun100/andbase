package com.andbase.web;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;

import com.ab.http.AbHttpListener;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbFileUtil;
import com.andbase.demo.model.Article;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class NetworkWeb {

	private AbHttpUtil mAbHttpUtil = null;
	private Context mContext = null;
	
	public NetworkWeb(Context context) {
		// 创建Http工具类
		mContext = context;
		mAbHttpUtil = AbHttpUtil.getInstance(context);
	}

	/**
	 * Create a new instance of SettingWeb.
	 */
	public static NetworkWeb newInstance(Context context) {
		NetworkWeb web = new NetworkWeb(context);
		return web;
	}

	/**
	 * 调用请求的模版
	 * @param param1  参数1
	 * @param param2 参数2
	 * @param abHttpListener 请求的监听器
	 */
	public void testHttpGet(String param1,String param2,final AbHttpListener abHttpListener){
        
		// 一个url地址
		String urlString = "http://www.amsoft.cn/rss.php";
		mAbHttpUtil.get(urlString, new AbStringHttpResponseListener(){

			@Override
			public void onSuccess(int statusCode, String content) {
				//将结果传递回去
				abHttpListener.onSuccess(content);
			}

			@Override
			public void onStart() {
				
			}

			@Override
			public void onFinish() {
				
			}

			@Override
			public void onFailure(int statusCode, String content,
					Throwable error) {
				//将失败错误信息传递回去
				abHttpListener.onFailure(content);
			}
			
		});
	}
	
	/**
	 * 调用一个列表请求
	 * @param param1  参数1
	 * @param param2 参数2
	 * @param abHttpListener 请求的监听器
	 */
	public void findLogList(String param1,String param2,final AbHttpListener abHttpListener){
		
		final String result = AbFileUtil.readAssetsByName(mContext, "article_list.json","UTF-8");
	    try {
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			// 解析返回头
			JSONObject jsonObject = new JSONObject(result);
			String resultCode = jsonObject.getString("resultCode");
			if ("200".equals(resultCode)) {
				//解析结果
				String list = jsonObject.getString("items");
				List<Article> mArticleList = gson.fromJson(list, new TypeToken<ArrayList<Article>>() {}.getType());
			    //将结果传递回去
				abHttpListener.onSuccess(mArticleList);
			} else {
				//服务器返回的出错消息
				String resultMessage = jsonObject.getString("resultMessage");
				//将错误信息传递回去
				abHttpListener.onFailure(resultMessage);
			}
		} catch (Exception e) {
			e.printStackTrace();
			abHttpListener.onFailure(e.getMessage());
		}
		
	}
	
	/**
	 * 调用一个列表请求
	 * @param param1  参数1
	 * @param param2 参数2
	 * @param abHttpListener 请求的监听器
	 */
	public void findLogList2(String param1,String param2,final AbHttpListener abHttpListener){
		
		final String result = AbFileUtil.readAssetsByName(mContext, "article_list.json","UTF-8");
		// 一个url地址
	    String urlString = "http://www.amsoft.cn/rss.php";
	    mAbHttpUtil.get(urlString, new AbStringHttpResponseListener(){

			@Override
			public void onSuccess(int statusCode, String content) {
				try {
					GsonBuilder builder = new GsonBuilder();
					Gson gson = builder.create();
					//模拟数据
					content = result;
					// 解析返回头
					JSONObject jsonObject = new JSONObject(content);
					String resultCode = jsonObject.getString("resultCode");
					if ("200".equals(resultCode)) {
						//解析结果
						String list = jsonObject.getString("items");
						List<Article> mArticleList = gson.fromJson(list, new TypeToken<ArrayList<Article>>() {}.getType());
					    //将结果传递回去
						abHttpListener.onSuccess(mArticleList);
					} else {
						//服务器返回的出错消息
						String resultMessage = jsonObject.getString("resultMessage");
						//将错误信息传递回去
						abHttpListener.onFailure(resultMessage);
					}
				} catch (Exception e) {
					e.printStackTrace();
					abHttpListener.onFailure(e.getMessage());
				}
			}

			@Override
			public void onStart() {
				//开始的状态传递回去
			}

			@Override
			public void onFinish() {
				//完成的状态传递回去
			}

			@Override
			public void onFailure(int statusCode, String content,
					Throwable error) {
				//将失败错误信息传递回去
				abHttpListener.onFailure(content);
			}
	    	
	    });
	}
	
	
	
}
