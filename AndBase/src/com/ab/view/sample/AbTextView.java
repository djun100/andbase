package com.ab.view.sample;


/**
 * 描述：中英混合乱换行问题 .
 *
 * @author amsoft.cn
 * @date：2013-5-17 下午6:46:29
 * @version v1.0
 */

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.FontMetrics;
import android.text.Layout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

public class AbTextView extends TextView {
    
    /**padding*/
    private float leftPadding = 0;
    private float topPadding = 0;
    private float rightPadding = 0;
    private float bottomPadding = 0;
    private float lineSpacing = 0;
    
    /**最大行数*/
    private int maxLines = 1;
    
    /**文字大小*/
    private float textSize = 14;
    
    /**文字颜色*/
    private int textColor = Color.WHITE;
    
    /**TextPaint*/
    private TextPaint mTextPaint = null;

    public AbTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTextPaint = this.getPaint();
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(textSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 清屏幕
        canvas.drawColor(Color.TRANSPARENT);
        drawText(canvas, this.getText().toString(), this.getWidth(), this.getPaint());
    }

    public void setPadding(float left,float top,float right,float bottom){
        leftPadding = left;
        topPadding = top;
        rightPadding = right;
        bottomPadding = bottom;
        this.invalidate();
    }


    public int subStringLength(String str, int maxPix, TextPaint paint) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        int currentIndex = 0;
        for (int i = 0; i < str.length(); i++) {
            // 获取一个字符
            String temp = str.substring(0, i + 1);
            float valueLength = paint.measureText(temp)+leftPadding+rightPadding;
            if (valueLength > maxPix) {
                currentIndex = i - 1;
                break;
            } else if (valueLength == maxPix) {
                currentIndex = i;
                break;
            }
        }
        // 短于最大像素返回最后一个字符位置
        if (currentIndex == 0) {
            currentIndex = str.length() - 1;
        }
        return currentIndex;
    }

    public float getStringWidth(String str, TextPaint paint) {
        float strWidth = paint.measureText(str);
        return strWidth;
    }

    public float getDesiredWidth(String str, TextPaint paint) {
        float strWidth = Layout.getDesiredWidth(str, paint);
        return strWidth;
    }

    public List<String> getDrawRowStr(String text, int maxWPix,
            TextPaint paint) {
        String[] texts = null;
        if (text.indexOf("\n") != -1) {
            texts = text.split("\n");
        } else {
            texts = new String[1];
            texts[0] = text;
        }
        // 共多少行
        List<String> mStrList = new ArrayList<String>();

        for (int i = 0; i < texts.length; i++) {
            String textLine = texts[i];
            // 计算这个文本显示为几行
            while (true) {
                // 可容纳的最后一个字的位置
                int endIndex = subStringLength(textLine, maxWPix, paint);
                if (endIndex <= 0) {
                    mStrList.add(textLine);
                } else {
                    if (endIndex == textLine.length() - 1) {
                        mStrList.add(textLine);
                    } else {
                        mStrList.add(textLine.substring(0, endIndex + 1));
                    }

                }
                // 获取剩下的
                if (textLine.length() > endIndex + 1) {
                    // 还有剩下的
                    textLine = textLine.substring(endIndex + 1);
                } else {
                    break;
                }
            }
        }

        return mStrList;
    }

    public int getDrawRowCount(String text, int maxWPix, TextPaint paint) {
        String[] texts = null;
        if (text.indexOf("\n") != -1) {
            texts = text.split("\n");
        } else {
            texts = new String[1];
            texts[0] = text;
        }
        // 共多少行
        List<String> mStrList = new ArrayList<String>();

        for (int i = 0; i < texts.length; i++) {
            String textLine = texts[i];
            // 计算这个文本显示为几行
            while (true) {
                // 可容纳的最后一个字的位置
                int endIndex = subStringLength(textLine, maxWPix, paint);
                if (endIndex <= 0) {
                    mStrList.add(textLine);
                } else {
                    if (endIndex == textLine.length() - 1) {
                        mStrList.add(textLine);
                    } else {
                        mStrList.add(textLine.substring(0, endIndex + 1));
                    }

                }
                // 获取剩下的
                if (textLine.length() > endIndex + 1) {
                    // 还有剩下的
                    textLine = textLine.substring(endIndex + 1);
                } else {
                    break;
                }
            }
        }

        return mStrList.size();
    }

    public int drawText(Canvas canvas, String text, int maxWPix,
            TextPaint paint) {
        if (TextUtils.isEmpty(text)) {
            return 1;
        }
        // 需要根据文字长度控制换行
        // 测量文字的长度
        List<String> mStrList = getDrawRowStr(text, maxWPix, paint);

        FontMetrics fm = paint.getFontMetrics();
        int hSize = (int)Math.ceil(fm.descent - fm.ascent);

        for (int i = 0; i < mStrList.size(); i++) {
            // 计算坐标
            float x = leftPadding;
            float y = topPadding+hSize/2+i*(hSize+lineSpacing)+bottomPadding;
            String textLine = mStrList.get(i);
            if(i < maxLines){
                canvas.drawText(textLine, x, y, paint);
            }
        }
        return mStrList.size();
    }


    @SuppressLint("Override")
    public int getMaxLines(){
        return maxLines;
    }

    @Override
    public void setMaxLines(int maxLines){
        this.maxLines = maxLines;
        this.invalidate();
    }
    
    public float getLineSpacing(){
        return lineSpacing;
    }

    public void setLineSpacing(float lineSpacing){
        this.lineSpacing = lineSpacing;
    }

    @Override
    public void setTextSize(float size) {
        this.textSize = size;
        Context c = getContext();
        Resources r;

        if (c == null)
            r = Resources.getSystem();
        else
            r = c.getResources();

        setRawTextSize(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, size, r.getDisplayMetrics()));
    }
    
    
    private void setRawTextSize(float size) {
        if (size != mTextPaint.getTextSize()) {
            mTextPaint.setTextSize(size);
            this.invalidate();
        }
    }

    public float getTextSize(){
        return textSize;
    }

    public int getTextColor(){
        return textColor;
    }

    @Override
    public void setTextColor(int textColor){
        this.textColor = textColor;
        mTextPaint.setColor(textColor);
        this.invalidate();
    }
    
    
}
