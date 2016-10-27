package com.ishow.ischool.widget.pickerview;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.commonlib.util.DateUtil;
import com.ishow.ischool.R;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by MrS on 2016/8/22.
 */
public class PickerDialogFragment extends DialogFragment implements View.OnClickListener, PickerWheelViewLinearlayout.wheelViewSelect {
    /**
     * 年月日的选择  PickerDialogFragment.Builder builder = new PickerDialogFragment.Builder();
     * builder.setBackgroundDark(true).setDialogTitle(R.string.switch_role).setDialogType(PickerDialogFragment.PICK_TYPE_DATE);
     * PickerDialogFragment fragment = builder.Build();
     * dialog.show(getSupportManager,"dialog);
     * dialog.addCallback(new Callback);
     * <p>
     * <p>
     * 需要多个滑轮列表或者 列与列之间需要联动的
     * <p>
     * PickerDialogFragment.Builder builder = new PickerDialogFragment.Builder();
     * builder.setBackgroundDark(true).setDialogTitle(R.string.switch_role).setDialogType(PickerDialogFragment.PICK_TYPE_OTHERS).setDatas(0,1,campus.get(0).positions);;
     * PickerDialogFragment fragment = builder.Build();
     * fragment.show(getChildFragmentManager(),"dialog");
     * fragment.addMultilinkPickCallback(new PickerDialogFragment.MultilinkPickCallback() );
     * <p>
     * <p>
     * //多个列表 之间不需要联动
     * fragment.addCallback(new Callback); 返回值1   各列选中的position数组  返回值2 各列选中的文本数组
     */
    //  @BindView(R.id.cancel)
    TextView cancel;
    //  @BindView(R.id.title)
    TextView title;
    //@BindView(R.id.ok)
    TextView ok;
    // @BindView(R.id.picker)
    TimePicker picker;

    PickerWheelViewLinearlayout linearlayout;

    private Dialog dialog;

    public static final int PICK_TYPE_DATE = 0;

    public static final int PICK_TYPE_OTHERS = 1;

    public static int PICK_TITLE = -1;

    public static int PICK_THEME = R.style.Comm_dialogfragment;

    public static int PICK_TYPE = PICK_TYPE_DATE;

    private View contentView;
    private int defalut;
    private int[] selectDefaluts;
    private int count;
    private ArrayList<ArrayList<String>> data;
    private int date_time;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if (bundle != null) {
            PICK_TYPE = bundle.getInt("PICK_TYPE");
            PICK_TITLE = bundle.getInt("PICK_TITLE");
            PICK_THEME = bundle.getInt("PICK_THEME");
            date_time = bundle.getInt("date_time");
            defalut = bundle.getInt("defalut");
            selectDefaluts =bundle.getIntArray("selectDefaluts");
            count = bundle.getInt("count");
            data = (ArrayList<ArrayList<String>>) bundle.getSerializable("data");
        }
        dialog = new Dialog(getContext(), PICK_THEME);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        if (PICK_TYPE == PICK_TYPE_DATE) {
            contentView = LayoutInflater.from(getContext()).inflate(R.layout.time_picker_linearlayout, null);
            picker = (TimePicker) contentView.findViewById(R.id.picker);
        } else if (PICK_TYPE == PICK_TYPE_OTHERS) {
            contentView = LayoutInflater.from(getContext()).inflate(R.layout.time_picker_controller, null);
            linearlayout = (PickerWheelViewLinearlayout) contentView.findViewById(R.id.PickerWheelViewLinearlayout);
        }

        cancel = (TextView) contentView.findViewById(R.id.cancel);
        title = (TextView) contentView.findViewById(R.id.title);
        ok = (TextView) contentView.findViewById(R.id.ok);
        cancel.setOnClickListener(this);
        ok.setOnClickListener(this);
        if (PICK_TITLE != -1) title.setText(getContext().getResources().getString(PICK_TITLE));


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(contentView);
        Window window = dialog.getWindow();
        window.setLayout(-1, -2);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        params.windowAnimations = R.style.Select_dialog_windowAnimationStyle;
        window.setAttributes(params);

        if (picker != null) {
           if (date_time<=0)picker.setDate(new Date().getTime());
            else picker.setDate((long)date_time*1000);
        }

        if (linearlayout != null) {
            setDatas(selectDefaluts,defalut, count, data);
        }

