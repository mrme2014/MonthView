package com.ishow.ischool.business.tabme;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.commonlib.util.DeviceUtils;
import com.ishow.ischool.R;
import com.ishow.ischool.adpter.FragmentAdapter;
import com.ishow.ischool.common.base.BaseActivity4Loading;
import com.ishow.ischool.common.rxbus.RxBus;
import com.ishow.ischool.event.WeeklyLoadEvent;
import com.ishow.ischool.util.AppUtil;
import com.ishow.ischool.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

import static android.animation.ObjectAnimator.ofPropertyValuesHolder;
import static com.ishow.ischool.R.id.share_layout;

/**
 * Created by mini on 16/11/21.
 */

public class Summary4WeeklyActivity extends BaseActivity4Loading {

    @BindView(R.id.tabs)
    TabLayout mTabs;
    @BindView(R.id.viewpager)
    ViewPager mViewPaper;
    @BindView(R.id.share_iv)
    ImageView shareIv;
    @BindView(share_layout)
    RelativeLayout shareLayout;


    private List<ShareSpec> mShareSpecs;
    private View shareView;
    private RecyclerView shareRecyclerView;
    private ShareAdapter mAdapter;
    private static final int PLATFORM_WX = 1;
    private static final int PLATFORM_QQ = 2;
    private PopupWindow popupWindow;


    int distanceX, distanceY;
    int duration, shareIconDuration;

    private FragmentAdapter mFragmentAdapter;
    WeeklySummaryFragment fragment1, fragment2;
    String titleLastWeek, titleBeforeLastWeek;
    private boolean isShown = false;
    private boolean isLoading = false;      // 当isloading为true时，禁用点击
    private View shareQqView, shareWxView;

