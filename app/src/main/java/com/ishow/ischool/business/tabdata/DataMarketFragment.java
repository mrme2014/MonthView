package com.ishow.ischool.business.tabdata;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ishow.ischool.R;
import com.ishow.ischool.business.campusperformance.CampusPerformanceActivity;
import com.ishow.ischool.business.salesprocess.SalesProcessActivity;
import com.ishow.ischool.common.base.BaseFragment4Crm;

import butterknife.OnClick;

/**
 * 市场业务统计
 */
public class DataMarketFragment extends BaseFragment4Crm {

    private OnFragmentInteractionListener mListener;

    public DataMarketFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DataMarketFragment.
     */
    public static DataMarketFragment newInstance() {
        DataMarketFragment fragment = new DataMarketFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_data_market;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void init() {

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    @OnClick({R.id.data_market, R.id.data_campus, R.id.data_other})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.data_market:
                startActivity(new Intent(getActivity(), SalesProcessActivity.class));
                break;
            case R.id.data_campus:
                startActivity(new Intent(getActivity(), CampusPerformanceActivity.class));
                break;
            case R.id.data_other:

                break;
        }
    }
}
