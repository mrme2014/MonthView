package com.ishow.ischool.application;

import com.ishow.ischool.BuildConfig;

/**
 * Created by abel on 16/8/8.
 */
public class Env {
    public static final String SITE_URL_RELEASE = "http://crmcore.ishowedu.com/";
    public static final String SITE_URL_DEV = "http://crmcore-dev.ishowedu.com/";
    public static final String SITE_URL_800 = "http://crmcore-dev.ishowedu.com:800/";
    public static final String SITE_URL = BuildConfig.release ? SITE_URL_RELEASE : SITE_URL_DEV;

    public static final String APPLY_URL_DEV = "http://crm.ishowedu.com:8080/Comm/App/applyMessage/action/apply/student_id/";
    public static final String APPLY_URL = "http://crm.ishowedu.com:8080/Comm/App/applyMessage/action/apply/student_id/";

    public static final int type_release = 1;
    public static final int type_debug = 2;
    public static final int type_debug800 = 3;
    public static int build_type = BuildConfig.release_type;

    public static String getSiteUrl() {
        String siteUrl = SITE_URL_RELEASE;
        switch (build_type) {
            case type_release:
                siteUrl = SITE_URL_RELEASE;
                break;
            case type_debug:
                siteUrl = SITE_URL_DEV;
                break;
            case type_debug800:
                siteUrl = SITE_URL_800;
                break;
        }
        return siteUrl;
    }

    public static String getApplyUrl() {
        String siteUrl = APPLY_URL;
        switch (build_type) {
            case type_release:
                siteUrl = APPLY_URL;
                break;
            case type_debug:
                siteUrl = APPLY_URL_DEV;
                break;
            case type_debug800:
                siteUrl = APPLY_URL_DEV;
                break;

        }
        return siteUrl;
    }
}
