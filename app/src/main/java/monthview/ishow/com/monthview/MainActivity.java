package monthview.ishow.com.monthview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import monthview.ishow.com.monthview.calendar.CalendarDialogFragment;
import monthview.ishow.com.monthview.calendar.HourDialogFragment;
import monthview.ishow.com.monthview.calendar.ScheduleMenuDialogFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.calendar)
    void onCalendarClick() {
        CalendarDialogFragment fragment =  CalendarDialogFragment.newInstance(2017,1,12,2);
        fragment.show(getSupportFragmentManager(), "dialog");
    }

    @OnClick(R.id.hours)
    void onHoursClick() {
        HourDialogFragment fragment = new HourDialogFragment();
        fragment.show(getSupportFragmentManager(), "dialog");
    }

    @OnClick(R.id.schedule)
    void onScheduleClick() {
        ScheduleMenuDialogFragment fragment = new ScheduleMenuDialogFragment();
        fragment.show(getSupportFragmentManager(), "dialog");
    }
}
