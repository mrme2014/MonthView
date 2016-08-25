package com.ishow.ischool.widget.custom;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ishow.ischool.R;
import com.ishow.ischool.application.Cons;
import com.ishow.ischool.bean.university.UniversityInfo;
import com.ishow.ischool.bean.user.Campus;
import com.ishow.ischool.bean.user.User;
import com.ishow.ischool.business.pickreferrer.PickReferrerActivity;
import com.ishow.ischool.business.universitypick.UniversityPickActivity;
import com.ishow.ischool.common.api.MarketApi;
import com.ishow.ischool.common.manager.CampusManager;
import com.ishow.ischool.common.manager.UserManager;
import com.ishow.ischool.util.AppUtil;
import com.ishow.ischool.widget.pickerview.PickerDialogFragment;
import com.ishow.ischool.widget.pickerview.PickerWheelViewPop;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wqf on 16/8/22.
 */
public class StatisticsFilterFragment extends DialogFragment implements InputLinearLayout.EidttextClick, View.OnTouchListener {
    @BindView(R.id.item_campus)
    InputLinearLayout campusIL;
    @BindView(R.id.time_type)
    TextView timeType;
    @BindView(R.id.start_time)
    EditText startTimeEt;
    @BindView(R.id.start_time_clear)
    ImageView startTimeIv;
    @BindView(R.id.end_time)
    EditText endTimeEt;
    @BindView(R.id.end_time_clear)
    ImageView endTimeIv;
    @BindView(R.id.item_pay_state)
    InputLinearLayout payStateIL;
    @BindView(R.id.item_source)
    InputLinearLayout sourceIL;
    @BindView(R.id.item_university)
    InputLinearLayout universityIL;
    @BindView(R.id.item_referrer)
    InputLinearLayout referrerIL;

    private Dialog dialog;

    private final int TYPE_START_TIME = 1;
    private final int TYPE_END_TIME = 2;

    private Boolean startTimeFlag = true;
    private GestureDetector mGestureDetector;
    private SimpleDateFormat sdf;

    private boolean isUserCampus;       // 是否是校区员工（非总部员工）
    private String mFilterCampusId;
    private String mFilterTimeType = "";
    private String mFilterStartTime;
    private String mFilterEndTime;
    private String mFilterPayState;
    private String mFilterSource;
    private String mFilterSourceName;
    private String mFilterUniversityId;
    private String mFilterProvinceId;
    private String mFilterReferrerId;
    private String mFilterUniversityName;
    private String mFilterReferrerName;

    private FilterCallback callback;
    private ArrayList<String> sources;
    private UniversityInfo mUniversityInfo;


