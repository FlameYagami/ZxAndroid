package com.zx.bean;

import com.zx.config.MapConst;

import java.util.LinkedHashMap;

/**
 * Created by 八神火焰 on 2016/12/16.
 */

public class AbilityDetailBean
{
    private static LinkedHashMap<String, Boolean> abilityDetailMap = new LinkedHashMap<>();

    static {
        for (LinkedHashMap.Entry<String, Integer> entry : MapConst.AbilityDetailMap.entrySet()) {
            abilityDetailMap.put(entry.getKey(), false);
        }
    }

    public static void initAbilityDetailMap() {
        for (LinkedHashMap.Entry<String, Boolean> entry : abilityDetailMap.entrySet()) {
            abilityDetailMap.put(entry.getKey(), false);
        }
    }

    public static LinkedHashMap<String, Boolean> getAbilityDetailMap() {
        return abilityDetailMap;
    }

    public static void setAbilityDetailMap(LinkedHashMap<String, Boolean> abilityDetailMap) {
        AbilityDetailBean.abilityDetailMap = abilityDetailMap;
    }
}
