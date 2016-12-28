package monthview.ishow.com.monthview.calendar;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import monthview.ishow.com.monthview.R;
import monthview.ishow.com.monthview.uiutil.DateUtil;
import monthview.ishow.com.monthview.base.BasicAdapter;
import monthview.ishow.com.monthview.widget.MonthView;

/**
 * Created by MrS on 2016/12/6.
 */

public class CalendarDialogFragment extends DialogFragment implements RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.calendar_date)
    TextView calendarDate;
    @BindView(R.id.calendar_mornig)
    RadioButton calendarMornig;
    @BindView(R.id.calendar_noon)
    RadioButton calendarNoon;
    @BindView(R.id.calendar_afternoon)
    RadioButton calendarAfternoon;
    @BindView(R.id.calendar_group)
    RadioGroup calendarGroup;
    @BindView(R.id.listview_calendar)
    ListView listviewCalendar;
    @BindView(R.id.calendar_sure)
    RadioButton sure;
    private View rootView;


    private int year, month, day, slot;
    private int checkRadioBtn = -1;
    private calendarAdapter adapter;

    public static CalendarDialogFragment newInstance(int year, int month, int day, int slot) {
        CalendarDialogFragment fragment = new CalendarDialogFragment();
        Bundle args = new Bundle();
        args.putInt("year", year);
        args.putInt("month", month);
        args.putInt("day", day);
        args.putInt("slot", slot);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            year = args.getInt("year");
            month = args.getInt("month");
            day = args.getInt("day");
            slot = args.getInt("slot");

        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getContext(), R.style.Comm_dialogfragment_windowAnimationStyle);
        Window window = dialog.getWindow();
        window.setLayout(-1, -1);
        window.requestFeature(Window.FEATURE_NO_TITLE);
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.activity_open_class_calendar, null);
        dialog.setContentView(rootView);
        dialog.setCanceledOnTouchOutside(true);
        ButterKnife.bind(this, rootView);
        adapter = new calendarAdapter(getContext(), null);
        listviewCalendar.setAdapter(adapter);
        calendarGroup.setOnCheckedChangeListener(this);

        setBtnEnable(false);
        initView();
        return dialog;
    }

    @OnClick(R.id.calendar_cancel)
    void onCalenarCanel() {
        this.dismiss();
    }

    @OnClick(R.id.calendar_clear)
    void onCalenarClear() {
        calendarGroup.clearCheck();
        checkRadioBtn = -1;
        onSelectFragment(-1, -1, -1, adapter.lastSelectMonth, -1);
    }

    private void initView() {
        if (year != 0 && month != 0 && day != 0) {
            calendarDate.setText(getString(R.string.claz_calendar_title, year, month, day));
        }
        if (slot > 0 && slot < 4) {
            ((RadioButton) calendarGroup.getChildAt(slot - 1)).setChecked(true);
        }
    }

    private void setBtnEnable(boolean b) {
        sure.setEnabled(b);
        //sure.setBackgroundColor(getResources().getColor(b ? R.color.comm_blue : R.color.calendar_radiobtn_unSelected));
        sure.setChecked(b);
    }


    @OnClick(R.id.calendar_sure)
    void onSureBtnClick() {
        if (callback != null) {
            callback.onSelect(year, month, day, DateUtil.date2Second(year + "-" + month + "-" + day), checkRadioBtn);
            this.dismiss();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        checkRadioBtn = calendarMornig.isChecked() ? 0 : (calendarNoon.isChecked() ? 1 : 2);
        if (year != 0 && month != 0 && day != 0) {
            setBtnEnable(true);
        } else setBtnEnable(false);

    }

    class calendarAdapter extends BasicAdapter<String> {
        private int lastSelectMonth = -1;

        @Override
        public int getCount() {
            return 13;
        }

        public calendarAdapter(Context context, List<String> datas) {
            super(context, datas);
            if (year != 0 && month != 0) {
                Calendar calendar = Calendar.getInstance();
                int yearNow = calendar.get(Calendar.YEAR);
                int monthNow = calendar.get(Calendar.MONTH) + 1;

                lastSelectMonth = Math.abs((yearNow * 12 + monthNow) - (year * 12 + month));
            }
        }

        @Override
        public View getContentView(final int position, View convertView, ViewGroup parent) {
            viewholder viewholder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.activity_open_class_calendar_item, null);
                viewholder = new viewholder(convertView);
                convertView.setTag(viewholder);
            } else viewholder = (calendarAdapter.viewholder) convertView.getTag();
            //viewholder.monthView.setNeedWeekTitle(true);
            viewholder.monthView.setSelectedDay(year, month, day);
            viewholder.monthView.upMonthDyas(position);
            viewholder.monthView.setOnCalendarDaySelectCallback(new MonthView.onCalendarDaySelectCallback() {
                @Override
                public void onSelect(int year1, int month1, int day1) {
                    onSelectFragment(year1, month1, day1, lastSelectMonth, position);
                    lastSelectMonth = position;
                }
            });
            return convertView;
        }

        class viewholder {
            MonthView monthView;

            public viewholder(View itemView) {
                monthView = (MonthView) itemView.findViewById(R.id.MonthView);
            }
        }
    }

    public void onSelectFragment(int year1, int month1, int day1, int lastSelectMonth, int position) {
        year = year1;
        month = month1;
        day = day1;
        if (year1 == -1 || month1 == -1 || day1 == -1) {
            calendarDate.setTextColor(getContext().getResources().getColor(R.color.calendar_title_unSelected));
            calendarDate.setText(getContext().getString(R.string.history_claz_time));
        } else {
            calendarDate.setText(getString(R.string.claz_calendar_title, year1, month1, day1));
            calendarDate.setTextColor(getContext().getResources().getColor(R.color.txt_2));
        }
        if (lastSelectMonth != -1 && lastSelectMonth != position) {
            int firstVisiblePosition = listviewCalendar.getFirstVisiblePosition();
            if (lastSelectMonth >= firstVisiblePosition) {
                MonthView childAt = (MonthView) listviewCalendar.getChildAt(lastSelectMonth - firstVisiblePosition);
                if (childAt != null) childAt.resetSelectDay();
            } else {
                MonthView childAt = (MonthView) listviewCalendar.getChildAt(lastSelectMonth);
                if (childAt != null) childAt.resetSelectDay();
            }

        }

        if (checkRadioBtn != -1) {
            setBtnEnable(true);
        } else setBtnEnable(false);
    }

    public interface calendarSelectedCallback {
        void onSelect(int year, int month, int day, int unix, int daytime);
    }

    private calendarSelectedCallback callback;

    public void setCalendarSelectedCallback(calendarSelectedCallback callback1) {
        callback = callback1;
    }
}
