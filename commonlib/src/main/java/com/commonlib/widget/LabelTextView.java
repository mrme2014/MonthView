package com.commonlib.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.commonlib.R;
import com.commonlib.util.UIUtil;

/**
 * Created by abel on 16/8/15.
 */
public class LabelTextView extends TextView {
    private Paint labelPaint,linePaint;
    private float textHeight;

    private String labelTextTop;
    private String labelTextLeft;
    private String labelTextRight;
    private String labelTextBottom;
    private float labelTextSize;
    private int labelTextColor;
    private float labelPadding;
    private boolean draw_bottom_line;
    private int bottom_line_color;

    public LabelTextView(Context context) {
        super(context);
        init(null);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LabelTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public LabelTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LabelTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public void init(AttributeSet attrs) {

        setImeOptions(EditorInfo.IME_ACTION_NEXT);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LabelTextView);

        labelTextTop = typedArray.getString(R.styleable.LabelTextView_label_text_top);
        labelTextLeft = typedArray.getString(R.styleable.LabelTextView_label_text_left);
        labelTextRight = typedArray.getString(R.styleable.LabelTextView_label_text_right);
        labelTextBottom = typedArray.getString(R.styleable.LabelTextView_label_text_bottom);
        labelTextSize = typedArray.getDimension(R.styleable.LabelTextView_label_text_size, UIUtil.dip2px(getContext(), 14));
        labelTextColor = typedArray.getColor(R.styleable.LabelTextView_label_text_color, 0xFF333333);
        labelPadding = typedArray.getDimension(R.styleable.LabelTextView_label_padding, 0);

        draw_bottom_line = typedArray.getBoolean(R.styleable.LabelTextView_draw_bottom_line ,false);
        bottom_line_color = typedArray.getColor(R.styleable.LabelTextView_bottom_line_color, 0);

        typedArray.recycle();

        labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        labelPaint.setTextSize(labelTextSize);
        labelPaint.setColor(labelTextColor);
        Paint.FontMetrics fontMetrics = labelPaint.getFontMetrics();
        textHeight = fontMetrics.descent - fontMetrics.ascent;

        if (draw_bottom_line){
            linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            linePaint.setStyle(Paint.Style.FILL);
            if(bottom_line_color==0)bottom_line_color = Color.parseColor("#d9dadd");
            linePaint.setColor(bottom_line_color);
            linePaint.setStrokeWidth(2);

        }
//        if (!TextUtils.isEmpty(labelTextTop)) {
//            int leftPadding = Math.max(getPaddingLeft(), (int) labelPaint.measureText(labelTextTop));
//            setPadding(leftPadding, getPaddingTop(), getPaddingRight(), getPaddingBottom());
//        }
//        topOffset = UIUtil.dip2px(getContext(), 2.5f);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int with = getWidth();
        int height = getHeight();

        if (!TextUtils.isEmpty(labelTextLeft)) {
            canvas.drawText(labelTextLeft, labelPadding, getBaseline(), labelPaint);
        }

        if (!TextUtils.isEmpty(labelTextTop)) {
            canvas.drawText(labelTextTop, (with - labelPaint.measureText(labelTextTop)) / 2, labelPadding + textHeight, labelPaint);
        }
        if (!TextUtils.isEmpty(labelTextRight)) {
            canvas.drawText(labelTextRight, (with - labelPaint.measureText(labelTextRight) - labelPadding), getBaseline(), labelPaint);
        }

        if (!TextUtils.isEmpty(labelTextBottom)) {
            canvas.drawText(labelTextBottom, (with - labelPaint.measureText(labelTextBottom)) / 2, height - labelPadding, labelPaint);
        }

        /*绘制下划线 */
        if(draw_bottom_line)
            canvas.drawLine(labelPadding,getMeasuredHeight(),getMeasuredWidth(),getMeasuredHeight(),linePaint);
    }


    @Override
    public void setInputType(int type) {
        super.setInputType(type);
        invalidate();
    }
}
