package com.ishow.ischool.business.tabbusiness;

import com.ishow.ischool.R;
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.business.classmaneger.classlist.ClassListActivity;
import com.ishow.ischool.business.communication.add.CommunicationAddActivity;
import com.ishow.ischool.business.communication.list.CommunicationListActivity;
import com.ishow.ischool.business.student.add.AddStudentActivity;
import com.ishow.ischool.business.student.list.StatisticsListActivity;
import com.ishow.ischool.common.manager.UserManager;

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
    ArrayList<Boolean> hasPermissions;

    public class TabSpec {
        public Class<?> intentClazz;
        public String text;
        public int iconResId;
        public boolean hasPermission;
    }

    List<TabSpec> getTabSpecs4Market() {
        intentClazz = new Class[]{StatisticsListActivity.class, CommunicationListActivity.class, AddStudentActivity.class, CommunicationAddActivity.class};
        texts = new String[]{"学员统计", "沟通记录", "添加学员", "添加沟通记录"};
        hasPermissions = new ArrayList<Boolean>(){{add(false);add(false);add(false);add(false);}};
        int icon1 = R.mipmap.icon_business_01_gray;
        int icon2 = R.mipmap.icon_business_02_gray;
        int icon3 = R.mipmap.icon_business_03_gray;
        int icon4 = R.mipmap.icon_business_04_gray;
        if (checkUserPermision(Resource.MARKET_STUDENT_STATISTICS)) {
            icon1 = R.mipmap.icon_business_01;
            hasPermissions.set(0, true);
        }
        if (checkUserPermision(Resource.SHARE_COMMUNICATION_INDEXM)) {
            icon2 = R.mipmap.icon_business_02;
            hasPermissions.set(1, true);
        }
        if (checkUserPermision(Resource.MARKET_STUDENT_ADD)) {
            icon3 = R.mipmap.icon_business_03;
            hasPermissions.set(2, true);
        }
        if (checkUserPermision(Resource.SHARE_COMMUNICATION_ADDM) && checkUserPermision(Resource.SHARE_COMMUNICATION_ADDM_1)) {
            icon4 = R.mipmap.icon_business_04;
            hasPermissions.set(3, true);
        }
        iconResIds = new int[]{icon1, icon2, icon3, icon4};
        int tabsize = intentClazz.length;
        tabSpecs = new ArrayList<>(tabsize);
        for (int i = 0; i < tabsize; i++) {
            TabSpec tabSpec = new TabSpec();
            tabSpec.intentClazz = intentClazz[i];
            tabSpec.text = texts[i];
            tabSpec.iconResId = iconResIds[i];
            tabSpec.hasPermission = hasPermissions.get(i);
            tabSpecs.add(tabSpec);
        }
        return tabSpecs;
    }

    List<TabSpec> getTabSpecs4Teach() {
        intentClazz = new Class[]{ClassListActivity.class, CommunicationListActivity.class, AddStudentActivity.class};
        texts = new String[]{"班级管理", "沟通记录", "添加沟通记录"};
        hasPermissions = new ArrayList<Boolean>(){{add(false);add(false);add(false);}};
        int icon1 = R.mipmap.icon_class_gray;
        int icon2 = R.mipmap.icon_business_02_gray;
        int icon3 = R.mipmap.icon_business_04_gray;
        if (checkUserPermision(Resource.EDUCATION_CLASSMANAGEMENT_INDEX)) {
            icon1 = R.mipmap.icon_class;
            hasPermissions.set(0, true);
        }
        if (checkUserPermision(Resource.SHARE_COMMUNICATION_INDEXE)) {
            icon2 = R.mipmap.icon_business_02;
            hasPermissions.set(1, true);
        }
        if (checkUserPermision(Resource.SHARE_COMMUNICATION_ADDE)) {
            icon2 = R.mipmap.icon_business_04;
            hasPermissions.set(2, true);
        }
        iconResIds = new int[]{icon1, icon2, icon3};
        int tabsize = intentClazz.length;
        tabSpecs = new ArrayList<>(tabsize);
        for (int i = 0; i < tabsize; i++) {
            TabSpec tabSpec = new TabSpec();
            tabSpec.intentClazz = intentClazz[i];
            tabSpec.text = texts[i];
            tabSpec.iconResId = iconResIds[i];
            tabSpec.hasPermission = hasPermissions.get(i);
            tabSpecs.add(tabSpec);
        }
        return tabSpecs;
    }


    public boolean checkUserPermision(int permission) {
        if (permission == Resource.NO_NEED_CHECK)
            return true;
        List<Integer> resurces = UserManager.getInstance().getResurces();
        if (resurces == null) {
            return false;
        }
        for (int i = 0; i < resurces.size(); i++) {
            if (resurces.get(i) == permission)
                return true;
        }
        return false;
    }

}
