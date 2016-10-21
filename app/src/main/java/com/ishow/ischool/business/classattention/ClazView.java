package com.ishow.ischool.business.classattention;

import com.commonlib.core.BaseView;

/**
 * Created by MrS on 2016/10/20.
 */

public interface ClazView<T> extends BaseView {
    void getResutSucess(T result);
    void CheckInSucess(String msg);
    void getResultEorre(String msg);
}
