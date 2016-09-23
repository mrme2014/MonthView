package com.ishow.ischool.business.salesprocess;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.commonlib.widget.imageloader.ImageLoaderUtil;
import com.commonlib.widget.pull.BaseViewHolder;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.saleprocess.MarketPositionObject;
import com.ishow.ischool.bean.saleprocess.Marketposition;
import com.ishow.ischool.bean.user.Avatar;
import com.ishow.ischool.bean.user.UserInfo;
import com.ishow.ischool.common.base.BaseListActivity4Crm;
import com.ishow.ischool.widget.custom.CircleImageView;
import com.ishow.ischool.widget.custom.FmItemTextView;

/**
 * Created by MrS on 2016/9/18.
 */
public class SelectPositionActivity extends BaseListActivity4Crm<SalesProcessPresenter, SalesProcessModel,MarketPositionObject> implements SalesProcessContract.View<Marketposition> {

    private SearchView searchView;
    private Marketposition marketpositions;
    private SearchSubordinatesFragment fragment;


    private int campus_id = -1;
    private int position_id =-1;
    private int REQUEST_CODE;
    private FragmentTransaction ft;
    public static final String PICK_POSITION = "pick_position";
    private String pick_position;

    @Override
    protected void initEnv() {
        super.initEnv();
        REQUEST_CODE = getIntent().getIntExtra("REQUEST_CODE", 0);
        campus_id = getIntent().getIntExtra("CAMPUS_ID",campus_id);

    }

    @Override
    protected void setUpView() {
        super.setUpView();
        recycler.enableLoadMore(false);
        recycler.enablePullToRefresh(false);

        setUpToolbar(R.string.select_subordinates, -1, MODE_BACK);
  /*      MenuItem item = mToolbar.getMenu().findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setSubmitButtonEnabled(true);
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchView.setQueryHint(getString(R.string.select_subordinates_searchview));
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
                    fragment = null;
                }
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ft = getSupportFragmentManager().beginTransaction();
                if (fragment == null) {
                    fragment = SearchSubordinatesFragment.newInstance(campus_id, query);
                }
                if (!fragment.isAdded()) {
                    ft.add(R.id.pullContent, fragment);
                }
                ft.show(fragment).commitAllowingStateLoss();
                return true;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });*/

        mPresenter.getOption("Marketposition", campus_id);

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
        this.marketpositions = marketpositions;
        loadSuccess(marketpositions.Marketposition);
    }

    @Override
    public void getListFail(String msg) {
        showToast(msg);
        loadFailed();
    }

    class selectHeadHolder extends BaseViewHolder {

        CircleImageView imageAvart;
        TextView name;

        public selectHeadHolder(View itemView) {
            super(itemView);
            imageAvart = (CircleImageView) itemView.findViewById(R.id.item_type1_avart);
            name = (TextView) itemView.findViewById(R.id.item_type1_name);
        }

        @Override
        public void onBindViewHolder(int position) {
            if (mUser == null)
                return;
            UserInfo userInfo = mUser.userInfo;
            Avatar avatar = mUser.avatar;
            if (imageAvart != null && avatar != null && avatar.file_name != null && avatar.file_name != "")
                ImageLoaderUtil.getInstance().loadImage(SelectPositionActivity.this, avatar.file_name, imageAvart);
            name.setText(userInfo == null ? "" : userInfo.user_name);

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
            intent.putExtra(SelectSubordinateActivity.PICK_QEQUEST_CODE,SelectSubordinateActivity.PICK_REQUEST_CODE);
            startActivityForResult(intent, SelectSubordinateActivity.PICK_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SelectSubordinateActivity.PICK_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            data.putExtra(PICK_POSITION,pick_position);
            setResult(REQUEST_CODE, data);
            this.finish();
        }
    }
}
