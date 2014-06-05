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
package com.ab.global;

// TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn
 * 名称：AbConstant.java 
 * 描述：常量.
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-10-16 下午1:33:39
 */
public class AbConstant {
	
	/** SharePreferences文件名. */
	public static final String SHAREPATH = "app_share";
    
    /** 返回码：成功. */
    public static final int RESULRCODE_OK = 0;
    
    /** 返回码：失败. */
    public static final int RESULRCODE_ERROR = -1;
    
    /** 显示Toast. */
    public static final int SHOW_TOAST = 0;
    
    /** 显示进度框. */
    public static final int SHOW_PROGRESS = 1;
    
    /** 删除进度框. */
	public static final int REMOVE_PROGRESS = 2;
	
	/** 删除底部进度框. */
	public static final int REMOVE_DIALOGBOTTOM = 3;
	
	/** 删除中间进度框. */
	public static final int REMOVE_DIALOGCENTER = 4;
	
	/** 删除顶部进度框. */
	public static final int REMOVE_DIALOGTOP = 5;
	
	/** View的类型. */
	public static final int LISTVIEW = 1;
	
	/** The Constant GRIDVIEW. */
	public static final int GRIDVIEW = 1;
	
	/** The Constant GALLERYVIEW. */
	public static final int GALLERYVIEW = 2;
	
	/** The Constant RELATIVELAYOUTVIEW. */
	public static final int RELATIVELAYOUTVIEW = 3;
	
	/** Dialog的类型. */
	public static final int DIALOGPROGRESS = 0;
	
	/** 常量底部Dialog. */
	public static final int DIALOGBOTTOM = 1;
	
	/** 常量中间Dialog. */
	public static final int DIALOGCENTER = 2;
	
	/** 常量顶部Dialog. */
	public static final int DIALOGTOP = 3;
	
	/** 连接失败的HTTP返回码. */
	public static final int CONNECT_FAILURE_CODE = 600;
	/** 连接失败的HTTP返回码. */
	public static final int CONNECT_TIMEOUT_CODE = 601;
	/** 响应失败的HTTP返回码. */
	public static final int RESPONSE_TIMEOUT_CODE = 602;
	/** 未处理的HTTP返回码. */
	public static final int UNTREATED_CODE = 900;
	
	/** The Constant CONNECTEXCEPTION. */
	public static final String CONNECTEXCEPTION = "无法连接到网络";
	
	/** The Constant UNKNOWNHOSTEXCEPTION. */
	public static final String UNKNOWNHOSTEXCEPTION = "连接远程地址失败";
	
	/** The Constant SOCKETEXCEPTION. */
	public static final String SOCKETEXCEPTION = "网络连接出错，请重试";
	
	/** The Constant SOCKETTIMEOUTEXCEPTION. */
	public static final String SOCKETTIMEOUTEXCEPTION = "连接超时，请重试";
	
	/** The Constant NULLPOINTEREXCEPTION. */
	public static final String NULLPOINTEREXCEPTION = "抱歉，远程服务出错了";
	
	/** The Constant NULLMESSAGEEXCEPTION. */
	public static final String NULLMESSAGEEXCEPTION = "抱歉，程序出错了";
	
	/** The Constant CLIENTPROTOCOLEXCEPTION. */
	public static final String CLIENTPROTOCOLEXCEPTION = "Http请求参数错误";
	
	/** 参数个数不够. */
	public static final String MISSINGPARAMETERS = "参数没有包含足够的值";
   
}
