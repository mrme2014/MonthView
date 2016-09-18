package com.ishow.ischool.business.salesprocess;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.commonlib.util.LogUtil;
import com.commonlib.widget.pull.BaseViewHolder;
import com.ishow.ischool.R;
import com.ishow.ischool.common.base.BaseListActivity4Crm;
import com.ishow.ischool.widget.custom.CircleImageView;
import com.ishow.ischool.widget.custom.FmItemTextView;

/**
 * Created by MrS on 2016/9/18.
 */
public class SelectSubordinatesActivity extends BaseListActivity4Crm {

    private SearchView searchView;

    @Override
    protected void setUpView() {
        super.setUpView();
        recycler.enableLoadMore(false);
        recycler.enablePullToRefresh(false);

        setUpToolbar(R.string.select_subordinates,R.menu.menu_pickreferrer,MODE_BACK);
        MenuItem item = mToolbar.getMenu().findItem(R.id.action_search);
        searchView = (SearchView)MenuItemCompat.getActionView(item);
        searchView.setQueryHint(getString(R.string.select_subordinates_searchview));
        //searchView.setIconifiedByDefault(true);
       // searchView.setSubmitButtonEnabled(true);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchView.clearFocus();
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                LogUtil.e(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    protected void setUpData() {
        super.setUpData();
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onRefresh(int action) {

    }

    @Override
    protected int getItemType(int position) {
        return super.getItemType(position);
    }

    @Override
    protected int getDataCounts() {
        return super.getDataCounts();
    }

    class selectHeadHolder extends BaseViewHolder{

        CircleImageView avart;
        TextView name;
        public selectHeadHolder(View itemView) {
            super(itemView);
            avart = (CircleImageView) itemView.findViewById(R.id.item_type1_avart);
            name = (TextView) itemView.findViewById(R.id.item_type1_name);
        }

        @Override
        public void onBindViewHolder(int position) {

        }

    }

    class selectBodyHolder extends BaseViewHolder{

        FmItemTextView textView;
        public selectBodyHolder(View itemView) {
            super(itemView);
            textView = (FmItemTextView) itemView.findViewById(R.id.item_type2);
        }

        @Override
        public void onBindViewHolder(int position) {

        }
    }
}
