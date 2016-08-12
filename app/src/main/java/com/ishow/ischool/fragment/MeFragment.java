package com.ishow.ischool.fragment;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ishow.ischool.R;
import com.ishow.ischool.common.base.presenter.impl.BasePresenter;
import com.ishow.ischool.common.base.view.impl.BaseFragment;
import com.ishow.ischool.widget.custom.CircleImageView;
import com.ishow.ischool.widget.custom.FmItemTextView;
import com.ishow.ischool.widget.custom.SelectDialogFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by abel on 16/8/8.
 */
public class MeFragment extends BaseFragment {
    @BindView(R.id.fm_me_header_avart)
    CircleImageView fmMeHeaderAvart;
    @BindView(R.id.fm_me_header_name)
    TextView fmMeHeaderName;
    @BindView(R.id.fm_me_header_job)
    TextView fmMeHeaderJob;

    @BindView(R.id.fm_me_switch_campus)
    FmItemTextView fmMeSwitchCampus;
    @BindView(R.id.fm_me_switch_role)
    FmItemTextView fmMeSwitchRole;
    @BindView(R.id.fm_me_notify_msg)
    FmItemTextView fmMeNotifyMsg;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    public void setUpView() {
        fmMeSwitchRole = (FmItemTextView) rootView.findViewById(R.id.fm_me_switch_role);
        fmMeSwitchRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                on_fm_me_switch_role_click();
            }
        });
    }

    @Override
    public BasePresenter bindPresenter() {
        return null;
    }


    /*头部个人信息点击事件*/
    @OnClick(R.id.fm_me_header_layout)
    private void on_fm_me_header_layout_click() {

    }

    /*角色切换*/
    @OnClick(R.id.fm_me_switch_role)
    private void on_fm_me_switch_role_click() {
        SelectDialogFragment.Builder builder = new SelectDialogFragment.Builder();
        SelectDialogFragment dialog= builder.setMessage("SAS", "ASDAS", "ASDAS").setMessageColor(R.color.colorAccent,R.color.green_press).Build();
        dialog.show(getChildFragmentManager());
    }

    /*消息通知*/
    @OnClick(R.id.fm_me_notify_msg)
    private void on_fm_me_notify_msg_click() {
        Toast.makeText(getContext(),"dsfsdfsd",Toast.LENGTH_SHORT).show();
    }

    /*晨读二维码*/
    @OnClick(R.id.fm_me_mornig_qrcode)
    private void on_fm_me_mornig_qrcode_click() {

    }

    /*修改密码*/
    @OnClick(R.id.fm_me_change_pwd)
    private void on_fm_me_change_pwd_click() {

    }

    /*客服*/
    @OnClick(R.id.fm_me_kefu)
    private void on_fm_me_kefu_click() {

    }

    /*退出*/
    @OnClick(R.id.fm_me_login_out)
    private void on_fm_me_login_out_click() {

    }
}
