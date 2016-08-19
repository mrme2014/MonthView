package com.ishow.ischool.business.kefu;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.commonlib.util.LogUtil;
import com.ishow.ischool.R;
import com.ishow.ischool.common.base.BaseActivity4Crm;

import butterknife.BindView;

/**
 * Created by MrS on 2016/8/15.
 */
public class KefuActivity extends BaseActivity4Crm {
    @BindView(R.id.webview)
    WebView webview;

    String url ="http://crm.ishowedu.com:8080/ischool/help.html";
    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_kefu, R.string.kefu_title);
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {
        WebSettings settings = webview.getSettings();
        settings.setAppCacheEnabled(true);
        settings.setDisplayZoomControls(false);
        settings.setBuiltInZoomControls(false);
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(false);

        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtil.e("url"+url);
                view.loadUrl(url);
                return true;
            }
        });
        webview.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (webview!=null&&webview.canGoBack()){
            webview.goBack();
        }
        super.onBackPressed();
    }
}
