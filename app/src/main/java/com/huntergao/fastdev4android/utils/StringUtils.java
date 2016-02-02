package com.huntergao.fastdev4android.utils;

/**
 * Created by HunterGao on 15/12/4.
 */
public class StringUtils {

    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        if(null == str || "".equals(str)){
            return true;
        }
        return false;
    }
}
