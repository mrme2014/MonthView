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
import android.widget.EditText;

import com.commonlib.R;
import com.commonlib.util.UIUtil;

/**
 * Created by abel on 16/8/12.
 */
public class LabelEditText extends EditText {

    private Paint labelPaint;
    private float textHeight;

    private String labelText;
    private int labelTextSize;
    private int labelTextColor;
    private float labelPadding;

    public LabelEditText(Context context) {
        super(context);
        init(null);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LabelEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public LabelEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LabelEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public void init(AttributeSet attrs) {

        setImeOptions(EditorInfo.IME_ACTION_NEXT);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LabelEditText);

        labelText = typedArray.getString(R.styleable.LabelEditText_label_text);
        labelTextSize = typedArray.getInt(R.styleable.LabelEditText_label_text_size, 14);
        labelTextColor = typedArray.getColor(R.styleable.LabelEditText_label_text_color, 0x333333);
        labelPadding = typedArray.getDimension(R.styleable.LabelEditText_label_padding, 0);

        typedArray.recycle();

        labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        labelPaint.setTextSize(UIUtil.dip2px(getContext(), labelTextSize));
        labelPaint.setColor(labelTextColor);
        Paint.FontMetrics fontMetrics = labelPaint.getFontMetrics();
        textHeight = fontMetrics.descent - fontMetrics.ascent;

        if (!TextUtils.isEmpty(labelText)) {
            int leftPadding = Math.max(getPaddingLeft(), (int) labelPaint.measureText(labelText));
            setPadding(leftPadding, getPaddingTop(), getPaddingRight(), getPaddingBottom());
        }
//        topOffset = UIUtil.dip2px(getContext(), 2.5f);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();

        if (!TextUtils.isEmpty(labelText)) {
            canvas.drawText(labelText, labelPadding, getBaseline(), labelPaint);
        }
    }


    @Override
    public void setInputType(int type) {
        super.setInputType(type);
        invalidate();
    }
}

