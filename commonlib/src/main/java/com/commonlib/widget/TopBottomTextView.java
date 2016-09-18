package com.commonlib.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.TextView;

import com.commonlib.R;
import com.commonlib.util.UIUtil;

/**
 * Created by MrS on 2016/8/17.
 */
public class TopBottomTextView extends TextView {
    private String labelTextTop;
    private String labelTextBottom;
    private int labelTextTopSize;
    private int labelTextTopColor = Color.parseColor("#222222");
    private float labelTopPadding;
    private int labelTextBottomColor = Color.parseColor("#999999");
    private int labelTextBottomSize;

    private Paint topPaint, btmPaint,strongPaint;
    private float labelTBPadding;
    private float topTxtHei;
    private float botTxtHei;
    private int labelleftPadding;
    private String[] strongSectxt;

    private int right_eclipse_padding = UIUtil.dip2px(getContext(),30);
    public TopBottomTextView(Context context) {
        super(context);
        init(null);
    }

    public TopBottomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TopBottomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TopBottomTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TopBottomTextView);

        labelTextTop = typedArray.getString(R.styleable.TopBottomTextView_label_text_top_str);

        labelTextTopSize = typedArray.getInt(R.styleable.TopBottomTextView_label_text_top_size, 14);
        labelTextTopSize = UIUtil.sp2px(getContext(), labelTextTopSize);
        labelTextTopColor = typedArray.getColor(R.styleable.TopBottomTextView_label_text_top_color, labelTextTopColor);


        labelTextBottom = typedArray.getString(R.styleable.TopBottomTextView_label_text_bottom);
        labelTextBottomSize = typedArray.getInt(R.styleable.TopBottomTextView_label_text_bottom_size, 12);
        labelTextBottomSize = UIUtil.sp2px(getContext(), labelTextBottomSize);

        labelTextBottomColor = typedArray.getColor(R.styleable.TopBottomTextView_label_text_bottom_color, labelTextBottomColor);

        labelTopPadding = typedArray.getInteger(R.styleable.TopBottomTextView_label_top_padding, 0);
        labelTopPadding = UIUtil.dip2px(getContext(), labelTopPadding);

        labelTBPadding = typedArray.getInteger(R.styleable.TopBottomTextView_label_TB_padding, 0);
        labelTBPadding = UIUtil.dip2px(getContext(), labelTBPadding);

        labelleftPadding = typedArray.getInteger(R.styleable.TopBottomTextView_label_left_padding, 0);
        labelleftPadding = UIUtil.dip2px(getContext(), labelleftPadding);

        topPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        topPaint.setTextSize(labelTextTopSize);
        topPaint.setColor(labelTextTopColor);

        btmPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        btmPaint.setTextSize(labelTextBottomSize);
        btmPaint.setColor(labelTextBottomColor);

        Paint.FontMetrics btmPaintFontMetrics = btmPaint.getFontMetrics();
        botTxtHei = btmPaintFontMetrics.descent - btmPaintFontMetrics.ascent;

        Paint.FontMetrics fontMetrics = topPaint.getFontMetrics();
        topTxtHei = fontMetrics.descent - fontMetrics.ascent;

        typedArray.recycle();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (labelTextTop!=null)canvas.drawText(labelTextTop, labelleftPadding, labelTopPadding, topPaint);

        if (labelTextBottom!=null)canvas.drawText(labelTextBottom, labelleftPadding, labelTopPadding + topTxtHei + labelTBPadding, btmPaint);

        if (strongSectxt!=null){
            int strLeft = labelleftPadding;
            for (int i = 0; i <strongSectxt.length ; i++) {
                if (i>0) {
                     strLeft = (int) (strLeft+ btmPaint.measureText(strongSectxt[i-1])+(i%2==1?10:20));
                }
                if (strLeft+btmPaint.measureText(strongSectxt[i])<getWidth()-right_eclipse_padding)
                    canvas.drawText(strongSectxt[i], strLeft, labelTopPadding + topTxtHei + labelTBPadding, i%2==1?strongPaint:btmPaint);
                else if (strLeft+btmPaint.measureText("...")<getWidth()-right_eclipse_padding) {
                    canvas.drawText("...", strLeft, labelTopPadding + topTxtHei + labelTBPadding, i%2==1?strongPaint:btmPaint);
                    break;
                }
            }
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        Parcelable superData = super.onSaveInstanceState();
        bundle.putParcelable("superData", superData);
        bundle.putInt("labelTextTopSize", labelTextTopSize);
        bundle.putInt("labelTextTopColor", labelTextTopColor);
        bundle.putInt("labelTextBottomColor", labelTextBottomColor);
        bundle.putInt("labelTextBottomSize", labelTextBottomSize);
        bundle.putInt("labelleftPadding", labelleftPadding);
        bundle.putFloat("labelTopPadding", labelTopPadding);
        bundle.putFloat("labelTBPadding", labelTBPadding);
        bundle.putFloat("topTxtHei", topTxtHei);
        bundle.putFloat("botTxtHei", botTxtHei);
        bundle.putString("labelTextTop", labelTextTop);
        bundle.putString("labelTextBottom", labelTextBottom);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle) state;
        Parcelable superData = bundle.getParcelable("super_data");
        labelTextTopSize = bundle.getInt("labelTextTopSize");
        labelTextTopColor = bundle.getInt("labelTextTopColor");
        labelTextBottomColor = bundle.getInt("labelTextBottomColor");
        labelTextBottomSize = bundle.getInt("labelTextBottomSize");
        labelleftPadding = bundle.getInt("labelleftPadding");
        labelTopPadding = bundle.getFloat("labelTopPadding", labelTopPadding);
        labelTBPadding = bundle.getFloat("labelTBPadding", labelTBPadding);
        botTxtHei = bundle.getFloat("botTxtHei", botTxtHei);
        labelTextTop = bundle.getString("labelTextTop");
        labelTextBottom = bundle.getString("labelTextBottom");

        super.onRestoreInstanceState(superData);
    }

    public void setSecondTxt(Spanned secondTxt){
        this.labelTextBottom = secondTxt.toString();
        invalidate();
    }

    public void setSpanedStr(String...strongSectxt){
        this.strongSectxt = strongSectxt;
        strongPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strongPaint.setColor(Color.parseColor("#61cdfd"));
        strongPaint.setTextSize(labelTextBottomSize);
        labelTextBottom = null;
        invalidate();
    }


    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
    }

    public void setFirstTxt(String firstTxt){
        this.labelTextTop = firstTxt;
        invalidate();
    }
}
