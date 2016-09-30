package com.ishow.ischool.business.salesprocess;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.commonlib.widget.LabelTextView;
import com.commonlib.widget.imageloader.ImageLoaderUtil;
import com.commonlib.widget.pull.BaseViewHolder;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.saleprocess.MarketPositionObject;
import com.ishow.ischool.bean.saleprocess.Marketposition;
import com.ishow.ischool.bean.user.Avatar;
import com.ishow.ischool.bean.user.CampusInfo;
import com.ishow.ischool.bean.user.UserInfo;
import com.ishow.ischool.common.base.BaseListActivity4Crm;
import com.ishow.ischool.common.manager.CampusManager;
import com.ishow.ischool.widget.custom.AvatarImageView;
import com.ishow.ischool.widget.custom.CircleImageView;
import com.ishow.ischool.widget.custom.FmItemTextView;
import com.ishow.ischool.widget.pickerview.PickerDialogFragment;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by MrS on 2016/9/18.
 */
public class SelectPositionActivity extends BaseListActivity4Crm<SalesProcessPresenter, SalesProcessModel, MarketPositionObject> implements SalesProcessContract.View<Marketposition>, View.OnClickListener {

    private Marketposition marketpositions;
    private LabelTextView ltv;

    private int campus_id = -1;
    private int position_id = -1;
    private int REQUEST_CODE;
    public static final String PICK_POSITION = "pick_position";
    public static final String PICK_CAMPUS = "pick_campus";
    public static final String PICK_CAMPUS_ID= "pick_campus_id";
    public  String pick_campus ;
    private String pick_position;
    private ArrayList<CampusInfo> campusInfos;

