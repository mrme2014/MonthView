package com.ishow.ischool.widget.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.commonlib.util.LogUtil;
import com.commonlib.util.UIUtil;
import com.ishow.ischool.R;

import java.util.List;

/**
 * Created by MrS on 2016/9/13.
 */
public class TableRowTextView extends TextView implements View.OnTouchListener {

    private List<String> list;

    private Paint linePaint, txtPaint;

    private float txtHeight;
    //private float cellWidth;
    private boolean shouldDrawBotLine;

    private int cellWidth = UIUtil.dip2px(getContext(), 120);
    private int minCellWidth = cellWidth;


    public TableRowTextView(Context context) {
        super(context);
        init();
    }

    public TableRowTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TableRowTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TableRowTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (list == null)
            return;

        int width = 0;
        width = list.size() * cellWidth;
        width = Math.max(width, UIUtil.getScreenWidthPixels(getContext()));
        cellWidth = width/list.size();
        int measureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        setMeasuredDimension(measureSpec, getMeasuredHeight());

        LogUtil.e("onMeasure"+width+"---"+cellWidth);
    }


    private void init() {

        if (linePaint == null) {
            linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            linePaint.setColor(ContextCompat.getColor(getContext(), R.color.chart_line));

            txtPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            txtPaint.setTextSize(UIUtil.sp2px(getContext(), 13));
            txtPaint.setColor(ContextCompat.getColor(getContext(), R.color.chart_gray_txt));

            Paint.FontMetrics fontMetrics = txtPaint.getFontMetrics();
            txtHeight = fontMetrics.descent - fontMetrics.ascent;
        }
        this.setOnTouchListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (list == null || list.size() <= 0)
            return;

        int width = getWidth();
        int height = getMeasuredHeight();

        for (int i = 0; i < list.size(); i++) {
            int txtWidth = (int) txtPaint.measureText(list.get(i));
            if (cellWidth<txtWidth)cellWidth =txtWidth;
            canvas.drawText(list.get(i), i * cellWidth + cellWidth / 2 - txtWidth / 2, (float) (height / 2) + txtHeight / 4, txtPaint);
            canvas.drawLine(cellWidth * i, 0, cellWidth * i, height, linePaint);//右边的线

        }

        canvas.drawLine(0, 0, width, 0, linePaint);//上面的线

        canvas.drawLine(width - 1, 0, width - 1, height, linePaint);//右边的线

        if (shouldDrawBotLine) canvas.drawLine(0, height, width, height, linePaint);//下面的线

       LogUtil.e("onDraw" + cellWidth + "--" + width + "---");
    }

    public void setShouldDrawBotLine(boolean shouldDrawBotLine) {
        this.shouldDrawBotLine = shouldDrawBotLine;
    }

    public void setTxtList(List<String> lists) {
        if (lists==null)
            return;
        this.list = lists;
        invalidate();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
