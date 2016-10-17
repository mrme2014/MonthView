package com.commonlib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.commonlib.util.LogUtil;
import com.commonlib.util.UIUtil;

import java.util.List;

/**
 * Created by abel on 16/10/13.
 */

public class CircleChartView extends View {

    private Paint circlePaint, textPaint, titlePaint;
    private float distance = 50;
    private float minRadius = 100;
    private List<Value> datas;
    private int count;
    private float textHeight;
    private Paint.FontMetrics fontMetrics;
    private int topPadding = 0;
    private String title;

    public CircleChartView(Context context) {
        super(context);
        init();
    }

    public CircleChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CircleChartView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(Color.parseColor("#22FFFFFF"));

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.parseColor("#FFFFFF"));
        textPaint.setTextSize(UIUtil.dip2px(getContext(), 12));

        titlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        titlePaint.setColor(Color.parseColor("#FFFFFF"));
        titlePaint.setTextSize(UIUtil.dip2px(getContext(), 14));

        fontMetrics = textPaint.getFontMetrics();
        textHeight = fontMetrics.descent - fontMetrics.ascent;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int with = getWidth();
        int height = getHeight();
//        LogUtil.d("xbin:onMeasure with=" + with + " height=" + height);
//        count = datas == null ? 4 : datas.size();
//        minRadius = with / 6;
//        distance = (height - topPadding - minRadius) / count;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int with = getWidth();
        int height = getHeight();
        count = datas == null ? 4 : datas.size();
        minRadius = with / 6;
        distance = (height - topPadding - minRadius) / count;
//        LogUtil.d("xbin:onDraw with=" + with + " height=" + height);

        if (!TextUtils.isEmpty(title)) {
            canvas.drawText(title, with / 2 - titlePaint.measureText(title) / 2, topPadding + distance / 2, titlePaint);
        }
        if (datas != null && !datas.isEmpty()) {
            for (int i = 0; i < count; i++) {
                Value data = datas.get(i);
                canvas.drawCircle(with / 2, height, minRadius + distance * i, circlePaint);
//            float textCenterVerticalBaselineY = height - (minRadius + distance * i - distance / 2);// - fontMetrics.descent + (fontMetrics.descent - fontMetrics.ascent) / 2;
                float textCenterVerticalBaselineY = topPadding + distance * (i + 1.5f);
                canvas.drawText(data.key, with / 2 - textPaint.measureText(data.key) / 2, textCenterVerticalBaselineY, textPaint);
                canvas.drawText(data.val, with / 2 - textPaint.measureText(data.val) / 2, textCenterVerticalBaselineY + textHeight * 1.2f, textPaint);
            }
        }

    }

    public void setData(List<Value> datas) {
        this.datas = datas;
        invalidate();
    }

    public void setData(List<Value> datas, String title) {
        this.datas = datas;
        this.title = title;
        invalidate();
    }

    public static class Value {
        public String key;
        public String val;

        public Value(String key, String val) {
            this.key = key;
            this.val = val;
        }
    }
}
