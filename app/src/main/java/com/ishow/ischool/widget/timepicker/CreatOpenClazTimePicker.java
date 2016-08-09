package com.ishow.ischool.widget.timepicker;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.ishow.ischool.R;
import com.ishow.ischool.util.LogUtil;
import com.ishow.ischool.util.UIUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by MrS on 2016/7/19.
 */
public class CreatOpenClazTimePicker extends PopupWindow implements PopupWindow.OnDismissListener {

    @Bind(R.id.open_claz_date_peroid)
    WheelView openClazDatePeroid;
    @Bind(R.id.open_claz_date_weekday)
    WheelView openClazDateWeekday;
    @Bind(R.id.open_claz_date_hour)
    WheelView openClazDateHour;
    @Bind(R.id.open_claz_date_min)
    WheelView openClazDateMin;
    private int START_YEAR = 1970;
    private int END_YEAR = 2017;
    private int mDay = 2;//默认 选中的天item
    private int defalutSelect = 2;//默认选中item


    private int defalut_start_hour = 6;
    private int mclaz_time_start_hour = 8;//  因为默开始时间是6点 但默认选中时间是8点。。。
    private int mclaz_time_end_hour = 8;// 这节课结束的时间。。。
    private int mclaz_time_start_min = 0;//  因为默开始时间是6点 但默认选中时间是8点。。。
    private int mclaz_time_end_min = 0;// 这节课结束的时间。。。

    public int defalut_start_year = 2017;


    /**
     * 0 选择 开班时间         2016-07-19
     * <p/>
     * 1 选择 上课时间下课时间  礼拜二 05：30--07-30
     * <p/>
     * <p/>
     * <p/>
     * 2 选择 上课日期       2016-07-19  上午
     * <p/>
     * 3 选择 上课日期       2016-07-19-2016-07-20
     */
    private int open_claz_style = 0;

    private boolean mclaz_time_be_start_hour = true;//是否开始时间已经选择过了

    private Calendar mCalendar;
    public int mWeekday_select = 0;//默认选择的星期几
    private int mWeekday_defalut_Now;
    private Context context;

    public CreatOpenClazTimePicker(Context context) {
        super(context);
        init(context);
    }

