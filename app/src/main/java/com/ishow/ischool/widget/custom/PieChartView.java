package com.ishow.ischool.widget.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.commonlib.util.LogUtil;
import com.commonlib.util.UIUtil;
import com.ishow.ischool.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrS on 2016/11/7.
 */

public class PieChartView extends View {
    private WeakReference<Bitmap> reference;
    private Bitmap bitmap;
    private int width, height;
    private int floorHeight;
    private Paint paint, txtPaint, numPaint, percentPaint;
    private int floorCount = 4;


    public List<String> des;
    public List<String> nums;
    private int rate1;
    private int rate2;

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
        if (des == null) {
            return;
        }
        width = Math.max(getWidth(), (floorCount - 1) * floorHeight * 2);
        height = Math.max(getHeight(), floorCount * floorHeight);

        for (int i = 0; i < floorCount; i++) {
            if (i == 0)
                paint.setAlpha(90);
            else if (i == 1)
                paint.setAlpha(130);
            else if (i == 2)
                paint.setAlpha(170);
            else if (i == 3) {
                paint.setColor(ContextCompat.getColor(getContext(), R.color.pie_color6));
                paint.setAlpha(230);

            }
            // RectF rectF = new RectF(floorHeight/2, floorHeight+floorHeight/2, width-floorHeight/2, height + 3* floorHeight-floorHeight/2);
            //  canvas.drawArc(rectF, 0, 180 + 60, false, percentPaint);
            canvas.drawCircle(width / 2, height, height - i * floorHeight, paint);
            if (i == 1)
                setFloorPercent(canvas, 1, R.color.pie_color1, rate1);
            if (i == 3)
                setFloorPercent(canvas, 3, R.color.pie_color1, rate2);
            if (des != null && des.size() > 0) {
                float textWidth = txtPaint.measureText(des.get(i));
                Paint.FontMetrics metrics = txtPaint.getFontMetrics();
                float textHeight = metrics.descent - metrics.ascent;
                float numWidth = numPaint.measureText(nums.get(i));
                Paint.FontMetrics numMetrics = numPaint.getFontMetrics();
                float numHeight = numMetrics.descent - numMetrics.ascent;
                canvas.drawText(des.get(i), width / 2 - textWidth / 2, floorHeight * i + floorHeight / 2 - numHeight / 2 + textHeight / 2, txtPaint);
                canvas.drawText(nums.get(i), width / 2 - numWidth / 2, floorHeight * i + floorHeight / 2 + numHeight / 2 + textHeight / 2, numPaint);
            }
        }

        //setFloorPercent(1, R.color.pie_color5, 60);
    }

    /**
     * @param des    每个floor的文本描述
     * @param colors 每个floor的颜色值
     */
    public void setFloorProperty(List<String> nums, int rate1, int rate2) {
        if (des == null) des = new ArrayList<>();
        des.add("带班人数");
        des.add("公开课");
        des.add("报名人数");
        des.add("全款人数");
        this.nums = nums;
        this.rate1 = rate1;
        this.rate2 = rate2;
        invalidate();
    }

    public RectF setFloorPercent(Canvas canvas, int floorIndex, int color, int angle) {
        LogUtil.e(angle + "setFloorPercent");
        if (angle == 0)
            return null;
        RectF rectF = new RectF((floorIndex - 1) * floorHeight + floorHeight / 2, floorIndex * floorHeight + floorHeight / 2, width - (floorIndex - 1) * floorHeight - floorHeight / 2, height + (floorCount - floorIndex) * floorHeight - floorHeight / 2);
        percentPaint.setStrokeWidth(floorHeight);
        percentPaint.setStyle(Paint.Style.STROKE);
        percentPaint.setColor(color);
        canvas.drawArc(rectF, 0, 180 + angle, false, percentPaint);
        return rectF;
    }
}
