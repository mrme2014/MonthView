package com.ishow.ischool.widget.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ishow.ischool.util.AppUtil;

/**
 * Created by MrS on 2016/10/11.
 */

public class DateChosePopWindow extends AlertDialog implements AdapterView.OnItemClickListener {

    AdapterView.OnItemClickListener listener;

    public DateChosePopWindow(@NonNull Context context) {
        super(context);init(context);
    }

    public DateChosePopWindow(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);init(context);
    }

    public DateChosePopWindow(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);init(context);
    }


    private void init(Context context) {
        ListView listView = new ListView(context);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
        listView.setLayoutParams(params);
        ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, AppUtil.getSpinnerData());
        setContentView(listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.gravity= Gravity.RIGHT;
        attributes.horizontalMargin=70;
        getWindow().setAttributes(attributes);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.dismiss();
        if (listener!=null)
            listener.onItemClick(parent,view,position,id);
    }
    public void setonItemClick(AdapterView.OnItemClickListener listener1){
        this.listener = listener1;
    }
}
