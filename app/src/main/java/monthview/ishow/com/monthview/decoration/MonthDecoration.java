package monthview.ishow.com.monthview.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.Calendar;

import monthview.ishow.com.monthview.R;
import monthview.ishow.com.monthview.uiutil.UIUtil;

/**
 * Created by MrS on 2016/12/27.
 */

public class MonthDecoration extends RecyclerView.ItemDecoration {
    private int year, month;
    private Context context;

    public int getExtraPadding() {
        return extraPadding;
    }

    private int extraPadding, leftPadding, textHeight, lineHeight, rightPadding;
    private Paint paint, bgPaint;

    public MonthDecoration(Context context) {
        this.context = context;

        Calendar instance = Calendar.getInstance();
        year = instance.get(Calendar.YEAR);
        month = instance.get(Calendar.MONTH) + 1;

        extraPadding = UIUtil.dip2px(context, 30);
        leftPadding = UIUtil.dip2px(context, 16);
        rightPadding = leftPadding * 2;


        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(UIUtil.sp2px(context, 13));
        paint.setColor(context.getResources().getColor(R.color.comm_line));

        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(context.getResources().getColor(R.color.comm_line));

        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        textHeight = (int) (paint.getTextSize() + fontMetrics.descent);
        lineHeight = 3;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        if (position % 12 == 0) {//小于12的时候  position=1 要显示年份，大于12的时候，position
            outRect.top = extraPadding;
        } else {
            outRect.top = 0;
        }
        if (position % 12 != 11) outRect.bottom = lineHeight;//12月份不要那条线了

        //LogUtil.e("getItemOffsets" + (month + position) % 12 + "---" + position);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {

            View childAt = parent.getChildAt(i);

            int adapterPosition = parent.getChildAdapterPosition(childAt);
            LinearLayoutManager manager = (LinearLayoutManager) parent.getLayoutManager();
            int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();

            int yearIndex = adapterPosition / 12;

            int bottom = childAt.getBottom();
            float top = Math.max(extraPadding, childAt.getTop());
            if (adapterPosition % 12 == 11 && bottom < top) {//当12月份进入header 且bottom小于top
                top = bottom;
            }
            if ((adapterPosition) % 12 == 0 || firstVisibleItemPosition == adapterPosition) {
                paint.setColor(ContextCompat.getColor(context, R.color.comm_blue));
                c.drawRect(new RectF(0, top - extraPadding, childAt.getWidth(), top), bgPaint);
                c.drawText(year + yearIndex + "", leftPadding, top - (extraPadding / 2 - textHeight / 3), paint);
            }
            if ((adapterPosition) % 12 == 11) {
                paint.setColor(context.getResources().getColor(R.color.comm_line));
                c.drawLine(0, childAt.getBottom(), childAt.getWidth(), childAt.getBottom(), paint);
            } else {
                paint.setColor(context.getResources().getColor(R.color.comm_line));
                c.drawLine(0, childAt.getBottom(), childAt.getWidth() - rightPadding, childAt.getBottom(), paint);
            }
            // LogUtil.e("onDraw" + (month + i) % 12 + "---" + month);
        }
    }
}