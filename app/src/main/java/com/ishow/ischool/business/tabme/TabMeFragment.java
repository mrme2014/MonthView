package com.ishow.ischool.business.tabme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.commonlib.util.DeviceUtils;
import com.commonlib.util.LogUtil;
import com.commonlib.widget.event.RxBus;
import com.commonlib.widget.imageloader.ImageLoaderUtil;
import com.ishow.ischool.R;
import com.ishow.ischool.application.Cons;
import com.ishow.ischool.application.Constants;
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.bean.user.Avatar;
import com.ishow.ischool.bean.user.CampusInfo;
import com.ishow.ischool.bean.user.Position;
import com.ishow.ischool.bean.user.PositionInfo;
import com.ishow.ischool.bean.user.User;
import com.ishow.ischool.bean.user.UserInfo;
import com.ishow.ischool.business.editpwd.EditPwdActivity;
import com.ishow.ischool.business.kefu.KefuActivity;
import com.ishow.ischool.business.morningqrcode.MorningReadActivity;
import com.ishow.ischool.business.personinfo.PersonInfoActivity;
import com.ishow.ischool.business.registrationform.registrationFormActivity;
import com.ishow.ischool.common.base.BaseFragment4Crm;
import com.ishow.ischool.common.manager.JumpManager;
import com.ishow.ischool.common.manager.UserManager;
import com.ishow.ischool.event.ChangeRoleEvent;
import com.ishow.ischool.util.AppUtil;
import com.ishow.ischool.widget.custom.AvatarImageView;
import com.ishow.ischool.widget.custom.CircleImageView;
import com.ishow.ischool.widget.custom.FmItemTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by abel on 16/8/8.
 */
public class TabMeFragment extends BaseFragment4Crm<MePresenter, MeModel> implements MePresenter.Iview {

    @BindView(R.id.fm_me_header_avart)
    public CircleImageView fmMeHeaderAvart;
    @BindView(R.id.fm_me_header_name)
    public TextView fmMeHeaderName;
    @BindView(R.id.fm_me_header_job)
    public TextView fmMeHeaderJob;

    @BindView(R.id.fm_me_switch_role)
    public FmItemTextView fmMeSwitchRole;
    @BindView(R.id.fm_me_summary_weekly)
    public FmItemTextView fmMeSummaryWeekly;
    @BindView(R.id.fm_me_mornig_qrcode)
    FmItemTextView fmMeMornigQrcode;
    @BindView(R.id.fm_me_version)
    FmItemTextView fmMeVersion;
    @BindView(R.id.fm_avart_txt)
    AvatarImageView fmAvartTxt;

    private User user;

   /* private List<Campus> campus;
    private List<Position> positions;
    private PositionInfo info;*/

    private String avartPath;
    private String TAG = TabMeFragment.class.getSimpleName();


    @Override
    public int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    public void init() {
        user = UserManager.getInstance().get();
        if (user == null)
            return;
        checkWeeklyPermission();
        UserInfo userInfo = user.userInfo;
        if (userInfo == null)
            return;
        Avatar avatar = user.avatar;
        if (avatar != null && !TextUtils.equals(avatar.file_name, "") && avatar.file_name != null)
            ImageLoaderUtil.getInstance().loadImage(getContext(), avatar.file_name, fmMeHeaderAvart);
        else {
            fmMeHeaderAvart.setVisibility(View.INVISIBLE);
            fmAvartTxt.setVisibility(View.VISIBLE);
            fmAvartTxt.setText(userInfo.user_name, userInfo.user_id, "");
        }

        PositionInfo info = user.positionInfo;
        if (info != null) {
            fmMeSwitchRole.setTipTxt(info.title);
            if (info.id != Resource.ROLE_PERMISSION_CHENDU)
                fmMeMornigQrcode.setVisibility(View.GONE);
            fmMeHeaderName.setText(userInfo.user_name);
            fmMeHeaderJob.setText(info.campus + "  " + info.title);
        }

        //List<Campus> campus = user.campus;
        List<Position> positions = user.position;
        if (positions != null && positions.size() <= 1) {
            Drawable[] drawables = fmMeSwitchRole.getCompoundDrawables();
            fmMeSwitchRole.setCompoundDrawables(drawables[0], null, null, null);
        }
        Drawable[] drawables = fmMeVersion.getCompoundDrawables();
        fmMeVersion.setCompoundDrawables(drawables[0], null, null, null);
        fmMeVersion.setTipTxt(DeviceUtils.getVersionName(getActivity()));
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (avartPath != "" && avartPath != null && fmMeHeaderAvart != null) {
            ImageLoaderUtil.getInstance().loadImage(getContext(), fmMeHeaderAvart, avartPath);
        }
    }