    public CreatOpenClazTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CreatOpenClazTimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public CreatOpenClazTimePicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        setOnDismissListener(this);
    }


    private void updateDay(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        openClazDateMin.resetData(getDayData(maxDay));
        if (mDay > maxDay) {
            openClazDateMin.setDefault(defalutSelect);
            mDay = defalutSelect + 1;
        } else {
            openClazDateMin.setDefault(mDay - 1);
        }
    }

    public void onCreateView(int open_claz_style) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.time_picker_for_creat_open_claz, null);
        ButterKnife.bind(this, view);
        setContentView(view);

        int width = UIUtil.getScreenWidthPixels(context)-UIUtil.dip2px(context,30);
        setWidth(width);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_round_corner));
        setOutsideTouchable(true);
        setAnimationStyle(R.style.PopupAnimation);

        this.open_claz_style = open_claz_style;
        if (open_claz_style == 1)
            creatWheelViewNeedData_of_timeClaz();
        else if (open_claz_style == 0)
            creatWheelViewNeedData_Of_TimeKaiban();
        else if (open_claz_style == 2)
            creatWheelViewNeedData_Of_CreatOpenClaz_date();
        else if (open_claz_style == 3)
            creatWheelViewNeedData_Of_CreatOpenClaz_validate_date();

        backgroundAlpha((Activity) context, 0.5f);
    }

    //1 选择 上课时间下课时间  礼拜二 05：30--07-30
    private void creatWheelViewNeedData_of_timeClaz() {
        //同样的  设置 上课时间  下课时间
        ArrayList<String> date = new ArrayList<>();
        date.add(context.getString(R.string.open_claz_time_picker_go));
        date.add(context.getString(R.string.open_claz_time_picker_off));
        openClazDatePeroid.setData(date);
        openClazDatePeroid.setOnSelectListener(mPeroidListner);
        openClazDatePeroid.setDefault(0);

        //同样的  设置  礼拜几
        ArrayList<String> weekdays = new ArrayList<>();
        String[] strings = context.getResources().getStringArray(R.array.week_days);
        for (int i = 0; i < strings.length; i++) {
            weekdays.add(strings[i]);
        }
        //得到当前星期几 设置上去  不然 礼拜四 设置个礼拜一 很奇怪
        mCalendar = Calendar.getInstance(Locale.CHINA);
        mWeekday_defalut_Now = mCalendar.get(Calendar.DAY_OF_WEEK);
        openClazDateWeekday.setData(weekdays);
        openClazDateWeekday.setOnSelectListener(mWeekdayListener);
        openClazDateWeekday.setDefault(mWeekday_defalut_Now >= 1 ? mWeekday_defalut_Now - 1 : mWeekday_defalut_Now);
        LogUtil.e(mWeekday_defalut_Now + "");
        mWeekday_select = mWeekday_defalut_Now;

        //同样的  设置 小时
        openClazDateHour.setData(getHourData());
        openClazDateHour.setOnSelectListener(mHourListener);
        openClazDateHour.setDefault(defalutSelect);

        //同样的  设置 分钟
        openClazDateMin.setData(getMimData());
        openClazDateMin.setOnSelectListener(mMinListener);
        openClazDateMin.setDefault(0);


    }

    //  0 选择 开班时间  2016-07-19
    private void creatWheelViewNeedData_Of_TimeKaiban() {
        //这里不需要第一个 了 隐藏第一个
        openClazDatePeroid.setVisibility(View.GONE);
        setYearMonthDay();
    }

    //3 选择 上课日期 07:20--09:20
    private void creatWheelViewNeedData_Of_CreatOpenClaz_validate_date() {
        ArrayList<String> date = new ArrayList<>();
        date.add(context.getString(R.string.open_claz_time_picker_start));
        date.add(context.getString(R.string.open_claz_time_picker_over));

        openClazDatePeroid.setData(date);
        openClazDatePeroid.setOnSelectListener(mPeroidListner);
        openClazDatePeroid.setDefault(0);

       // setYearMonthDay();
        openClazDateWeekday.setVisibility(View.GONE);
        //同样的  设置 小时
        openClazDateHour.setData(getHourData());
        openClazDateHour.setOnSelectListener(mHourListener);
        openClazDateHour.setDefault(defalutSelect);

        //同样的  设置 分钟
        openClazDateMin.setData(getMimData());
        openClazDateMin.setOnSelectListener(mMinListener);
        openClazDateMin.setDefault(0);
    }

    // *  2 选择 上课日期 2016-07-19 上午
    private void creatWheelViewNeedData_Of_CreatOpenClaz_date() {
        //设置 上午 下午
        openClazDatePeroid.setData(getDataOfPeroid2());
        openClazDatePeroid.setOnSelectListener(mPeroidListner);
        openClazDatePeroid.setDefault(0);

        //设置 年份
        setYearMonthDay();
    }

    private void setYearMonthDay() {
        mCalendar = Calendar.getInstance(Locale.CHINA);

        //得到当前年份+1
        END_YEAR = mCalendar.get(Calendar.YEAR);
        //设置 年分
        openClazDateWeekday.setData(getYearData());
        openClazDateWeekday.setOnSelectListener(mYearListner);
        openClazDateWeekday.setDefault(openClazDateWeekday.getListSize() - 1);
        defalut_start_year = END_YEAR;
      //  mclaz_time_start_year = mclaz_time_end_year = defalut_start_year;


        int month = mCalendar.get(Calendar.MONTH);
        //设置 月份
        openClazDateHour.setData(getMonthData());
        openClazDateHour.setOnSelectListener(mMonthListener);
        openClazDateHour.setDefault(month);
       // mclaz_time_start_month = mclaz_time_end_month = month+1;

        //设置 日
        int day = mCalendar.get(Calendar.DAY_OF_MONTH) - 1;
        openClazDateMin.setData(getDayData(mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)));
        openClazDateMin.setOnSelectListener(mDayListener);
        openClazDateMin.setDefault(day);
        //mclaz_time_start_day = mclaz_time_end_day = day+1;
    }

    @OnClick({R.id.open_claz_date_cancel, R.id.open_claz_date_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.open_claz_date_cancel:
                if (event != null)
                    event.onCreatOpenClazPickerCancelEvent();
                break;
            case R.id.open_claz_date_ok:
                /**
                 *  0 选择 开班时间 2016-07-19
                 *
                 *  1 礼拜二 05：30--07-30
                 *
                 *  2 选择 上课日期 2016-07-19上午
                 *
                 *  3 选择 上课日期 2016-07-19-2016-07-20
                 */
                if (open_claz_style == 0) {
                    getResultOfStyle0();
                } else if (open_claz_style == 1)
                    getResultOfStyle1();
                else if (open_claz_style == 2)
                    getResultOfStyle2();
                else if (open_claz_style == 3)
                    getResultOfStyle3();
                break;
        }
    }

    // 3 选择 有效日期   09:20--11:20
    private void getResultOfStyle3() {

        if (mclaz_time_end_hour < mclaz_time_start_hour) {
            showToast(context.getString(R.string.open_claz_time_over_month_less_than_start_month));
            return;
        }
        if (mclaz_time_end_min < mclaz_time_start_min) {
            showToast(context.getString(R.string.open_claz_time_over_month_less_than_start_month));
            return;

        }
        if (mclaz_time_end_hour*3600+mclaz_time_end_min*60-mclaz_time_start_hour*3600-mclaz_time_end_min*60<3600)
        {
            showToast(context.getString(R.string.open_claz_time_valitate_time_too_short));
            return;
        }
        String selectHour_end = getSelectHour(mclaz_time_end_hour);
        String selectMin_end = getSelectHour(mclaz_time_end_min);

        String selectHour_start = getSelectHour(mclaz_time_start_hour);
        String selectMin_start = getSelectHour(mclaz_time_start_min);

        String begin =selectHour_start + ":" + selectMin_start;
        String end = selectHour_end + ":" + selectMin_end;

        if (event != null)
            event.onCreatOpenClazPickerConfirmEvent(begin + "-"+ end,0,begin,end);
    }

    // 2 选择 上课日期      2016-07-19,上午
    private void getResultOfStyle2() {
        String mMonth = getSelectMonth();
        String mDay = getSelectDay();
        String mYear= openClazDateWeekday.getSelectedText().substring(0,4);
        String time_picker =mYear+"-"+mMonth+"-"+mDay;
        int time_type = 1;
        if (openClazDatePeroid.getSelected()==0)time_type=1;
        else if (openClazDatePeroid.getSelected()==1)time_type=2;
        else if (openClazDatePeroid.getSelected()==2)time_type=3;
        if (event != null)
            event.onCreatOpenClazPickerConfirmEvent( time_picker +"，"  +openClazDatePeroid.getSelectedText(),time_type,time_picker,null);

    }

    // *  0 选择 开班时间    2016-07-19
    private void getResultOfStyle0() {
        String mMonth = getSelectMonth();
        String mDay = getSelectDay();
        String mYear = openClazDateWeekday.getSelectedText();
      if (mYear.length()>=4)mYear = mYear.substring(0,4);

        String time_picker =mYear+"-"+mMonth+"-"+mDay;
        if (event!=null)
            event.onCreatOpenClazPickerConfirmEvent(time_picker,0, time_picker,null);
    }

    // 1 礼拜二 05：30--07-30
    private void getResultOfStyle1() {
        if (mWeekday_select < mWeekday_defalut_Now) {
            showToast(context.getString(R.string.open_class_date_is_early));
            //这里默认一节课的最短时间是1小时
        } else if ((mclaz_time_end_hour * 3600 + mclaz_time_end_min * 60 - mclaz_time_start_hour * 3600 - mclaz_time_start_min * 60) < 3600) {
            showToast(context.getString(R.string.open_class_time_too_short));
        } else {
            String start_hour = getSelectHour(mclaz_time_start_hour);
            String start_min = getSelectMin(mclaz_time_start_min);

            String over_hour = getSelectHour(mclaz_time_end_hour);
            String over_min = getSelectMin(mclaz_time_end_min);

            String weekday = openClazDateWeekday.getSelectedText();
            if (event != null) {
                String begin_Time = start_hour + ":" + start_min;
                String end_time   = over_hour+ ":" + over_min;
                event.onCreatOpenClazPickerConfirmEvent(weekday + " " + begin_Time + "-" + end_time ,0,begin_Time,end_time);
            }
        }
    }

    @NonNull
    private String getSelectDay() {
        int day = openClazDateMin.getSelected();
        return day > 10 ? (day + 1) + "" : "0" + (day + 1);
    }

    @NonNull
    private String getSelectMonth() {
        int month = openClazDateHour.getSelected();
        return month > 10 ? (month + 1) + "" : "0" + (month + 1);
    }

    @NonNull
    private String getSelectHour(int hour) {
        return hour > 10 ? hour + "" : "0" + hour;
    }

    @NonNull
    private String getSelectMin(int min) {
        return min > 10 ? min + "" : "0" + min;
    }

    private ArrayList<String> getYearData() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = START_YEAR; i <= END_YEAR; i++) {
            list.add(i + "年");
        }
        return list;
    }


    private ArrayList<String> getMonthData() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 1; i <= 12; i++) {
            list.add(i + "月");
        }
        return list;
    }

    private ArrayList<String> getDayData(int endDay) {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 1; i <= endDay; i++) {
            list.add(i + "日");
        }
        return list;
    }

    private ArrayList<String> getHourData() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 6; i < 22; i++) {
            list.add(i + "时");
        }
        return list;
    }

    private ArrayList<String> getMimData() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 60; i++) {
            list.add(i + "分");
        }
        return list;
    }

    private ArrayList<String> getDataOfPeroid2() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("上午");
        list.add("下午");
        list.add("晚上");
        return list;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    updateDay(mMonth);
                    break;
                case 1:
                    // openClazDateWeekday.setEnable(false);
                    mclaz_time_be_start_hour = false;
                    // showToast(context.getString(R.string.open_claz_time_week_interceput));
                    break;
                case 2:
                    //  openClazDateWeekday.setEnable(true);
                    //  showToast(context.getString(R.string.open_claz_time_week_no_interceput));
                    mclaz_time_be_start_hour = true;
                    break;
            }
        }
    };
    private WheelView.OnSelectListener mPeroidListner = new WheelView.OnSelectListener() {
        @Override
        public void endSelect(int day, String text) {
            if (day == 0) {
                handler.sendEmptyMessage(2);
            } else {
                handler.sendEmptyMessage(1);
            }
        }

        @Override
        public void selecting(int day, String text) {
        }
    };

    private WheelView.OnSelectListener mWeekdayListener = new WheelView.OnSelectListener() {

        @Override
        public void endSelect(int mWeekday, String text) {
            mWeekday_select = mWeekday;
        }

        @Override
        public void selecting(int id, String text) {

        }
    };
    private WheelView.OnSelectListener mHourListener = new WheelView.OnSelectListener() {
        @Override
        public void endSelect(int hour, String text) {
            if (mclaz_time_be_start_hour) {
                mclaz_time_start_hour = defalut_start_hour + hour;
            } else {
                mclaz_time_end_hour = defalut_start_hour + hour;
            }
            LogUtil.e(hour + "===" + mclaz_time_start_hour + "+" + mclaz_time_start_min + "---" + mclaz_time_end_hour + "+" + mclaz_time_end_min);
        }

        @Override
        public void selecting(int id, String text) {

        }
    };

    private WheelView.OnSelectListener mMinListener = new WheelView.OnSelectListener() {
        @Override
        public void endSelect(int min, String text) {
            if (mclaz_time_be_start_hour) {
                mclaz_time_start_min = min;
            } else {
                mclaz_time_end_min = min;
            }
            LogUtil.e(min + "===" + mclaz_time_start_hour + "+" + mclaz_time_start_min + "---" + mclaz_time_end_hour + "+" + mclaz_time_end_min);
        }

        @Override
        public void selecting(int day, String text) {
        }
    };

    WheelView.OnSelectListener mYearListner = new WheelView.OnSelectListener() {


        @Override
        public void endSelect(int id, String text) {
           /*  if (mclaz_time_be_start_hour)
               mclaz_time_start_year = defalut_start_year - (openClazDateWeekday.getListSize() - id);
            else
                mclaz_time_end_year = defalut_start_year - (openClazDateWeekday.getListSize() - id);*/
        }

        @Override
        public void selecting(int id, String text) {

        }
    };

    private int mMonth;
    private WheelView.OnSelectListener mMonthListener = new WheelView.OnSelectListener() {
        @Override
        public void endSelect(int month, String text) {
            mMonth = month;
            handler.sendEmptyMessage(0);
//            if (mclaz_time_be_start_hour) mclaz_time_start_month = mMonth;
//            else mclaz_time_end_month = mMonth;
        }

        @Override
        public void selecting(int id, String text) {
        }
    };
    private WheelView.OnSelectListener mDayListener = new WheelView.OnSelectListener() {
        @Override
        public void endSelect(int day, String text) {
            mDay = day + 1;
            /*if (mclaz_time_be_start_hour) mclaz_time_start_day = mDay;
            else mclaz_time_end_day = mDay;*/
        }

        @Override
        public void selecting(int day, String text) {
        }
    };

    private void showToast(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    private onCreatOpenClazPickerEvent event;

    public void setonCreatOpenClazPickerEvent(onCreatOpenClazPickerEvent e) {
        this.event = e;
    }

    @Override
    public void onDismiss() {
        backgroundAlpha((Activity) context, 1f);
        ButterKnife.unbind(this);
    }

    public interface onCreatOpenClazPickerEvent {
        //  UI显示的 time_picker
        //  1早上 2下午 3晚上
        //  开始时间戳  秒
        //   结束时间戳 秒
        void onCreatOpenClazPickerConfirmEvent(String time_picker, int time_type, String beginTime, String endTime);

        void onCreatOpenClazPickerCancelEvent();
    }


    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

}
