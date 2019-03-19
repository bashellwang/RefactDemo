package com.bashellwang.refactdemo.log;

import com.bashellwang.refactdemo.BuildConfig;

import timber.log.Timber;


/**
 * Created by liang.wang on 2019/3/18.
 * <p>
 */
public class ALog {

    private static boolean bLogEnable = BuildConfig.DEBUG;

    public static void setLogEnable(boolean enable) {
        bLogEnable = enable;
    }

    public static void init() {
        if (bLogEnable) {
            Timber.plant(new ThreadInfoTree());

        } else {
            Timber.plant(new ReleaseTree());
        }
    }

    public static void e(String format, Object... args) {
        Timber.e(format, args);
    }

    public static void w(String format, Object... args) {
        Timber.w(format, args);
    }

    public static void v(String format, Object... args) {
        Timber.v(format, args);
    }

    public static void d(String format, Object... args) {
        Timber.d(format, args);
    }

    public static void i(String format, Object... args) {
        Timber.i(format, args);
    }

    public static void e(Throwable e) {
        Timber.e(e);
    }


}
