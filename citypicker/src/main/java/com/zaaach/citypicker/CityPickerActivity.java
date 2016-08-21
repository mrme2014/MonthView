package com.zaaach.citypicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.commonlib.util.LogUtil;
import com.zaaach.citypicker.adapter.CityListAdapter;
import com.zaaach.citypicker.adapter.ResultListAdapter;
import com.zaaach.citypicker.db.DBManager;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.utils.LimitQueue;
import com.zaaach.citypicker.utils.SearchLocalHistoryUtil;
import com.zaaach.citypicker.utils.StringUtils;
import com.zaaach.citypicker.view.SideLetterBar;

import java.util.ArrayList;
import java.util.List;

/**
 * author zaaach on 2016/1/26.
 */
public class CityPickerActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int REQUEST_CODE_PICK_CITY = 2333;
    public static final String KEY_PICKED_CITY = "picked_city";

    private ListView mListView;
    private ListView mResultListView;
    private SideLetterBar mLetterBar;
    private EditText searchBox;
    private ImageView clearBtn;
    private ViewGroup emptyView;
    private Toolbar toolbar;
    private TextView title;

    private CityListAdapter mCityAdapter;
    private ResultListAdapter mResultAdapter;
    private List<City> mAllCities;
    private DBManager dbManager;

    private AMapLocationClient mLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

        initData();
        initView();
        initLocation();
    }

    private void initLocation() {
        mLocationClient = new AMapLocationClient(this);
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);       //高精度模式
        option.setOnceLocation(true);
        mLocationClient.setLocationOption(option);
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        String city = aMapLocation.getCity();
                        String district = aMapLocation.getDistrict();
                        LogUtil.d("CityPickerActivity onLocationChanged", "city: " + city);
                        LogUtil.d("CityPickerActivity onLocationChanged", "district: " + district);
                        String location = StringUtils.extractLocation(city, district);
                        mCityAdapter.updateLocateState(LocateState.SUCCESS, location);
                    } else {
                        //定位失败
                        mCityAdapter.updateLocateState(LocateState.FAILED, null);
                    }
                }
            }
        });
        mLocationClient.startLocation();
    }

    private void initData() {
        dbManager = new DBManager(this);
        dbManager.copyDBFile();
        mAllCities = dbManager.getAllCities();
        mCityAdapter = new CityListAdapter(this, mAllCities);
        mCityAdapter.setOnCityClickListener(new CityListAdapter.OnCityClickListener() {
            @Override
            public void onCityClick(String name) {
                back(name);
            }

            @Override
            public void onLocateClick() {
                Log.e("onLocateClick", "重新定位...");
                mCityAdapter.updateLocateState(LocateState.LOCATING, null);
                mLocationClient.startLocation();
            }
        });

        mResultAdapter = new ResultListAdapter(this, null);
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(com.commonlib.R.drawable.ic_return);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("选择城市");
        mListView = (ListView) findViewById(R.id.listview_all_city);
        mListView.setAdapter(mCityAdapter);

        TextView overlay = (TextView) findViewById(R.id.tv_letter_overlay);
        mLetterBar = (SideLetterBar) findViewById(R.id.side_letter_bar);
        mLetterBar.setOverlay(overlay);
        mLetterBar.setOnLetterChangedListener(new SideLetterBar.OnLetterChangedListener() {
            @Override
            public void onLetterChanged(String letter) {
                int position = mCityAdapter.getLetterPosition(letter);
                mListView.setSelection(position);
            }
        });

        searchBox = (EditText) findViewById(R.id.et_search);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString();
                if (TextUtils.isEmpty(keyword)) {
                    clearBtn.setVisibility(View.GONE);
                    emptyView.setVisibility(View.GONE);
                    mResultListView.setVisibility(View.GONE);
                } else {
                    clearBtn.setVisibility(View.VISIBLE);
                    mResultListView.setVisibility(View.VISIBLE);
                    List<City> result = dbManager.searchCity(keyword);
                    if (result == null || result.size() == 0) {
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        emptyView.setVisibility(View.GONE);
                        mResultAdapter.changeData(result);
                    }
                }
            }
        });

        emptyView = (ViewGroup) findViewById(R.id.empty_view);
        mResultListView = (ListView) findViewById(R.id.listview_search_result);
        mResultListView.setAdapter(mResultAdapter);
        mResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                back(mResultAdapter.getItem(position).getName());
            }
        });

        clearBtn = (ImageView) findViewById(R.id.iv_search_clear);
        clearBtn.setOnClickListener(this);
    }

    private void cacheLocalHistory(String city) {
        LimitQueue<String> limitQueue = new LimitQueue<String>(3);
        List<String> cities = SearchLocalHistoryUtil.getCities(CityPickerActivity.this);   // 先取本地已缓存的数据
        if (cities != null && cities.size() > 0) {
            for (int i = 0; i < cities.size(); i++) {
                if (!city.equals(cities.get(i))) {      // 如果历史记录中存在，则删除历史记录中的，重新入队
                    limitQueue.offer(cities.get(i));
                }
            }
        }
        limitQueue.offer(city);     // 通过队列把新的数据入队

        List<String> queueDatas = new ArrayList<String>();
        for (int j = 0; j < limitQueue.getQueueSize(); j++) {
            String queueData = limitQueue.get(j);
            queueDatas.add(queueData);
        }
        SearchLocalHistoryUtil.saveCities(CityPickerActivity.this, queueDatas);
    }

    private void back(String city) {
        cacheLocalHistory(city);
        Intent data = new Intent();
        data.putExtra(KEY_PICKED_CITY, city);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_search_clear) {
            searchBox.setText("");
            clearBtn.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
            mResultListView.setVisibility(View.GONE);
        } else if (v.getId() == R.id.back) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stopLocation();
    }
}
