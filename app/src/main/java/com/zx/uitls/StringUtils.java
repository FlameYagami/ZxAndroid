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
}
