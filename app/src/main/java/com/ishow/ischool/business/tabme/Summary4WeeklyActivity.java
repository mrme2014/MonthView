package com.ishow.ischool.business.tabme;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ishow.ischool.R;
import com.ishow.ischool.common.base.BaseActivity4Crm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mini on 16/11/21.
 */

public class Summary4WeeklyActivity extends BaseActivity4Crm<MePresenter, MeModel> {

    private List<ShareSpec> mShareSpecs;
    private View shareView;
    private RecyclerView shareRecyclerView;
    private ShareAdapter mAdapter;
    private static final int PLATFORM_WX = 1;
    private static final int PLATFORM_QQ = 2;
    private PopupWindow popupWindow;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_summary_weekly, R.string.summary_weekly, R.menu.menu_summary, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        initShare();
    }

    @Override
    protected void setUpData() {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.summary:
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    //设置位置
                    popupWindow.showAtLocation(mToolbar, Gravity.BOTTOM, 0, 0);
                }
                break;
        }
        return true;
    }


    void toShare(int platform, String shareContent) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        // 查询所有可以分享的Activity
        List<ResolveInfo> resInfo = Summary4WeeklyActivity.this.getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        if (!resInfo.isEmpty()) {
            Intent targeted = null;
            for (ResolveInfo info : resInfo) {
                String sharePlatActivityName = info.activityInfo.name;
                switch (platform) {
                    case PLATFORM_WX:
                        if (sharePlatActivityName.equals("com.tencent.mm.ui.tools.ShareImgUI")) {
                            targeted = new Intent(Intent.ACTION_SEND);
                            targeted.setType("text/plain");
                            ActivityInfo activityInfo = info.activityInfo;
                            Log.v("包名", "packageName=" + activityInfo.packageName + "Name=" + activityInfo.name);
                            // 分享出去的内容
                            targeted.putExtra(Intent.EXTRA_TEXT, "这是我的分享内容\n冲刺目标：258\n公开课目标" + getPackageName());
                            // 分享出去的标题
                            targeted.putExtra(Intent.EXTRA_TITLE, "EXTRA_SUBJECT");
                            targeted.putExtra(Intent.EXTRA_SUBJECT, "EXTRA_SUBJECT");
                            targeted.setPackage(activityInfo.packageName);
                            targeted.setClassName(activityInfo.packageName, info.activityInfo.name);
                        }
                        break;
                    case PLATFORM_QQ:
                        if (sharePlatActivityName.equals("com.tencent.mobileqq.activity.JumpActivity")) {
                            targeted = new Intent(Intent.ACTION_SEND);
                            targeted.setType("text/plain");
                            ActivityInfo activityInfo = info.activityInfo;
                            Log.v("包名", "packageName=" + activityInfo.packageName + "Name=" + activityInfo.name);
                            // 分享出去的内容
                            targeted.putExtra(Intent.EXTRA_TEXT, "这是我的分享内容\n冲刺目标：258\n公开课目标" + getPackageName());
                            // 分享出去的标题
                            targeted.putExtra(Intent.EXTRA_TITLE, "EXTRA_SUBJECT");
                            targeted.putExtra(Intent.EXTRA_SUBJECT, "EXTRA_SUBJECT");
                            targeted.setPackage(activityInfo.packageName);
                            targeted.setClassName(activityInfo.packageName, info.activityInfo.name);
                        }
                        break;
                }
            }

            if (targeted != null) {
                try {
                    startActivity(targeted);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(this, "未找到该分享应用", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "未找到该分享应用", Toast.LENGTH_SHORT).show();
            }
        }
        popupWindow.dismiss();
    }

    void initShare() {
        mShareSpecs = new ArrayList<>();
        mShareSpecs.add(new ShareSpec("微信", R.mipmap.ic_launcher));
        mShareSpecs.add(new ShareSpec("QQ", R.mipmap.ic_launcher));

        shareView = getLayoutInflater().inflate(R.layout.share_view ,null);
        shareRecyclerView = (RecyclerView) shareView.findViewById(R.id.share_recyclerview);
        shareRecyclerView.setLayoutManager(new GridLayoutManager(Summary4WeeklyActivity.this, 2));
        mAdapter = new ShareAdapter();
        shareRecyclerView.setAdapter(mAdapter);

        //防止重复按按钮
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        //设置PopupWindow的View
        popupWindow = new PopupWindow(shareView, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //设置背景,这个没什么效果，不添加会报错
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置点击弹窗外隐藏自身
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        //设置动画
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
    }

    class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    Summary4WeeklyActivity.this).inflate(R.layout.item_share, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.tv.setText(mShareSpecs.get(position).platformName);
            holder.tv.setCompoundDrawablesWithIntrinsicBounds(0, mShareSpecs.get(position).platformIcon, 0, 0);
            holder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mShareSpecs.get(position).platformName.equals("微信")) {
                        toShare(PLATFORM_WX, "");
                    } else if (mShareSpecs.get(position).platformName.equals("QQ")) {
                        toShare(PLATFORM_QQ, "");
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mShareSpecs.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tv;

            public MyViewHolder(View view) {
                super(view);
                tv = (TextView) view.findViewById(R.id.share_tv);
            }
        }
    }


    class ShareSpec {
        private String platformName;
        private int platformIcon;

        public ShareSpec(String platformName, int platformIcon) {
            this.platformName = platformName;
            this.platformIcon = platformIcon;
        }
    }

}
