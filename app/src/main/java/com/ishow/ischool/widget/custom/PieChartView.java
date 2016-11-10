package com.ishow.ischool.widget.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.commonlib.util.LogUtil;
import com.commonlib.util.UIUtil;
import com.ishow.ischool.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrS on 2016/11/7.
 */

public class PieChartView extends View {
    private int width, height;
    private int floorHeight;
    private Paint paint, txtPaint, numPaint, percentPaint;
    private int floorCount = 4;


    public List<String> des;
    public List<String> nums;
    private int rate1;
    private int rate2;
    private Biulder biulder;

    public PieChartView(Context context) {
        super(context);
        init(context);
    }

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PieChartView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        //  floorHeight = UIUtil.dip2px(context, 68);
        floorHeight = UIUtil.getScreenWidthPixels(context) / (floorCount * 2 - 2);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        //paint.setStrokeWidth(floorHeight);
        paint.setStyle(Paint.Style.FILL);

        txtPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        txtPaint.setTextSize(UIUtil.sp2px(context, 11));
        txtPaint.setAlpha(200);
        txtPaint.setColor(ContextCompat.getColor(context, R.color.white));

        numPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        numPaint.setTextSize(UIUtil.sp2px(context, 14));
        numPaint.setColor(ContextCompat.getColor(context, R.color.white));

        percentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        percentPaint.setStyle(Paint.Style.STROKE);
        percentPaint.setStrokeWidth(floorHeight);
        percentPaint.setColor(ContextCompat.getColor(context, R.color.pie_color1));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBitmap(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureSpec = MeasureSpec.makeMeasureSpec(floorCount * floorHeight, MeasureSpec.EXACTLY);
        setMeasuredDimension(widthMeasureSpec, measureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

    private void drawBitmap(Canvas canvas) {
        if (biulder == null) {
            return;
        }

        width = Math.max(getWidth(), (floorCount - 1) * floorHeight * 2);
        height = Math.max(getHeight(), floorCount * floorHeight);

        if (biulder.baseColor != 0)
            paint.setColor(ContextCompat.getColor(getContext(), biulder.baseColor));
        /*当前 需要绘制的弧形的下标 */
        int curDrawArcIndex = 0;
        for (int i = 0; i < floorCount; i++) {
            if (i == 0)
                paint.setAlpha(90);
            else if (i == 1)
                paint.setAlpha(130);
            else if (i == 2)
                paint.setAlpha(170);
            else if (i == 3) {
                paint.setAlpha(255);
            }
            // RectF rectF = new RectF(floorHeight/2, floorHeight+floorHeight/2, width-floorHeight/2, height + 3* floorHeight-floorHeight/2);
            //  canvas.drawArc(rectF, 0, 180 + 60, false, percentPaint);
            /*绘制圆*/
            canvas.drawCircle(width / 2, height, height - i * floorHeight, paint);

            /*绘制弧  且当前floor是 需要绘制的*/
            if (biulder != null && biulder.floorIndex.contains(i)) {
                setFloorPercent(canvas, i, biulder.floorColor.get(curDrawArcIndex) == 0 ? R.color.pie_color1 : biulder.floorColor.get(curDrawArcIndex), biulder.floorPercenter.get(curDrawArcIndex));
                curDrawArcIndex++;
            }

            if (biulder.des != null && biulder.des.size() > 0) {

                Paint.FontMetrics metrics = txtPaint.getFontMetrics();
                float textWidth = txtPaint.measureText(biulder.des.get(i));
                float textHeight = metrics.descent - metrics.ascent;

                Paint.FontMetrics numMetrics = numPaint.getFontMetrics();
                float numWidth = numPaint.measureText(biulder.nums.get(i));
                float numHeight = numMetrics.descent - numMetrics.ascent;

                canvas.drawText(biulder.des.get(i), width / 2 - textWidth / 2, floorHeight * i + floorHeight / 2 - numHeight / 2 + textHeight / 2, txtPaint);
                canvas.drawText(biulder.nums.get(i), width / 2 - numWidth / 2, floorHeight * i + floorHeight / 2 + numHeight / 2 + textHeight / 2, numPaint);
            }
        }

        //setFloorPercent(1, R.color.pie_color5, 60);
    }

    public static class Biulder {
        public List<String> des;
        public List<String> nums;
        private int baseColor;
        private List<Integer> floorIndex = new ArrayList<>();
        private List<Integer> floorColor = new ArrayList<>();
        private List<Integer> floorPercenter = new ArrayList<>();

        public Biulder setPieChartBaseColor(int baseColor) {
            this.baseColor = baseColor;
            return this;
        }

        public Biulder setDrawTxtDes(List<String> drawDes) {
            this.des = drawDes;
            return this;
        }

        public Biulder setDrawNums(List<String> drawNums) {
            this.nums = drawNums;
            return this;
        }

        /**
         * @param floorIndex1  需要绘制遮罩的楼层下标   从0开始
         * @param percentColor 需要绘制遮罩楼层 的颜色
         * @param floorRate    需要绘制遮罩楼层 的百分比
         * @return
         */
        public Biulder DrawPercentFloor(int floorIndex1, int percentColor, String floorRate) {
            floorIndex.add(floorIndex1);
            floorColor.add(percentColor);

            /*如果 包含 百分比 符号 就进行 分割*/
            if (!TextUtils.equals(floorRate, "") && floorRate != null && floorRate.contains("%")) {
                int rate = Integer.valueOf(floorRate.substring(0, floorRate.length() - 1));
                floorPercenter.add(rate);
            } else if (!TextUtils.equals(floorRate, "") && floorRate != null) {
                /*m没有百分比符号 就默认是数字 */
                floorPercenter.add(Integer.valueOf(floorRate));
            } else floorPercenter.add(0);
            return this;
        }
    }

    public void invalidate(PieChartView.Biulder biulder) {
        this.biulder = biulder;
        invalidate();
    }

    /*  * @param rate1 需要绘制百分比的 第一个 floor的 百分比
      * @param rate2 需要绘制百分比的 第二个 floor的 百分比*//*
    public voidsetFloorProperty(List<String> nums, String rate1, String rate2) {
        if (des == null) des = new ArrayList<>();
        des.add("带班人数");
        des.add("公开课");
        des.add("报名人数");
        des.add("全款人数");
        this.nums = nums;
        if (TextUtils.equals(rate1, "") && rate1 != null)
            this.rate1 = Integer.valueOf(rate1.substring(0, rate1.length() - 1));
        if (TextUtils.equals(rate2, "") && rate2 != null)
            this.rate2 = Integer.valueOf(rate2.substring(0, rate2.length() - 1));

        invalidate();
    }*/

    public RectF setFloorPercent(Canvas canvas, int floorIndex, int color, int angle) {
        LogUtil.e(angle + "setFloorPercent");
        if (angle == 0)
            return null;
        RectF rectF = new RectF((floorIndex - 1) * floorHeight + floorHeight / 2, floorIndex * floorHeight + floorHeight / 2, width - (floorIndex - 1) * floorHeight - floorHeight / 2, height + (floorCount - floorIndex) * floorHeight - floorHeight / 2);
        percentPaint.setStrokeWidth(floorHeight);
        percentPaint.setStyle(Paint.Style.STROKE);
        percentPaint.setColor(ContextCompat.getColor(getContext(), color));
        canvas.drawArc(rectF, 0, 180 + angle, false, percentPaint);
        return rectF;
    }
}
