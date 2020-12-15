package com.stackstech.honeybee.core.util;

import java.util.Arrays;

/**
 *
 */
public class StringUtil {

    public static String StringSort(String str) {
        //将字符串转换成字符数组
        char[] array = str.toCharArray();
        //将字符数组排序
        Arrays.sort(array);
        //将字符数组转换成字符串
        return new String(array);
    }
}
