package com.commonlib.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by abel on 16/8/12.
 */
public class LabelCleanableEditText extends LabelEditText {

    private OnClickListener mListener;

    public LabelCleanableEditText(Context context) {
        super(context);
    }

    public LabelCleanableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LabelCleanableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int DRAWABLE_RIGHT = 2;
        //可以获得上下左右四个drawable，右侧排第二。图标没有设置则为空。
        Drawable rightIcon = getCompoundDrawables()[DRAWABLE_RIGHT];
        if (rightIcon != null && event.getAction() == MotionEvent.ACTION_UP) {
            //检查点击的位置是否是右侧的删除图标
            //注意，使用getRwwX()是获取相对屏幕的位置，getX()可能获取相对父组件的位置
            int leftEdgeOfRightDrawable = getRight() - getPaddingRight()
                    - rightIcon.getBounds().width();
            if (event.getRawX() >= leftEdgeOfRightDrawable) {
                if (mListener != null) {
                    setText("");
                    mListener.onClick(this);
                }
            }
        }

        return super.onTouchEvent(event);
    }

    public void setClearListener(OnClickListener listener) {
        this.mListener = listener;
    }


}

