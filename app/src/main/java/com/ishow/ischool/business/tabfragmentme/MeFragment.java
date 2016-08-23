package com.ishow.ischool.business.tabfragmentme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.commonlib.application.ActivityStackManager;
import com.commonlib.widget.imageloader.ImageLoaderUtil;
import com.ishow.ischool.R;
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

import java.util.ArrayList;
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

    private User user;

    private Position selectPosition;
    private String[] selectResult;

    private List<Campus> campus;
    private List<Position> positions;

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
            // LogUtil.e(avatar.file_name+"Avatar");
            ImageLoaderUtil.getInstance().loadImage(getContext(), avatar.file_name, fmMeHeaderAvart);
        fmMeHeaderName.setText(userInfo.user_name);
        campus = user.campus;
        fmMeHeaderJob.setText(userInfo.job);
        if (campus != null && campus.size() <= 1) {
            Drawable[] drawables = fmMeSwitchRole.getCompoundDrawables();
            fmMeSwitchRole.setCompoundDrawables(drawables[0], null, null, null);
        }


        PositionInfo info = user.positionInfo;
        if (info != null) fmMeSwitchRole.setTipTxt(info.title);

    }

    /*头部个人信息点击事件*/
    @OnClick(R.id.fm_me_header_layout)
    public void on_fm_me_header_layout_click() {
        JumpManager.jumpActivityForResult((Activity) getContext(), PersonInfoActivity.class, 100);
    }


    /*角色切换*/
    //PickerWheelViewPop pop;
    @OnClick(R.id.fm_me_switch_role)
    public void on_fm_me_switch_role_click() {
        if (user != null) {
            PickerDialogFragment.Builder builder = new PickerDialogFragment.Builder();
            builder.setBackgroundDark(true).setDialogTitle(R.string.switch_role).setDialogType(PickerDialogFragment.PICK_TYPE_OTHERS);
            if (campus != null && campus.size() <= 1)
                builder.setDatas(0,1,campus.get(0).positions);
            else  builder.setDatas(0,2,mPresenter.getCampusArrayList(campus),campus.get(0).positions);
            PickerDialogFragment fragment = builder.Build();
            fragment.show(getChildFragmentManager(),"dialog");
            fragment.addMultilinkPickCallback(new PickerDialogFragment.MultilinkPickCallback() {
                @Override
                public ArrayList<String> endSelect(int colum, int selectPosition, String text) {
                    //只有一列的返回空
                    if (campus != null && campus.size() <= 1)
                        return null;
                    //有2列的 但是选中的是第二列 也就是职位列表的 不需要变化
                    if (colum == 1)
                        return null;
                    //更新 数据源
                    return mPresenter.getPositionForCampus(campus, selectPosition);
                }
                @Override
                public void onPickResult(Object object, String... result) {
                    selectPosition = mPresenter.getSelectPosition(user.position, result[result.length - 1]);
                    selectResult = result;
                    if (selectPosition != null) {
                        handProgressbar(true);
                        mPresenter.change(selectPosition.campus_id, selectPosition.id);
                    }
                }
            });
        }
    }

    /*消息通知*/
    @OnClick(R.id.fm_me_notify_msg)
    public void on_fm_me_notify_msg_click() {
//        CommuDialogFragment pop = new CommuDialogFragment();
//        pop.show(getChildFragmentManager(),"dialog");
//        pop.addOnSelectResultCallback(new CommuDialogFragment.selectResultCallback() {
//            @Override
//            public void onResult(int statePosition, int confidencePosition, int refusePosition, int orderPosition, int startUnix, int endUnix) {
//                LogUtil.e(statePosition+"-"+confidencePosition+"-"+refusePosition);
//            }
//        });
//        Intent intent = new Intent(getActivity(), StudentDetailActivity.class);
//        intent.putExtra(StudentDetailActivity.P_STUDENT_ID, 7);
//        startActivity(intent);
        PickerDialogFragment dialog = new PickerDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("PICK_TITLE", R.string.choose_birthday);
        bundle.putInt("PICK_TYPE", PickerDialogFragment.PICK_TYPE_DATE);
        bundle.putInt("PICK_THEME", R.style.Comm_dialogfragment);//PickerDialogFragment.STYLE_NO_FRAME
        dialog.setArguments(bundle);
        dialog.show(getChildFragmentManager(), "dialog");


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
        JumpManager.jumpActivity(getContext(), KefuActivity.class);
    }

    /*退出*/
    @OnClick(R.id.fm_me_login_out)
    public void on_fm_me_login_out_click() {
        handProgressbar(true);
        mPresenter.logout();
    }

    @Override
    public void onNetSucess() {
        handProgressbar(false);
        UserManager.getInstance().clear();
        ActivityStackManager.getInstance().clear();
        JumpManager.jumpActivity(getContext(), LoginActivity.class);
        getActivity().finish();


    }

    @Override
    public void onNetFailed(String msg) {
        handProgressbar(false);
        showToast(msg);
    }

    @Override
    public void onChangeSucess() {
        handProgressbar(false);
        if (selectResult != null) {
            fmMeSwitchRole.setTipTxt(selectResult[selectResult.length - 1]);
            //更新本地 用户信息的 posiiotnInfo的 信息
            UserManager.getInstance().updateCurrentPositionInfo(selectPosition);
        }
    }

    @Override
    public void onChageFailed(String msg) {
        handProgressbar(false);
        showToast(msg);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            String path = data.getStringExtra("bitmap");
            if (!TextUtils.equals(path,"")){
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                fmMeHeaderAvart.setImageBitmap(bitmap);
            }
        }
    }
}
