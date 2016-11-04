package com.ishow.ischool.business.tabindex;

import android.os.Bundle;

import com.ishow.ischool.R;
import com.ishow.ischool.common.base.BaseFragment4Crm;

public class TabIndexMarketFragment extends BaseFragment4Crm {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    public TabIndexMarketFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabIndexMarketFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TabIndexMarketFragment newInstance(String param1, String param2) {
        TabIndexMarketFragment fragment = new TabIndexMarketFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tab_index_market;
    }

    @Override
    public void init() {

    }

}
