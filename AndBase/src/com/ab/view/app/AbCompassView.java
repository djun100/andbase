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
package com.ab.view.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
/**
 * 
 * © 2012 amsoft.cn
 * 名称：AbCompassView.java 
 * 描述：指南针
 * @author 还如一梦中
 * @date：2013-8-23 下午2:03:29
 * @version v1.0
 */
public class AbCompassView extends View {
    private Drawable mCompassDrawable = null;
    private int w = 40;
    private int h = 40;
    private float mDirection = 0.0f;
    private float posCompassX = 20;
    private float posCompassY = 20;
	
    public AbCompassView(Context context) {
        super(context);
        
    }

	public AbCompassView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override 
    protected void onDraw(Canvas canvas) {
        w = canvas.getWidth();
        h = canvas.getHeight();
        posCompassX = w/2;
        posCompassY = h/2;
        drawPictures(canvas);
    }
	
	private void drawPictures(Canvas canvas) {
		if (mCompassDrawable != null) {
			// 图片资源在view的位置，此处相当于充满view
			mCompassDrawable.setBounds(0, 0, w, h);
			canvas.save();
			// 绕图片中心点旋转
			canvas.rotate(mDirection, posCompassX, posCompassY);
			// 把旋转后的图片画在view上，即保持旋转后的样子
			mCompassDrawable.draw(canvas);
			// 保存一下
			canvas.restore();
		}

    }

	@Override  
	protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec){   
	    int height = View.MeasureSpec.getSize(heightMeasureSpec);    
	    int width = View.MeasureSpec.getSize(widthMeasureSpec);    
	    setMeasuredDimension(width,height);
	}  


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

	public float getDirection() {
		return mDirection;
	}

	public void setDirection(float direction) {
		this.mDirection = direction;
		this.invalidate();
	}

	/**
	 * 
	 * 描述：设置指南针图片
	 * @param drawable 必须设置才能显示
	 * @throws 
	 */
	public void setCompassDrawable(Drawable drawable) {
		this.mCompassDrawable = drawable;
	}
	
    
}
