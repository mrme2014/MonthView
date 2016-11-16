package com.ishow.ischool.business.tabindex;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.ishow.ischool.R;
import com.ishow.ischool.adpter.FragmentAdapter;
import com.ishow.ischool.common.base.BaseFragment4Crm;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by abel on 16/11/4.
 */

public class TabIndexFragment extends BaseFragment4Crm {

    @BindView(R.id.viewpager)
    ViewPager mViewPager;
//    @BindView(R.id.title_radio)
//    RadioGroup mTitleRadioGroup;
//    @BindView(R.id.title_radio_1)
//    RadioButton titleRadioButton1;
//    @BindView(R.id.title_radio_2)
//    RadioButton titleRadioButton2;


    FragmentAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tab_index;
    }

    @Override
    public void init() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new TabIndexMarketFragment().setParentFragment(this));
        fragments.add(new TabIndexTeachFragment().setParentFragment(this));

        mAdapter = new FragmentAdapter(getChildFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapter);
    }

    public void setCurrentItem(int index) {
        mViewPager.setCurrentItem(index);
    }

    public int getCurrentItem() {
        return mViewPager.getCurrentItem();
    }

    public interface TabFragment {
        void setCurrentItem(int index);
    }
}
