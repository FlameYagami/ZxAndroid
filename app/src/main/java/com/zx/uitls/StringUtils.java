package com.zx.uitls;

import java.util.List;

/**
 * Created by 八神火焰 on 2017/1/12.
 */

public class StringUtils
{
    public static String changeList2String(List<?> list) {
        StringBuilder builder = new StringBuilder();
        for (Object str : list) {
            builder.append(str);
            builder.append("|");
        }
        String string = builder.toString();
        return string.substring(0, string.length() - 1);
    }

    /**
     * 将string字符串中的小写字母改为大写
     *
     * @param string 小写字符串
     * @return 大写字符串
     */
    public static String toUpperCase(String string) {
        char tempCharArray[] = string.toCharArray();
        for (int i = 0; i != tempCharArray.length; i++) {
            if (tempCharArray[i] >= 'a' && tempCharArray[i] <= 'z') {
                tempCharArray[i] = Character.toUpperCase(tempCharArray[i]);
            }
        }
        return String.valueOf(tempCharArray);
    }
}
