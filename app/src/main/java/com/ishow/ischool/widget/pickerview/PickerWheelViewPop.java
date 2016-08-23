package com.ishow.ischool.widget.pickerview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
 * Created by MrS on 2016/8/12.
 * <p>
 * <p>
 * 这里动态 生成 几列WheelView 适用于大多数的选择 功能，，，年月日的生日筛选 在另一个 timePicker里面
 * <p>
 * 分开来写逻辑省很多代码
 */
public class PickerWheelViewPop extends PopupWindow implements View.OnClickListener, PickerWheelViewLinearlayout.wheelViewSelect, PopupWindow.OnDismissListener {

    private PickerWheelViewLinearlayout viewById;
    private TimePicker timePicker;
    private Context context;
    private User user;

    public PickerWheelViewPop(Context context) {
        super(context);
        init(context);
    }

    public PickerWheelViewPop(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PickerWheelViewPop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public PickerWheelViewPop(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        setOutsideTouchable(false);
        setFocusable(true);
        setOnDismissListener(this);
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
            if (viewById!=null)viewById.setwheelViewSelect(null);
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
     * 年月日选择类型的 界面
     *
     * @param
     * @param //面板中间的 标题 string资源ｉｄ  -1代表不显示标题
     */

    public void renderYMDPanel(int titleResId) {
        renderPanel(titleResId, R.layout.time_picker_linearlayout);
        timePicker.setDate(new Date().getTime());
    }

    /**
     * 这个所能展示的类型比较多了   时+分+秒  礼拜几+时+分  上午+时+分， 已报名+年+月
     *
     * @param
     * @param //面板中间的 标题 string资源ｉｄ  -1代表不显示标题
     */
    public void initMultiSelectPanel( int titleResId) {
        renderPanel(titleResId, R.layout.time_picker_controller);
    }

    private void renderPanel(int titleResId, int layResId) {
        this.setOutsideTouchable(true);
        this.setAnimationStyle(R.style.Select_dialog_windowAnimationStyle);

        View contentView = LayoutInflater.from(context).inflate(layResId, null);

        if (layResId == R.layout.time_picker_controller) {

            viewById = (PickerWheelViewLinearlayout) contentView.findViewById(R.id.PickerWheelViewLinearlayout);
            viewById.setwheelViewSelect(this);

        } else if (layResId == R.layout.time_picker_linearlayout)
            timePicker = (TimePicker) contentView.findViewById(R.id.picker);

        View cancel = contentView.findViewById(R.id.cancel);
        View ok = contentView.findViewById(R.id.ok);
        cancel.setOnClickListener(this);
        ok.setOnClickListener(this);
        TextView textView = (TextView) contentView.findViewById(R.id.title);
        if (titleResId != -1) textView.setText(context.getString(titleResId));

      /*  WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        params.gravity= Gravity.BOTTOM;
        params.verticalMargin=2000;
        contentView.setLayoutParams(params);*/

        setContentView(contentView);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);


        setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        //backgroundAlpha(0.5f);
    }

    /**
     * 给pop内容区添加view
     *
     * @param count 需要有几列wheelview  最多5个
     * @param datas
     */
    public void setDatas(int defalut, int count, ArrayList<String>... datas) {
        if (count >= 5)
            throw new IndexOutOfBoundsException("the param count must less than 5.");
        /*if (datas==null||datas.length<count)
            throw new IndexOutOfBoundsException("the param datas must not be null and its length must be equal to count.");*/
      //  viewById.initWheelSetDatas(defalut, count, datas);
    }

    /**
     * 更改某一个wheelview的数据
     *
     * @param index    界面中从左到右第几个wheelview  从0开始
     * @param newDatas 新的数据源
     */
    public void resfreshData(int index, ArrayList<String> newDatas) {
        viewById.resfreshData(index, newDatas);
    }

    @Override
    public void onClick(View v) {
        this.dismiss();
        if (v.getId() == R.id.ok) {
            if (callback != null) {
                if (timePicker != null) {//1这个1  可以直接转换成时间戳
                    String[] pickedTimeExt = timePicker.getPickedTimeExt();
                    callback.onPickCallback(DateUtil.date2UnixTime(pickedTimeExt[0]), pickedTimeExt);
                }
                //这个1  就不一定了  可以是时间戳 或者是 列表中 position等等。

                else if (viewById != null) {
                    String[] selectResult = viewById.getSelectResult();
                    //这个 回调的判断 是对 角色切换 返回 选中的 职位名称 和 Position 对象的
                    if (selectResult != null  && user != null) {
                        String s = selectResult[selectResult.length-1];
                        callback.onPickCallback(getSelectPosition(s), selectResult);
                    } else callback.onPickCallback(viewById.getSelectResultId(), selectResult);

                }
            }
        }
    }

    @Override
    public void endSelect(WheelView wheelView, int id, String text) {
        if (wheelView.getId() == 0&&user!=null)
            refreshPositionForCampus(id);
    }

    private PickCallback callback;

    @Override
    public void onDismiss() {
        //backgroundAlpha(1f);
    }

    public interface PickCallback<T> {
        /*object 这个参数 在众多筛选条件中 UI界面上显示的是 string[] result  但服务器需要 int id,或者时间戳什么的.*/
        void onPickCallback( T object, String... result);
    }

    public void addPickCallback(PickCallback callback1) {
        this.callback = callback1;
    }


    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = ((Activity)context).getWindow().getAttributes();
        lp.alpha = bgAlpha;
        ((Activity)context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        ((Activity)context).getWindow().setAttributes(lp);
    }
}
