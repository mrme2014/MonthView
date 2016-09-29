package com.ishow.ischool.business.salesprocess;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.commonlib.core.BaseFragment4mvp;
import com.commonlib.widget.pull.BaseListAdapter;
import com.commonlib.widget.pull.BaseViewHolder;
import com.commonlib.widget.pull.DividerItemDecoration;
import com.commonlib.widget.pull.PullRecycler;
import com.commonlib.widget.pull.layoutmanager.MyLinearLayoutManager;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.saleprocess.Subordinate;
import com.ishow.ischool.bean.saleprocess.SubordinateObject;
import com.ishow.ischool.business.user.pick.UserPickActivity;
import com.ishow.ischool.widget.custom.AvatarImageView;
import com.ishow.ischool.widget.custom.CircleTransform;

import java.util.ArrayList;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MrS on 2016/9/23.
 */

public class SearchSubordinatesFragment extends BaseFragment4mvp<SalesProcessPresenter, SalesProcessModel> implements SalesProcessContract.View<Subordinate>, PullRecycler.OnRecyclerRefreshListener {

    public static SearchSubordinatesFragment newInstance(int campus_id, int position_id, String keywords) {
        SearchSubordinatesFragment fragment = new SearchSubordinatesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("campus_id", campus_id);
        bundle.putString("keywords", keywords);
        bundle.putInt("position_id", position_id);
        fragment.setArguments(bundle);
        return fragment;
    }

    PullRecycler pullRecycler;
    Adapter adapter;
    int campus_id;
    String keywords;
    int position_id;

    ArrayList<SubordinateObject> lists;
    @Override
    public void init() {
        //search(campus_id,position_id,keywords);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            campus_id = arguments.getInt("campus_id");
            keywords = arguments.getString("keywords");
            position_id = arguments.getInt("position_id");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        pullRecycler = new PullRecycler(getContext());
        pullRecycler.setId(R.id.recycleview);
        pullRecycler.setOnRefreshListener(this);
        pullRecycler.setLayoutManager(new MyLinearLayoutManager(getContext().getApplicationContext()));
        pullRecycler.addItemDecoration(new DividerItemDecoration(getContext().getApplicationContext(), com.commonlib.R.drawable.widget_list_divider));
        pullRecycler.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));
        adapter = new Adapter();
        pullRecycler.setAdapter(adapter);
        return pullRecycler;
    }
    public void search(int campus_id,int position_id,String keywords) {
        if (pullRecycler==null)
            return;
        pullRecycler.setRefreshing();
        if (keywords != null && keywords != "")
            getOptionSubordinateKeyWords(campus_id, position_id, keywords);
        else getOptionSubdinate(campus_id, position_id);
    }

    private void getOptionSubdinate(int campus_id, int position_id) {
        TreeMap<String,Integer> map = new TreeMap<>();
        if (campus_id!=1){
            map.put("campus_id",campus_id);
            map.put("position_id",position_id);
        }
        mPresenter.getOptionSubordinate("Subordinate",map);
    }

    private void getOptionSubordinateKeyWords(int campus_id, int position_id, String keywords) {
        TreeMap<String,Integer> map = new TreeMap<>();
        if (campus_id!=1){
            map.put("campus_id",campus_id);
            map.put("position_id",position_id);
        }
        mPresenter.getOptionSubordinateKeyWords("Subordinate", map, keywords);
    }

    @Override
    public void getListSuccess(Subordinate subordinate) {
        pullRecycler.onRefreshCompleted();
        if (subordinate != null)
            lists = subordinate.Subordinate;
        if (lists.size()==0){
            pullRecycler.showEmptyView();
        }else{
            pullRecycler.resetView();
            adapter.notifyDataSetChanged();
        }


    }

    @Override
    public void getListFail(String msg) {
        pullRecycler.onRefreshCompleted();
        pullRecycler.showEmptyView();
    }

    @Override
    public void onRefresh(int action) {
        if (keywords == null || keywords == "")
            getOptionSubdinate(campus_id,position_id);
        else
            getOptionSubordinateKeyWords(campus_id, position_id, keywords);
    }

    private class Adapter extends BaseListAdapter {

        @Override
        protected int getDataCount() {
            return lists == null ? 0 : lists.size();
        }

        @Override
        protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_pick_referrer, parent, false);
            return new ViewHolder(view);
        }
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.avatar_text)
        AvatarImageView avatarTv;
        @BindView(R.id.avatar_iv)
        ImageView avatarIv;
        @BindView(R.id.referrer_name)
        TextView referrerName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(final int position) {
            SubordinateObject data = lists.get(position);
            if (data.file_name != null && !TextUtils.isEmpty(data.file_name)) {
                avatarTv.setVisibility(View.GONE);
                avatarIv.setVisibility(View.VISIBLE);
                Glide.with(getContext().getApplicationContext()).load(data.user_name).fitCenter().placeholder(R.mipmap.img_header_default)
                        .transform(new CircleTransform(getContext().getApplicationContext())).into(new ImageViewTarget<GlideDrawable>(avatarIv) {
                    @Override
                    protected void setResource(GlideDrawable resource) {
                        avatarIv.setImageDrawable(resource);
                    }

                    @Override
                    public void setRequest(Request request) {
                        avatarIv.setTag(position);
                        avatarIv.setTag(R.id.glide_tag_id, request);
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
            Intent intent = new Intent(getActivity(), UserPickActivity.class);
            intent.putExtra(SelectSubordinateActivity.PICK_USER, lists.get(position));
            getActivity().setResult(getActivity().RESULT_OK, intent);
            getActivity().finish();
        }
    }
}
