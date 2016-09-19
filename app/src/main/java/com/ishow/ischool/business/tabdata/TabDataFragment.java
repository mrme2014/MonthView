package com.ishow.ischool.business.tabdata;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ishow.ischool.R;
import com.ishow.ischool.adpter.FragmentAdapter;
import com.ishow.ischool.bean.user.CampusInfo;
import com.ishow.ischool.common.base.BaseFragment4Crm;
import com.ishow.ischool.common.manager.CampusManager;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by wqf on 16/9/6.
 */
public class TabDataFragment extends BaseFragment4Crm<TabDataPresenter, TabDataModel> implements TabDataContract.View,
        DataTeachFragment.OnFragmentInteractionListener, DataMarketFragment.OnFragmentInteractionListener {

    //    @BindView(R.id.tabs)
//    TabLayout mTabs;
    @BindView(R.id.title_radio)
    RadioGroup mTitleRadioGroup;
    @BindView(R.id.title_radio_1)
    RadioButton titleRadioButton1;
    @BindView(R.id.title_radio_2)
    RadioButton titleRadioButton2;
    @BindView(R.id.viewpager)
    ViewPager mViewPaper;
    private FragmentAdapter mFragmentAdapter;
    private DataMarketFragment dataMarketFragment;
    private DataTeachFragment dataTeachFragment;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_data;
    }

    @Override
    public void init() {
        mPresenter.getCampusList();     //进入app获取所有校区信息
        ArrayList<Fragment> fragments = new ArrayList<>();
        dataMarketFragment = DataMarketFragment.newInstance();
        dataTeachFragment = DataTeachFragment.newInstance();
        fragments.add(dataMarketFragment);
        fragments.add(dataTeachFragment);

        ArrayList<String> titleList = new ArrayList<>();
        titleList.add(getString(R.string.data_market));
        titleList.add(getString(R.string.data_teach));
        mFragmentAdapter = new FragmentAdapter(getFragmentManager(), fragments, titleList);
        mViewPaper.setAdapter(mFragmentAdapter);
        mViewPaper.setCurrentItem(0);


        mTitleRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    if (group.getChildAt(i).getId() == checkedId) {
                        mViewPaper.setCurrentItem(i);
                        return;
                    }
                }
            }
        });

        mViewPaper.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTitleRadioGroup.check(mTitleRadioGroup.getChildAt(position).getId());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        titleRadioButton1.setChecked(true);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void getListSuccess(ArrayList<CampusInfo> campusInfos) {
        CampusManager.getInstance().init(getActivity().getApplicationContext());
        CampusManager.getInstance().save(campusInfos);
    }

    @Override
    public void getListFail(String msg) {

    }

}
