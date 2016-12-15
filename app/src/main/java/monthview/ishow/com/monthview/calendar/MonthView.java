package monthview.ishow.com.monthview.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import monthview.ishow.com.monthview.R;
import monthview.ishow.com.monthview.UIUtil.UIUtil;

/**
 * Created by MrS on 2016/12/6.
 */

public class MonthView extends View implements View.OnTouchListener {

    private Paint normalPaint, titlePaint, unSelectPaint, selectPaint, defalutPaint;

    private int cellWidth, leftRightPd;

    private List<String> days;

    private int height;
    private int width;

    private int dayOfMonth = -1;

    private int month;
    private int year;
    private boolean needWeekTitle;
    private String[] weekTitle;
    private String monthTitle;

    private int weekTitleHeight;
    private int monthTitleHeight;

    private float dayWidth;
    private float dayHeight;

    private boolean isCurMonth;//  当前绘制的月份是否是 本月
    private int selectDay = -1, lastSelectDay = -1;
    private int firstDayOfWeek;
    private int setDay, setYear, setMonth;
    private int maxDayOfMonth;

    public MonthView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        normalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        normalPaint.setColor(Color.BLACK);
        normalPaint.setTextSize(UIUtil.sp2px(context, 12));

        titlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        titlePaint.setColor(Color.BLACK);
        titlePaint.setTextSize(UIUtil.sp2px(context, 18));

        unSelectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        unSelectPaint.setColor(getContext().getResources().getColor(R.color.txt_9));
        unSelectPaint.setTextSize(UIUtil.sp2px(context, 12));

        selectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectPaint.setStyle(Paint.Style.FILL);
        selectPaint.setColor(context.getResources().getColor(R.color.comm_blue));

        defalutPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        defalutPaint.setStyle(Paint.Style.STROKE);
        defalutPaint.setStrokeWidth(1);
        defalutPaint.setColor(getResources().getColor(R.color.txt_9));

        leftRightPd = UIUtil.dip2px(context, 10);
        setNeedWeekTitle(true);
        upMonthDyas(0);

