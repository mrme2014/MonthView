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
import android.view.animation.AccelerateDecelerateInterpolator;

import com.commonlib.util.LogUtil;
import com.commonlib.util.UIUtil;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.attribute.PieChartEntry;
import com.nineoldandroids.animation.IntEvaluator;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrS on 2016/11/7.
 */

public class PieChartView extends View {
    private int width, height;
    private int floorHeight;
    private Paint paint, txtPaint, numPaint, percentPaint;
    public int floorCount = 4;

    private Biulder biulder;
    private float timePercent;
    private List<PieChartEntry> datas;


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
        drawPieChart(canvas);
       // drawBitmap(canvas);
    }

    private void drawPieChart(Canvas canvas) {
        if (datas == null || datas.size() <= 0)
            return;
        width = getWidth();
        height = getHeight();

        for (int i = 0; i <floorCount; i++) {

            PieChartEntry entry = datas.get(i);
            /*绘制底层圆弧*/
            setFloorPercent(canvas, i, ContextCompat.getColor(getContext(), entry.floorColor), 180);

            Paint.FontMetrics metrics = txtPaint.getFontMetrics();
            float textWidth = txtPaint.measureText(entry.floorDes);
            float textHeight = metrics.descent - metrics.ascent;

            Paint.FontMetrics numMetrics = numPaint.getFontMetrics();
            float numWidth = numPaint.measureText(entry.floorNum);
            float numHeight = numMetrics.descent - numMetrics.ascent;

            canvas.drawText(entry.floorDes, width / 2 - textWidth / 2, floorHeight * i + floorHeight / 2 - numHeight / 2 + textHeight / 2, txtPaint);
            canvas.drawText(Math.round(Integer.valueOf(entry.floorNum) * timePercent)+ "", width / 2 - numWidth / 2, floorHeight * i + floorHeight / 2 + numHeight / 2 + textHeight / 2, numPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureSpec = MeasureSpec.makeMeasureSpec(floorCount * floorHeight, MeasureSpec.EXACTLY);
        setMeasuredDimension(widthMeasureSpec, measureSpec);
    }


    @Deprecated
    private void drawBitmap(Canvas canvas) {
        if (biulder == null) {
            return;
        }
        width = getWidth();
        height = getHeight();

        if (biulder.baseColor != 0)
            paint.setColor(ContextCompat.getColor(getContext(), biulder.baseColor));
        /*当前 需要绘制的弧形的下标 */
        for (int i = 0; i < floorCount; i++) {
            if (i == 0)
                paint.setAlpha(90);
            else if (i == 1)
                paint.setAlpha(120);
            else if (i == 2)
                paint.setAlpha(150);
            else if (i == 3) {
                paint.setAlpha(180);
            }
            /*绘制圆*/
            // 或者 canvas.drawCircle(width / 2, height, height - i * floorHeight, paint);
            setFloorPercent(canvas, i, paint.getColor(), 180);
            if (biulder.des != null && biulder.des.size() > 0) {

                Paint.FontMetrics metrics = txtPaint.getFontMetrics();
                float textWidth = txtPaint.measureText(biulder.des.get(i));
                float textHeight = metrics.descent - metrics.ascent;

                Paint.FontMetrics numMetrics = numPaint.getFontMetrics();
                float numWidth = numPaint.measureText(biulder.nums.get(i));
                float numHeight = numMetrics.descent - numMetrics.ascent;

                canvas.drawText(biulder.des.get(i), width / 2 - textWidth / 2, floorHeight * i + floorHeight / 2 - numHeight / 2 + textHeight / 2, txtPaint);
                canvas.drawText(Math.round(90 * timePercent) + "", width / 2 - numWidth / 2, floorHeight * i + floorHeight / 2 + numHeight / 2 + textHeight / 2, numPaint);
            }
        }
    }


    private void setFloorPercent( Canvas canvas, int floorIndex, int color, int angle) {
        if (angle <= 0)
            return;
        if (angle > 180) {
            angle = 180;
        }

        RectF rectF = new RectF((floorIndex - 1) * floorHeight + floorHeight / 2, floorIndex * floorHeight + floorHeight / 2, width - (floorIndex - 1) * floorHeight - floorHeight / 2, height + (floorCount - floorIndex) * floorHeight - floorHeight / 2);
        percentPaint.setStrokeWidth(floorHeight );
        percentPaint.setStyle(Paint.Style.STROKE);
        percentPaint.setColor(color);

        canvas.drawArc(rectF, -180 - 3, angle + 6, false, percentPaint);
        LogUtil.e("setFloorPercent"+rectF.toString());
    }

    public List<PieChartEntry> getDatas() {
        return datas;
    }

    public void setDatas(List<PieChartEntry> datas) {
        this.datas = datas;
       // invalidate();
        startRoateAnimation();
    }

    public void setDatasNoAnimation(List<PieChartEntry> datas) {
        this.datas = datas;
        timePercent = 1;
        postInvalidate();
    }

    private void startRoateAnimation() {
        final ValueAnimator anim = ValueAnimator.ofObject(new IntEvaluator(), 0, 100);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int vale = (int) animation.getAnimatedValue();
                timePercent = vale * 1.0f / 100;
                postInvalidate();
            }
        });
        anim.setDuration(2500);
        anim.start();
    }


    @Deprecated
    public void invalidate(PieChartView.Biulder biulder) {
        this.biulder = biulder;
        // invalidate();
        startRoateAnimation();
    }

    @Deprecated
    public void invalidateNoAnimation(PieChartView.Biulder biulder) {
        this.biulder = biulder;
        timePercent = 1;
        postInvalidate();
    }

    @Deprecated
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
                String substring = floorRate.substring(0, floorRate.length() - 1);
                float rate = Float.valueOf(substring);
                float angle = rate * 180 / 100;
                floorPercenter.add(60);
            } else if (!TextUtils.equals(floorRate, "") && floorRate != null) {
                /*m没有百分比符号 就默认是数字 */
                float rate = Float.valueOf(floorRate);
                float angle = rate * 180 / 100;
                floorPercenter.add(60);
            } else {
                floorPercenter.add(0);
            }
            return this;
        }
    }
}
