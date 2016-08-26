package com.commonlib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.commonlib.R;
import com.commonlib.util.UIUtil;

import java.lang.reflect.Field;

/**
 * Created by abel on 16/8/12.
 */
public class CountEditText extends EditText {

    private Paint labelPaint;
    private float textHeight;

    private float CountTextSize;
    private int CountTextColor;
    private int mMaxLength;

    public CountEditText(Context context) {
        super(context);
        init(null);
    }


    public CountEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public CountEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public void init(AttributeSet attrs) {

        setImeOptions(EditorInfo.IME_ACTION_NEXT);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CountEditText);

        CountTextSize = typedArray.getDimension(R.styleable.CountEditText_count_text_size, UIUtil.dip2px(getContext(), 12));
        CountTextColor = typedArray.getColor(R.styleable.CountEditText_count_text_color, 0x333333);

        typedArray.recycle();

        labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        labelPaint.setTextSize(CountTextSize);
        labelPaint.setColor(CountTextColor);
        Paint.FontMetrics fontMetrics = labelPaint.getFontMetrics();
        textHeight = fontMetrics.descent - fontMetrics.ascent;

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mMaxLength = getMaxLength() - editable.length();
            }
        });

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();
        canvas.drawText(mMaxLength + "", w - labelPaint.measureText(mMaxLength + ""), h - textHeight, labelPaint);
    }


    @Override
    public void setInputType(int type) {
        super.setInputType(type);
        invalidate();
    }

    public int getMaxLength() {
        int length = 0;
        try {
            InputFilter[] inputFilters = getFilters();
            for (InputFilter filter : inputFilters) {
                Class<?> c = filter.getClass();
                if (c.getName().equals("android.text.InputFilter$LengthFilter")) {
                    Field[] f = c.getDeclaredFields();
                    for (Field field : f) {
                        if (field.getName().equals("mMax")) {
                            field.setAccessible(true);
                            length = (Integer) field.get(filter);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return length;
    }
}

