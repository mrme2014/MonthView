package monthview.ishow.com.monthview.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import monthview.ishow.com.monthview.R;
import monthview.ishow.com.monthview.UIUtil.UIUtil;


/**
 * Created by MrS on 2016/12/7.
 */

public class HourView extends View implements View.OnTouchListener {

    private Paint hourPaint, selectPaint;
    private int cellRadius;
    private float texth, textw;
    private int firstHour = -1, secHour = -1;
    private int leftRightPd;
    private int rowNumCount = 6;

    public HourView(Context context) {
        super(context);
        init(context);
    }

    public HourView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HourView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HourView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        hourPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        hourPaint.setColor(getResources().getColor(R.color.txt_9));
        hourPaint.setTextSize(UIUtil.sp2px(context, 12));

        selectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectPaint.setColor(getResources().getColor(R.color.comm_greeen));
        selectPaint.setStyle(Paint.Style.FILL);

        // Paint.FontMetrics metrics = hourPaint.getFontMetrics();
        //  texth = metrics.descent - metrics.ascent;
        Rect rect = new Rect();
        hourPaint.getTextBounds("00:00", 0, 5, rect);
        texth = rect.bottom - rect.top;

        leftRightPd = UIUtil.dip2px(context, 20);

        setOnTouchListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - 2 * leftRightPd;
        cellRadius = width / rowNumCount;
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(cellRadius * 4, MeasureSpec.EXACTLY);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (secHour == -1 && firstHour != -1) {
            float circex = leftRightPd + (firstHour - 1) % rowNumCount * cellRadius + cellRadius / 2;
            float circey = (firstHour - 1) / rowNumCount * cellRadius + cellRadius / 2;
            canvas.drawCircle(circex, circey, (float) (cellRadius / 2.5), selectPaint);

        } else if (secHour != -1) {
            int line = secHour / rowNumCount - firstHour / rowNumCount;
            int lastLineLeft = secHour % rowNumCount;
            int firstLineLeft = firstHour % rowNumCount;
            if (firstLineLeft == 0)
                line++;
            if (firstLineLeft == 0 && lastLineLeft != 0)
                line++;
            if (line >= 1) {
                for (int j = 0; j < line + ((firstLineLeft == 0 || lastLineLeft == 0) ? 0 : 1); j++) {

                    float lineleft = leftRightPd + (firstHour % rowNumCount == 0 ? rowNumCount - 1 : firstHour % rowNumCount - 1) * cellRadius;
                    float linetop = j * cellRadius + cellRadius / 6 + (firstHour - 1) / rowNumCount * cellRadius;
                    float linebottom = linetop + cellRadius - 2 * cellRadius / 6;
                    float lineright = getWidth() - getPaddingRight() - getPaddingRight() - leftRightPd;

                    if (j == line && lastLineLeft != 0)
                        lineright = leftRightPd + firstLineLeft * cellRadius;

                    if (((firstLineLeft == 0 || lastLineLeft == 0) ? j == line - 1 : j == line) && firstLineLeft == 0) {
                        lineright = leftRightPd + ((lastLineLeft == 0 ? rowNumCount : lastLineLeft)) * cellRadius;
                    }

                    if (j == line && firstLineLeft != 0 && lastLineLeft != 0)
                        lineright = leftRightPd + (lastLineLeft) * cellRadius;

                    if (j >= 1) {
                        lineleft = leftRightPd;
                    }

                    RectF rectF = new RectF(lineleft, linetop, lineright, linebottom);
                    canvas.drawRoundRect(rectF, cellRadius / 2, cellRadius / 2, selectPaint);
                }
                  /*如果 第二时间 和第一个时间在同一行上*/
            } else {
                float lineleft = leftRightPd + (firstHour % rowNumCount == 0 ? rowNumCount - 1 : firstHour % rowNumCount - 1) * cellRadius;
                float linetop = firstHour / rowNumCount * cellRadius + cellRadius / 6; /*+ firstHour / 6 * cellRadius;*/
                float lineright = getWidth() - getPaddingRight() - getPaddingRight() - leftRightPd - (rowNumCount - (secHour % rowNumCount == 0 ? rowNumCount : secHour % rowNumCount)) * cellRadius;
                float linebottom = linetop + cellRadius - 2 * cellRadius / rowNumCount;
                RectF rectF = new RectF(lineleft, linetop, lineright, linebottom);
                canvas.drawRoundRect(rectF, cellRadius / 2, cellRadius / 2, selectPaint);
            }

        }

        for (int i = 0; i < 24; i++) {
            String text = "";
            if (i < 10) {
                text = "0" + i + ":00";
            } else text = i + ":00";

            textw = hourPaint.measureText(text);
            float textx = leftRightPd + i % rowNumCount * cellRadius + cellRadius / 2 - textw / 2;
            float texty = i / rowNumCount * cellRadius + cellRadius / 2 + texth / 2;
            if (firstHour != -1 && secHour == -1 && i == firstHour - 1) {
                hourPaint.setColor(Color.WHITE);
                canvas.drawText(text, textx, texty, hourPaint);
            } else if (secHour != -1 && i >= firstHour - 1 && i <= secHour - 1) {
                hourPaint.setColor(Color.WHITE);
                canvas.drawText(text, textx, texty, hourPaint);
            } else {
                hourPaint.setColor(getResources().getColor(R.color.txt_9));
                canvas.drawText(text, textx, texty, hourPaint);
            }

        }

    }

    private float downX;
    private float downY;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            downX = event.getX();
            downY = event.getY();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            float touchx = event.getX();
            float touchy = event.getY();

            if (Math.abs(touchy - downY) > ViewConfiguration.get(getContext()).getScaledTouchSlop() || Math.abs(touchx - downX) > ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                return false;
            }

            float v1 = (touchx - leftRightPd + cellRadius / 2) / cellRadius;
            float v2 = (touchy - cellRadius / 2) / cellRadius;
            int indexX = Math.round(v1);
            int indexY = Math.round(v2);
            int touchHour = (indexY) * rowNumCount + indexX;

            if (touchHour > 24)
                return false;

            if (firstHour == -1) {
                firstHour = touchHour;
            } else if (secHour == -1) {
                if (touchHour <= firstHour) {
                    firstHour = touchHour;
                } else secHour = touchHour;
            } else {
                firstHour = touchHour;
                secHour = -1;
            }
            //  Toast.makeText(getContext(), touchHour + "", Toast.LENGTH_SHORT).show();
            invalidate();
            if (callback != null) {
                callback.onSelected(firstHour - 1, secHour - 1);
            }

        }
        return true;
    }

    public interface hourSelectCallback {
        void onSelected(int hour1, int hour2);

    }

    private hourSelectCallback callback;

    public void setOnHourSelectCallback(hourSelectCallback callback1) {
        callback = callback1;
    }

}
