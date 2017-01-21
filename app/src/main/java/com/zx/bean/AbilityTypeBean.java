package com.zx.bean;

import com.zx.config.MapConst;

import java.util.LinkedHashMap;

/**
 * Created by 八神火焰 on 2016/12/16.
 */

public class AbilityTypeBean
{
    private static LinkedHashMap<String, Boolean> abilityTypeMap = new LinkedHashMap<>();

    static {
        for (LinkedHashMap.Entry<String, String> entry : MapConst.AbilityTypeMap.entrySet()) {
            abilityTypeMap.put(entry.getKey(), false);
        }
    }

    public static void initAbilityTypeMap() {
        for (LinkedHashMap.Entry<String, Boolean> entry : abilityTypeMap.entrySet()) {
            abilityTypeMap.put(entry.getKey(), false);
        }
    }

    public static LinkedHashMap<String, Boolean> getAbilityTypeMap() {
        return abilityTypeMap;
    }

    public static void setAbilityTypeMap(LinkedHashMap<String, Boolean> abilityTypeMap) {
        AbilityTypeBean.abilityTypeMap = abilityTypeMap;
    }
}