    @Override
    protected void initEnv() {
        super.initEnv();
        RxBus.getDefault().register(this, WeeklyLoadEvent.class, new Action1<WeeklyLoadEvent>() {
            @Override
            public void call(WeeklyLoadEvent event) {
                if (event.isLoadSuccess()) {
                    showContentView();
                    shareIv.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void setUpContentView() {
        setLoadContentView(R.layout.activity_summary_weekly, R.string.summary_weekly, -1, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        showContentView();
        initViewPager();
        initShare();
    }

    private void initViewPager() {
        SimpleDateFormat sdf4Title = new SimpleDateFormat("MM.dd");
        SimpleDateFormat sdf4Share = new SimpleDateFormat("MM月dd日");
        ArrayList<Fragment> fragments = new ArrayList<>();

        fragment1 = WeeklySummaryFragment.newInstance(AppUtil.getLastWeekStart(), AppUtil.getLastWeekEnd(),
                getString(R.string.weekly_lastweek_share,
                        sdf4Share.format(AppUtil.getLastWeekStart() * 1000),
                        sdf4Share.format(AppUtil.getLastWeekEnd() * 1000)));
        fragment2 = WeeklySummaryFragment.newInstance(AppUtil.getWeekBeforeLastStart(), AppUtil.getWeekBeforeLastEnd(),
                getString(R.string.weekly_beforelastweek_share,
                        sdf4Share.format(AppUtil.getWeekBeforeLastStart() * 1000),
                        sdf4Share.format(AppUtil.getWeekBeforeLastEnd() * 1000)));
        titleLastWeek = getString(R.string.weekly_lastweek,
                sdf4Title.format(AppUtil.getLastWeekStart() * 1000),
                sdf4Title.format(AppUtil.getLastWeekEnd() * 1000));
        titleBeforeLastWeek = getString(R.string.weekly_beforelastweek,
                sdf4Title.format(AppUtil.getWeekBeforeLastStart() * 1000),
                sdf4Title.format(AppUtil.getWeekBeforeLastEnd() * 1000));


        // 应测试要求提供的测试数据，正式版本需切换上面注释代码
//        fragment1 = WeeklySummaryFragment.newInstance(AppUtil.getTimeStamp(11, 7, AppUtil.TYPE_START),
//                AppUtil.getTimeStamp(11, 13, AppUtil.TYPE_END),
//                getString(R.string.weekly_lastweek_share,
//                        sdf4Share.format(AppUtil.getTimeStamp(11, 7, AppUtil.TYPE_START) * 1000),
//                        sdf4Share.format(AppUtil.getTimeStamp(11, 13, AppUtil.TYPE_END) * 1000)));
//        fragment2 = WeeklySummaryFragment.newInstance(AppUtil.getTimeStamp(10, 31, AppUtil.TYPE_START),
//                AppUtil.getTimeStamp(11, 6, AppUtil.TYPE_END),
//                getString(R.string.weekly_beforelastweek_share,
//                        sdf4Share.format(AppUtil.getTimeStamp(10, 31, AppUtil.TYPE_START) * 1000),
//                        sdf4Share.format(AppUtil.getTimeStamp(11, 6, AppUtil.TYPE_END) * 1000)));
//        titleLastWeek = getString(R.string.weekly_lastweek,
//                sdf4Title.format(AppUtil.getTimeStamp(11, 7, AppUtil.TYPE_START) * 1000),
//                sdf4Title.format(AppUtil.getTimeStamp(11, 13, AppUtil.TYPE_END) * 1000));
//        titleBeforeLastWeek = getString(R.string.weekly_beforelastweek,
//                sdf4Title.format(AppUtil.getTimeStamp(10, 31, AppUtil.TYPE_START) * 1000),
//                sdf4Title.format(AppUtil.getTimeStamp(11, 6, AppUtil.TYPE_END) * 1000));

        fragments.add(fragment1);
        fragments.add(fragment2);

        ArrayList<String> titleList = new ArrayList<>();
        titleList.add(titleLastWeek);
        titleList.add(titleBeforeLastWeek);
        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titleList);
        mViewPaper.setAdapter(mFragmentAdapter);
        mViewPaper.setCurrentItem(0);
        mTabs.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabs.setupWithViewPager(mViewPaper);
    }

    @Override
    protected void setUpData() {

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        initAnim();
    }

    void initAnim() {
        int shareWidth = shareIv.getWidth();
        int shareHeight = shareIv.getHeight();
        int[] sharePoint = new int[2];
        shareIv.getLocationInWindow(sharePoint);
        distanceX = DeviceUtils.dip2px(Summary4WeeklyActivity.this, 80);
        distanceY = DeviceUtils.dip2px(Summary4WeeklyActivity.this, 80);
    }

    void startAnim() {
        PropertyValuesHolder ptxLeft, ptxRight, pty;
        PropertyValuesHolder sx, sy;
        PropertyValuesHolder alphaPvh;
        if (!isShown) {
            duration = 400;
            shareIconDuration = 200;
            isShown = true;
            ptxLeft = PropertyValuesHolder.ofFloat("translationX", 0f, -distanceX);
            ptxRight = PropertyValuesHolder.ofFloat("translationX", 0f, distanceX);
            pty = PropertyValuesHolder.ofFloat("translationY", 0f, -distanceY);
            sx = PropertyValuesHolder.ofFloat("scaleX", 0f, 1f);
            sy = PropertyValuesHolder.ofFloat("scaleY", 0f, 1f);
            alphaPvh = PropertyValuesHolder.ofFloat("alpha", 0f, 1f);
            shareLayout.setVisibility(View.VISIBLE);
        } else {
            duration = 200;
            shareIconDuration = 100;
            isShown = false;
            ptxLeft = PropertyValuesHolder.ofFloat("translationX", -distanceX, 0f);
            ptxRight = PropertyValuesHolder.ofFloat("translationX", distanceX, 0f);
            pty = PropertyValuesHolder.ofFloat("translationY", -distanceY, 0f);
            sx = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f);
            sy = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f);
            alphaPvh = PropertyValuesHolder.ofFloat("alpha", 1f, 0f);
        }



        if (shareQqView == null) {
            shareQqView = getLayoutInflater().inflate(R.layout.view_share_qq, null);
            RelativeLayout.LayoutParams lpQq = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            lpQq.addRule(RelativeLayout.CENTER_HORIZONTAL);
            lpQq.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            shareQqView.setLayoutParams(lpQq);
            shareLayout.addView(shareQqView);
            shareQqView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startAnim();
                    if (getShareContent() == null) {
                        ToastUtil.showToast(Summary4WeeklyActivity.this, "请选择分享数据");
                    } else {
                        toShare(PLATFORM_QQ, getShareContent());
                    }
                }
            });
        }
        if (shareWxView == null) {
            shareWxView = getLayoutInflater().inflate(R.layout.view_share_wx, null);
            RelativeLayout.LayoutParams lpWx = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            lpWx.addRule(RelativeLayout.CENTER_HORIZONTAL);
            lpWx.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            shareWxView.setLayoutParams(lpWx);
            shareLayout.addView(shareWxView);
            shareWxView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startAnim();
                    if (getShareContent() == null) {
                        ToastUtil.showToast(Summary4WeeklyActivity.this, "请选择分享数据");
                    } else {
                        toShare(PLATFORM_WX, getShareContent());
                    }
                }
            });
        }
        ObjectAnimator translationAnimatorLeft = ofPropertyValuesHolder(shareQqView, ptxLeft, pty);
        ObjectAnimator scaleAnimatorLeft = ofPropertyValuesHolder(shareQqView, sx, sy);
        ObjectAnimator alphaAnimatorLeft = ofPropertyValuesHolder(shareQqView, alphaPvh);
        ObjectAnimator translationAnimatorRight = ofPropertyValuesHolder(shareWxView, ptxRight, pty);
        ObjectAnimator scaleAnimatorRight = ofPropertyValuesHolder(shareWxView, sx, sy);
        ObjectAnimator alphaAnimatorRight = ofPropertyValuesHolder(shareWxView, alphaPvh);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(translationAnimatorLeft).with(scaleAnimatorLeft).with(alphaAnimatorLeft).with(translationAnimatorRight).with(scaleAnimatorRight).with(alphaAnimatorRight);
        animSet.setDuration(duration);
        animSet.setInterpolator(new DecelerateInterpolator());
        animSet.addListener(animatorListenerAdapter);
        animSet.start();
    }


    String getShareContent() {
        String content1 = fragment1.getShareContent();
        String content2 = fragment2.getShareContent();
        if (TextUtils.isEmpty(content1) && TextUtils.isEmpty(content2)) {
            return null;
        } else {
            return mUser.positionInfo.campus + ":\n\n" + content1 + content2;
        }
    }

    AnimatorListenerAdapter animatorListenerAdapter = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
            isLoading = true;

            // 分享按钮动画
            ObjectAnimator shareIconAnim = ObjectAnimator.ofFloat(shareIv, "alpha", 1f, 0);
            shareIconAnim.setDuration(shareIconDuration);
            shareIconAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (isShown) {
                        shareIv.setImageResource(R.mipmap.icon_share_close);
                    } else {
                        shareIv.setImageResource(R.mipmap.icon_share);
                    }
                    PropertyValuesHolder pvhA = PropertyValuesHolder.ofFloat("alpha", 0, 1f);
                    ObjectAnimator.ofPropertyValuesHolder(shareIv, pvhA).setDuration(shareIconDuration).start();
                }
            });
            shareIconAnim.start();

            // 背景动画
            if (isShown) {
                ObjectAnimator.ofFloat(shareLayout, "alpha", 0, 0.85f).setDuration(duration).start();
            } else {
                ObjectAnimator.ofFloat(shareLayout, "alpha", 0.85f, 0).setDuration(duration).start();
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            shareQqView.clearAnimation();
            shareWxView.clearAnimation();
            isLoading = false;
            if (!isShown) {
                shareLayout.setVisibility(View.GONE);
            }
        }
    };

    @OnClick({R.id.share_iv, R.id.share_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share_iv:
                if (!isLoading) {
                    startAnim();
                }
                break;
            case R.id.share_layout:
                if (!isLoading) {
                    startAnim();
                }
                break;
        }
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
                            targeted.putExtra(Intent.EXTRA_TEXT, shareContent);
                            // 分享出去的标题
                            targeted.putExtra(Intent.EXTRA_TITLE, "EXTRA_SUBJECT");
                            targeted.putExtra(Intent.EXTRA_SUBJECT, "EXTRA_SUBJECT");
                            targeted.setPackage(activityInfo.packageName);
                            targeted.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
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
                            targeted.putExtra(Intent.EXTRA_TEXT, shareContent);
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

        shareView = getLayoutInflater().inflate(R.layout.share_view, null);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getDefault().unregister(this);
    }
}
