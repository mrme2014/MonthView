package com.ishow.ischool.business.tabindex;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioButton;

import com.commonlib.widget.base.MySpinner;
import com.ishow.ischool.R;
import com.ishow.ischool.adpter.FragmentAdapter;
import com.ishow.ischool.common.base.BaseFragment4Crm;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static com.ishow.ischool.R.id.title_radio_1;

/**
 * Created by abel on 16/11/4.
 */

public class TabIndexFragment extends BaseFragment4Crm {

    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(title_radio_1)
    RadioButton titleRadioButton1;
    @BindView(R.id.title_radio_2)
    RadioButton titleRadioButton2;
    @BindView(R.id.home_choose_time_sp)
    MySpinner chooseTimeSpinner;

    FragmentAdapter mAdapter;
    TabIndexMarketFragment tabIndexMarketFragment;
    TabIndexTeachFragment tabIndexTeachFragment;
    int curIndex = 0;

    private static final int TYPE_WEEK = 3;
    private static final int TYPE_LAST_WEEK = 4;
    private static final int TYPE_MONTH = 1;
    private static final int TYPE_LAST_MONTH = 2;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tab_index;
    }

    @Override
    public void init() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        tabIndexMarketFragment = new TabIndexMarketFragment();
        tabIndexTeachFragment = new TabIndexTeachFragment();
        fragments.add(tabIndexMarketFragment);
        fragments.add(tabIndexTeachFragment);

        mAdapter = new FragmentAdapter(getChildFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setCurFragment(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        initSp();
    }

    void initSp() {
        String[] chooseTimes = getResources().getStringArray(R.array.home_choose_times);
        ArrayList<String> chooseTimesArray = new ArrayList<>();
        for (int i = 0; i < chooseTimes.length; i++) {
            chooseTimesArray.add(chooseTimes[i]);
        }
        chooseTimeSpinner.setDatas(chooseTimesArray);
        chooseTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int timeType = -1;
                switch (position) {
                    case 0:
                        timeType = TYPE_WEEK;
                        break;
                    case 1:
                        timeType = TYPE_LAST_WEEK;
                        break;
                    case 2:
                        timeType = TYPE_MONTH;
                        break;
                    case 3:
                        timeType = TYPE_LAST_MONTH;
                        break;
                }
                if (curIndex == 0) {
                    if (position != tabIndexMarketFragment.mSpPosition) {
                        tabIndexMarketFragment.update(position, chooseTimeSpinner.getSelectedValue(), timeType);
                    }
                } else {
                    if (position != tabIndexTeachFragment.mSpPosition) {
                        tabIndexTeachFragment.update(position, chooseTimeSpinner.getSelectedValue(), timeType);
                    }
                }
            }
        });
        chooseTimeSpinner.setPosition(0);
    }

    void setCurFragment(int index) {
        curIndex = index;
        switch (index) {
            case 0:
                mViewPager.setCurrentItem(0);
                chooseTimeSpinner.setPosition(tabIndexMarketFragment.mSpPosition);
                titleRadioButton1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                titleRadioButton1.setTextColor(getResources().getColor(R.color.white));
                titleRadioButton2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                titleRadioButton2.setTextColor(getResources().getColor(R.color.trans_txt_55));
                break;
            case 1:
                mViewPager.setCurrentItem(1);
                chooseTimeSpinner.setPosition(tabIndexTeachFragment.mSpPosition);
                titleRadioButton2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                titleRadioButton2.setTextColor(getResources().getColor(R.color.white));
                titleRadioButton1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                titleRadioButton1.setTextColor(getResources().getColor(R.color.trans_txt_55));
                break;
        }
    }

    @OnClick({title_radio_1, R.id.title_radio_2})
    void onClick(View view) {
        switch (view.getId()) {
            case title_radio_1:
                if (curIndex != 0) {
                    setCurFragment(0);
                }
                break;
            case R.id.title_radio_2:
                if (curIndex != 1) {
                    setCurFragment(1);
                }
                break;
        }
    }
}
