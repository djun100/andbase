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
package com.ab.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ab.global.AbAppData;

// TODO: Auto-generated Javadoc
/**
 * © 2012 amsoft.cn
 * 名称：AbViewUtil.java 
 * 描述：View工具类.
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-01-17 下午11:52:13
 */

public class AbViewUtil {
    
    /** 日志标记. */
    private static String TAG = "AbViewUtil";
    
    /** 日志标记开关. */
    private static final boolean D = AbAppData.DEBUG;
    
    /**
     * 无效值
     */
    public static final int INVALID = Integer.MIN_VALUE;

	/**
	 * 描述：重置AbsListView的高度. item 的最外层布局要用
	 * RelativeLayout,如果计算的不准，就为RelativeLayout指定一个高度
	 * 
	 * @param absListView
	 *            the abs list view
	 * @param lineNumber
	 *            每行几个 ListView一行一个item
	 * @param verticalSpace
	 *            the vertical space
	 */
	public static void setAbsListViewHeight(AbsListView absListView,
			int lineNumber, int verticalSpace) {

		int totalHeight = getAbsListViewHeight(absListView, lineNumber,
				verticalSpace);
		ViewGroup.LayoutParams params = absListView.getLayoutParams();
		params.height = totalHeight;
		((MarginLayoutParams) params).setMargins(0, 0, 0, 0);
		absListView.setLayoutParams(params);
	}

	/**
	 * 描述：获取AbsListView的高度.
	 *
	 * @param absListView            the abs list view
	 * @param lineNumber            每行几个 ListView一行一个item
	 * @param verticalSpace            the vertical space
	 * @return the abs list view height
	 */
	public static int getAbsListViewHeight(AbsListView absListView,
			int lineNumber, int verticalSpace) {
		int totalHeight = 0;
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		absListView.measure(w, h);
		ListAdapter mListAdapter = absListView.getAdapter();
		if (mListAdapter == null) {
			return totalHeight;
		}

		int count = mListAdapter.getCount();
		if (absListView instanceof ListView) {
			for (int i = 0; i < count; i++) {
				View listItem = mListAdapter.getView(i, null, absListView);
				listItem.measure(w, h);
				totalHeight += listItem.getMeasuredHeight();
			}
			if (count == 0) {
				totalHeight = verticalSpace;
			} else {
				totalHeight = totalHeight
						+ (((ListView) absListView).getDividerHeight() * (count - 1));
			}

		} else if (absListView instanceof GridView) {
			int remain = count % lineNumber;
			if (remain > 0) {
				remain = 1;
			}
			if (mListAdapter.getCount() == 0) {
				totalHeight = verticalSpace;
			} else {
				View listItem = mListAdapter.getView(0, null, absListView);
				listItem.measure(w, h);
				int line = count / lineNumber + remain;
				totalHeight = line * listItem.getMeasuredHeight() + (line - 1)
						* verticalSpace;
			}

		}
		return totalHeight;

	}

	/**
	 * 测量这个view，最后通过getMeasuredWidth()获取宽度和高度.
	 * 
	 * @param v
	 *            要测量的view
	 * @return 测量过的view
	 */
	public static void measureView(View v) {
		ViewGroup.LayoutParams p = v.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		v.measure(childWidthSpec, childHeightSpec);
	}

	
	/**
     * 描述：dip转换为px.
     *
     * @param context the context
     * @param dipValue the dip value
     * @return px值
     */
    public static float dip2px(Context context, float dipValue) {
        DisplayMetrics mDisplayMetrics = AbAppUtil.getDisplayMetrics(context);
        return applyDimension(TypedValue.COMPLEX_UNIT_DIP,dipValue,mDisplayMetrics);
    }

    /**
     * 描述：px转换为dip.
     *
     * @param context the context
     * @param pxValue the px value
     * @return dip值
     */
    public static float px2dip(Context context, float pxValue) {
        DisplayMetrics mDisplayMetrics = AbAppUtil.getDisplayMetrics(context);
        return pxValue / mDisplayMetrics.density;
    }
    