        //if (callback!=null&&callback instanceof PickCallback||callback instanceof MultilinkPickCallback)callback.onDialogCreatCompelete();
        //if (pickcallback != null) pickcallback.onDialogCreatCompelete();
        // else if (multicallback != null) multicallback.onDialogCreatCompelete();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, "dialog");
    }

    public static class Builder {
        private boolean dark;
        private int titleResId;
        private int defalut;
        private int count;
        private ArrayList<ArrayList<String>> data;
        private int pickType;
        private int[] selectDefaluts;
        private int dateTime;

        public Builder setBackgroundDark(boolean dark) {
            this.dark = dark;
            return this;
        }

        public Builder setDialogTitle(int titleResId) {
            this.titleResId = titleResId;
            return this;
        }

        public Builder setDialogType(int pickType) {
            this.pickType = pickType;
            return this;
        }

        public Builder setDateTime(int dateTime) {
            this.dateTime = dateTime;
            return this;
        }

        public Builder setDatas(int defalut, int count, ArrayList<String>... datas) {

            this.defalut = defalut;
            this.count = count;
            data = new ArrayList<ArrayList<String>>();
            for (int i = 0; i < datas.length; i++) {
                data.add(datas[i]);
            }
            return this;
        }

        //为每一列 都设置一个默认选中项  没有被设置的默认被设置为第0项选中 超过列数  无效

        //设置了这个参数后 setDatas(int defalut, int count, ArrayList<String>... datas)  default 参数将无效
        public Builder setDefalut(int...defalut){
            selectDefaluts = defalut;
            return this;
        }

        public PickerDialogFragment Build() {
            Bundle bundle = new Bundle();
            bundle.putInt("PICK_THEME", dark ? R.style.Comm_dialogfragment : DialogFragment.STYLE_NO_FRAME);
            bundle.putInt("PICK_TITLE", titleResId);
            bundle.putInt("defalut", defalut);
            bundle.putIntArray("selectDefaluts", selectDefaluts);
            bundle.putInt("count", count);
            bundle.putSerializable("data", data);
            bundle.putInt("PICK_TYPE", pickType);
            bundle.putInt("date_time", dateTime);
            PickerDialogFragment fragment = new PickerDialogFragment();
            fragment.setArguments(bundle);
            return fragment;
        }
    }

    /**
     * 给面板内容区添加view
     *
     * @param count 需要有几列wheelview  最多5个
     * @param datas
     */
    private void setDatas(int[] selectDefaluts,int defalut, int count, ArrayList<ArrayList<String>> datas) {
        if (linearlayout == null)
            return;
        //如果是 多级联动的 但是就一列  直接取消回调
        if (count == 1)
            linearlayout.setwheelViewSelect(null);
        else linearlayout.setwheelViewSelect(this);
        //最多5列 wheelview吧
        if (count >= 5)
            throw new IndexOutOfBoundsException("the param count must less than 5.");
        //有几列 就要有几个arrylist 数据
        if (datas == null || datas.size() < count)
            throw new IndexOutOfBoundsException("the param datas must not be null and its length must be equal to count.");
        linearlayout.initWheelSetDatas(selectDefaluts,defalut, count, datas);

    }

    public void resfreshData(int index, ArrayList<String> newDatas) {
        linearlayout.resfreshData(index, newDatas);

    }

    @Override
    public void endSelect(WheelView wheelView, int id, String text) {
        //if (wheelView.getId() == 0 && user != null)
        //    refreshPositionForCampus(id);
        //因为multicallback  需要多级联动 在 每一列的 wheelview  选中 后 都会回调这个联动方法 到业务逻辑界面
        if (multicallback != null) {
            ArrayList endSelect = multicallback.endSelect(wheelView.getId(), id, text);
            resfreshData(wheelView.getId() + 1, endSelect);
        }
    }

    private Callback callback;

    // private PickCallback pickcallback;

    private MultilinkPickCallback multicallback;

    @Override
    public void onClick(View view) {
        this.dismiss();
        if (view.getId() == R.id.ok) {
            if (picker != null) {//1这个1  可以直接转换成时间戳
                String[] pickedTimeExt = picker.getPickedTimeExt();
                if (callback != null)

                    callback.onPickResult(DateUtil.date2Second(pickedTimeExt[0]), pickedTimeExt);
            } else if (linearlayout != null) {
                String[] selectResult = linearlayout.getSelectResult();
                if (callback != null)
                    callback.onPickResult(linearlayout.getSelectResultId(), selectResult);
                else if (multicallback != null)
                    multicallback.onPickResult(linearlayout.getSelectResultId(), selectResult);
            }
        }

    }

    /*普通单列表 或者 年月日 这样的 设置这个监听即可*/
    public interface Callback<T> {
        void onPickResult(T selectIds, String... result);
    }

    public void addCallback(Callback callback1) {
        this.callback = callback1;
    }

    /*适用于 PickerDialogfragment new 出来之后 在填充数据 需要wheel之间联动的*/
    public interface MultilinkPickCallback<T> extends Callback<T> {
        ArrayList<String> endSelect(int colum, int selectPosition, String text);
    }

    public void addMultilinkPickCallback(MultilinkPickCallback callback1) {
        this.multicallback = callback1;
    }
}
