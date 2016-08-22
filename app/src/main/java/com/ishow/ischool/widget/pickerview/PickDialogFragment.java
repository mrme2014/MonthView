package com.ishow.ischool.widget.pickerview;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
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
public class PickDialogFragment extends DialogFragment implements View.OnClickListener, PickerWheelViewLinearlayout.wheelViewSelect {
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

    private static final int PICK_TYPE_DATE = 0;

    private static final int PICK_TYPE_OTHERS = 1;

    private static int PICK_TITLE = -1;

    private int PICK_TYPE = PICK_TYPE_DATE;

    private View contentView;
    private User user;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getContext());
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            PICK_TYPE = bundle.getInt("PICK_TYPE");
            PICK_TITLE = bundle.getInt("PICK_TITLE");
        }
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
        if (PICK_TITLE != -1) title.setText(getContext().getString(PICK_TITLE));

        Dialog dialog = getDialog();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(contentView);
        Window window = dialog.getWindow();
        window.setLayout(-1, -2);
        WindowManager.LayoutParams params = window.getAttributes();
        params.windowAnimations = R.style.Select_dialog_windowAnimationStyle;
        window.setAttributes(params);

        if (picker != null) {
            picker.setDate(new Date().getTime());
        }

        if ( linearlayout!=null) linearlayout.setwheelViewSelect(this);
        return contentView;
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
                        callback.onPickCallback(DateUtil.date2UnixTime(pickedTimeExt[0]), pickedTimeExt);
                    }
                    //这个1  就不一定了  可以是时间戳 或者是 列表中 position等等。

                    else if (linearlayout != null) {
                        String[] selectResult = linearlayout.getSelectResult();
                        //这个 回调的判断 是对 角色切换 返回 选中的 职位名称 和 Position 对象的
                        if (selectResult != null  && user != null) {
                            String s = selectResult[selectResult.length-1];
                            callback.onPickCallback(getSelectPosition(s), selectResult);
                        } else callback.onPickCallback(linearlayout.getSelectResultId(), selectResult);

                    }
                }
                break;
        }
    }

    public void renderCampusPositionselectPanel(User user) {
        this.user = user;
        if (user == null)
            return;
        //UserManager.getInstance().initCampusPositions(user);

        List<Campus> campus = user.campus;

        if (campus == null) return;

        if (campus.size() == 1) {
            ArrayList<String> positions = campus.get(0).positions;
            setDatas(0, 1, positions);
            if (linearlayout!=null)linearlayout.setwheelViewSelect(null);
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
     * 给pop内容区添加view
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
        if (wheelView.getId() == 0&&user!=null)
            refreshPositionForCampus(id);
    }

    private PickCallback callback;

    public interface PickCallback<T> {
        /*object 这个参数 在众多筛选条件中 UI界面上显示的是 string[] result  但服务器需要 int id,或者时间戳什么的.*/
        void onPickCallback( T object, String... result);
    }

    public void addPickCallback(PickCallback callback1) {
        this.callback = callback1;
    }
}
