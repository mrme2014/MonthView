package com.ishow.ischool.widget.custom;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.commonlib.widget.LabelTextView;
import com.ishow.ischool.R;
import com.ishow.ischool.widget.pickerview.PickerDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by abel on 16/8/23.
 */
public class CommunEditDialog extends DialogFragment {

    public static final int max_length = 200;
    @BindView(R.id.dialog_title)
    TextView dialogTitle;

    private OnClickListener listener;
    public int date;
    //public String dateStr;

    @BindView(R.id.dialog_cancel)
    TextView cancelTv;

    @BindView(R.id.dialog_sure)
    TextView sureTv;

    @BindView(R.id.dialog_content)
    EditText contentEt;

    @BindView(R.id.dialog_date)
    LabelTextView dateLtv;

    @BindView(R.id.dialog_content_count)
    TextView contentCountTv;
    private PickerDialogFragment dateDialog;
    private String content;
    private boolean noNeedDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_commun_edit_fragment, container, false);
        ButterKnife.bind(this, view);

        Bundle arguments = getArguments();
        if (arguments != null) {
            content = arguments.getString("content");
            noNeedDate = arguments.containsKey("needDate");
        }
        initViews();
        return view;
    }

    private void initViews() {
        contentEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() >= max_length + 1) {
                    contentCountTv.setText(0 + "");
                    String s = editable.toString();
                    contentEt.setText(s.substring(0, max_length));
                    contentEt.setSelection(max_length);
                } else {
                    contentCountTv.setText((max_length - editable.length()) + "");
                    contentEt.setSelection(editable.length());
                }
            }
        });

        if (content != "" && content != null)
            contentEt.setText(content);
        setNeedDate(noNeedDate);

    }

    @OnClick({R.id.dialog_date, R.id.dialog_sure, R.id.dialog_cancel})
    void onClickDate(View view) {
        switch (view.getId()) {
            case R.id.dialog_date:
                PickerDialogFragment.Builder builder = new PickerDialogFragment.Builder();
                builder.setBackgroundDark(true).setDialogTitle(R.string.choose_date).setDialogType(PickerDialogFragment.PICK_TYPE_DATE);
                dateDialog = builder.Build();
                dateDialog.show(getActivity().getSupportFragmentManager(), "dialog");
                dateDialog.addCallback(new PickerDialogFragment.Callback<Integer>() {
                    @Override
                    public void onPickResult(Integer object, String... result) {
                        date = (int) object;
                        dateLtv.setText(result[0]);
                    }
                });
                break;
            case R.id.dialog_sure:
                if (!noNeedDate) {
                    if (TextUtils.isEmpty(contentEt.getText().toString()) || date == 0) {
                        Toast.makeText(getContext(), R.string.check_commun_add, Toast.LENGTH_LONG).show();
                    } else {
                        if (listener != null) {
                            listener.onClick(contentEt.getText().toString(), date);
                            dismiss();
                        }
                    }
                }else{
                    if (TextUtils.isEmpty(contentEt.getText().toString())) {
                        Toast.makeText(getContext(), R.string.check_commun_add, Toast.LENGTH_LONG).show();
                    } else {
                        if (listener != null) {
                            listener.onClick(contentEt.getText().toString(), date);
                            dismiss();
                        }
                    }
                }

                break;
            case R.id.dialog_cancel:
                dismiss();
                break;
        }

    }


    public CommunEditDialog setOnClickListener(OnClickListener listener) {
        this.listener = listener;
        return this;
    }

    public void setNeedDate(boolean noNeedDate) {
        if (noNeedDate) {
            dateLtv.setLayoutParams(new RelativeLayout.LayoutParams(0,0));
            dialogTitle.setText(getString(com.commonlib.R.string.str_add_beizhu));
        }
    }

    public interface OnClickListener {
        void onClick(String content, long date);
    }
}
