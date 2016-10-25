package com.ishow.ischool.business.tabdata;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.commonlib.widget.CircleChartView;
import com.google.gson.Gson;
import com.ishow.ischool.R;
import com.ishow.ischool.application.Resource;
import com.ishow.ischool.bean.saleprocess.SaleProcess;
import com.ishow.ischool.bean.teachprocess.ChartProcess;
import com.ishow.ischool.bean.teachprocess.TeachProcess;
import com.ishow.ischool.bean.user.User;
import com.ishow.ischool.business.campusperformance.education.Performance4EduActivity;
import com.ishow.ischool.business.classattention.ClassAttendActivity;
import com.ishow.ischool.business.statistic.other.OtherStatisticActivity;
import com.ishow.ischool.common.base.BaseFragment4Crm;
import com.ishow.ischool.common.manager.JumpManager;
import com.ishow.ischool.common.manager.UserManager;
import com.ishow.ischool.util.AppUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 教务教学数据统计
 */
public class DataTeachFragment extends BaseFragment4Crm<DataTeachPreseneter, DataTeachModel> implements DataTeachContract.View {

    private OnFragmentInteractionListener mListener;
    //    @BindView(R.id.bar_chart)
//    HorizontalBarChart mBarChart;
    @BindView(R.id.circle_chartview)
    CircleChartView mCircleChartView;

    private SaleProcess mSaleProcess;
    private int campus_id, curuser_position_id, user_id, position_id;
    private long start_time, end_time;

    public DataTeachFragment() {
        // Required empty public constructor
    }

    public static DataTeachFragment newInstance() {
        DataTeachFragment fragment = new DataTeachFragment();
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
        return R.layout.fragment_data_teach;
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

        User mUser = UserManager.getInstance().get();
        campus_id = mUser.campusInfo.id;
        curuser_position_id = position_id = mUser.positionInfo.id;
        // user_id = mUser.userInfo.user_id;
        user_id = mUser.userInfo.user_id;
        Calendar calendar = Calendar.getInstance();
        start_time = AppUtil.getDayAgo(7);
        end_time = AppUtil.getTodayEnd();

//        mPresenter.initChart(mBarChart);

        getTeachProcessData();
    }

    private void getTeachProcessData() {
        TreeMap<String, Integer> map = new TreeMap();
        map.put("start_time", (int) start_time);
        map.put("end_time", (int) end_time);
//        map.put("position_id", position_id);
//        map.put("user_id", user_id);
        mPresenter.getTeachingProcess(map);
    }