    /**
     * 描述：sp转换为px.
     *
     * @param context the context
     * @param spValue the sp value
     * @return sp值
     */
    public static float sp2px(Context context, float spValue) {
        DisplayMetrics mDisplayMetrics = AbAppUtil.getDisplayMetrics(context);
        return applyDimension(TypedValue.COMPLEX_UNIT_SP,spValue,mDisplayMetrics);
    }
    
    /**
     * 描述：px转换为sp.
     *
     * @param context the context
     * @param spValue the sp value
     * @return sp值
     */
    public static float px2sp(Context context, float pxValue) {
        DisplayMetrics mDisplayMetrics = AbAppUtil.getDisplayMetrics(context);
        return pxValue / mDisplayMetrics.scaledDensity;
    }

	/**
	 * 描述：根据屏幕大小缩放.
	 *
	 * @param context the context
	 * @param pxValue the px value
	 * @return the int
	 */
	public static int resize(Context context, float value) {
		DisplayMetrics mDisplayMetrics = AbAppUtil.getDisplayMetrics(context);
		return resize(mDisplayMetrics.widthPixels,
				mDisplayMetrics.heightPixels, value);
	}

	/**
	 * 描述：根据屏幕大小缩放.
	 *
	 * @param displayWidth the display width
	 * @param displayHeight the display height
	 * @param pxValue the px value
	 * @return the int
	 */
	public static int resize(int displayWidth, int displayHeight, float pxValue) {
		float scale = 1;
		try {
			float scaleWidth = (float) displayWidth / AbAppData.uiWidth;
			float scaleHeight = (float) displayHeight / AbAppData.uiHeight;
			scale = Math.min(scaleWidth, scaleHeight);
		} catch (Exception e) {
		}
		return Math.round(pxValue * scale);
	}

	/**
	 * 适配大小.
	 *
	 * @param context the context
	 * @param unit TypedValue.COMPLEX_UNIT_DIP
	 * @param value 单位值
	 * @return the px size
	 */
	public static float getPXValue(Context context,int unit,float value) {
	    DisplayMetrics mDisplayMetrics = AbAppUtil.getDisplayMetrics(context);
		return TypedValue.applyDimension(unit, value,mDisplayMetrics);
	}

	/**
     * 适配大小.
     *
     * @param unit TypedValue.COMPLEX_UNIT_DIP
     * @param value 单位值
     * @return the px size
     */
	public static float getPXValue(int unit,float value) {
		Resources mResources = Resources.getSystem();
		return TypedValue.applyDimension(unit, value,
				mResources.getDisplayMetrics());
	}
	
	/**
	 * TypedValue官方源码中的算法，任意单位转换为PX单位
	 * @param unit  TypedValue.COMPLEX_UNIT_DIP
	 * @param value 对应单位的值
	 * @param metrics 密度
	 * @return px值
	 */
    public static float applyDimension(int unit, float value,
                                       DisplayMetrics metrics){
        switch (unit) {
        case TypedValue.COMPLEX_UNIT_PX:
            return value;
        case TypedValue.COMPLEX_UNIT_DIP:
            return value * metrics.density;
        case TypedValue.COMPLEX_UNIT_SP:
            return value * metrics.scaledDensity;
        case TypedValue.COMPLEX_UNIT_PT:
            return value * metrics.xdpi * (1.0f/72);
        case TypedValue.COMPLEX_UNIT_IN:
            return value * metrics.xdpi;
        case TypedValue.COMPLEX_UNIT_MM:
            return value * metrics.xdpi * (1.0f/25.4f);
        }
        return 0;
    }
    
