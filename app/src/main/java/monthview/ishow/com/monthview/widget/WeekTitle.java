package monthview.ishow.com.monthview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

import monthview.ishow.com.monthview.R;
import monthview.ishow.com.monthview.uiutil.UIUtil;


/**
 * Created by MrS on 2016/12/6.
 */

public class WeekTitle extends TextView {
    private Paint unSelectPaint;
    private String[] weekTitleStrings;
    private int cellWidth;
    private int weekTitleHeight;
    private int weekHeight;

    public WeekTitle(Context context) {
        super(context);
        init(context);
    }

    public WeekTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WeekTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public WeekTitle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        unSelectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        unSelectPaint.setColor(getContext().getResources().getColor(R.color.txt_9));
        unSelectPaint.setTextSize(UIUtil.sp2px(context, 12));
        weekTitleStrings = getResources().getStringArray(R.array.week_title);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (weekTitleStrings != null)
            cellWidth = getWidth() / weekTitleStrings.length;
        weekHeight = getMeasuredHeight();
        weekHeight += UIUtil.dip2px(getContext(), 3);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(weekHeight, MeasureSpec.EXACTLY);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (weekTitleStrings != null) {
            for (int i = 0; i < weekTitleStrings.length; i++) {
                String text = weekTitleStrings[i];
                float weekTitleWidth = unSelectPaint.measureText(text);
                Paint.FontMetrics metrics = unSelectPaint.getFontMetrics();
                float weekTitleHeight = metrics.descent - metrics.ascent;
                canvas.drawText(text, cellWidth * i + (cellWidth / 2 - weekTitleWidth / 2),  weekTitleHeight, unSelectPaint);
            }
            canvas.drawLine(0, getHeight(), getWidth(), getHeight(), unSelectPaint);
        }
    }

    public void setWeekTitleStrings(String[] weekTitleStrings) {
        this.weekTitleStrings = weekTitleStrings;
        invalidate();
    }
}
