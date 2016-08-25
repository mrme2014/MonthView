package com.ishow.ischool.business.user.pick;

import com.ishow.ischool.bean.user.UserListResult;
import com.ishow.ischool.common.api.ApiObserver;

/**
 * Created by wqf on 16/8/18.
 */
public class UserPickPresenter extends UserPickContract.Presenter {
    @Override
    public void listUser(int campusId, String keyword, int page) {
        mModel.listUser(campusId, keyword, page).subscribe(new ApiObserver<UserListResult>() {
            @Override
            public void onSuccess(UserListResult userListResult) {
                mView.getListSuccess(userListResult);
            }

            @Override
            public void onError(String msg) {
                mView.getListFail(msg);
            }

            @Override
            protected boolean isAlive() {
                return mView != null && !mView.isActivityFinished();
            }
        });
    }
}