    /*头部个人信息点击事件*/
    @OnClick({R.id.fm_me_header_layout, R.id.fm_me_switch_role, R.id.fm_me_summary_weekly, R.id.fm_me_mornig_qrcode, R.id.fm_me_change_pwd,
            R.id.fm_me_kefu, R.id.fm_me_version, R.id.fm_me_login_out})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.fm_me_header_layout:
                JumpManager.jumpActivityForResult((Activity) getContext(), PersonInfoActivity.class, 100, Resource.NO_NEED_CHECK);
                break;
            case R.id.fm_me_switch_role:
                user = UserManager.getInstance().get();
                mPresenter.switchRole(getChildFragmentManager(), user.campus, user.position, user.positionInfo);
                break;
            case R.id.fm_me_summary_weekly:
                JumpManager.jumpActivity(getContext(), Summary4WeeklyActivity.class, Resource.NO_NEED_CHECK);
                break;
            case R.id.fm_me_mornig_qrcode:
                JumpManager.jumpActivity(getContext(), MorningReadActivity.class, Resource.NO_NEED_CHECK);
                break;
            case R.id.fm_me_change_pwd:
                JumpManager.jumpActivity(getContext(), EditPwdActivity.class, Resource.NO_NEED_CHECK);
                break;
            case R.id.fm_me_kefu:
                JumpManager.jumpActivity(getContext(), KefuActivity.class, Resource.NO_NEED_CHECK);
                break;
            case R.id.fm_me_login_out:
                mPresenter.logout();
                break;
            case R.id.fm_me_version:
                Intent intent = new Intent(getActivity(), registrationFormActivity.class);
                intent.putExtra(registrationFormActivity.STUDENT_ID, 585);
                intent.putExtra(registrationFormActivity.REQUEST_CODE, 100);
                intent.putExtra(registrationFormActivity.STUDENT_STATUS, 1);
                startActivityForResult(intent, 100);
                break;
        }
    }

    @Override
    public void onNetSucess() {
        AppUtil.logout(getActivity());
        getActivity().finish();
    }

    @Override
    public void onNetFailed(String msg) {
        showToast(msg);
    }

    @Override
    public void onChangeSucess(User changedUser) {
        LogUtil.e("changedUser");
        PositionInfo positionInfo = changedUser.positionInfo;
        CampusInfo campusInfo = changedUser.campusInfo;
        if (positionInfo == null || campusInfo == null) {
            showToast(R.string.switch_role_failed);
            return;
        }
        fmMeSwitchRole.setTipTxt(positionInfo.title);
        //更新本地 用户信息的 posiiotnInfo的 信息
        UserManager.getInstance().initCampusPositions(changedUser);

        fmMeHeaderJob.setText(campusInfo.name + "  " + positionInfo.title);

        if (positionInfo.id != Resource.ROLE_PERMISSION_CHENDU)
            fmMeMornigQrcode.setVisibility(View.GONE);
        else fmMeMornigQrcode.setVisibility(View.VISIBLE);

        RxBus.getInstance().post(campusInfo.name);
        LogUtil.e("changedUser post ChangeRoleEvent");
        com.ishow.ischool.common.rxbus.RxBus.getDefault().post(new ChangeRoleEvent(changedUser));
        checkWeeklyPermission();
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
            if (fmMeHeaderAvart != null && avartPath != null && avartPath != "") {
                fmMeHeaderAvart.setVisibility(View.VISIBLE);
                fmAvartTxt.setVisibility(View.GONE);
                ImageLoaderUtil.getInstance().loadImage(getActivity(), fmMeHeaderAvart, avartPath);

            }
        }
    }


    void checkWeeklyPermission() {
        if (user.positionInfo.campusId != Constants.CAMPUS_HEADQUARTERS) {      // 非总部
            if (user.positionInfo.id == Cons.Position.Xiaozhang ||
                    user.positionInfo.id == Cons.Position.Shichangzongjian) {
                fmMeSummaryWeekly.setVisibility(View.VISIBLE);
            }
        }
    }
}
