package com.ishow.ischool.widget.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import com.commonlib.util.UIUtil;
import com.ishow.ischool.R;

/**
 * Created by MrS on 2016/10/8.
 */

public class TableRowTextViewTeach extends TextView {
    private String[] args;
    private Paint linePaint;
    private Paint txtPaint;
    private float txtHeight;

    public TableRowTextViewTeach(Context context) {
        super(context);
        init();
    }

    public TableRowTextViewTeach(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TableRowTextViewTeach(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TableRowTextViewTeach(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
        args =new String[4];
        args[0]="哈哈";
        args[1]="哈哈";
        args[2]="哈哈";
        args[3]="哈哈";

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (args == null)
            return;
        int width = getWidth();
        int height = getHeight();

        String arg = args[0];
        String arg1 = "初级:" + args[1];
        String arg2 = "中级:" + args[2];
        String arg3 = "高级:" + args[3];
        canvas.drawText(arg, width / 2 - txtPaint.measureText(arg)/2, height / 4 + txtHeight / 4, txtPaint);
        canvas.drawLine(0,height/2,width,height/2,linePaint);
        canvas.drawText(arg1, width / 3 / 2 - txtPaint.measureText(arg1)/2, 3 * height / 4 + txtHeight / 4, txtPaint);
        canvas.drawLine( width / 3,height/2, width/3 ,height,linePaint);
        canvas.drawText(arg2, width / 3 + width / 3 / 2 -txtPaint.measureText(arg2)/2, 3 * height / 4 + txtHeight / 4, txtPaint);
        canvas.drawLine( 2*width / 3,height/2,2* width / 3,height,linePaint);
        canvas.drawText(arg3,2* width / 3+ width / 3 / 2 - txtPaint.measureText(arg3)/2, 3 * height / 4 + txtHeight /4, txtPaint);
        canvas.drawLine( width ,height/2, width ,height,linePaint);
    }

    public void updateList(String... args) {
        this.args = args;
        invalidate();
    }
}
