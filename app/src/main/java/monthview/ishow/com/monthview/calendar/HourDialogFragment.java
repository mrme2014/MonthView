package monthview.ishow.com.monthview.calendar;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import monthview.ishow.com.monthview.R;
import monthview.ishow.com.monthview.widget.HourView;

/**
 * Created by MrS on 2016/12/7.
 */

public class HourDialogFragment extends DialogFragment implements monthview.ishow.com.monthview.widget.HourView.hourSelectCallback {
    @BindView(R.id.HourView)
    HourView HourView;
    @BindView(R.id.calendar_sure)
    Button button;
    private View rootView;
    private Unbinder unbinder;
    private int hour1;
    private int hour2;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getContext(), R.style.Select_dialog);
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.activity_open_class_hour, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(rootView);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
        dialog.setCanceledOnTouchOutside(true);
        window.setLayout(-1, -2);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        unbinder = ButterKnife.bind(this, rootView);
        setBtnEnable(false);
        HourView.setOnHourSelectCallback(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @OnClick({R.id.hour_cancel, R.id.calendar_sure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.hour_cancel:
                this.dismiss();
                break;
            case R.id.calendar_sure:
                String h1 = "";
                String h2 = "";
                if (hour1 < 10) {
                    h1 = "0" + hour1 + ":00";
                } else {
                    h1 = hour1 + ":00";
                }
                if (hour2 < 10) {
                    h2 = "0" + hour2 + ":00";
                } else {
                    h2 = hour2 + ":00";
                }

                if (callback != null) {
                    callback.onSelected(h1, h2);
                    this.dismiss();
                }
                break;
        }
    }

    private void setBtnEnable(boolean b) {
        button.setEnabled(b);
        button.setAlpha(b ? 1.0f : 0.5f);
        button.setClickable(b);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSelected(int hour1, int hour2) {
        this.hour1 = hour1;
        this.hour2 = hour2;
        if (hour1 >=0 && hour2 > 0)
            setBtnEnable(true);
        else setBtnEnable(false);
    }

    public interface hourSelectCallback {
        void onSelected(String hour1, String hour2);
    }

    private hourSelectCallback callback;

    public void setOnHourSelectCallback(hourSelectCallback callback1) {
        callback = callback1;
    }
}
