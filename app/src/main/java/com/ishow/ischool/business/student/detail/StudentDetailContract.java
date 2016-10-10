package com.ishow.ischool.business.student.detail;

import com.commonlib.core.BaseModel;
import com.commonlib.core.BasePresenter;
import com.commonlib.core.BaseView;
import com.facebook.datasource.DataSource;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.student.Student;
import com.ishow.ischool.bean.user.Avatar;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by abel on 16/8/18.
 */
public interface StudentDetailContract {
    interface Model extends BaseModel {
        Observable<ApiResult<Student>> getStudent(HashMap<String, String> params);

        Observable<ApiResult<Object>> editStudent(HashMap<String, String> params);

        Observable getQiNiuToken(int user_id);

    }

    interface View extends BaseView {
        void onGetStudentSuccess(Student student);

        void onGetStudentFailed(String msg);

        void onEditStudentSuccess(Object student);

        void onEditStudentFailed(String msg);

        void onUploadAvatarFailed(String msg);

        void updateAvatar(int per_net_token_sucess, Avatar avatar);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        abstract void getStudent(HashMap<String, String> params);

        abstract void editStudent(HashMap<String, String> params);

        abstract void uploadImg(final int user_id, final String filePath);
    }
}
