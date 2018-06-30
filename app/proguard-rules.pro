# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\AnitaLin\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
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


-dontwarn com.droi.sdk.**
-dontwarn okio.**
-dontwarn okhttp3.**
-keep class com.droi.sdk.** { *; }
-keep interface com.droi.sdk.** { *; }
-keep class com.tyd.aidlservice.internal.** { *; }
-keep interface com.tyd.aidlservice.internal.** { *; }
-keep class * extends com.droi.sdk.core.DroiObject { @com.droi.sdk.core.DroiExpose *; @com.droi.sdk.core.DroiReference *;}
-keep class * extends com.droi.sdk.core.DroiUser { @com.droi.sdk.core.DroiExpose *; @com.droi.sdk.core.DroiReference *;}