    @Override
    protected void initEnv() {
        super.initEnv();

        REQUEST_CODE = getIntent().getIntExtra("REQUEST_CODE", 0);
        campus_id = getIntent().getIntExtra("CAMPUS_ID", campus_id);
        pick_campus  = getIntent().getStringExtra("CAMPUS_NAME");
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        recycler.enableLoadMore(false);
        recycler.enablePullToRefresh(false);
        recycler.setOnRefreshListener(this);
        if (mUser.userInfo.campus_id == 1) {
            setUpToolbar(R.string.select_subordinates, R.menu.menu_sale, MODE_BACK);
            Menu menu = mToolbar.getMenu();
            MenuItem item = menu.getItem(0);
            item.setIcon(R.mipmap.icon_screen_down_white);
            ltv = (LabelTextView) MenuItemCompat.getActionView(item);
            ltv.setAboutMenuItem();
            Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.icon_screen_down_white);
            ltv.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
            ltv.setUpMenu(true);
            // PositionInfo positionInfo = mUser.positionInfo;
            ltv.setEllipsizeText(pick_campus==null?getString(R.string.select_subordinates_menu_default):pick_campus, 7);
             ltv.setOnClickListener(this);

        } else {
            setUpToolbar(R.string.select_subordinates, -1, MODE_BACK);
        }
        getPositionData();
    }

    private void getPositionData() {
        handProgressbar(true);
        TreeMap<String, Integer> map = new TreeMap<>();
        if (campus_id != 1) {
            map.put("campus_id", campus_id);
        }
        mPresenter.getOption("Marketposition", map);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        return super.onMenuItemClick(item);
    }

    private void getCampusSucess(final ArrayList<CampusInfo> campusInfos) {
        if (campusInfos == null || campusInfos.size() == 0) return;
        PickerDialogFragment.Builder builder = new PickerDialogFragment.Builder();
        ArrayList<String> campus = new ArrayList();
        for (int i = 0; i < campusInfos.size(); i++) {
            campus.add(campusInfos.get(i).name);
        }
        builder.setBackgroundDark(true).setDialogTitle(R.string.switch_campus).setDialogType(PickerDialogFragment.PICK_TYPE_OTHERS).setDatas(0, 1, campus);
        PickerDialogFragment fragment = builder.Build();
        fragment.show(getSupportFragmentManager(), "dialog");
        fragment.addCallback(new PickerDialogFragment.Callback<int[]>() {
            @Override
            public void onPickResult(int[] selectIds, String... result) {
                pick_campus = result[0];
                ltv.setEllipsizeText(result[0], 7);
                campus_id = campusInfos.get(selectIds[0]).id;
                getPositionData();
            }
        });
    }

    @Override
    protected void setUpData() {
        super.setUpData();
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0)
            return new selectHeadHolder(LayoutInflater.from(this).inflate(R.layout.activity_select_subordinates_list_item_type1, parent, false));
        return new selectBodyHolder(LayoutInflater.from(this).inflate(R.layout.activity_select_subordinates_list_item_type2, parent, false));
    }

    @Override
    public void onRefresh(int action) {
        recycler.onRefreshCompleted();
    }

    @Override
    protected int getItemType(int position) {
        if (position == 0) return 0;
        return 1;
    }

    @Override
    protected int getDataCounts() {
        return marketpositions == null ? 1 : marketpositions.Marketposition.size() + 1;
    }


    @Override
    public void getListSuccess(Marketposition marketpositions) {
        handProgressbar(false);
        this.marketpositions = marketpositions;
        loadSuccess(marketpositions.Marketposition);
    }

    @Override
    public void getListFail(String msg) {
        handProgressbar(false);
        showToast(msg);
        loadFailed();
    }

    @Override
    public void onClick(View v) {
        if (campusInfos == null) {
            campusInfos = CampusManager.getInstance().get();
        }
        getCampusSucess(campusInfos);
    }


    class selectHeadHolder extends BaseViewHolder {

        CircleImageView imageAvart;
        TextView name;
        AvatarImageView avatarImageView;

        public selectHeadHolder(View itemView) {
            super(itemView);
            imageAvart = (CircleImageView) itemView.findViewById(R.id.item_type1_avart);
            name = (TextView) itemView.findViewById(R.id.item_type1_name);
            avatarImageView = (AvatarImageView) itemView.findViewById(R.id.sales_avart_txt);
        }

        @Override
        public void onBindViewHolder(int position) {
            if (mUser == null)
                return;
            UserInfo userInfo = mUser.userInfo;
            Avatar avatar = mUser.avatar;
            if (imageAvart != null && avatar != null && avatar.file_name != null && avatar.file_name != "")
                ImageLoaderUtil.getInstance().loadImage(SelectPositionActivity.this, avatar.file_name, imageAvart);
            else {
                imageAvart.setVisibility(View.GONE);
                avatarImageView.setVisibility(View.VISIBLE);
                avatarImageView.setText(userInfo.user_name, userInfo.user_id, "");
            }
            name.setText(userInfo == null ? "" : userInfo.user_name);
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            //加个 if判断 是因为 如果数据为空的时候  显示了空界面 但还是 点击可响应
            if (marketpositions != null && marketpositions.Marketposition != null && marketpositions.Marketposition.size() > 0) {
                Intent data = new Intent();
                data.putExtra("no_choice", true);
                setResult(REQUEST_CODE, data);
                SelectPositionActivity.this.finish();
            }
        }
    }

    class selectBodyHolder extends BaseViewHolder {

        FmItemTextView textView;

        public selectBodyHolder(View itemView) {
            super(itemView);
            textView = (FmItemTextView) itemView.findViewById(R.id.item_type2);
        }

        @Override
        public void onBindViewHolder(int position) {

            if (textView != null)
                textView.setText(marketpositions.Marketposition.get(position - 1).name);
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            position_id = marketpositions.Marketposition.get(position - 1).id;
            pick_position = marketpositions.Marketposition.get(position - 1).name;
            Intent intent = new Intent(SelectPositionActivity.this, SelectSubordinateActivity.class);
            intent.putExtra(SelectSubordinateActivity.PICK_CAMPUS_ID, campus_id);
            intent.putExtra(SelectSubordinateActivity.PICK_POSITION_ID, position_id);
            intent.putExtra(SelectSubordinateActivity.PICK_QEQUEST_CODE, SelectSubordinateActivity.PICK_REQUEST_CODE);
            startActivityForResult(intent, SelectSubordinateActivity.PICK_REQUEST_CODE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SelectSubordinateActivity.PICK_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            data.putExtra(PICK_POSITION, pick_position);
            data.putExtra(PICK_CAMPUS, pick_campus);
            data.putExtra(PICK_CAMPUS_ID, campus_id);
            data.putExtra(SelectSubordinateActivity.PICK_POSITION_ID, position_id);
            setResult(REQUEST_CODE, data);
            this.finish();
        }
    }
}
