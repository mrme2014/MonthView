package com.ishow.ischool.widget.table;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * Created by wqf on 16/9/23.
 */

public class BodyHorizontalScrollView extends HorizontalScrollView {

    private LinkScrollChangeListener listener;

    public BodyHorizontalScrollView(Context context) {
        super(context);
        setFadingEdgeLength(0);
    }

    public BodyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFadingEdgeLength(0);
    }

    public BodyHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFadingEdgeLength(0);
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
