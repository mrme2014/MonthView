package com.ishow.ischool.application;

import com.ishow.ischool.BuildConfig;

/**
 * Created by abel on 16/8/8.
 */
public class Env {
    public static final String SITE_URL_RELEASE = "http://crmcore.ishowedu.com/";
    public static final String SITE_URL_DEV = "http://crmcore-dev.ishowedu.com/";
    public static final String SITE_URL = BuildConfig.release ? SITE_URL_RELEASE : SITE_URL_DEV;
}
