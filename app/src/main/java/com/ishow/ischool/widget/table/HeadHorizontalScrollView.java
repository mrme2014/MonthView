package com.ishow.ischool.widget.table;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/**
 * Created by wqf on 16/9/23.
 * 由于头部标题的ScrollView一般是不能去主动滑动的，所以要拦截掉ScrollView的touch事件
 */

public class HeadHorizontalScrollView extends HorizontalScrollView {

    public HeadHorizontalScrollView(Context context) {
        super(context);
    }

    public HeadHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeadHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
