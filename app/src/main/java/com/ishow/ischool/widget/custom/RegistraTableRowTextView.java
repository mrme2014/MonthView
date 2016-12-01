package com.ishow.ischool.widget.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import com.commonlib.util.UIUtil;
import com.ishow.ischool.R;

import java.util.List;

/**
 * Created by MrS on 2016/9/13.
 */
public class RegistraTableRowTextView extends TextView {


    private Paint linePaint, txtPaint;

    private float txtHeight;

    private boolean shouldDrawBotLine = false;

    private int txtWidth;
    private int cellWidth;
    private List<Integer> iconList;
    private List<String> list;

    public RegistraTableRowTextView(Context context) {
        super(context);
        init();
    }

    public RegistraTableRowTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RegistraTableRowTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RegistraTableRowTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
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
        int width = getWidth();
        int height = getMeasuredHeight();
        if (list != null) {
            cellWidth = width / list.size();
            for (int i = 0; i < list.size(); i++) {
                String needDraw = list.get(i) == null ? "null" : list.get(i);
                txtWidth = (int) txtPaint.measureText(needDraw);
                if (cellWidth > txtWidth) {
                    canvas.drawText(needDraw, i * cellWidth + cellWidth / 2 - txtWidth / 2, (float) (height / 2) + txtHeight / 4, txtPaint);
                } else {
                    //String needDraw = list.get(i);
                    int overLength = needDraw.length() - getCellFixLength(needDraw);
                    overLength = overLength < 0 ? 0 : overLength;
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
        } else {
            cellWidth = width / (iconList == null ? 6 : iconList.size());
            for (int i = 0; i < (iconList == null ? 6 : iconList.size()); i++) {
                int cellValue = (iconList == null ? 0 : iconList.get(i));
                Bitmap cellBitmap = null;
                if (cellValue == 1) {
                    cellBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_checkbox_week_active2);
                } else if (cellValue == 0) {
                    cellBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_checkbox_weekly_normal);
                }
                canvas.drawBitmap(cellBitmap, i * cellWidth + cellWidth / 2 - cellBitmap.getWidth() / 2, (float) (height / 2) - cellBitmap.getHeight() / 2, txtPaint);
                canvas.drawLine(cellWidth * i, 0, cellWidth * i, height, linePaint);//右边的线
            }
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

    public void setIconList(List<Integer> lists) {
        if (lists == null)
            return;
        this.iconList = lists;
        // measure(getMeasuredWidth(), MeasureSpec.makeMeasureSpec(UIUtil.dip2px(getContext(), 45), MeasureSpec.EXACTLY));
        invalidate();
    }

    private int getCellFixLength(String txt) {
        int overLength = txtWidth - cellWidth;//15

        int byteLength = txtWidth / txt.length();//10
        if (overLength <= byteLength)
            return 1;
        int i = overLength / byteLength + 1;

        return i < 0 ? 0 : i;
    }
}
