package com.commonlib.widget.pull;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.commonlib.util.UIUtil;

/**
 * Created by wqf on 16/8/26.
 */
public class BaseItemDecor extends RecyclerView.ItemDecoration {

    private Paint mPaint;
    private int mLeftMargin;
    private Context mContext;

    /**
     * @param mContext
     * @param leftMargin   单位:dp
     */
    public BaseItemDecor(Context context, int leftMargin) {
        mContext = context;
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#d9dadd"));
        mLeftMargin = leftMargin;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 1; i < childCount; i++) {      // 从1开始，第一个item上面不会绘制
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getTop() - params.bottomMargin -
                    Math.round(ViewCompat.getTranslationY(child));
            final int bottom = top + 1;
            c.drawRect(left + UIUtil.sp2px(mContext, mLeftMargin), top, right, bottom, mPaint);
//            mPaint.setTextSize(20f);
//            c.drawText("item:" + i, left + 5, top + 20, mPaint);
        }
    }

    // 内嵌偏移
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, 0);
    }
}
