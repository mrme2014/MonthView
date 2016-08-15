package com.ishow.ischool.business.tabbusiness;

import com.ishow.ischool.R;
import com.ishow.ischool.business.addstudent.AddStudentActivity;
import com.ishow.ischool.business.communicationlist.CommunicationListActivity;
import com.ishow.ischool.business.statisticslist.StatisticsListActivity;
import com.zaaach.citypicker.CityPickerActivity;

import java.util.ArrayList;
import java.util.List;

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
        intentClazz = new Class[]{StatisticsListActivity.class, CommunicationListActivity.class, AddStudentActivity.class, CityPickerActivity.class};
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

}
