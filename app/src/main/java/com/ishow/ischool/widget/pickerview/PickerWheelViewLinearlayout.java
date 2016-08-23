package com.ishow.ischool.widget.pickerview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.ishow.ischool.R;

import java.util.ArrayList;

/**
 * Created by MrS on 2016/8/12.
 * <p>
 * <p>
 * 适用于 列与列直接关联不是很密切 可以有联动，，，但像年月日样式的选择。。。。联动
 */
public class PickerWheelViewLinearlayout extends LinearLayout implements WheelView.OnSelectListener {

    public PickerWheelViewLinearlayout(Context context) {
        super(context);
    }

    public PickerWheelViewLinearlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PickerWheelViewLinearlayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PickerWheelViewLinearlayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 初始化 wheelView对象 设置监听啥的
     *
     * @param cloums 需要有个 WheelView 滑动滚轮
     */
    private SparseArray<WheelView> array;

    public void initWheelSetDatas(int defalut, int cloums, ArrayList<ArrayList<String>> datas) {
        if (array == null) array = new SparseArray<>(cloums);

        for (int i = 0; i < cloums; i++) {
            WheelView wheelView = (WheelView) LayoutInflater.from(getContext()).inflate(R.layout.wheelview, null);
            wheelView.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
            if (datas != null && i < datas.size())
                wheelView.setData(datas.get(i));
            else wheelView.setData(getMonthData());//这句话是测试用的
            wheelView.setId(i);
            wheelView.setDefault(defalut);
            wheelView.setOnSelectListener(this);
            array.put(i, wheelView);
            addView(wheelView);
        }
    }

    /**
     * 更改某一个wheelview的数据
     *
     * @param index    界面中从左到右第几个wheelview  从0开始
     * @param newDatas 新的数据源
     */
    WheelView wheelView;
    public void resfreshData(int index, final ArrayList<String> newDatas) {
        if (newDatas==null)
            return;
        if (array != null) {
            if (index >= array.size())
                index = array.size() - 1;
                wheelView = array.get(index);
                if (wheelView != null)
                    wheelView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            wheelView.resetData(newDatas);
                            wheelView.setDefault(0);
                        }
                    }, 10);

        }
    }

    //private String colTxt1, colTxt2, colTxt3, colTxt4, colTxt5;

    @Override
    public void endSelect(WheelView wheelView, int id, String text) {
        if (select != null) {
            select.endSelect(wheelView, id, text);
        }
        /* switch (wheelView.getId()) {
            case 0:
                colTxt1 = text;
                break;
            case 1:
                colTxt2 = text;
                break;
            case 2:
                colTxt3 = text;
                break;
            case 3:
                colTxt4 = text;
                break;
            case 4:
                colTxt5 = text;
                break;
            default:
                throw new IndexOutOfBoundsException("must less than or equal 5 wheelview at the same time.");

        }*/
    }

    @Override
    public void selecting(int id, String text) {

    }

    private ArrayList<String> getMonthData() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 1; i <= 12; i++) {
            list.add(i + "月");
        }
        return list;
    }

    public String[] getSelectResult() {
        if (array == null)
            return null;
        String[] result = new String[array.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = array.get(i).getSelectedText();
        }
        return result;
    }

    public int[] getSelectResultId() {
        if (array == null)
            return null;
        int[] result = new int[array.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = array.get(i).getSelected();
        }
        return result;
    }

    wheelViewSelect select;

    public interface wheelViewSelect {
        void endSelect(WheelView wheelView, int id, String text);
    }

    public void setwheelViewSelect(wheelViewSelect select1) {
        this.select = select1;
    }
}