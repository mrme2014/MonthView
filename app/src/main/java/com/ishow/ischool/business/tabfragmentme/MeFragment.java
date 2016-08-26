package com.ishow.ischool.business.tabfragmentme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.commonlib.application.ActivityStackManager;
import com.commonlib.widget.imageloader.ImageLoaderUtil;
import com.ishow.ischool.R;
import com.ishow.ischool.application.Resourse;
import com.ishow.ischool.bean.user.Avatar;
import com.ishow.ischool.bean.user.Campus;
import com.ishow.ischool.bean.user.Position;
import com.ishow.ischool.bean.user.PositionInfo;
import com.ishow.ischool.bean.user.User;
import com.ishow.ischool.bean.user.UserInfo;
import com.ishow.ischool.business.editpwd.EditPwdActivity;
import com.ishow.ischool.business.kefu.KefuActivity;
import com.ishow.ischool.business.login.LoginActivity;
import com.ishow.ischool.business.morningqrcode.MorningReadActivity;
import com.ishow.ischool.business.personinfo.PersonInfoActivity;
import com.ishow.ischool.common.base.BaseFragment4Crm;
import com.ishow.ischool.common.manager.JumpManager;
import com.ishow.ischool.common.manager.UserManager;
import com.ishow.ischool.widget.custom.CircleImageView;
import com.ishow.ischool.widget.custom.FmItemTextView;
import com.ishow.ischool.widget.pickerview.PickerDialogFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by abel on 16/8/8.
 */
public class MeFragment extends BaseFragment4Crm<MePresenter, MeModel> implements MePresenter.Iview {

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
    @BindView(R.id.fm_me_mornig_qrcode)
    FmItemTextView fmMeMornigQrcode;

    private User user;

    private List<Campus> campus;
    private List<Position> positions;

    private String avartPath;
    private String TAG = MeFragment.class.getSimpleName();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    public void init() {
        user = UserManager.getInstance().get();
        if (user == null)
            return;
        UserInfo userInfo = user.userInfo;
        if (userInfo == null)
            return;
        Avatar avatar = user.avatar;
        if (avatar != null && !TextUtils.equals(avatar.file_name, ""))
            //PicUtils.loadUserHeader(getContext(),fmMeHeaderAvart,avatar.file_name);
            ImageLoaderUtil.getInstance().loadImage(getContext(), avatar.file_name, fmMeHeaderAvart);
        fmMeHeaderName.setText(userInfo.user_name);
        fmMeHeaderJob.setText(userInfo.job);

        campus = user.campus;
        if (campus != null && campus.size() <= 1) {
            Drawable[] drawables = fmMeSwitchRole.getCompoundDrawables();
            fmMeSwitchRole.setCompoundDrawables(drawables[0], null, null, null);
        }

        PositionInfo info = user.positionInfo;
        if (info != null) fmMeSwitchRole.setTipTxt(info.title);
        if (info.id != Resourse.ROLE_PERMISSION_CHENDU) fmMeMornigQrcode.setVisibility(View.GONE);

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (avartPath != "" && avartPath != null && fmMeHeaderAvart != null) {
            ImageLoaderUtil.getInstance().loadImage(getContext(), fmMeHeaderAvart, avartPath);
        }
    }

    /*头部个人信息点击事件*/
    @OnClick(R.id.fm_me_header_layout)
    public void on_fm_me_header_layout_click() {
        JumpManager.jumpActivityForResult((Activity) getContext(), PersonInfoActivity.class, 100,Resourse.NO_NEED_CHECK);
    }


    /*角色切换*/
    //PickerWheelViewPop pop;
    @OnClick(R.id.fm_me_switch_role)
    public void on_fm_me_switch_role_click() {
        if (user == null)
            return;
        positions = user.position;
        mPresenter.switchRole(getChildFragmentManager(), campus, positions);
    }

    /*消息通知*/
    @OnClick(R.id.fm_me_notify_msg)
    public void on_fm_me_notify_msg_click() {
        PickerDialogFragment dialog = new PickerDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("PICK_TITLE", R.string.choose_birthday);
        bundle.putInt("PICK_TYPE", PickerDialogFragment.PICK_TYPE_DATE);
        bundle.putInt("PICK_THEME", R.style.Comm_dialogfragment);//PickerDialogFragment.STYLE_NO_FRAME
        dialog.setArguments(bundle);
        dialog.show(getChildFragmentManager());


    }

    /*晨读二维码*/
    @OnClick(R.id.fm_me_mornig_qrcode)
    public void on_fm_me_mornig_qrcode_click() {
        JumpManager.jumpActivity(getContext(), MorningReadActivity.class,Resourse.NO_NEED_CHECK);
    }

    /*修改密码*/
    @OnClick(R.id.fm_me_change_pwd)
    public void on_fm_me_change_pwd_click() {
        JumpManager.jumpActivity(getContext(), EditPwdActivity.class,Resourse.NO_NEED_CHECK);
    }

    /*客服*/
    @OnClick(R.id.fm_me_kefu)
    public void on_fm_me_kefu_click() {
        JumpManager.jumpActivity(getContext(), KefuActivity.class,Resourse.NO_NEED_CHECK);
    }

    /*退出*/
    @OnClick(R.id.fm_me_login_out)
    public void on_fm_me_login_out_click() {
        mPresenter.logout();
    }

    @Override
    public void onNetSucess() {
        UserManager.getInstance().clear();
        ActivityStackManager.getInstance().clear();
        JumpManager.jumpActivity(getContext(), LoginActivity.class,Resourse.NO_NEED_CHECK);
        getActivity().finish();
    }

    @Override
    public void onNetFailed(String msg) {
        showToast(msg);
    }

    @Override
    public void onChangeSucess(String selectCampus, String txt, Position selectPosition, List<Integer> resources) {
        fmMeSwitchRole.setTipTxt(txt);
        //更新本地 用户信息的 posiiotnInfo的 信息
        UserManager.getInstance().updateCurrentPositionInfo(selectPosition,resources);

        if (selectPosition.id != Resourse.ROLE_PERMISSION_CHENDU) fmMeMornigQrcode.setVisibility(View.GONE);
        else fmMeMornigQrcode.setVisibility(View.VISIBLE);

        com.commonlib.widget.event.RxBus.getInstance().post(selectCampus);

    }

    @Override
    public void onChageFailed(String msg) {
        showToast(msg);
    }

    @Override
    public void showProgressbar(boolean show) {
        handProgressbar(show);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            avartPath = data.getStringExtra("tempath");
            if (fmMeHeaderAvart != null && avartPath != null && avartPath != "")
                ImageLoaderUtil.getInstance().loadImage(getActivity(), fmMeHeaderAvart, avartPath);
        }
    }

}
