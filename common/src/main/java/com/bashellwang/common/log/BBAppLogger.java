package com.bashellwang.common.log;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.text.TextUtils;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

public class BBAppLogger {
    public static String CONFIG_APP_LOG_FLAG = "default";
    public static boolean CONFIG_NO_LOG = false;
    public static boolean CONFIG_GENERATE_TRACE = false;

    public BBAppLogger() {
    }

    public static void e(String format, Object... args) {
        if (!CONFIG_NO_LOG) {
            try {
                String ss = getLogString(format, args);
                Log.e(CONFIG_APP_LOG_FLAG, ss);
                if (ss.contains("UnknownFormatConversionException")) {
                    e("OK");
                }
            } catch (Exception var3) {
                ;
            }

        }
    }

    public static void d(String format, Object... args) {
        if (!CONFIG_NO_LOG) {
            try {
                String ss = getLogString(format, args);
                Log.d(CONFIG_APP_LOG_FLAG, ss);
                if (ss.contains("UnknownFormatConversionException")) {
                    d("OK");
                }
            } catch (Exception var3) {
                ;
            }

        }
    }

    public static void w(String format, Object... args) {
        if (!CONFIG_NO_LOG) {
            try {
                String ss = getLogString(format, args);
                Log.w(CONFIG_APP_LOG_FLAG, ss);
                if (ss.contains("UnknownFormatConversionException")) {
                    w("OK");
                }
            } catch (Exception var3) {
                ;
            }

        }
    }

    public static void v(String format, Object... args) {
        if (!CONFIG_NO_LOG) {
            try {
                String ss = getLogString(format, args);
                Log.v(CONFIG_APP_LOG_FLAG, ss);
                if (ss.contains("UnknownFormatConversionException")) {
                    v("OK");
                }
            } catch (Exception var3) {
                ;
            }

        }
    }

    public static void i(String format, Object... args) {
        if (!CONFIG_NO_LOG) {
            try {
                if (!CONFIG_GENERATE_TRACE) {
                    generateDebugTrace();
                }

                Log.i(CONFIG_APP_LOG_FLAG, getLogString(format, args));
            } catch (Exception var3) {
                ;
            }

        }
    }

    public static void throwException(String exceptionMsg) {
        if (!CONFIG_NO_LOG) {
            throw new RuntimeException(exceptionMsg);
        }
    }

    public static void e(Throwable e) {
        if (!CONFIG_NO_LOG) {
            StackTraceElement[] stackTraceElement = e.getStackTrace();
            int currentIndex = -1;

            for (int i = 0; i < stackTraceElement.length; ++i) {
                if (stackTraceElement[i].getMethodName().compareTo("e") == 0) {
                    currentIndex = i + 1;
                    break;
                }
            }

            if (currentIndex >= 0) {
                String fullClassName = stackTraceElement[currentIndex].getClassName();
                String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
                String methodName = stackTraceElement[currentIndex].getMethodName();
                String lineNumber = String.valueOf(stackTraceElement[currentIndex].getLineNumber());
                String traceContent = String.format(Locale.US, "position at %s.%s(%s.java:%s)", fullClassName, methodName, className, lineNumber);
                Log.e(CONFIG_APP_LOG_FLAG, traceContent);
            } else {
                Writer result = new StringWriter();
                PrintWriter printWriter = new PrintWriter(result);
                e.printStackTrace(printWriter);
                Log.e(CONFIG_APP_LOG_FLAG, result.toString());
            }

        }
    }

    private static void generateDebugTrace() {
        StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
        int currentIndex = -1;

        for (int i = 0; i < stackTraceElement.length; ++i) {
            if (stackTraceElement[i].getMethodName().compareTo("i") == 0) {
                currentIndex = i + 1;
                break;
            }
        }

        if (currentIndex == -1) {
            Log.i(CONFIG_APP_LOG_FLAG, "CANNOT GENERATE DEBUG");
        } else {
            String fullClassName = stackTraceElement[currentIndex].getClassName();
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            String methodName = stackTraceElement[currentIndex].getMethodName();
            String lineNumber = String.valueOf(stackTraceElement[currentIndex].getLineNumber());
            String traceContent = String.format(Locale.US, "position at %s.%s(%s.java:%s)", fullClassName, methodName, className, lineNumber);
            Log.i(CONFIG_APP_LOG_FLAG, traceContent);
        }
    }

    private static String getThreadName() {
        return String.format(Locale.ENGLISH, "[thread_id:%d name=%s] ", Thread.currentThread().getId(), Thread.currentThread().getName());
    }

    private static String getLogString(String format, Object... args) {
        if (!TextUtils.isEmpty(format)) {
            if (args != null && args.length > 0) {
                try {
                    return __generateLineInfo() + getThreadName() + String.format(Locale.ENGLISH, format, args);
                } catch (Exception var3) {
                    e(var3);
                    return __generateLineInfo() + getThreadName() + format;
                }
            } else {
                return __generateLineInfo() + getThreadName() + format;
            }
        } else {
            return "";
        }
    }

    private static String __generateLineInfo() {
        StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
        int currentIndex = -1;

        String method;
        for (int i = 0; i < stackTraceElement.length; ++i) {
            if (stackTraceElement[i].getClassName().equals(BBAppLogger.class.getName())) {
                method = stackTraceElement[i].getMethodName();
                if (method.equals("e") || method.equals("w") || method.equals("i") || method.equals("d") || method.equals("v")) {
                    currentIndex = i + 1;
                    break;
                }
            }
        }

        if (currentIndex == -1) {
            Log.v(CONFIG_APP_LOG_FLAG, "CANNOT GENERATE LINE INFO");
            return "";
        } else {
            StackTraceElement traceElement = stackTraceElement[currentIndex];
            method = traceElement.getClassName();
            String className = method.substring(method.lastIndexOf(".") + 1);
            return traceElement.getMethodName() + "(" + className + ".java:" + traceElement.getLineNumber() + "): ";
        }
    }
}

