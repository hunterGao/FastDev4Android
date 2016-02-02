package com.huntergao.fastdev4android.utils;

/**
 * 有关时间的工具类
 * Created by HunterGao on 15/12/21.
 */
public class TimeUtil {
    /**
     * 传入的时间是否符合 HH:mm:ss格式
     * @param time 传入的时间
     * @return 是否符合要求
     */
    public static boolean isTimePattern(String time){
        String timePattern = "^(0\\d{1}|1\\d{1}|2[0-3]):([0-5]\\d{1}):([0-5]\\d{1})$";
        return time.matches(timePattern);
    }
}
