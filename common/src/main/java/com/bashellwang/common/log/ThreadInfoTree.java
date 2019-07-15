package com.bashellwang.common.log;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by liang.wang on 2019/3/18.
 */
public class ThreadInfoTree extends Timber.DebugTree {

    @Override
    protected @Nullable String createStackElementTag(@NotNull StackTraceElement element) {
        return super.createStackElementTag(element);

    }

    @Override
    protected boolean isLoggable(@Nullable String tag, int priority) {
        return super.isLoggable(tag, priority);
    }

    @Override
    protected void log(int priority, @Nullable String tag, @NotNull String message, @Nullable Throwable t) {

        ArrayList<String> strList = getTagFromStackTrace();
        tag = strList.get(0);
        message += strList.get(1);



        super.log(priority, tag, message, t);
    }

    public ArrayList<String> getTagFromStackTrace() {



        ArrayList<String> strList = new ArrayList<>();
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        if (stackTrace.length <= 7) {
            throw new IllegalStateException(
                    "Synthetic stacktrace didn't have enough elements: are you using proguard?");
        }

        String threadName = Thread.currentThread().getName();
        long threadId = Thread.currentThread().getId();
        strList.add(String.format("%1$s%2$s%3$s%4$s%5$s%6$s", createStackElementTag(stackTrace[7]), "<", threadName, " ", threadId, ">"));
        StringBuilder sb = new StringBuilder(":");
//        sb.append(stackTrace[7].getClassName()).append(".")
//                .append(stackTrace[7].getMethodName()).append("(").append(stackTrace[7].getFileName()).append(":").append(stackTrace[7].getLineNumber()).append(") ");

        sb.append("(").append(stackTrace[7].getFileName()).append(":").append(stackTrace[7].getLineNumber()).append(")");
        strList.add(sb.toString());
        return strList;
    }
}
