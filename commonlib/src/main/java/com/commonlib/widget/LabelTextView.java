package com.commonlib.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
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
    private Paint labelPaint;
    private float textHeight;

    private String labelTextTop;
    private String labelTextLeft;
    private String labelTextRight;
    private String labelTextButton;
    private float labelTextSize;
    private int labelTextColor;
    private float labelPadding;

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
        labelTextButton = typedArray.getString(R.styleable.LabelTextView_label_text_button);
        labelTextSize = typedArray.getDimension(R.styleable.LabelTextView_label_text_size, UIUtil.dip2px(getContext(), 14));
        labelTextColor = typedArray.getColor(R.styleable.LabelTextView_label_text_color, 0x333333);
        labelPadding = typedArray.getDimension(R.styleable.LabelTextView_label_padding, 0);

        typedArray.recycle();

        labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        labelPaint.setTextSize(labelTextSize);
        labelPaint.setColor(labelTextColor);
        Paint.FontMetrics fontMetrics = labelPaint.getFontMetrics();
        textHeight = fontMetrics.descent - fontMetrics.ascent;

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
            canvas.drawText(labelTextRight, (with - labelPaint.measureText(labelTextTop) - getPaddingRight()), getBaseline(), labelPaint);
        }
    }


    @Override
    public void setInputType(int type) {
        super.setInputType(type);
        invalidate();
    }
}
