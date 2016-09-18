package com.ishow.ischool.widget.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;


/**
 * Created by Administrator on 2016/6/14.
 */
public class DispatchHScrollView extends HorizontalScrollView {
    private GestureDetector mGestureDetector;
    private View tableHead;

    public DispatchHScrollView(Context context) {
        super(context);
        init(context);
    }

    public DispatchHScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr); init(context);
    }

    public DispatchHScrollView(Context context, AttributeSet attrs) {
        super(context, attrs); init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DispatchHScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes); init(context);
    }

    private void init(Context context) {
        mGestureDetector = new GestureDetector(context,new HScrollDetector());
        setFadingEdgeLength(0);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
    }

    class HScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //LogUtil.e(distanceX+"***"+distanceY);
            if(Math.abs(distanceX) > Math.abs(distanceY)) {
                return true;
            }
            return false;
        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
       // LogUtil.e(l+"-*--"+t+"---"+tableHead.getLeft()+"---"+tableHead.getRight()+"---"+tableHead.getTop()+"---"+tableHead.getBottom());
        tableHead.layout(-l,tableHead.getTop(),tableHead.getRight(),tableHead.getBottom());
    }

    public void setTableHead(View tableHead){

        this.tableHead = tableHead;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        tableHead=null;
    }
}
