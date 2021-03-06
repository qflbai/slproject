package com.sl.shenmian.lib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.Locale;

/**
 * @author WenXian Bai
 * @Date: 2018/3/27.
 * @Description: 系统工具
 */

public class SystemUtil {
    /**
     * 获取系统当前的语言
     */
    public static String getSystemtLanguage(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();

        return language;
    }

    /**
     * 获取当前国家
     * @param context
     * @return
     */
    public static String getLocale(Context context){
       return context.getResources().getConfiguration().locale.getCountry();
    }

    /**
     * 判断是否是中文系统
     */
    public static boolean isZh(Context context) {
        return getSystemtLanguage(context).equals("zh");
    }

    /**
     * 获取SDK版本号
     *
     * @return
     */
    public static int getSDKVersion() {
        return Integer.valueOf(Build.VERSION.SDK_INT);
    }

    /**
     * 获取应用包名
     *
     * @param context
     * @return
     */
    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    /**
     * 获取包信息
     *
     * @param context
     * @return
     */
    public static PackageInfo getPackageInfo(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(context), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return packageInfo;
    }

    /**
     * 获取当前应用版本编号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        return packageInfo.versionCode;
    }

    /**
     * 获取应用版本号
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        return packageInfo.versionName;
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return  系统版本号
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取屏幕分辨率
     *
     * @param context
     * @return
     */
    public static DisplayMetrics getScreenResolution(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        return displayMetrics;
    }

    /**
     * 获取最小宽度
     *
     * @param activity
     * @return
     */
    public static float getSW(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int heightPixels = dm.heightPixels;
        int widthPixels = dm.widthPixels;
        float density = dm.density;
        float heightDP = heightPixels / density;
        float widthDP = widthPixels / density;
        float smallestWidthDP;
        if (widthDP < heightDP) {
            smallestWidthDP = widthDP;
        } else {
            smallestWidthDP = heightDP;
        }
        activity = null;
        return smallestWidthDP;
    }

    /**
     * 判断虚拟导航栏是否显示
     *
     * @param activity
     * @return true(显示虚拟导航栏)，false(不显示或不支持虚拟导航栏)
     */
    public static boolean isNavigationBarShow(@NonNull Activity activity) {
        boolean show;
        Window window = activity.getWindow();
        Display display = window.getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getRealSize(point);

        View decorView = window.getDecorView();
        Configuration conf = activity.getResources().getConfiguration();
        if (Configuration.ORIENTATION_LANDSCAPE == conf.orientation) {
            View contentView = decorView.findViewById(android.R.id.content);
            show = (point.x != contentView.getWidth());
        } else {
            Rect rect = new Rect();
            decorView.getWindowVisibleDisplayFrame(rect);
            show = (rect.bottom != point.y);
        }
        activity = null;
        return show;
    }


    /**
     * 设置导航栏是否可见
     *
     * @param activity
     * @param isVisible
     */
    public static void setNavBarVisibility(@NonNull Activity activity, boolean isVisible) {
        Window window = activity.getWindow();
        if (isVisible) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            View decorView = window.getDecorView();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                int visibility = decorView.getSystemUiVisibility();
                decorView.setSystemUiVisibility(visibility & ~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
        activity = null;
    }

    /**
     * 设置屏幕常亮
     */
    public static void setScreeON(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取导航栏高度
     *
     * @param context
     * @return
     */
    public static int getNavigationHeight(Context context) {
        int result = 0;
        int resourceId = 0;
        int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid != 0) {
            resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");

            return context.getResources().getDimensionPixelSize(resourceId);
        } else {
            return 0;
        }
    }

    /**
     * 判断是否有网络连接
     */
    public static boolean isNetOk(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 获取手机model
     *
     * @return
     */
    public static String getModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机Imei
     *
     * @param context
     * @return
     */
    public static String getImei(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = null;
        try {
            imei = telephonyManager.getDeviceId();
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        return imei;
    }

    /**
     * 获取手机品牌
     * @return
     */
    public static String getBrand() {
        return Build.BRAND;  //手机品牌
    }


}
