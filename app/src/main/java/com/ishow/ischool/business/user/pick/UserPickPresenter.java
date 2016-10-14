package com.ishow.ischool.business.user.pick;

import com.ishow.ischool.bean.saleprocess.Marketposition;
import com.ishow.ischool.bean.saleprocess.Subordinate;
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


    @Override
    public void getPosition(int campusId) {
        mModel.getPosition(campusId).subscribe(new ApiObserver<Marketposition>() {
            @Override
            public void onSuccess(Marketposition marketposition) {
                mView.getListSuccess(marketposition);
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

    @Override
    public void getUserByPosition(int campusId, int position_id, String keywords, int page) {
        mModel.getUserByPosition(campusId, position_id, keywords, page).subscribe(new ApiObserver<Subordinate>() {
            @Override
            public void onSuccess(Subordinate subordinate) {
                mView.getListSuccess(subordinate);
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
