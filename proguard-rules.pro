# To enable ProGuard in your project, edit project.properties
# to define the proguard.config property as described in that file.
#
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ${sdk.dir}/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the ProGuard
# include property in project.properties.
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
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keepattributes *Annotation*
-keepattributes Signature

#-dontusemixedcaseclassnames      # 是否使用大小写混合
-skipnonpubliclibraryclasses      # 不混淆第三方jar
#-dontskipnonpubliclibraryclasses      # 是否混淆第三方jar
#-dontpreverify     # 混淆时是否做预校验
#-verbose      # 混淆时是否记录日志
-keep class * extends java.lang.annotation.Annotation  # 不混淆注解

#-printmapping mapping.txt
-ignorewarnings

-dontwarn base.**
-keep class base.** { *; }
-dontwarn entities.**
-keep class entities.** { *; }

-dontwarn ch.**
-keep class ch.**{*;}
-dontwarn org.**
-keep class org.**{*;}
-dontwarn **.R$*
-keep class **.R$* {*;}
-dontwarn **.R
-keep class **.R{*;}
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep public class * extends java.io.Serializable
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


-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}


-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <methods>;
}

-keepclassmembers class * {
    public static <fields>;
}

