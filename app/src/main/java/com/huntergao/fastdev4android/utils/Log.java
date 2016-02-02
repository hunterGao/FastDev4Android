package com.huntergao.fastdev4android.utils;

/**
 * 重写系统日志管理类
 * 使用方法:还是和系统的Log类一样使用,但该类会打印比系统更多的日志信息,包括类名称/运行的方法/行数和日志信息
 * Created by HunterGao on 15/12/4.
 */
public class Log {

    /**
     * 设置是否打开Log日志开关
     */
    private static boolean mIsShow = true;

    /**
     * 根据tag打印v信息
     * @param tag
     * @param msg
     */
    public static void v(String tag, String msg){
        if(mIsShow){
            StackTraceElement stackTraceElement = new Throwable().getStackTrace()[1];
            //添加类名称
            StringBuilder sb = new StringBuilder(stackTraceElement.getClassName());
            sb.append("::");
            //添加方法名
            sb.append(stackTraceElement.getMethodName());
            sb.append("@");
            //添加行数
            sb.append(stackTraceElement.getLineNumber());
            sb.append(">>>");
            android.util.Log.v(tag, sb.append(msg).toString());
        }
    }

    /**
     * 根据tag打印v信息,包括Throwable信息
     * @param tag
     * @param msg
     * @param throwable
     */
    public static void v(String tag, String msg, Throwable throwable){
        if(mIsShow){
            android.util.Log.v(tag, msg, throwable);
        }
    }

    /**
     * 根据tag打印i信息
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg){
        if(mIsShow){
            StackTraceElement stackTraceElement = new Throwable().getStackTrace()[1];
            //添加类名称
            StringBuilder sb = new StringBuilder(stackTraceElement.getClassName());
            sb.append("::");
            //添加方法名
            sb.append(stackTraceElement.getMethodName());
            sb.append("@");
            //添加行数
            sb.append(stackTraceElement.getLineNumber());
            sb.append(">>>");
            android.util.Log.i(tag, sb.append(msg).toString());
        }
    }

    /**
     * 根据tag打印i信息,包括Throwable信息
     * @param tag
     * @param msg
     * @param throwable
     */
    public static void i(String tag, String msg, Throwable throwable){
        if(mIsShow){
            android.util.Log.i(tag, msg, throwable);
        }
    }

    /**
     * 根据tag打印d信息
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg){
        if(mIsShow){
            StackTraceElement stackTraceElement = new Throwable().getStackTrace()[1];
            //添加类名称
            StringBuilder sb = new StringBuilder(stackTraceElement.getClassName());
            sb.append("::");
            //添加方法名
            sb.append(stackTraceElement.getMethodName());
            sb.append("@");
            //添加行数
            sb.append(stackTraceElement.getLineNumber());
            sb.append(">>>");
            android.util.Log.v(tag, sb.append(msg).toString());
        }
    }

    /**
     * 根据tag打印d信息,包括Throwable信息
     * @param tag
     * @param msg
     * @param throwable
     */
    public static void d(String tag, String msg, Throwable throwable){
        if(mIsShow){
            android.util.Log.v(tag, msg, throwable);
        }
    }

    /**
     * 根据tag打印w信息
     * @param tag
     * @param msg
     */
    public static void w(String tag, String msg){
        if(mIsShow){
            StackTraceElement stackTraceElement = new Throwable().getStackTrace()[1];
            //添加类名称
            StringBuilder sb = new StringBuilder(stackTraceElement.getClassName());
            sb.append("::");
            //添加方法名
            sb.append(stackTraceElement.getMethodName());
            sb.append("@");
            //添加行数
            sb.append(stackTraceElement.getLineNumber());
            sb.append(">>>");
            android.util.Log.w(tag, sb.append(msg).toString());
        }
    }

    /**
     * 根据tag打印w信息,包括Throwable信息
     * @param tag
     * @param msg
     * @param throwable
     */
    public static void w(String tag, String msg, Throwable throwable){
        if(mIsShow){
            android.util.Log.w(tag, msg, throwable);
        }
    }

    /**
     * 根据tag打印e信息
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg){
        if(mIsShow){
            StackTraceElement stackTraceElement = new Throwable().getStackTrace()[1];
            //添加类名称
            StringBuilder sb = new StringBuilder(stackTraceElement.getClassName());
            sb.append("::");
            //添加方法名
            sb.append(stackTraceElement.getMethodName());
            sb.append("@");
            //添加行数
            sb.append(stackTraceElement.getLineNumber());
            sb.append(">>>");
            android.util.Log.e(tag, sb.append(msg).toString());
        }
    }

    /**
     * 根据tag打印e信息,包括Throwable信息
     * @param tag
     * @param msg
     * @param throwable
     */
    public static void e(String tag, String msg, Throwable throwable){
        if(mIsShow){
            android.util.Log.e(tag, msg, throwable);
        }
    }

    public static void setIsShow(boolean mIsShow) {
        Log.mIsShow = mIsShow;
    }
}
