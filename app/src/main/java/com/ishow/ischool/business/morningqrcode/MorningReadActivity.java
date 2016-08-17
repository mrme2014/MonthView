package com.ishow.ischool.business.morningqrcode;

import android.widget.ImageView;
import android.widget.TextView;

import com.commonlib.widget.imageloader.ImageLoaderUtil;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.user.Avatar;
import com.ishow.ischool.bean.user.Qrcode;
import com.ishow.ischool.bean.user.User;
import com.ishow.ischool.bean.user.UserInfo;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.common.manager.UserManager;
import com.ishow.ischool.widget.custom.CircleImageView;

import butterknife.BindView;

/**
 * Created by MrS on 2016/8/12.
 */
public class MorningReadActivity extends BaseActivity4Crm {
    @BindView(R.id.qrcode_name)
    TextView qrcodeName;
    @BindView(R.id.qrcode_code)
    ImageView qrcodeCode;
    @BindView(R.id.qrcode_avart)
    CircleImageView qrcodeAvart;
    private User user;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_mornig_read_qrcode, R.string.morning_read_qrcode_title, -1, 0);
    }

    @Override
    protected void setUpView() {
        user = UserManager.getInstance().get();
    }

    @Override
    protected void setUpData() {
        if (user == null)
            return;
        Avatar avatar = user.getAvatar();
        if (avatar != null)
            ImageLoaderUtil.getInstance().loadImage(this, avatar.file_name, qrcodeAvart);
        Qrcode qrcode = user.getQrcode();
        if (qrcode != null)
            ImageLoaderUtil.getInstance().loadImage(this, qrcode.file_name, qrcodeCode);
        UserInfo userInfo = user.getUserInfo();
        if (userInfo != null) qrcodeName.setText(userInfo.nick_name);
    }

}
