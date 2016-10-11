package com.ishow.ischool.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.ishow.ischool.R;
import com.ishow.ischool.widget.custom.FmItemTextView;
import com.ishow.ischool.widget.pickerview.PickerDialogFragment;

/**
 * Created by MrS on 2016/10/11.
 */

public class TimeSeletByUserDialog extends DialogFragment implements View.OnClickListener {
    private Dialog dialog;
    private FmItemTextView startTime;
    private FmItemTextView endTime;
    private TextView cancel;
    private TextView Ok;
    private int end_time;
    private int start_time;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getContext());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View contentView = View.inflate(getContext(), R.layout.time_picker_custom_user, null);
        dialog.setContentView(contentView);

        startTime = (FmItemTextView) contentView.findViewById(R.id.start_time);
        endTime = (FmItemTextView) contentView.findViewById(R.id.end_time);
        cancel = (TextView) contentView.findViewById(R.id.cancel);
        Ok = (TextView) contentView.findViewById(R.id.ok);
        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);
        cancel.setOnClickListener(this);
        Ok.setOnClickListener(this);
        return dialog;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.start_time:
                showDatePickDialog(true);
                break;
            case R.id.end_time:
                showDatePickDialog(false);
                break;
            case R.id.cancel:
                this.dismiss();
                break;
            case R.id.ok:
                if (end_time < start_time) {
                    if (callback!=null){
                        callback.onEorr((getString(R.string.end_le_start)));
                    }
                    break;
                }
                if (startTime.getTipTxt() == null || startTime.getTipTxt() == ""||startTime.getTipTxt() == getString(R.string.select_start_time)) {
                    if (callback!=null){
                        callback.onEorr((getString(R.string.start_nonull)));
                    }
                    break;
                }
                if (endTime.getTipTxt() == "" || endTime.getTipTxt() == null||endTime.getTipTxt()== getString(R.string.select_end_time)) {
                    if (callback!=null){
                        callback.onEorr((getString(R.string.end_nonull)));
                    }
                    break;
                }
                if (callback!=null){
                    callback.onResult(start_time,end_time);
                }
                this.dismiss();
                break;
        }
    }

    private void showToast(String string) {
        Snackbar.make(startTime, string, Snackbar.LENGTH_SHORT).show();
    }

    private void showDatePickDialog(final boolean pick_start) {
        PickerDialogFragment.Builder builder = new PickerDialogFragment.Builder();
        builder.setBackgroundDark(true).setDialogTitle(R.string.choose_date).setDialogType(PickerDialogFragment.PICK_TYPE_DATE);
        PickerDialogFragment fragment = builder.Build();
        fragment.show(getChildFragmentManager());
        fragment.addCallback(new PickerDialogFragment.Callback<Integer>() {
            @Override
            public void onPickResult(Integer selectIds, String... result) {
                if (pick_start) {
                    startTime.setTipTxt(result[0]);
                    start_time = selectIds;
                } else {
                    end_time = selectIds;
                    endTime.setTipTxt(result[0]);
                }
            }
        });
    }

    OnSelectResultCallback callback;

    public interface OnSelectResultCallback {
        void onResult(int start_time, int end_time);
        void onEorr(String error);
    }

    public void setOnSelectResultCallback(OnSelectResultCallback callback1) {
        this.callback =callback1;
    }
}
