package com.ishow.ischool.widget.custom;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.commonlib.widget.LabelTextView;
import com.ishow.ischool.R;
import com.ishow.ischool.util.AppUtil;
import com.ishow.ischool.widget.pickerview.PickerWheelViewPop;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by abel on 16/8/23.
 */
public class CommunEditDialog extends DialogFragment {

    public static final int max_length = 200;

    private OnClickListener listener;
    public int date;
    public String dateStr;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_commun_edit_fragment, container, false);
        ButterKnife.bind(this, view);

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
                contentCountTv.setText((max_length - editable.length()) + "");
            }
        });
    }

    @OnClick({R.id.dialog_date, R.id.dialog_sure, R.id.dialog_cancel})
    void onClickDate(View view) {
        switch (view.getId()) {
            case R.id.dialog_date:
                AppUtil.showTimePickerDialog(getActivity(), view, new PickerWheelViewPop.PickCallback() {

                    @Override
                    public void onPickCallback(Object object, String... result) {
                        date = (int) object;
                        dateLtv.setText(result[0]);
                    }
                });
                break;
            case R.id.dialog_sure:
                if (listener != null) {
                    listener.onClick(contentEt.getText().toString(), date);
                }
                dismiss();
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

    public interface OnClickListener {
        void onClick(String content, long date);
    }
}
