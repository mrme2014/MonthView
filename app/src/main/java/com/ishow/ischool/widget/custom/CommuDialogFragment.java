package com.ishow.ischool.widget.custom;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commonlib.util.DateUtil;
import com.commonlib.widget.LabelTextView;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.user.User;
import com.ishow.ischool.business.user.pick.UserPickActivity;
import com.ishow.ischool.util.AppUtil;
import com.ishow.ischool.widget.pickerview.PickerDialogFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by MrS on 2016/8/18.
 */
public class CommuDialogFragment extends DialogFragment {
    @BindView(R.id.commun_state)
    LabelTextView communState;
    @BindView(R.id.commun_date_start)
    LabelTextView communDateStart;
    @BindView(R.id.commun_date_end)
    LabelTextView communDateEnd;
    @BindView(R.id.commun_refuse)
    LabelTextView communRefuse;
    @BindView(R.id.commun_confidence)
    LabelTextView communConfidence;
    @BindView(R.id.commun_order)
    LabelTextView communOrder;
    @BindView(R.id.commun_block_top)
    TextView communBlockTop;
    @BindView(R.id.commun_date_label)
    TextView communDateLabel;
    @BindView(R.id.commu_reset)
    TextView commuReset;
    @BindView(R.id.commu_ok)
    TextView commuOk;
    @BindView(R.id.commun_block_bottom)
    TextView communBlockBottom;
    @BindView(R.id.root)
    LinearLayout root;

    private int statePosition;
    private long startUnix;
    private long endUnix;
    private int refusePosition;
    private int confidencePosition;
    private int orderPosition;

