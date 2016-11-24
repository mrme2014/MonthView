package com.ishow.ischool.business.student.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.commonlib.util.UIUtil;
import com.ishow.ischool.R;
import com.ishow.ischool.application.Env;
import com.ishow.ischool.util.QRCodeUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by abel on 16/11/21.
 */

public class QrcodeFragment extends DialogFragment {
    private Unbinder unbinder;

    @BindView(R.id.qrcode_close)
    ImageView qrcodeCloseIv;
    @BindView(R.id.qrcode)
    ImageView qrcodeIv;
    @BindView(R.id.qrtext)
    TextView qrtextTv;

    public static QrcodeFragment newInstance(int studentId) {

        Bundle args = new Bundle();
        args.putInt("student_id", studentId);
        QrcodeFragment fragment = new QrcodeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_qrcode, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpView();
    }

    private void setUpView() {
        Bundle bundle = getArguments();
        int studentId = bundle.getInt("student_id", 0);
        qrcodeIv.setImageBitmap(QRCodeUtil.encodeAsBitmap(Env.getApplyUrl() + studentId, UIUtil.dip2px(getContext(), 100f), UIUtil.dip2px(getContext(), 100f)));
    }

    @OnClick({R.id.qrcode_close})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.qrcode_close:
                dismiss();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
