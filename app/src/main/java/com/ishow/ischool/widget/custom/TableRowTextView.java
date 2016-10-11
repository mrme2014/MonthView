package com.ishow.ischool.widget.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import com.commonlib.util.LogUtil;
import com.commonlib.util.UIUtil;
import com.ishow.ischool.R;

import java.util.List;

/**
 * Created by MrS on 2016/9/13.
 */
public class TableRowTextView extends TextView {

    private List<String> list;

    private Paint linePaint, txtPaint;

    private float txtHeight;

    private boolean shouldDrawBotLine = false;

    private int cellWidth = UIUtil.dip2px(getContext(), 100);
    private int fixedCellWidth = UIUtil.dip2px(getContext(), 100);
    private int txtWidth;

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
        //cellWidth = UIUtil.dip2px(getContext(), 100);

        int width = list.size() * cellWidth;
        width = Math.max(width, UIUtil.getScreenWidthPixels(getContext()) - fixedCellWidth);
        cellWidth = width / list.size();
        int measureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        setMeasuredDimension(measureSpec, getMeasuredHeight());
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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (list == null || list.size() <= 0)
            return;

        int width = getWidth();
        int height = getMeasuredHeight();

        for (int i = 0; i < list.size(); i++) {
            String needDraw = list.get(i) == null ? "hahah" : list.get(i);
            txtWidth = (int) txtPaint.measureText(needDraw);
            if (cellWidth> txtWidth) {
                canvas.drawText(needDraw, i * cellWidth + cellWidth / 2 - txtWidth / 2, (float) (height / 2) + txtHeight / 4, txtPaint);
            } else {
                //String needDraw = list.get(i);
                int overLength = needDraw.length() - getCellFixLength(needDraw);
                overLength = overLength < 0 ? 0 : overLength;
                LogUtil.e(i + "--getCellFixLength---" + overLength + "----" + needDraw.length());
                String needTop = needDraw.substring(0, overLength);
                String needBottom = needDraw.substring(overLength, needDraw.length());
                float txtTopWidth = txtPaint.measureText(needTop);
                float txtBottWidth = txtPaint.measureText(needBottom);
                canvas.drawText(needTop, i * cellWidth + cellWidth / 2 - txtTopWidth / 2, (float) (height / 2) - 4, txtPaint);
                canvas.drawText(needBottom, i * cellWidth + cellWidth / 2 - txtBottWidth / 2, (float) (height / 2) + txtHeight, txtPaint);
                //canvas.drawText(list.get(i), i * cellWidth + cellWidth / 2 - txtWidth / 2, (float) (height / 2) + txtHeight / 4, txtPaint);
            }
            canvas.drawLine(cellWidth * i, 0, cellWidth * i, height, linePaint);//右边的线
        }

        canvas.drawLine(0, 0, width, 0, linePaint);//上面的线

        canvas.drawLine(width - 1, 0, width - 1, height, linePaint);//右边的线

        if (shouldDrawBotLine) canvas.drawLine(0, height, width, height, linePaint);//下面的线

    }

    public void setShouldDrawBotLine(boolean shouldDrawBotLine) {
        this.shouldDrawBotLine = shouldDrawBotLine;
    }

    public void setTxtList(List<String> lists) {
        if (lists == null)
            return;
        this.list = lists;
        // measure(getMeasuredWidth(), MeasureSpec.makeMeasureSpec(UIUtil.dip2px(getContext(), 45), MeasureSpec.EXACTLY));
        invalidate();
    }

    private int getCellFixLength(String txt) {
        int overLength = txtWidth - cellWidth;//15

        int byteLength = txtWidth / txt.length();//10
        if (overLength <= byteLength)
            return 1;
        int i = overLength / byteLength+1 ;

        return i < 0 ? 0 : i;
    }
}
