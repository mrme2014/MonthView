package com.ishow.ischool.widget.table;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/**
 * Created by wqf on 16/9/23.
 */

public class BodyHorizontalScrollView extends HorizontalScrollView {

    private LinkScrollChangeListener listener;
    private GestureDetector mGestureDetector;

    public BodyHorizontalScrollView(Context context) {
        super(context);
        init(context);
    }

    public BodyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BodyHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        mGestureDetector = new GestureDetector(context, new HScrollDetector());
        setFadingEdgeLength(0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
    }

    class HScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (Math.abs(distanceX) > Math.abs(distanceY)) {
                return true;
            }
            return false;
        }
    }

    public void setMyScrollChangeListener(LinkScrollChangeListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (null != listener)
            listener.onscroll(this, l, t, oldl, oldt);
    }

    /**
     * 控制滑动速度
     */
//    @Override
//    public void fling(int velocityY) {
//        super.fling(velocityY / 2);
//    }

    public interface LinkScrollChangeListener {
        void onscroll(BodyHorizontalScrollView view, int l, int t, int oldl, int oldt);
    }
}
