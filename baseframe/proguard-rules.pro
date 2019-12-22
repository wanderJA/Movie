-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

-dontoptimize
-dontpreverify

-dontwarn
-ignorewarnings

-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService


-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}


-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-optimizationpasses 5
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*


-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-keep class com.umeng.** {*;}

#保留mapping文件中的行号，崩溃的时候就有源文件和行号的信息
-keepattributes SourceFile,LineNumberTable

#用来保持生成的表名不被混淆
-keep class **$Properties

#rxbus
-keepclassmembers class ** {
    @com.wander.baseframe.library.rxbus.Subscribe <methods>;
}
-keep enum com.wander.baseframe.library.rxbus.EventThread { *; }


# 3D 地图 V5.0.0之后：
    -keep   class com.amap.api.maps.**{*;}
    -keep   class com.autonavi.**{*;}
    -keep   class com.amap.api.trace.**{*;}

#    定位
    -keep class com.amap.api.location.**{*;}
    -keep class com.amap.api.fence.**{*;}
    -keep class com.autonavi.aps.amapapi.model.**{*;}

#    搜索
    -keep   class com.amap.api.services.**{*;}

#    2D地图
    -keep class com.amap.api.maps2d.**{*;}
    -keep class com.amap.api.mapcore2d.**{*;}

#    导航
    -keep class com.amap.api.navi.**{*;}
    -keep class com.autonavi.**{*;}



################友盟###############
-keep class com.umeng.** {*;}
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

######## Glide ########
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
 -keep class com.wander.baseframe.library.net.retrofit.converter.** { *; }



######## EventBus ########
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }


################Timber#################
-dontwarn org.jetbrains.annotations.**



####### 解决The rule `-keep public class * extends androidx.versionedparcelable.VersionedParcelable {
          #  <init>();
          #}` uses extends but actually matches implements错误
-keep public class * implements androidx.versionedparcelable.VersionedParcelable


################retrofit###############
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions


################RxJava and RxAndroid###############
-dontwarn org.mockito.**
-dontwarn org.junit.**
-dontwarn org.robolectric.**

-keep class io.reactivex.** { *; }
-keep interface io.reactivex.** { *; }

-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-dontwarn okio.**
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**

-dontwarn io.reactivex.**
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

-keep class sun.misc.Unsafe { *; }

-dontwarn java.lang.invoke.*

-keep class io.reactivex.schedulers.Schedulers {
    public static <methods>;
}
-keep class io.reactivex.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class io.reactivex.schedulers.TestScheduler {
    public <methods>;
}
-keep class io.reactivex.schedulers.Schedulers {
    public static ** test();
}
-keepclassmembers class io.reactivex.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class io.reactivex.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    long producerNode;
    long consumerNode;
}

-keepclassmembers class io.reactivex.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    io.reactivex.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class io.reactivex.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    io.reactivex.internal.util.atomic.LinkedQueueNode consumerNode;
}

-dontwarn io.reactivex.internal.util.unsafe.**


################okhttp###############
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn com.squareup.okhttp.**



######## Arouter ########
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep public class com.alibaba.android.arouter.facade.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}

# 如果使用了 byType 的方式获取 Service，需添加下面规则，保护接口
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider

# 如果使用了 单类注入，即不定义接口实现 IProvider，需添加下面规则，保护实现
# -keep class * implements com.alibaba.android.arouter.facade.template.IProvider
