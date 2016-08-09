package com.ishow.ischool.view;

import com.ishow.ischool.bean.user.User;
import com.ishow.ischool.common.base.view.IView;

/**
 * Created by abel on 16/8/9.
 */
public interface ILoginView extends IView {

    void loginSuccess(User user);

    void loginError(String msg);
}