    @Override
    public void getTeachingProcessSucess(TeachProcess process) {
//        Gson gson = new Gson();
//        String str = "{ \"selfChartData\": { \"head\": [ \"带班人数\", \"升学基数\", \"升学人数\", \"全款人数\", \"升学率\", \"全款率\" ], \"body\": [ [ 1031, 21, 1, 0, \"4.76%\", \"0.00%\" ] ] }, \"tableListData_22\": { \"head\": [ \"总人数\", \"各组别原始人数-初级\", \"各组别原始人数-中级\", \"各组别原始人数-高级\", \"各组别原始人数-总\", \"升学基数-初级\", \"升学基数-中级\", \"升学基数-高级\", \"升学基数-总\", \"升学-初升中\", \"升学-中升高\", \"升学-高升影\", \"升学-总\", \"全款\", \"退款\", \"升学率\", \"全款率\", \"退款率\" ], \"body\": [ [ 31, 14, 14, 4, 32, 6, 11, 4, 21, 1, 0, 0, 1, 0, 0, \"4.76%\", \"0.00%\", \"0.00%\" ] ] }, \"tableListData\": { \"head\": [ \"级别\", \"组长\", \"老师\", \"学习顾问\", \"班级\", \"原始人数\", \"升学基数\", \"升学\", \"全款\", \"退款\", \"升学率\", \"全款率\", \"退款率\" ], \"body\": [ [ \"ishow初级\", \"张鹤0初级组长\", \"张鹤初级老师\", \"张鹤0初级学习顾问\", \"16年0905初\", 4, 0, 0, 0, 0, \"0.00%\", \"0.00%\", \"0.00%\" ], [ \"ishow初级\", \"张鹤0初级组长\", \"张鹤初中级老师\", \"张鹤0初级学习顾问\", \"16年0909\", 2, 0, 0, 0, 0, \"0.00%\", \"0.00%\", \"0.00%\" ], [ \"ishow初级\", \"张鹤0初级组长\", \"张鹤初中级老师\", \"张鹤0初级学习顾问\", \"16年0912初\", 2, 0, 0, 0, 0, \"0.00%\", \"0.00%\", \"0.00%\" ], [ \"ishow中级\", \"张鹤0中级组长\", \"张鹤初中级老师\", \"张鹤0中级学习顾问\", \"16年0912中\", 3, 0, 0, 0, 0, \"0.00%\", \"0.00%\", \"0.00%\" ], [ \"ishow初级\", \"张鹤0初级组长\", \"张鹤初级老师\", \"张鹤初中高影学顾\", \"初级老师17初\", 3, 3, 1, 0, 0, \"33.33%\", \"0.00%\", \"0.00%\" ], [ \"ishow初级\", \"张鹤0初级组长\", \"张鹤初中级老师\", \"张鹤0初级学习顾问\", \"初中级老师17初\", 3, 3, 0, 0, 0, \"0.00%\", \"0.00%\", \"0.00%\" ], [ \"ishow中级\", \"张鹤0中级组长\", \"张鹤中级老师\", \"张鹤初中高影学顾\", \"中级老师17中\", 3, 3, 0, 0, 0, \"0.00%\", \"0.00%\", \"0.00%\" ], [ \"ishow中级\", \"张鹤0中级组长\", \"张鹤初中级老师\", \"张鹤0中级学习顾问\", \"初中级老师17中\", 2, 2, 0, 0, 0, \"0.00%\", \"0.00%\", \"0.00%\" ], [ \"ishow中级\", \"张鹤0中级组长\", \"张鹤教总高影组中高师\", \"张鹤教学多角色\", \"教总17中\", 2, 2, 0, 0, 0, \"0.00%\", \"0.00%\", \"0.00%\" ], [ \"ishow高级\", \"张鹤教总高影组中高师\", \"张鹤教总高影组中高师\", \"张鹤初中高影学顾\", \"教总17高\", 2, 2, 0, 0, 0, \"0.00%\", \"0.00%\", \"0.00%\" ], [ \"ishow高级\", \"张鹤教总高影组中高师\", \"张鹤教学多角色\", \"张鹤教学多角色\", \"市场主管17高\", 2, 2, 0, 0, 0, \"0.00%\", \"0.00%\", \"0.00%\" ], [ \"ishow中级\", \"张鹤0中级组长\", \"张鹤教总高影组中高师\", \"张鹤教学多角色\", \"教总23中\", 2, 2, 0, 0, 0, \"0.00%\", \"0.00%\", \"0.00%\" ], [ \"ishow中级\", \"张鹤0中级组长\", \"张鹤初中级老师\", \"张鹤0中级学习顾问\", \"初中级老师23中\", 2, 2, 0, 0, 0, \"0.00%\", \"0.00%\", \"0.00%\" ], [ \"ishow影视\", \"张鹤教总高影组中高师\", \"张鹤教学多角色\", \"\", \"张多角色影视班\", 0, 0, 0, 0, 0, \"0.00%\", \"0.00%\", \"0.00%\" ] ] }, \"option\": { \"start_time\": 1472659200, \"end_time\": 1475164800, \"isCampus\": false, \"campus_id\": 1, \"campus_name\": \"总部\", \"position_id\": 22, \"position_name\": \"\", \"user_id\": 107, \"user_name\": \"张鹤教总高影组中高师\", \"avatar\": \"\" } } ";
//        process = gson.fromJson(str, TeachProcess.class);
        ArrayList<CircleChartView.Value> yVals1 = new ArrayList<>();

        List<String> heads = process.selfChartData.head;
        List<String> bodys = process.selfChartData.body.get(0);

        if (heads.isEmpty()) {
            String data = "{\"head\":[\"带班人数\",\"升学基数\",\"升学人数\",\"全款人数\"],\"body\":[[0,0,0,0]]}";
            Gson gson = new Gson();
            ChartProcess chartProcess = gson.fromJson(data, ChartProcess.class);
            for (int i = 0; i < 4; i++) {
                yVals1.add(new CircleChartView.Value(chartProcess.head.get(i), "0"));
            }
        } else {
            for (int i = 0; i < heads.size() - 2; i++) {
                yVals1.add(new CircleChartView.Value(heads.get(i), bodys.get(i)));
            }
        }

        mCircleChartView.setData(yVals1, getString(R.string.data_title_last_7));
    }

    @Override
    public void getTeachingProcessFaild(String msg) {
        showToast(msg);
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    @OnClick({R.id.data_market, R.id.data_campus, R.id.data_other})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.data_market:
               startActivity(new Intent(getActivity(), ClassAttendActivity.class));
              /*  if (JumpManager.checkUserPermision(getActivity(), Resource.DATA_DATAANALYZE_TEACHINGPROCESSANALYSIS)) {
                    startActivity(new Intent(getActivity(), TeachProcessActivity.class));
                }*/
                break;
            case R.id.data_campus://DATA_DATAANALYZE_BAZAARCONTRAST
                if (JumpManager.checkUserPermision(getActivity(), Resource.DATA_DATAANALYZE_TEACHINGCONTRAST)) {
                    startActivity(new Intent(getActivity(), Performance4EduActivity.class));
                }
                break;
            case R.id.data_other:
                if (JumpManager.checkUserPermision(getActivity(), Resource.DATA_DATAANALYZE_TEACHINGOTHER)) {
                    Intent intent = new Intent(getActivity(), OtherStatisticActivity.class);
                    intent.putExtra(OtherStatisticActivity.IS_TEACH_DATA, true);
                    startActivity(intent);
                }
                break;
        }
    }

}
