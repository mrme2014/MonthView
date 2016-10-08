package com.ishow.ischool.widget.table;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by mini on 16/9/28.
 */

public class MyLinearLayout4ListView extends LinearLayout {

    private MyLinearLayoutBaseAdapter adapter;
    private OnItemClickListener onItemClickListener;

    public MyLinearLayout4ListView(Context context) {
        super(context);
    }

    public MyLinearLayout4ListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAdapter(MyLinearLayoutBaseAdapter adapter) {
        this.adapter = adapter;
        // setAdapter 时添加 view
        bindView();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;

    }

    /**
     * 绑定 adapter 中所有的 view
     */
    private void bindView() {
        if (adapter == null) {
            return;
        }

        for (int i = 0; i < adapter.getCount(); i++) {
            View convertView = null;
            final View v = adapter.getView(i, convertView);
            final int position = i;
            final Object obj = adapter.getItem(i);

            // view 点击事件触发时回调我们自己的接口
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClicked(v, obj, position);
                    }
                }
            });

            addView(v);
        }
    }

    /**
     * 回调接口
     */
    public interface OnItemClickListener {
        /**
         * @param v        点击的 view
         * @param obj      点击的 view 所绑定的对象
         * @param position 点击位置的 index
         */
        public void onItemClicked(View v, Object obj, int position);
    }
}
