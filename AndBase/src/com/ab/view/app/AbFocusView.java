package com.ab.view.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.LinearLayout;

import com.ab.R;

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
        
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.AbFocusView, defStyle, 0);
        selectorDrawable = a.getDrawable(R.styleable.AbFocusView_borderDrawable);
        a.recycle();
        if (this.selectorDrawable != null)
        {
            this.setBackgroundDrawable(selectorDrawable);
        }

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
