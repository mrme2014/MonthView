package com.commonlib.widget.base;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.commonlib.R;
import com.commonlib.util.LogUtil;

import java.util.ArrayList;

import static android.R.attr.id;

/**
 * Created by abel on 16/11/9.
 */

public class MySpinner extends TextView {

    private PopupWindow mPopup;
    private BaseAdapter mAdapter;
    private ListView mListView;
    private ArrayList<String> mDatas;

    private int mPosition;
    private String mValue;
    private AdapterView.OnItemClickListener mListener;

    public MySpinner(Context context) {
        super(context);
        init();
    }

    public MySpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MySpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MySpinner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LogUtil.d("-----------MySpinner init");
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });
    }

    private void initPop() {
        if (mPopup == null) {
            View contentView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_dropdown, null);
            mPopup = new PopupWindow(contentView);
            mPopup.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mPopup.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            //外部是否可以点击
            mPopup.setBackgroundDrawable(new ColorDrawable(0x00000000));
            mPopup.setOutsideTouchable(true);
            mPopup.setFocusable(true);

            mListView = (ListView) contentView.findViewById(R.id.spinner_dropdown_list);
            mAdapter = new SpinnerAdapter();
            mListView.setAdapter(mAdapter);

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mPosition = position;
                    mValue = mDatas.get(position);
                    setText(mValue);
                    dismissPopup();
                    if (mListener != null) {
                        mListener.onItemClick(parent, view, position, id);
                    }
                }

            });

        }
    }

    private void dismissPopup() {
        mPopup.dismiss();
    }

    public void setOnItemSelectedListener(AdapterView.OnItemClickListener listener) {
        this.mListener = listener;
    }

    public void setAdapter(BaseAdapter adapter, ArrayList mDatas) {
        mAdapter = adapter;
        this.mDatas = mDatas;
        mListView.setAdapter(mAdapter);
    }

    public void setDatas(ArrayList<String> mDatas) {
        this.mDatas = mDatas;
        setText(mDatas.get(mPosition));
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        this.mPosition = position;
        this.mValue = mDatas.get(position);
        setText(mValue);
        if (mListener != null) {
            mListener.onItemClick(null, null, position, id);
        }
    }

    public void show() {
        initPop();
        mPopup.showAsDropDown(this);
    }

    class SpinnerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDatas == null ? 0 : mDatas.size();
        }

        @Override
        public String getItem(int position) {
            return mDatas == null ? null : mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Viewholder viewholder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_dropdown_item, parent, false);
                viewholder = new Viewholder(convertView);
                convertView.setTag(viewholder);
            } else {
                viewholder = (Viewholder) convertView.getTag();
            }
            viewholder.textview.setText(getItem(position));

            return convertView;
        }
    }

    class Viewholder {
        TextView textview;

        public Viewholder(View view) {
            textview = (TextView) view;
        }
    }

}
