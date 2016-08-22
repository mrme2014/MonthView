package com.ishow.ischool.widget.pickerview;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.commonlib.util.DateUtil;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.user.Campus;
import com.ishow.ischool.bean.user.Position;
import com.ishow.ischool.bean.user.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by MrS on 2016/8/22.
 */
public class PickerDialogFragment extends DialogFragment implements View.OnClickListener, PickerWheelViewLinearlayout.wheelViewSelect {
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
    private User user;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if (bundle != null) {
            PICK_TYPE = bundle.getInt("PICK_TYPE");
            PICK_TITLE = bundle.getInt("PICK_TITLE");
            PICK_THEME = bundle.getInt("PICK_THEME");
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
            picker.setDate(new Date().getTime());
        }

        if (linearlayout != null) {
            linearlayout.setwheelViewSelect(this);
        }

        //if (callback!=null&&callback instanceof PickCallback||callback instanceof MultilinkPickCallback)callback.onDialogCreatCompelete();
        if (pickcallback != null) pickcallback.onDialogCreatCompelete();
        else if (multicallback != null) multicallback.onDialogCreatCompelete();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                this.dismiss();
                break;
            case R.id.ok:
                this.dismiss();
                if (callback != null) {
                    if (picker != null) {//1这个1  可以直接转换成时间戳
                        String[] pickedTimeExt = picker.getPickedTimeExt();
                        callback.onPickResult(DateUtil.date2UnixTime(pickedTimeExt[0]), pickedTimeExt);
                    }
                    //这个1  就不一定了  可以是时间戳 或者是 列表中 position等等。

                    else if (linearlayout != null) {
                        String[] selectResult = linearlayout.getSelectResult();
                        //这个 回调的判断 是对 角色切换 返回 选中的 职位名称 和 Position 对象的
                        if (selectResult != null && user != null) {
                            String s = selectResult[selectResult.length - 1];
                            callback.onPickResult(getSelectPosition(s), selectResult);
                        } else
                            callback.onPickResult(linearlayout.getSelectResultId(), selectResult);

                    }
                }
                break;
        }
    }

    public void renderPanel(User user) {
        this.user = user;
        if (user == null)
            return;
        //UserManager.getInstance().initCampusPositions(user);

        List<Campus> campus = user.campus;

        if (campus == null) return;

        if (campus.size() == 1) {
            ArrayList<String> positions = campus.get(0).positions;
            setDatas(0, 1, positions);
            if (linearlayout != null) linearlayout.setwheelViewSelect(null);
        } else {
            ArrayList<String> campusList = new ArrayList<>();
            for (int i = 0; i < campus.size(); i++) {
                campusList.add(campus.get(i).name);
            }

            setDatas(0, 2, campusList, campus.get(0).positions);
        }
    }

    private void refreshPositionForCampus(int index) {
        if (user != null) {
            List<Campus> campus = user.campus;
            if (campus != null) resfreshData(1, campus.get(index).positions);
        }
    }

    private Position getSelectPosition(String selectTxt) {
        if (user != null) {
            List<Position> position = user.position;
            if (position == null)
                return null;
            for (int i = 0; i < position.size(); i++) {
                if (TextUtils.equals(selectTxt, position.get(i).title))
                    return position.get(i);
            }
        }
        return null;
    }

    /**
     * 给面板内容区添加view
     *
     * @param count 需要有几列wheelview  最多5个
     * @param datas
     */
    public void setDatas(int defalut, int count, ArrayList<String>... datas) {
        if (linearlayout == null)
            return;
        if (count >= 5)
            throw new IndexOutOfBoundsException("the param count must less than 5.");
        if (datas == null || datas.length < count)
            throw new IndexOutOfBoundsException("the param datas must not be null and its length must be equal to count.");
        linearlayout.initWheelSetDatas(defalut, count, datas);

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
            multicallback.endSelect(wheelView,id,text);
        }
    }

    private Callback callback;

    private PickCallback pickcallback;

    private MultilinkPickCallback multicallback;

    /*普通单列表 或者 年月日 这样的 设置这个监听即可*/
    public interface Callback<T> {
        void onPickResult(T object, String... result);
    }

    /*适用于 PickerDialogfragment new 出来之后 在填充数据 且不需要wheel之间联动的*/
    public interface PickCallback<T> extends Callback<T> {
        /*object 这个参数 在众多筛选条件中 UI界面上显示的是 string[] result  但服务器需要 int id,或者时间戳什么的.*/
        void onDialogCreatCompelete();
    }

    /*适用于 PickerDialogfragment new 出来之后 在填充数据 需要wheel之间联动的*/
    public interface MultilinkPickCallback<T> extends PickCallback<T> {
        void endSelect(WheelView wheelView, int id, String text);
    }

    public void addCallback(Callback callback1) {
        this.callback = callback1;
    }

    public void addPickCallback(PickCallback callback1) {
        this.pickcallback = callback1;
    }

    public void addMultilinkPickCallback(MultilinkPickCallback callback1) {
        this.multicallback = callback1;
    }
}
