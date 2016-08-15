package com.ishow.ischool.widget.pickerview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.ishow.ischool.R;

import java.util.ArrayList;

/**
 * Created by MrS on 2016/8/12.
 * <p/>
 * <p/>
 * 这里动态 生成 几列WheelView 适用于大多数的选择 功能，，，年月日的生日筛选 在另一个 timePicker里面
 * <p/>
 * 分开来写逻辑省很多代码
 */
public class PickerWheelViewPop extends PopupWindow implements View.OnClickListener {

    private PickerWheelViewLinearlayout viewById;
    private TimePicker timePicker;

    public PickerWheelViewPop(Context context) {
        super(context);
    }

    public PickerWheelViewPop(Context context, AttributeSet attrs) {
        super(context, attrs);
        initMultiSelectPanel(context);
    }

    public PickerWheelViewPop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PickerWheelViewPop(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void initYearMDPanel(Context context) {
        this.setOutsideTouchable(true);
        this.setAnimationStyle(R.style.Select_dialog_windowAnimationStyle);
        View contentView = LayoutInflater.from(context).inflate(R.layout.time_picker, null);
        timePicker = (TimePicker) contentView.findViewById(R.id.picker);
        View cancel = contentView.findViewById(R.id.cancel);
        View ok = contentView.findViewById(R.id.ok);
        setContentView(contentView);
        setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new ColorDrawable(Color.parseColor("#e1e1e1")));

        cancel.setOnClickListener(this);
        ok.setOnClickListener(this);
    }

    /*这里往下 是除了  年月日选择的 */
    public void initMultiSelectPanel(Context context) {
        this.setOutsideTouchable(true);
        this.setAnimationStyle(R.style.Select_dialog_windowAnimationStyle);
        View contentView = LayoutInflater.from(context).inflate(R.layout.time_picker_controller, null);
        viewById = (PickerWheelViewLinearlayout) contentView.findViewById(R.id.PickerWheelViewLinearlayout);
        View cancel = contentView.findViewById(R.id.cancel);
        View ok = contentView.findViewById(R.id.ok);
        setContentView(contentView);
        setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new ColorDrawable(Color.parseColor("#e1e1e1")));

        cancel.setOnClickListener(this);
        ok.setOnClickListener(this);
    }

    /**
     * 给pop内容区添加view
     *
     * @param count 需要有几列wheelview  最多5个
     * @param datas
     */
    public void renderPanel(int defalut, int count, ArrayList<String>... datas) {
        if (count >= 5)
            throw new IndexOutOfBoundsException("the param count must less than 5.");
        /*if (datas==null||datas.length<count)
            throw new IndexOutOfBoundsException("the param datas must not be null and its length must be equal to count.");*/
        viewById.initWheelSetDatas(defalut, count, datas);
    }

    /**
     * 更改某一个wheelview的数据
     *
     * @param index    界面中从左到右第几个wheelview  从0开始
     * @param newDatas 新的数据源
     */
    public void resfreshData(int index, ArrayList<String> newDatas) {
        resfreshData(index, newDatas);
    }

    @Override
    public void onClick(View v) {
        this.dismiss();
        if (callback != null){
            callback.onPickCallback("ddd",1,2,3);
        }

    }

    private PickCallback callback;

    public interface PickCallback {
        /*ids 这个参数 在众多筛选条件中 UI界面上显示的是 string result  但服务器需要 int id,或者时间戳什么的.*/
        void onPickCallback(String result, int... ids);
    }

    public void addPickCallback(PickCallback callback1) {
        this.callback = callback1;
    }
}
