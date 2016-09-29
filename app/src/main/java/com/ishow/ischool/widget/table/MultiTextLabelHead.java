package com.ishow.ischool.widget.table;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.commonlib.util.UIUtil;
import com.ishow.ischool.R;


/**
 * Created by mini on 16/9/26.
 */

public class MultiTextLabelHead extends TextView {

    private String topTextStr, bottomTextStr1, bottomTextStr2, bottomTextStr3, centerTextStr;
    private Paint linePaint, topTextPaint, bottomTextPaint;

    public MultiTextLabelHead(Context context) {
        super(context);
    }

    public MultiTextLabelHead(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MultiTextLabelHead(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    void init(AttributeSet attrs) {
        setWillNotDraw(false);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.mutilTextViewHead);

        topTextStr = typedArray.getString(R.styleable.mutilTextViewHead_mutiltv_top_text);
        bottomTextStr1 = typedArray.getString(R.styleable.mutilTextViewHead_mutiltv_bottom1_text);
        bottomTextStr2 = typedArray.getString(R.styleable.mutilTextViewHead_mutiltv_bottom2_text);
        bottomTextStr3 = typedArray.getString(R.styleable.mutilTextViewHead_mutiltv_bottom3_text);
        centerTextStr = typedArray.getString(R.styleable.mutilTextViewHead_mutiltv_only_center_text);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(getContext().getResources().getColor(R.color.comm_line));
        linePaint.setStrokeWidth(2);

        topTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        topTextPaint.setTextSize(UIUtil.sp2px(getContext(), 15));
        topTextPaint.setColor(getContext().getResources().getColor(R.color.txt_3));

        bottomTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bottomTextPaint.setTextSize(UIUtil.sp2px(getContext(), 13));
        bottomTextPaint.setColor(getContext().getResources().getColor(R.color.txt_3));

        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int minWidth = UIUtil.dip2px(getContext(), 120);
//        if (centerTextStr != null && !centerTextStr.equals("")) {
//            minWidth = UIUtil.dip2px(getContext(), 100);
//        }
        // 在wrap_content的情况下默认长度为150dp,高度为80dp
        int minHeight = UIUtil.dip2px(getContext(), 70);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(minWidth, minHeight);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(minWidth, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, minHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float height = getMeasuredHeight();
        canvas.drawLine(0, 0, 0, height, linePaint);

        if (centerTextStr != null && !centerTextStr.equals("")) {
            float centerHeight = height / 2 - (Math.abs(bottomTextPaint.ascent()) - Math.abs(bottomTextPaint.descent())) / 2;       // baseline的位置
            canvas.drawText(centerTextStr, getMeasuredWidth() / 2 - bottomTextPaint.measureText(centerTextStr) / 2, centerHeight, bottomTextPaint);
        } else {
            canvas.drawText(topTextStr, getMeasuredWidth() / 2 - topTextPaint.measureText(topTextStr) / 2, height / 2 - topTextPaint.getFontMetrics().descent, topTextPaint);
            float bottomTextMargin = ((getMeasuredWidth() - getPaddingLeft() - getPaddingRight()) / 3 - bottomTextPaint.measureText(bottomTextStr1)) / 2;
            float itemWidth = (getMeasuredWidth() - getPaddingLeft() - getPaddingRight()) / 3;
            float bottomHeight = height / 4 * 3 - (Math.abs(bottomTextPaint.ascent()) - Math.abs(bottomTextPaint.descent())) / 2;
            canvas.drawText(bottomTextStr1, getPaddingLeft() + bottomTextMargin, bottomHeight, bottomTextPaint);
            canvas.drawText(bottomTextStr2, getPaddingLeft() + bottomTextMargin + itemWidth, bottomHeight, bottomTextPaint);
            canvas.drawText(bottomTextStr3, getPaddingLeft() + bottomTextMargin + itemWidth * 2, bottomHeight, bottomTextPaint);
        }
    }

    void setTopTextStr(String str) {
        topTextStr = str;
        invalidate();
    }

    void setBottomTextStr1(String str) {
        bottomTextStr1 = str;
        invalidate();
    }

    void setBottomTextStr2(String str) {
        bottomTextStr2 = str;
        invalidate();
    }

    void setBottomTextStr3(String str) {
        bottomTextStr3 = str;
        invalidate();
    }

    void setCenterTextStr(String str) {
        centerTextStr = str;
        invalidate();
    }
}
