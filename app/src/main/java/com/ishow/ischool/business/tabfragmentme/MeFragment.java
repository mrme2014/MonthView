package com.ishow.ischool.business.tabfragmentme;

import android.view.Gravity;
import android.widget.TextView;

import com.commonlib.application.ActivityStackManager;
import com.ishow.ischool.R;
import com.ishow.ischool.business.editpwd.EditPwdActivity;
import com.ishow.ischool.business.login.LoginActivity;
import com.ishow.ischool.business.morningqrcode.MorningReadActivity;
import com.ishow.ischool.common.base.BaseFragment4Crm;
import com.ishow.ischool.common.manager.JumpManager;
import com.ishow.ischool.widget.custom.CircleImageView;
import com.ishow.ischool.widget.custom.FmItemTextView;
import com.ishow.ischool.widget.pickerview.PickerWheelViewPop;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by abel on 16/8/8.
 */
public class MeFragment extends BaseFragment4Crm implements MeView {

    @BindView(R.id.fm_me_header_avart)
    public CircleImageView fmMeHeaderAvart;
    @BindView(R.id.fm_me_header_name)
    public TextView fmMeHeaderName;
    @BindView(R.id.fm_me_header_job)
    public TextView fmMeHeaderJob;

    @BindView(R.id.fm_me_switch_role)
    public FmItemTextView fmMeSwitchRole;
    @BindView(R.id.fm_me_notify_msg)
    public FmItemTextView fmMeNotifyMsg;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    public void init() {

    }
    /*头部个人信息点击事件*/
    @OnClick(R.id.fm_me_header_layout)
    public void on_fm_me_header_layout_click() {

    }

    /*角色切换*/
    @OnClick(R.id.fm_me_switch_role)
    public void on_fm_me_switch_role_click() {
//        SelectDialogFragment.Builder builder = new SelectDialogFragment.Builder();
//        SelectDialogFragment dialog= builder.setMessage("SAS", "ASDAS", "ASDAS").setMessageColor(R.color.colorAccent,R.color.green_press).Build();
//        dialog.show(getChildFragmentManager());

        PickerWheelViewPop pop = new PickerWheelViewPop(getContext());
        pop.renderPanel(1, 3, new ArrayList<String>());
        pop.showAtLocation(fmMeSwitchRole, Gravity.BOTTOM, 0, 0);
    }

    /*消息通知*/
    @OnClick(R.id.fm_me_notify_msg)
    public void on_fm_me_notify_msg_click() {

    }

    /*晨读二维码*/
    @OnClick(R.id.fm_me_mornig_qrcode)
    public void on_fm_me_mornig_qrcode_click() {
        JumpManager.jumpActivity(getContext(), MorningReadActivity.class);
    }

    /*修改密码*/
    @OnClick(R.id.fm_me_change_pwd)
    public void on_fm_me_change_pwd_click() {
        JumpManager.jumpActivity(getContext(), EditPwdActivity.class);
    }

    /*客服*/
    @OnClick(R.id.fm_me_kefu)
    public void on_fm_me_kefu_click() {

    }

    /*退出*/
    @OnClick(R.id.fm_me_login_out)
    public void on_fm_me_login_out_click() {

    }

    @Override

    public void logoutSucess() {
        JumpManager.jumpActivity(getContext(), LoginActivity.class);
        ActivityStackManager.getInstance().clear();
    }

    @Override
    public void logutFailed(String msg) {
        showToast(msg);
    }
}
