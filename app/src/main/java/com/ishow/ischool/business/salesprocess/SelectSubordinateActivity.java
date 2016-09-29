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


    protected void setUpData() {

    }

    /*@Override
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
                    getSupportFragmentManager().beginTransaction().remove(fragment).show(fragment1).commitAllowingStateLoss();
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
                    ft.add(R.id.pullContent, fragment).hide(fragment1);
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
        fragment1 =new SearchSubordinatesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("campus_id", campus_id);
        bundle.putInt("position_id", position_id);
        fragment1.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.pullContent,fragment1).commitAllowingStateLoss();
//        TreeMap<String,Integer> map= new TreeMap<>();
//        if (campus_id!=1){
//            map.put("campus_id",campus_id);
//            map.put("position_id",position_id);
//        }
//        mPresenter.getOptionSubordinate("Subordinate",map);

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
        recycler.onRefreshCompleted();
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
    }*/
}
