package monthview.ishow.com.monthview.calendar;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;

import monthview.ishow.com.monthview.R;
import monthview.ishow.com.monthview.base.BaseListDilaogFragment;
import monthview.ishow.com.monthview.decoration.MonthDecoration;
import monthview.ishow.com.monthview.pull.BaseViewHolder;

/**
 * Created by MrS on 2016/12/27.
 */

public class ScheduleMenuDialogFragment extends BaseListDilaogFragment {
    private int month, year;
    private int dataCounts, startYear2016 = 2016;
    private MonthDecoration monthDecoration;

    @Override
    protected void onDialogCreate() {
        super.onDialogCreate();
        Window window = getDialog().getWindow();
        window.setLayout(-2, -1);
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.LEFT;
        window.setAttributes(params);

        Calendar instance = Calendar.getInstance();
        month = instance.get(Calendar.MONTH) + 1;
        year = instance.get(Calendar.YEAR);
        year = 2017;
        dataCounts = (year % startYear2016) * 12 + month + 12;//当前月份往前推到2016年1月份，在当前月后推12个月的数量

        recycler.onRefreshCompleted();
        RecyclerView recyclerView = recycler.getRecyclerView();
        recyclerView.setLayoutParams(new RelativeLayout.LayoutParams(-2, -1));
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int needScroll = (year - startYear2016) * 12 + month - 1;//当前月跟2016年1月份相隔多少
        manager.scrollToPositionWithOffset(needScroll, monthDecoration.getExtraPadding());

        //LogUtil.e("onDialogCreate" + needScroll);
    }

    @Override
    protected int getDataCounts() {
        return dataCounts;
    }

    @Override
    protected int getDialogTheme() {
        return Theme.SLIDE_FROM_TO_LEFT_THEME;
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        monthDecoration = new MonthDecoration(getContext());
        return monthDecoration;
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.activity_schedule_month_list_item, parent, false));
    }

    @Override
    protected void onListRefresh(int action) {

    }

    class ViewHolder extends BaseViewHolder {

        private TextView itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = (TextView) itemView;
        }

        @Override
        public void onBindViewHolder(int position) {
            position = position % 12 + 1;
            itemView.setText(String.valueOf(position));
        }

        @Override
        public void onItemClick(View view, int position) {
            if (callback != null)
                callback.onSelected(startYear2016 + (position + 1) / 12, (position + 1) % 12);
        }
    }

    public interface onScheduleSelectCallback {
        void onSelected(int year, int month);
    }

    private onScheduleSelectCallback callback;

    public void addonScheduleSelectCallback(onScheduleSelectCallback callback1) {
        callback = callback1;
    }
}
