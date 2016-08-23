package com.ishow.ischool.business.tabbusiness;

import com.commonlib.http.ApiFactory;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.ApiResult;
import com.ishow.ischool.bean.system.CampusInfo;
import com.ishow.ischool.business.addstudent.AddStudentActivity;
import com.ishow.ischool.business.communication.add.CommunicationAddActivity;
import com.ishow.ischool.business.communication.list.CommunicationListActivity;
import com.ishow.ischool.business.statisticslist.StatisticsListActivity;
import com.ishow.ischool.common.api.MarketApi;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wqf on 16/8/14.
 */
public class TabBusinessModel implements TabBusinessContract.Model {

    Class<?>[] intentClazz = null;
    String[] texts;
    int[] iconResIds;
    ArrayList<TabSpec> tabSpecs;

    public class TabSpec {
        public Class<?> intentClazz;
        public String text;
        public int iconResId;
    }

    List<TabSpec> getTabSpecs() {
        intentClazz = new Class[]{StatisticsListActivity.class, CommunicationListActivity.class, AddStudentActivity.class, CommunicationAddActivity.class};
        texts = new String[]{"学员统计", "沟通记录", "添加学员", "添加沟通记录"};
        iconResIds = new int[]{R.mipmap.icon_business_01, R.mipmap.icon_business_02, R.mipmap.icon_business_03, R.mipmap.icon_business_04};
        int tabsize = intentClazz.length;
        tabSpecs = new ArrayList<>(tabsize);
        for (int i = 0; i < tabsize; i++) {
            TabSpec tabSpec = new TabSpec();
            tabSpec.intentClazz = intentClazz[i];
            tabSpec.text = texts[i];
            tabSpec.iconResId = iconResIds[i];
            tabSpecs.add(tabSpec);
        }
        return tabSpecs;
    }


    public Observable<ApiResult<ArrayList<CampusInfo>>> getCampusList() {
        return ApiFactory.getInstance().getApi(MarketApi.class).getCampusList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
}