    /**
     * 按比例缩放View，布局中的尺寸需要设置为px单位
     * @param view
     */
    public static void initPXView(View view){
        if (view instanceof TextView){
            TextView textView = (TextView) view;
            setPXTextSize(textView,textView.getTextSize());
        }

        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) view.getLayoutParams();
        if (null != params){
            int width = INVALID;
            int height = INVALID;
            if (params.width != ViewGroup.LayoutParams.WRAP_CONTENT
                && params.width != ViewGroup.LayoutParams.FILL_PARENT){
                width = params.width;
            }

            if (params.height != ViewGroup.LayoutParams.WRAP_CONTENT
                && params.height != ViewGroup.LayoutParams.FILL_PARENT){
                height = params.height;
            }
            
            //size
            setPXViewSize(view,width,height);

            // Padding
            setPXPadding(view,view.getPaddingLeft(),view.getPaddingTop(),view.getPaddingRight(),view.getPaddingBottom());
        }
        
        // Margin
        if(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams){
            ViewGroup.MarginLayoutParams mMarginLayoutParams = (ViewGroup.MarginLayoutParams) view
                    .getLayoutParams();
            if (mMarginLayoutParams != null){
                setPXMargin(view,mMarginLayoutParams.leftMargin,mMarginLayoutParams.topMargin,mMarginLayoutParams.rightMargin,mMarginLayoutParams.bottomMargin);
            }
        }
       
    }
    
    /**
     * 设置px尺寸文字大小
     * @param textView button
     * @param pxSize
     * @return
     */
    public static void setPXTextSize(TextView textView,float pxSize) {
        
        int size = resize(textView.getContext(),pxSize);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,size);
        //等价于
        //int size = resize(textView.getContext(), px2sp(textView.getContext(),pxSize));
        //textView.setTextSize(size);
    }
    
   /**
    * 设置View的PX尺寸
    * @param view  如果是代码new出来的View，需要设置一个适合的LayoutParams
    * @param widthPixels
    * @param heightPixels
    */
    public static void setPXViewSize(View view,int widthPixels, int heightPixels){
        int paramWidth = resize(view.getContext(), widthPixels);
        int paramHeight = resize(view.getContext(), heightPixels);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if(params == null){
            Log.e(TAG, "setPXViewSize出错,如果是代码new出来的View，需要设置一个适合的LayoutParams");
            return;
        }
        if (widthPixels != INVALID){
            params.width = paramWidth;
        }
        if (heightPixels != INVALID){
            params.height = paramHeight;
        }
        view.setLayoutParams(params);
    }

	/**
	 * 设置PX padding.
	 *
	 * @param view the view
	 * @param left the left padding in pixels
     * @param top the top padding in pixels
     * @param right the right padding in pixels
     * @param bottom the bottom padding in pixels
	 */
	public static void setPXPadding(View view, int left,
			int top, int right, int bottom) {
		int paramLeft = resize(view.getContext(), left);
		int paramTop = resize(view.getContext(), top);
		int paramRight = resize(view.getContext(), right);
		int paramBottom = resize(view.getContext(), bottom);
		view.setPadding(paramLeft, paramTop, paramRight, paramBottom);
	}

	/**
	 * 设置 PX margin.
	 * 
	 * @param view the view
	 * @param left the left margin in pixels
	 * @param top the top margin in pixels
	 * @param right the right margin in pixels
	 * @param bottom the bottom margin in pixels
	 */
	public static void setPXMargin(View view, int left, int top,
			int right, int bottom) {
		int paramLeft = resize(view.getContext(), left);
		int paramTop = resize(view.getContext(), top);
		int paramRight = resize(view.getContext(), right);
		int paramBottom = resize(view.getContext(), bottom);
		
		if(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams){
            ViewGroup.MarginLayoutParams mMarginLayoutParams = (ViewGroup.MarginLayoutParams) view
                    .getLayoutParams();
            if (mMarginLayoutParams != null){
                if (left != INVALID) {
                    mMarginLayoutParams.leftMargin = paramLeft;
                }
                if (right != INVALID) {
                    mMarginLayoutParams.rightMargin = paramRight;
                }
                if (top != INVALID) {
                    mMarginLayoutParams.topMargin = paramTop;
                }
                if (bottom != INVALID) {
                    mMarginLayoutParams.bottomMargin = paramBottom;
                }
                view.setLayoutParams(mMarginLayoutParams);
            }
        }
		
	}

}