    private Dialog dialog;
    private String orderName;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getContext(), R.style.Comm_dialogfragment_windowAnimationStyle);
        Window win = dialog.getWindow();
        // 一定要设置Background，如果不设置，window属性设置无效
        win.setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        WindowManager.LayoutParams params = win.getAttributes();
        params.gravity = Gravity.TOP;
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        win.setAttributes(params);

        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.popwindow_filter_commu, null);
        //  View viewById = contentView.findViewById(R.id.root);
        // viewById.setTop(UIUtil.getToolbarSize(getContext()));
        ButterKnife.bind(this, contentView);

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(contentView);

        setupView();
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        Window window = dialog.getWindow();
        window.setLayout(-1, -1);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @OnClick({R.id.commun_block_top, R.id.commun_block_bottom, R.id.commun_state, R.id.commun_date_start, R.id.commun_date_end, R.id.commun_refuse, R.id.commun_confidence, R.id.commun_order, R.id.commu_reset, R.id.commu_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.commun_state:
                showPickPop(R.string.comm_dialog_stu_state_title, 0, 1, AppUtil.getStateList(), new PickerDialogFragment.Callback<int[]>() {

                    @Override
                    public void onPickResult(int[] position, String... result) {
                        if (result != null) communState.setText(result[0]);
                        if (position != null) statePosition = position[0] + 1;
                    }
                });
                break;
            case R.id.commun_date_start:
                showTimePickPop(true);
                break;
            case R.id.commun_date_end:
                showTimePickPop(false);
                break;
            case R.id.commun_refuse:
                showPickPop(R.string.commun_refuse, 4, 1, AppUtil.getRefuseList(), new PickerDialogFragment.Callback<int[]>() {

                    @Override
                    public void onPickResult(int[] position, String... result) {
                        if (position != null) refusePosition = position[0] + 1;
                        if (result != null) communRefuse.setText(result[0]);
                    }
                });
                break;
            case R.id.commun_confidence:
                showPickPop(R.string.commun_confidence, 1, 1, AppUtil.getBeliefList(), new PickerDialogFragment.Callback<int[]>() {
                    @Override
                    public void onPickResult(int[] position, String... result) {
                        if (position != null) confidencePosition = position[0] + 1;
                        if (result != null) communConfidence.setText(result[0]);
                    }
                });
                break;
            case R.id.commun_order:
                Intent intent = new Intent(getActivity(), UserPickActivity.class);
                intent.putExtra(UserPickActivity.P_TITLE,getString(R.string.pick_banliren));
                startActivityForResult(intent, UserPickActivity.REQUEST_CODE_PICK_USER);
                break;
            case R.id.commu_reset:
                resetSlectResult();
                break;
            case R.id.commu_ok:
                this.dismiss();
                if (callback != null)
                    callback.onResult(statePosition, confidencePosition, refusePosition, orderPosition, startUnix, endUnix);
                break;

            case R.id.commun_block_top:
            case R.id.commun_block_bottom:
                this.dismiss();
                if (callback != null)
                    callback.cancelDilaog();
                break;
        }
    }

    private void showPickPop(int titleResId, int defalut, int colum, ArrayList<String> datas, PickerDialogFragment.Callback callback) {
        PickerDialogFragment.Builder builder = new PickerDialogFragment.Builder();
        PickerDialogFragment dialog = builder.setDialogTitle(titleResId)
                .setDialogType(PickerDialogFragment.PICK_TYPE_OTHERS)
                .setBackgroundDark(true)
                .setDatas(defalut, colum, datas).Build();
        dialog.show(getChildFragmentManager(), "dialog");
        dialog.addCallback(callback);
    }


    private void resetSlectResult() {
        communState.setText("");
        communDateStart.setText("");
        communDateEnd.setText("");
        communConfidence.setText("");
        communRefuse.setText("");
        communOrder.setText("");
        startUnix = 0;
        endUnix = 0;
        statePosition = 0;
        confidencePosition = 0;
        refusePosition = 0;
        orderPosition = 0;
    }

    private void setupView() {
        communState.setText(AppUtil.getStateById(statePosition));
        if (startUnix > 0) {
            communDateStart.setText(DateUtil.parseSecond2Str(startUnix, "yyyy-MM-dd"));
        }
        if (endUnix > 0) {
            communDateEnd.setText(DateUtil.parseSecond2Str(endUnix, "yyyy-MM-dd"));
        }
        communConfidence.setText(AppUtil.getBeliefById(confidencePosition));
        communRefuse.setText(AppUtil.getRefuseById(refusePosition));
        communOrder.setText(orderName);
    }

    private void showTimePickPop(final boolean start) {
        PickerDialogFragment.Builder builder = new PickerDialogFragment.Builder();
        PickerDialogFragment dialog = builder.setDialogTitle(R.string.commun_date).setDialogType(PickerDialogFragment.PICK_TYPE_DATE).setBackgroundDark(true).Build();
        dialog.show(getChildFragmentManager(), "dialog");
        dialog.addCallback(new PickerDialogFragment.Callback<Integer>() {
            @Override
            public void onPickResult(Integer unix, String... result) {
                if (start) {
                    if (endUnix > 0 && unix > endUnix) {
                        showTimeError();
                        startUnix = 0;
                        communDateStart.setText("");
                    } else {
                        startUnix = unix;
                        if (result != null) communDateStart.setText(result[0]);
                    }
                } else {
                    if (startUnix > 0 && unix < startUnix) {
                        showTimeError();
                        endUnix = 0;
                        communDateEnd.setText("");
                    } else {
                        endUnix = unix + 86399;
                        if (result != null) communDateEnd.setText(result[0]);
                    }
                }
            }
        });
    }

    private selectResultCallback callback;


    public interface selectResultCallback {
        void cancelDilaog();

        void onResult(int statePosition, int confidencePosition, int refusePosition, int orderPosition, long startUnix, long endUnix);
    }

    public void addOnSelectResultCallback(selectResultCallback callback1) {
        this.callback = callback1;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == UserPickActivity.REQUEST_CODE_PICK_USER && data != null) {
                User user = data.getParcelableExtra(UserPickActivity.PICK_USER);
                communOrder.setText(user.userInfo.user_name);
                orderPosition = user.userInfo.user_id;
                orderName = user.userInfo.user_name;
            }
        }
    }

    void showTimeError() {
        final Snackbar snackbar = Snackbar.make(communBlockBottom, getString(R.string.time_error), Snackbar.LENGTH_LONG);
        snackbar.setAction("朕知道了", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }
}
