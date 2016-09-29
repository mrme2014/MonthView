package com.ishow.ischool.business.salesprocess;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.commonlib.widget.pull.BaseViewHolder;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.saleprocess.Subordinate;
import com.ishow.ischool.bean.saleprocess.SubordinateObject;
import com.ishow.ischool.business.user.pick.UserPickActivity;
import com.ishow.ischool.common.base.BaseListActivity4Crm;
import com.ishow.ischool.widget.custom.AvatarImageView;
import com.ishow.ischool.widget.custom.CircleTransform;

import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MrS on 2016/9/18.
 */
public class SelectSubordinateActivity extends BaseListActivity4Crm<SalesProcessPresenter, SalesProcessModel,SubordinateObject> implements SalesProcessContract.View<Subordinate> {

    private SearchView searchView;
    private Subordinate subordinate;
    private SearchSubordinatesFragment fragment;



    public static final String PICK_USER = "data";
    public static final String P_TITLE = "title";
    public static final String PICK_CAMPUS_ID = "campus_id";
    public static final String PICK_POSITION_ID = "position_id";
    public static final String  PICK_QEQUEST_CODE = "request_code";
    public static final int PICK_REQUEST_CODE=1000;

    private int campus_id = -1;
    private int position_id=-1;
    private FragmentTransaction ft;
    @Override
    protected void initEnv() {
        super.initEnv();

        campus_id = getIntent().getIntExtra(PICK_CAMPUS_ID,campus_id);
        position_id = getIntent().getIntExtra(PICK_POSITION_ID,position_id);
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        recycler.enableLoadMore(false);
        recycler.enablePullToRefresh(false);

        setUpToolbar(R.string.select_subordinates, R.menu.menu_pickreferrer, MODE_BACK);
        MenuItem item = mToolbar.getMenu().findItem(R.id.action_search);
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
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ft = getSupportFragmentManager().beginTransaction();
                if (fragment==null) {
                    fragment = new SearchSubordinatesFragment();
                    ft.add(R.id.pullContent, fragment);
                    ft.show(fragment).commit();
                }
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

              //  if (fragment!=null)fragment = SearchSubordinatesFragment.newInstance(campus_id,position_id,query);
                fragment.search(campus_id,position_id,query);

                return true;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        TreeMap<String,Integer> map= new TreeMap<>();
        if (campus_id!=1){
            map.put("campus_id",campus_id);
            map.put("position_id",position_id);
        }
        mPresenter.getOptionSubordinate("Subordinate",map);

    }

    @Override
    protected void setUpData() {
        super.setUpData();
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        return new selectBodyHolder(LayoutInflater.from(this).inflate(R.layout.item_pick_referrer, parent, false));
    }

    @Override
    public void onRefresh(int action) {

    }



    @Override
    protected int getDataCounts() {
        return subordinate == null ? 0 : subordinate.Subordinate.size() ;
    }


    @Override
    public void getListSuccess(Subordinate subordinate) {
        this.subordinate = subordinate;
        loadSuccess(subordinate.Subordinate);
    }

    @Override
    public void getListFail(String msg) {
        showToast(msg);
        loadFailed();
    }
    class selectBodyHolder extends BaseViewHolder {
        @BindView(R.id.avatar_text)
        AvatarImageView avatarTv;
        @BindView(R.id.avatar_iv)
        ImageView avatarIv;
        @BindView(R.id.referrer_name)
        TextView referrerName;
      //  FmItemTextView textView;

        public selectBodyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(final int position) {
            SubordinateObject data = subordinate.Subordinate.get(position);
            if (data.file_name != null && !TextUtils.isEmpty(data.file_name)) {
                avatarTv.setVisibility(View.GONE);
                avatarIv.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext()).load(data.user_name).fitCenter().placeholder(R.mipmap.img_header_default)
                        .transform(new CircleTransform(getApplicationContext())).into(new ImageViewTarget<GlideDrawable>(avatarIv) {
                    @Override
                    protected void setResource(GlideDrawable resource) {
                        avatarIv.setImageDrawable(resource);
                    }
                    @Override
                    public void setRequest(Request request) {
                        avatarIv.setTag(position);
                        avatarIv.setTag(R.id.glide_tag_id,request);
                    }

                    @Override
                    public Request getRequest() {
                        return (Request) avatarIv.getTag(R.id.glide_tag_id);
                    }
                });
            } else {
                avatarIv.setVisibility(View.GONE);
                avatarTv.setVisibility(View.VISIBLE);
                avatarTv.setText(data.user_name, data.id, data.file_name);
            }
            referrerName.setText(data.user_name);
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemClick(view, position);
            Intent intent = new Intent(SelectSubordinateActivity.this, UserPickActivity.class);
            intent.putExtra(PICK_USER,subordinate.Subordinate.get(position));
            setResult(RESULT_OK,intent);
            SelectSubordinateActivity.this.finish();

        }
    }
}
