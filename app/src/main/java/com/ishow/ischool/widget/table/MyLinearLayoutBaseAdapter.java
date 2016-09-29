package com.ishow.ischool.widget.table;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import java.util.List;

/**
 * Created by mini on 16/9/28.
 */

public abstract class MyLinearLayoutBaseAdapter<T> {
    protected List<T> list;
    protected Context context;

    public MyLinearLayoutBaseAdapter(Context context, List<T> list) {
        this.context = context;
        this.list = list;
    }

    public LayoutInflater getLayoutInflater() {
        if (context != null) {
            return LayoutInflater.from(context);
        }

        return null;
    }

    public int getCount() {
        if (list != null) {
            return list.size();
        }

        return 0;
    }

    public Object getItem(int position) {
        if (list != null) {
            return list.get(position);
        }

        return null;
    }

    /**
     * 供子类复写
     *
     * @param position
     * @return
     */
    abstract View getView(int position);

}

