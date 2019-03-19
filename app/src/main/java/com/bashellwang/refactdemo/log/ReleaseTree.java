package com.bashellwang.refactdemo.log;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by liang.wang on 2019/3/18.
 */
public class ReleaseTree extends ThreadInfoTree {

    @Override
    protected boolean isLoggable(@Nullable String tag, int priority) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
            return false;
        }
        return true;
    }

    @Override
    protected void log(int priority, @Nullable String tag, @NotNull String message, @Nullable Throwable t) {
        if (!isLoggable(tag, priority)) {
            return;
        }
        super.log(priority, tag, message, t);

        // TODO  这里将日志本地持久化
    }
}
