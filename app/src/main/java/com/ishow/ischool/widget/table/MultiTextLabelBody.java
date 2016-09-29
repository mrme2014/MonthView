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

public class MultiTextLabelBody extends TextView {

    private String leftextStr, centerTextStr, rightTextStr;
    private Paint linePaint, textPaint;

    public MultiTextLabelBody(Context context) {
        super(context);
    }

    public MultiTextLabelBody(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MultiTextLabelBody(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.mutilTextViewBody);

        leftextStr = typedArray.getString(R.styleable.mutilTextViewBody_mutiltv_left_text);
        centerTextStr = typedArray.getString(R.styleable.mutilTextViewBody_mutiltv_center_text);
        rightTextStr = typedArray.getString(R.styleable.mutilTextViewBody_mutiltv_right_text);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(getContext().getResources().getColor(R.color.comm_line));
        linePaint.setStrokeWidth(2);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(UIUtil.dip2px(getContext(), 13));
        textPaint.setColor(getContext().getResources().getColor(R.color.txt_6));

        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 在wrap_content的情况下默认长度为100dp,高度为80dp
        int minWidth = UIUtil.dip2px(getContext(), 120);
        int minHeight = UIUtil.dip2px(getContext(), 40);
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

        float oneThird = (getMeasuredWidth() - getPaddingLeft() - getPaddingRight()) / 3;
        float baselineHeight = height / 2 + (Math.abs(textPaint.ascent()) - Math.abs(textPaint.descent())) / 2;
        if (leftextStr != null && !leftextStr.equals("")) {
            canvas.drawText(leftextStr, getPaddingLeft() + (oneThird - textPaint.measureText(leftextStr)) / 2, baselineHeight, textPaint);
            canvas.drawText(centerTextStr, getPaddingLeft() + (oneThird - textPaint.measureText(centerTextStr)) / 2 + oneThird, baselineHeight, textPaint);
            canvas.drawText(rightTextStr, getPaddingLeft() + (oneThird - textPaint.measureText(rightTextStr)) / 2 + oneThird * 2, baselineHeight, textPaint);
        } else {
            canvas.drawText(centerTextStr, getMeasuredWidth() / 2 - textPaint.measureText(centerTextStr) / 2, baselineHeight, textPaint);
        }
    }

    void setLeftextStr(String str) {
        leftextStr = str;
        invalidate();
    }

    void setCenterTextStr(String str) {
        centerTextStr = str;
        invalidate();
    }

    void setRightTextStr(String str) {
        rightTextStr = str;
        invalidate();
    }
}
