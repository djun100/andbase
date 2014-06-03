package com.andbase.friend;

import android.content.Context;

import com.ab.db.orm.dao.AbDBDaoImpl;
import com.andbase.db.DBSDHelper;
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * 名称：FriendDao.java 
 * 描述：用户信息
 * @author amsoft.cn
 * @date：2013-7-31 下午4:12:36
 * @version v1.0
 */
public class FriendDao extends AbDBDaoImpl<Friend> {
	public FriendDao(Context context) {
		super(new DBSDHelper(context),Friend.class);
	}
}
