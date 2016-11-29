# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/abel/work/adt-bundle-mac/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#-dontwarn retrofit.*
#-dontwarn com.gotye.*
#-dontwarn com.aplipay.*
#-dontwarn butterknife.*
-ignorewarnings

-optimizationpasses 5          # 指定代码的压缩级别
-dontusemixedcaseclassnames   # 是否使用大小写混合
-dontpreverify           # 混淆时是否做预校验
-verbose                # 混淆时是否记录日志
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法

-keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
    native <methods>;
}
-keepclasseswithmembers class * {   # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {# 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity { # 保持自定义控件类不被混淆
    public void *(android.view.View);
}
-keepclassmembers enum * {     # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable { # 保持 Parcelable 不被混淆
    public static final android.os.Parcelable$Creator *;
}

#event bus
-keepclassmembers class ** {
    public void onEvent*(**);
    void onEvent*(**);
}

#butterknife
-dontwarn butterknife.internal.**
-keep class **$$ViewInjector { *; }
-keepnames class * { @butterknife.InjectView *;}
-keep class butterknife.** { *; }
-keep class **$$ViewBinder { *; }
-keep class javax.** { *; }
-keep class java.** { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}


#javascript交互，如下，测试过可以。

 -keepclassmembers class * {
     @android.webkit.JavascriptInterface <methods>;
 }

 -keepattributes JavascriptInterface
 -keep public class com.mypackage.MyClass$MyJavaScriptInterface
 -keep public class * implements com.mypackage.MyClass$MyJavaScriptInterface
 -keepclassmembers class com.mypackage.MyClass$MyJavaScriptInterface {
     <methods>;
 }

 # retrofit
 -dontwarn retrofit.**
 -keep class retrofit.** { *; }
 -keepattributes Signature
 -keepattributes Exceptions

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

 # glide
 -keep public class * implements com.bumptech.glide.module.GlideModule
 -keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
     **[] $VALUES;
     public *;
 }

 ## nineoldandroids-2.4.0.jar
 -keep public class com.nineoldandroids.** {*;}

 ## support-v4
 -dontwarn android.support.v4.**
 -keep class android.support.v4.** { *; }
 -keep interface android.support.v4.app.** { *; }
 -keep public class * extends android.support.v4.**
 -keep public class * extends android.app.Fragment

 ## okhttp
 -keep class com.squareup.okhttp.** { *; }


 ##
 -keep class com.ishow.base.** { *; }
 -keep interface com.ishow.base.** { *; }

 -keep class com.ishow.biz.widget.** { *; }


-keep public class com.ishow.videochat.R$*{
    public static final int *;
}

#gson
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.idea.fifaalarmclock.entity.***
-keep class com.google.gson.stream.** { *; }

# jpush
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }

# system
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.**
-keep public class com.android.vending.licensing.ILicensingService


#BuildConfig
-keep class com.ishow.ischool.BuildConfig { *; }

#greenDao
-keep class de.greenrobot.dao.** {*;}
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
    public static java.lang.String TABLENAME;
}
-keep class **$Properties

#bean
-keep class com.ishow.ischool.bean.** { *; }

#高德定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

#bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}