        this.setOnTouchListener(this);

    }

    public MonthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MonthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MonthView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (days == null)
            return;
        width = getMeasuredWidth();
        cellWidth = width / 7;
        height = getMeasuredHeight();

        int more = days.size() % 7;
        int row = days.size() / 7 + (more == 0 ? 0 : 1);

        Rect rect = new Rect();
        titlePaint.getTextBounds(monthTitle, 0, monthTitle.length(), rect);
        monthTitleHeight = rect.bottom - rect.top + cellWidth;
        height = cellWidth * (row) + monthTitleHeight + leftRightPd;

        if (needWeekTitle && weekTitle != null) {
            weekTitleHeight = UIUtil.dip2px(getContext(), 20);
            height += weekTitleHeight + cellWidth;
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (days == null)
            return;

        if (needWeekTitle && weekTitle != null) {
            for (int i = 0; i < weekTitle.length; i++) {
                String text = weekTitle[i];
                float weekTitleWidth = normalPaint.measureText(text);
                Paint.FontMetrics metrics = normalPaint.getFontMetrics();
                float weekTitleHeight = metrics.descent - metrics.ascent;
                canvas.drawText(text, cellWidth * i + (cellWidth / 2 - weekTitleWidth / 2), weekTitleHeight, unSelectPaint);
            }
            canvas.drawLine(0, weekTitleHeight, width, weekTitleHeight, unSelectPaint);
        }

        canvas.drawText(monthTitle, leftRightPd, leftRightPd + weekTitleHeight + monthTitleHeight - cellWidth, titlePaint);


        for (int i = 0; i < days.size(); i++) {
            String text = days.get(i);
            Paint.FontMetrics metrics = normalPaint.getFontMetrics();
            dayHeight = metrics.descent - metrics.ascent;
            dayWidth = normalPaint.measureText(text);
            float texty = (i / 7 * cellWidth + weekTitleHeight + leftRightPd + monthTitleHeight + cellWidth / 2 - dayHeight / 2);
            float textx = i % 7 * cellWidth + (cellWidth / 2 - dayWidth / 2);

            /*如果是 当前月的当前天 要绘制一个灰色圆圈*/
            if (isCurMonth && dayOfMonth + firstDayOfWeek - 2 == i) {
                float circlex = i % 7 * cellWidth + cellWidth / 2;
                float circley = i / 7 * cellWidth + weekTitleHeight + leftRightPd + monthTitleHeight + cellWidth / 4;
                canvas.drawCircle(circlex, circley, cellWidth / 2, defalutPaint);
            }


            if (TextUtils.equals(text, "-1")) {
                continue;
            } else if (Integer.valueOf(text) >= dayOfMonth) {
                normalPaint.setColor(Color.BLACK);
                canvas.drawText(text, textx, texty, normalPaint);
            } else {
                normalPaint.setColor(Color.GRAY);
                canvas.drawText(text, textx, texty, normalPaint);
            }

            if (i == selectDay) {
                float circlex = i % 7 * cellWidth + cellWidth / 2;
                float circley = i / 7 * cellWidth + weekTitleHeight + leftRightPd + monthTitleHeight + cellWidth / 4;
                canvas.drawCircle(circlex, circley, cellWidth / 2, selectPaint);
                normalPaint.setColor(Color.WHITE);
                canvas.drawText(text, textx, texty, normalPaint);
            }

        }
    }

    public void upMonthDyas(int position) {
        if (days == null) days = new ArrayList<>();
        days.clear();
        Calendar calendar = Calendar.getInstance();
        //当前月的当前天 需要绘制 一个灰色圆圈}
        if (position == 0) {
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            isCurMonth = true;
        } else {
            isCurMonth = false;
            dayOfMonth = -1;
        }


        calendar.add(Calendar.MONTH, position); // 12
        maxDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);


        monthTitle = year + "年" + month + "月";
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); //一个月的第一天 是礼拜几  比如  12月1号  是礼拜四  那么firstDayOfWeek=5
        //日      一      二       三       四        五       六
        //                            firstDayOfWeek

        /*添加的-1  不进行绘制   因为 第一天加入是礼拜四 那么 日-------三 都要留空的*/
        if (firstDayOfWeek >= 2) {
            for (int i = 0; i < firstDayOfWeek - 1; i++) {
                days.add("-1");
            }
        }
        /*这里是添加每月的每一天 到需要绘制的days数组中*/
        for (int i = 0; i < maxDayOfMonth; i++) {
            days.add(String.valueOf(i + 1));
        }
          /*这里是避免listview的复用导致 多个monthview有被同时选中的情况*/
        if (year == setYear && month == setMonth) {
            selectDay = setDay + firstDayOfWeek - 2;
        } else
            selectDay = -1;

        requestLayout();
    }

    public void setSelectedDay(int year1, int month1, int day1) {
        this.setYear = year1;
        this.setMonth = month1;
        this.setDay = day1;
    }

    public void setNeedWeekTitle(boolean needWeekTitle) {
        this.needWeekTitle = needWeekTitle;
        weekTitle = getResources().getStringArray(R.array.week_title);
    }

    public void resetSelectDay() {
        this.selectDay = -1;
        invalidate();
    }

    private float downX;
    private float downY;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            downX = event.getX();
            downY = event.getY() - (weekTitleHeight + leftRightPd + monthTitleHeight);

        } else if (event.getAction() == MotionEvent.ACTION_UP) {

            float touchx = event.getX();
            float touchy = event.getY() - (weekTitleHeight + leftRightPd + monthTitleHeight);

            if (Math.abs(touchy - downY) > ViewConfiguration.get(getContext()).getScaledTouchSlop() || Math.abs(touchx - downX) > ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                return false;
            }

            if (touchy > 0) {
                float v1 = touchx / cellWidth;
                float v2 = touchy / cellWidth;
                int indexX = (int) v1 + 1;
                int indexY = (int) v2;
                int touchDay = indexY * 7 + indexX - 1;
                if (touchDay >= dayOfMonth + firstDayOfWeek - 2 && touchDay < maxDayOfMonth + firstDayOfWeek - 1) {
                    selectDay = touchDay;
                    invalidate();
                    if (callback != null)
                        callback.onSelect(year, month, selectDay - firstDayOfWeek + 2);
                }

               // LogUtil.e("ACTION_DOWN v1 =" + v1 + "--v2=" + v2 + "--indexX=" + indexX + "--indexY" + indexY);
            }
        }
        return true;
    }

    public interface onCalendarDaySelectCallback {
        void onSelect(int year, int month, int day);
    }

    private onCalendarDaySelectCallback callback;

    public void setOnCalendarDaySelectCallback(onCalendarDaySelectCallback callback1) {
        this.callback = callback1;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        Parcelable superData = super.onSaveInstanceState();
        bundle.putParcelable("super_data", superData);
        bundle.putInt("select_day", selectDay);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        Bundle bundle = (Bundle) state;
        selectDay = bundle.getInt("select_day");
        Parcelable superData = bundle.getParcelable("super_data");
        super.onRestoreInstanceState(superData);
    }

}
