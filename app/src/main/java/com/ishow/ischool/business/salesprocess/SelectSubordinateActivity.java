package com.ishow.ischool.business.salesprocess;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.ishow.ischool.R;
import com.ishow.ischool.bean.saleprocess.Subordinate;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MrS on 2016/9/18.
 */
public class SelectSubordinateActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private SearchView searchView;
    private Subordinate subordinate;
    private SearchSubordinatesFragment fragment;
    private SearchSubordinatesFragment fragment1;


    public static final String PICK_USER = "data";
    public static final String P_TITLE = "title";
    public static final String PICK_CAMPUS_ID = "campus_id";
    public static final String PICK_POSITION_ID = "position_id";
    public static final String PICK_QEQUEST_CODE = "request_code";
    public static final int PICK_REQUEST_CODE = 1000;


    private int campus_id = -1;
    private int position_id = -1;
    private FragmentTransaction ft;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        campus_id = getIntent().getIntExtra(PICK_CAMPUS_ID, campus_id);
        position_id = getIntent().getIntExtra(PICK_POSITION_ID, position_id);
        setContentView(R.layout.activity_selectsubordinate);
        ButterKnife.bind(this);

        toolbar.inflateMenu(R.menu.menu_pickreferrer);
        toolbarTitle.setText(getString(R.string.select_subordinates));
        toolbar.setNavigationIcon(R.mipmap.icon_return);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectSubordinateActivity.this.finish();
            }
        });
        setUpView();
    }


    protected void setUpView() {
        Menu Menuitem = toolbar.getMenu();
        MenuItem item = Menuitem.getItem(0);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setSubmitButtonEnabled(true);
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchView.setQueryHint(getString(R.string.select_subordinates_searchview));
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction().remove(fragment).show(fragment1).commitAllowingStateLoss();
                    fragment = null;
                }
                toolbar.setNavigationIcon(R.mipmap.icon_return);
                return false;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ft = getSupportFragmentManager().beginTransaction();
                if (fragment == null) {
                    fragment = new SearchSubordinatesFragment();
                    ft.add(R.id.pullContent, fragment).hide(fragment1);
                    ft.show(fragment).commit();
                }
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                //  if (fragment!=null)fragment = SearchSubordinatesFragment.newInstance(campus_id,position_id,query);
                fragment.search(campus_id, position_id, query);

                return true;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        fragment1 = new SearchSubordinatesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("campus_id", campus_id);
        bundle.putInt("position_id", position_id);
        fragment1.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.pullContent, fragment1).commitAllowingStateLoss();
    }
}
