package com.ishow.ischool.business.user.pick;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.commonlib.widget.imageloader.ImageLoaderUtil;
import com.commonlib.widget.pull.BaseViewHolder;
import com.commonlib.widget.pull.PullRecycler;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.saleprocess.MarketPositionObject;
import com.ishow.ischool.bean.saleprocess.Marketposition;
import com.ishow.ischool.bean.user.Avatar;
import com.ishow.ischool.bean.user.UserInfo;
import com.ishow.ischool.common.base.BaseListActivity4Crm;
import com.ishow.ischool.common.manager.CampusManager;
import com.ishow.ischool.widget.custom.AvatarImageView;
import com.ishow.ischool.widget.custom.CircleImageView;
import com.ishow.ischool.widget.custom.FmItemTextView;

import java.util.ArrayList;

/**
 * Created by mini on 2016/10/13.
 */
public class PositionPickActivity extends BaseListActivity4Crm<UserPickPresenter, UserPickModel, MarketPositionObject> implements UserPickContract.View<Marketposition> {

    public static final int REQUEST_CODE_PICKPOSITION = 1001;
    private Marketposition marketpositions;

    private int mPickPositionId;
    private String mPickPositionName;
    private int mPickCampusId;
    private String mPickCampusName;
    public static final String PICK_POSITION_ID = "pick_position_id";
    public static final String PICK_POSITION_NAME = "pick_position_name";
    public static final String PICK_CAMPUS_ID = "pick_campus_id";
    public static final String PICK_CAMPUS_NAME = "pick_campus_name";
    private MenuItem campusMenu;

    @Override
    protected void initEnv() {
        super.initEnv();

        mPickCampusId = getIntent().getIntExtra("CAMPUS_ID", -1);
        mPickCampusName = getIntent().getStringExtra("CAMPUS_NAME");
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        setUpToolbar(R.string.select_subordinates, R.menu.menu_pickposition, MODE_BACK);
        campusMenu = mToolbar.getMenu().findItem(R.id.campus);
        String subtitle = mPickCampusId == 1 ? "所有校区" : CampusManager.getInstance().getCampusNameById(mPickCampusId);
        campusMenu.setTitle(subtitle);
        campusMenu.setChecked(false);
    }

    @Override
    public void onRefresh(int action) {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }

        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mCurrentPage = 1;
        }

        mPresenter.getPosition(mPickCampusId);
    }

    @Override
    public void getListSuccess(Marketposition marketpositions) {
        this.marketpositions = marketpositions;
        loadSuccess(marketpositions.Marketposition);
    }

    @Override
    public void getListFail(String msg) {
        showToast(msg);
        loadFailed();
    }


    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0)
            return new selectHeadHolder(LayoutInflater.from(this).inflate(R.layout.activity_select_subordinates_list_item_type1, parent, false));
        return new selectBodyHolder(LayoutInflater.from(this).inflate(R.layout.activity_select_subordinates_list_item_type2, parent, false));
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
                ImageLoaderUtil.getInstance().loadImage(PositionPickActivity.this, avatar.file_name, imageAvart);
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
                Intent intent = new Intent();
                intent.putExtra("no_choice", true);
                setResult(REQUEST_CODE_PICKPOSITION, intent);
                PositionPickActivity.this.finish();
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
            mPickPositionId = marketpositions.Marketposition.get(position - 1).id;
            mPickPositionName = marketpositions.Marketposition.get(position - 1).name;
            Intent intent = new Intent(PositionPickActivity.this, UserPickActivity.class);
            intent.putExtra(PositionPickActivity.PICK_CAMPUS_ID, mPickCampusId);
            intent.putExtra(PositionPickActivity.PICK_POSITION_ID, mPickPositionId);
            startActivityForResult(intent, UserPickActivity.REQUEST_CODE_PICKUSER);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UserPickActivity.REQUEST_CODE_PICKUSER && resultCode == RESULT_OK && data != null) {
            data.putExtra(PositionPickActivity.PICK_POSITION_ID, mPickPositionId);
            data.putExtra(PositionPickActivity.PICK_POSITION_NAME, mPickPositionName);
            data.putExtra(PositionPickActivity.PICK_CAMPUS_ID, mPickCampusId);
            data.putExtra(PositionPickActivity.PICK_CAMPUS_NAME, mPickCampusName);

            setResult(RESULT_OK, data);
            this.finish();
        }
    }
}
