package com.ishow.ischool.business.pickreferrer;

import com.ishow.ischool.bean.user.UserListResult;
import com.ishow.ischool.common.api.ApiObserver;

/**
 * Created by wqf on 16/8/18.
 */
public class PickReferrerPresenter extends PickReferrerContract.Presenter {
    @Override
    public void getReferrers(String keyword, int page) {
        mModel.getReferrers(keyword, page).subscribe(new ApiObserver<UserListResult>() {
            @Override
            public void onSuccess(UserListResult userListResult) {
                mView.getListSuccess(userListResult);
            }

            @Override
            public void onError(String msg) {
                mView.getListFail(msg);
            }
        });
    }
}
