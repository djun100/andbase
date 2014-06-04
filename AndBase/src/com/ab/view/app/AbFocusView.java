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
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.LinearLayout;

public class AbFocusView extends LinearLayout implements OnFocusChangeListener{
    
    private Drawable selectorDrawable;
    private int selectorResourse;

    public AbFocusView(Context context){
        this(context, null);
    }

    public AbFocusView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public AbFocusView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs);
        
       /* TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.AbFocusView, defStyle, 0);
        selectorDrawable = a.getDrawable(R.styleable.AbFocusView_borderDrawable);
        a.recycle();
        if (this.selectorDrawable != null)
        {
            this.setBackgroundDrawable(selectorDrawable);
        }
        */
        /*<?xml version="1.0" encoding="utf-8"?>
        <resources>
            <!-- focus view -->
            <declare-styleable name="AbFocusView">
                <attr name="borderDrawable" format="reference" />
            </declare-styleable>

        </resources>*/

        setClickable(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setWillNotDraw(false);
        setOnFocusChangeListener(this);
        setDrawingCacheEnabled(true);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus){
        if (hasFocus){
        }else{
        }
    }

    public Drawable getSelectorDrawable(){
        return selectorDrawable;
    }

    public void setSelectorDrawable(Drawable selectorDrawable){
        this.selectorDrawable = selectorDrawable;
        if (this.selectorDrawable != null){
            this.setBackgroundDrawable(selectorDrawable);
        }
    }

    public int getSelectorResourse(){
        return selectorResourse;
    }

    public void setSelectorResourse(int selectorResourse){
        this.selectorResourse = selectorResourse;
        this.selectorDrawable = this.getResources().getDrawable(selectorResourse);
        if (this.selectorDrawable != null){
            this.setBackgroundDrawable(selectorDrawable);
        }
    }
    

}
