package com.ishow.ischool.widget.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import com.ishow.ischool.R;

import java.util.List;

/**
 * Created by MrS on 2016/9/13.
 */
public class TableRowTextView extends TextView {
    private List<String> list;
    private Paint linePaint,txtPaint;

    private int line_color;

    public TableRowTextView(Context context) {
        super(context);init();
    }

    public TableRowTextView(Context context, AttributeSet attrs) {
        super(context, attrs);init();
    }

    public TableRowTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TableRowTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);init();
    }


    private void init() {

        if (linePaint==null){
            linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            line_color = ContextCompat.getColor(getContext(), R.color.comm_line);
            linePaint.setColor(line_color);

            txtPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            linePaint.setColor(ContextCompat.getColor(getContext(), R.color.txt_9));
        }
    }

    public void setLargeText(List<String> list){
        this.list = list;


        // new Canvas();
        invalidate();

    }
}