    public static StatisticsFilterFragment newInstance(Map<String, String> params, String source_name, String college_name, String referrer_name) {
        StatisticsFilterFragment fragment = new StatisticsFilterFragment();
        Bundle args = new Bundle();
        Iterator iter = params.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            args.putString(entry.getKey().toString(), entry.getValue().toString());
        }
        args.putString("source_name", source_name);
        args.putString("college_name", college_name);
        args.putString("referrer_name", referrer_name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mFilterSource = bundle.getString("source", "");
            mFilterCampusId = bundle.getString("campus_id", "");
            mFilterTimeType = bundle.getString("time_type", "");
            mFilterStartTime = bundle.getString("start_time", "");
            mFilterEndTime = bundle.getString("end_time", "");
            mFilterPayState = bundle.getString("pay_state", "");
            mFilterUniversityId = bundle.getString("college_id", "");
            mFilterProvinceId = bundle.getString("province_id", "");
            mFilterReferrerId = bundle.getString("referrer", "");
            mFilterSourceName = bundle.getString("source_name", "");
            mFilterUniversityName = bundle.getString("college_name", "");
            mFilterReferrerName = bundle.getString("referrer_name", "");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getContext(), R.style.Comm_dialogfragment_windowAnimationStyle);
        Window win = dialog.getWindow();
        // 一定要设置Background，如果不设置，window属性设置无效
        win.setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        WindowManager.LayoutParams params = win.getAttributes();
        params.gravity = Gravity.TOP;
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        win.setAttributes(params);

        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.filter_layout, null);
        //  View viewById = contentView.findViewById(R.id.root);
        // viewById.setTop(UIUtil.getToolbarSize(getContext()));
        ButterKnife.bind(this, contentView);

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(contentView);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        Window window = dialog.getWindow();
        window.setLayout(-1, -1);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        initFilter();
    }

    void initFilter() {
        User user = UserManager.getInstance().get();
        isUserCampus = (user.userInfo.campus_id == Campus.HEADQUARTERS) ? false : true;
        if (!isUserCampus) {     // 总部才显示“所属校区”筛选条件
            campusIL.setVisibility(View.VISIBLE);
            mFilterCampusId = Campus.HEADQUARTERS + "";       // 总部获取学院统计列表campus_id传1
        } else {
            mFilterCampusId = user.userInfo.campus_id + "";
        }
        if (mFilterTimeType.equals("2")) {
            timeType.setText(getString(R.string.item_matriculation_time));
        } else {
            mFilterTimeType = "1";
            timeType.setText(getString(R.string.item_register_time));
        }
        if (!TextUtils.isEmpty(mFilterStartTime)) {
            startTimeEt.setText(sdf.format(new Date(Long.parseLong(mFilterStartTime) * 1000)));
        }
        if (!TextUtils.isEmpty(mFilterEndTime)) {
            endTimeEt.setText(sdf.format(new Date(Long.parseLong(mFilterEndTime) * 1000)));
        }
        if (!TextUtils.isEmpty(mFilterPayState)) {
            payStateIL.setContent(AppUtil.getPayState().get(Integer.parseInt(mFilterPayState) - 1));
        }

        // 来源方式权限限定
        int curPositionId = user.positionInfo.id;
//        if (curPositionId == Cons.Position.Chendujiangshi.ordinal()) {
//            mFilterSource = MarketApi.TYPESOURCE_READING + "";
//        } else if (curPositionId == Cons.Position.Xiaoliaozhuanyuan.ordinal()) {
//            mFilterSource = MarketApi.TYPESOURCE_CHAT + "";
        if (curPositionId == Cons.Position.Xiaoyuanjingli.ordinal() || curPositionId == Cons.Position.Shichangzhuguan.ordinal()) {
            sources = new ArrayList<String>() {{
                add("晨读");
                add("转介绍");
                add("全部来源");
            }};
            sourceIL.setVisibility(View.VISIBLE);
        } else if (curPositionId == Cons.Position.Xiaoliaozhuguan.ordinal()) {
            sources = new ArrayList<String>() {{
                add("校聊");
                add("转介绍");
                add("全部来源");
            }};
            sourceIL.setVisibility(View.VISIBLE);
        } else {
            sources = new ArrayList<String>() {{
                add("晨读");
                add("校聊");
                add("转介绍");
                add("全部来源");
            }};
            sourceIL.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(mFilterSource)) {
            sourceIL.setContent(mFilterSourceName);
        }
        if (!TextUtils.isEmpty(mFilterUniversityId)) {
            universityIL.setContent(mFilterUniversityName);
        }
        if (!TextUtils.isEmpty(mFilterReferrerId)) {
            referrerIL.setContent(mFilterReferrerName);
        }
        campusIL.setOnEidttextClick(this);
        payStateIL.setOnEidttextClick(this);
        sourceIL.setOnEidttextClick(this);
        universityIL.setOnEidttextClick(this);
        referrerIL.setOnEidttextClick(this);
        mGestureDetector = new GestureDetector(new Gesturelistener());
        startTimeEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                startTimeFlag = true;
                return mGestureDetector.onTouchEvent(motionEvent);
            }
        });
        endTimeEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                startTimeFlag = false;
                return mGestureDetector.onTouchEvent(motionEvent);
            }
        });
    }

    @OnClick({R.id.commun_block_top, R.id.time_type, R.id.start_time_clear, R.id.end_time_clear, R.id.filter_reset, R.id.filter_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.time_type:
                showPickPop(-1, 0, 1, new ArrayList<String>(2) {{
                    add("登记时间");
                    add("上课时间");
                }}, new PickerWheelViewPop.PickCallback<int[]>() {
                    @Override
                    public void onPickCallback(int[] position, String... result) {
                        if (result != null) timeType.setText(result[0]);
                        if (position != null) mFilterTimeType = (position[0] + 1) + "";
                    }
                });
                break;
            case R.id.start_time_clear:
                startTimeEt.setText("");
                startTimeIv.setVisibility(View.GONE);
                break;
            case R.id.end_time_clear:
                endTimeEt.setText("");
                endTimeIv.setVisibility(View.GONE);
                break;
            case R.id.filter_reset:
                resetFilter();
                break;
            case R.id.filter_ok:
                this.dismiss();
                if (callback != null) {
                    HashMap<String, String> params = new HashMap<>();
                    if (!TextUtils.isEmpty(mFilterStartTime) || !TextUtils.isEmpty(mFilterEndTime)) {
                        params.put("time_type", mFilterTimeType);
                        if (!TextUtils.isEmpty(mFilterStartTime)) {
                            params.put("start_time", mFilterStartTime);
                        }
                        if (!TextUtils.isEmpty(mFilterEndTime)) {
                            params.put("end_time", mFilterEndTime);
                        }
                    }
                    if (!TextUtils.isEmpty(mFilterPayState)) {
                        params.put("pay_state", mFilterPayState);
                    }
                    if (!TextUtils.isEmpty(mFilterSource)) {
                        params.put("source", mFilterSource);
                    }
                    if (!TextUtils.isEmpty(mFilterUniversityId)) {
                        params.put("college_id", mFilterUniversityId);
                        params.put("province_id", mFilterProvinceId);
                    }
                    if (!TextUtils.isEmpty(mFilterReferrerId)) {
                        params.put("referrer", mFilterReferrerId);
                    }
                    callback.onFinishFilter(Integer.parseInt(mFilterCampusId), mFilterSource, params, mFilterSourceName, mFilterUniversityName, mFilterReferrerName);
                }
                break;
            case R.id.commun_block_top:
            case R.id.commun_block_bottom:
                this.dismiss();
                if (callback != null)
                    callback.onCancelDilaog();
                break;
        }
    }

    void resetFilter() {
        campusIL.setContent("");
        startTimeEt.setText("");
        endTimeEt.setText("");
        payStateIL.setContent("");
        sourceIL.setContent("");
        universityIL.setContent("");
        referrerIL.setContent("");
        if (!isUserCampus) {
            mFilterCampusId = Campus.HEADQUARTERS + "";       // 总部获取学院统计列表campus_id传1
        }
        mFilterStartTime = "";
        mFilterEndTime = "";
        mFilterPayState = "";
        mFilterSource = "";
        mFilterUniversityId = "";
        mFilterProvinceId = "";
        mFilterReferrerId = "";
        mFilterSourceName = "";
        mFilterUniversityName = "";
        mFilterReferrerName = "";
    }

    @Override
    public void onEdittextClick(View view) {
        switch (view.getId()) {
            case R.id.item_campus:
                showPickPop(R.string.item_campus, 0, 1, CampusManager.getInstance().getCampusName(), new PickerWheelViewPop.PickCallback<int[]>() {
                    @Override
                    public void onPickCallback(int[] position, String... result) {
                        if (result != null) campusIL.setContent(result[0]);
                        if (position != null) mFilterCampusId = (position[0] + 1) + "";
                    }
                });
                break;
            case R.id.item_pay_state:
//                showPickPop(R.string.item_pay_state, 0, 1, AppUtil.getPayState(), new PickerWheelViewPop.PickCallback<int[]>() {
//                    @Override
//                    public void onPickCallback(int[] position, String... result) {
//                        if (result != null) payStateIL.setContent(result[0]);
//                        if (position != null) mFilterPayState = (position[0] + 1) + "";
//                    }
//                });
                PickerDialogFragment.Builder payBuilder = new PickerDialogFragment.Builder();
                payBuilder.setBackgroundDark(true)
                        .setDialogTitle(R.string.item_pay_state)
                        .setDialogType(PickerDialogFragment.PICK_TYPE_OTHERS)
                        .setDatas(0, 1, AppUtil.getPayState());
                ;
                PickerDialogFragment payFragment = payBuilder.Build();
                payFragment.show(getChildFragmentManager(), "dialog");
                payFragment.addMultilinkPickCallback(new PickerDialogFragment.MultilinkPickCallback() {
                    @Override
                    public ArrayList<String> endSelect(int colum, int selectPosition, String text) {
                        return null;
                    }

                    @Override
                    public void onPickResult(Object object, String... result) {
                        payStateIL.setContent(result[0]);
                        mFilterPayState = (AppUtil.getPayState().indexOf(result[0]) + 1) + "";
                    }
                });
                break;
            case R.id.item_source:
                PickerDialogFragment.Builder fromBuilder = new PickerDialogFragment.Builder();
                fromBuilder.setBackgroundDark(true)
                        .setDialogTitle(R.string.item_from)
                        .setDialogType(PickerDialogFragment.PICK_TYPE_OTHERS)
                        .setDatas(0, 1, sources);
                ;
                PickerDialogFragment fromFragment = fromBuilder.Build();
                fromFragment.show(getChildFragmentManager(), "dialog");
                fromFragment.addMultilinkPickCallback(new PickerDialogFragment.MultilinkPickCallback() {
                    @Override
                    public ArrayList<String> endSelect(int colum, int selectPosition, String text) {
                        return null;
                    }

                    @Override
                    public void onPickResult(Object object, String... result) {
                        mFilterSourceName = result[0];
                        sourceIL.setContent(mFilterSourceName);
                        switch (mFilterSourceName) {
                            case "晨读":
                                mFilterSource = MarketApi.TYPESOURCE_READING + "";
                                break;
                            case "转介绍":
                                mFilterSource = MarketApi.TYPESOURCE_RECOMMEND + "";
                                break;
                            case "校聊":
                                mFilterSource = MarketApi.TYPESOURCE_CHAT + "";
                                break;
                            case "全部来源":
                                mFilterSource = MarketApi.TYPESOURCE_ALL + "";
                                break;
                        }

                    }
                });
                break;
            case R.id.item_university:
                startActivityForResult(new Intent(getActivity(), UniversityPickActivity.class), UniversityPickActivity.REQUEST_CODE_PICK_UNIVERSITY);
                break;
            case R.id.item_referrer:
                startActivityForResult(new Intent(getActivity(), PickReferrerActivity.class), PickReferrerActivity.REQUEST_CODE_PICK_REFERRER);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
        switch (requestCode) {
            case UniversityPickActivity.REQUEST_CODE_PICK_UNIVERSITY:
                if (data != null) {
                    mUniversityInfo = data.getParcelableExtra(UniversityPickActivity.KEY_PICKED_UNIVERSITY);
                    mFilterUniversityName = mUniversityInfo.name;
                    universityIL.setContent(mFilterUniversityName);
                    mFilterUniversityId = mUniversityInfo.id + "";
                    mFilterProvinceId = mUniversityInfo.prov_id + "";
                    //                    city_id = mUniversityInfo.city_id;
                }
                break;
            case PickReferrerActivity.REQUEST_CODE_PICK_REFERRER:
                if (data != null) {
                    User user = data.getParcelableExtra(PickReferrerActivity.PICKREFERRER);
                    mFilterReferrerName = user.userInfo.user_name;
                    referrerIL.setContent(mFilterReferrerName);
                    mFilterReferrerId = user.userInfo.user_id + "";
                }
                break;
        }
//        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    private class Gesturelistener implements GestureDetector.OnGestureListener {

        public boolean onDown(MotionEvent e) {
            // TODO Auto-generated method stub
            return false;
        }

        public void onShowPress(MotionEvent e) {
            // TODO Auto-generated method stub
        }

        public boolean onSingleTapUp(MotionEvent e) {
            // TODO Auto-generated method stub
            if (startTimeFlag) {
                showTimePickPop(TYPE_START_TIME);
            } else {
                showTimePickPop(TYPE_END_TIME);
            }
            return false;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            // TODO Auto-generated method stub
            return false;
        }

        public void onLongPress(MotionEvent e) {
            // TODO Auto-generated method stub
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            // TODO Auto-generated method stub
            return false;
        }
    }

    private void showTimePickPop(final int type) {

        AppUtil.showTimePickerDialog(getChildFragmentManager(), R.string.commun_date, new PickerDialogFragment.Callback<Integer>() {
            @Override
            public void onPickResult(Integer unix, String... result) {
                if (type == TYPE_START_TIME) {
                    startTimeEt.setText(result[0]);
                    mFilterStartTime = String.valueOf(unix);
                    startTimeIv.setVisibility(View.VISIBLE);
                } else if (type == TYPE_END_TIME) {
                    endTimeEt.setText(result[0]);
                    mFilterEndTime = String.valueOf(unix);
                    endTimeIv.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void showPickPop(int titleResId, int defalut, int colum, ArrayList<String> datas, PickerWheelViewPop.PickCallback callback) {
        PickerWheelViewPop pop = new PickerWheelViewPop(getContext());
        pop.initMultiSelectPanel(titleResId);
        pop.setDatas(defalut, colum, datas);
        pop.showAtLocation(universityIL, Gravity.BOTTOM, 0, 0);
        pop.addPickCallback(callback);
    }

    public interface FilterCallback {
        void onFinishFilter(int campusId, String source, Map<String, String> map, String source_name, String university_name, String referrer_name);

        void onCancelDilaog();
    }

    public void setOnFilterCallback(FilterCallback callback) {
        this.callback = callback;
    }
}
