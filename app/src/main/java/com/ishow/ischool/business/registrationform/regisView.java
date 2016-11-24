package com.ishow.ischool.business.registrationform;

import com.commonlib.core.BaseView;
import com.ishow.ischool.bean.registrationform.RegistraResult;

/**
 * Created by MrS on 2016/11/21.
 */

public interface regisView extends BaseView {

    void getRegistraInfo(RegistraResult registraResult);

    void getRegistraError(String error);

    void payActionSucess(String info);
}
