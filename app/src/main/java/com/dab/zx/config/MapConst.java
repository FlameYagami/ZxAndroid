package com.dab.zx.config;


import com.dab.zx.R;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by 八神火焰 on 2016/12/13.
 */

public class MapConst {
    public static final HashMap<String, Integer>       GuideMap         = new HashMap<>();
    public static final HashMap<String, Integer>       SignMap          = new HashMap<>();
    public static final HashMap<String, Integer>       CampMap          = new HashMap<>();
    public static final HashMap<String, Integer>       RareMap          = new HashMap<>();
    public static final LinkedHashMap<String, String>  KeySearchMap     = new LinkedHashMap<>();
    public static final LinkedHashMap<String, String>  AbilityTypeMap   = new LinkedHashMap<>();
    public static final LinkedHashMap<String, Integer> AbilityDetailMap = new LinkedHashMap<>();

    static {
        GuideMap.put("B22", R.drawable.ic_guide_b22);
        GuideMap.put("B23", R.drawable.ic_guide_b23);
        GuideMap.put("E09", R.drawable.ic_guide_e09);
        GuideMap.put("E10", R.drawable.ic_guide_e10);
        GuideMap.put("CP04", R.drawable.ic_guide_cp04);
        GuideMap.put("CP05", R.drawable.ic_guide_cp05);

        SignMap.put("点燃", R.drawable.ic_sign_ig);
        SignMap.put("觉醒之种", R.drawable.ic_sign_el);

        CampMap.put("红", R.drawable.ic_camp_red);
        CampMap.put("蓝", R.drawable.ic_camp_blue);
        CampMap.put("白", R.drawable.ic_camp_white);
        CampMap.put("黑", R.drawable.ic_camp_black);
        CampMap.put("绿", R.drawable.ic_camp_green);
        CampMap.put("无", R.drawable.ic_camp_void);

        KeySearchMap.put("卡名", SQLitConst.ColumnCName);
        KeySearchMap.put("日名", SQLitConst.ColumnJName);
        KeySearchMap.put("卡编", SQLitConst.ColumnNumber);
        KeySearchMap.put("画师", SQLitConst.ColumnIllust);
        KeySearchMap.put("能力", SQLitConst.ColumnAbility);

        AbilityTypeMap.put("Lv", "Lv");
        AbilityTypeMap.put("射程", "【常】射程");
        AbilityTypeMap.put("绝界", "【常】绝界");
        AbilityTypeMap.put("起始卡", "【常】起始卡");
        AbilityTypeMap.put("生命恢复", "【常】生命恢复");
        AbilityTypeMap.put("虚空使者", "【常】虚空使者");
        AbilityTypeMap.put("进化原力", "【自】进化原力");
        AbilityTypeMap.put("零点优化", "【※】零点优化");

        AbilityDetailMap.put("卡牌登场", 10);
        AbilityDetailMap.put("卡牌移位", 11);
        AbilityDetailMap.put("卡牌破坏", 12);
        AbilityDetailMap.put("卡牌除外", 13);
        AbilityDetailMap.put("卡牌抽取", 14);
        AbilityDetailMap.put("卡牌检索", 15);
        AbilityDetailMap.put("资源放置", 20);
        AbilityDetailMap.put("废弃放置", 21);
        AbilityDetailMap.put("充能放置", 22);
        AbilityDetailMap.put("生命放置", 23);
        AbilityDetailMap.put("返回手牌", 24);
        AbilityDetailMap.put("返回卡组", 25);
        AbilityDetailMap.put("阵营相关", 30);
        AbilityDetailMap.put("种族相关", 31);
        AbilityDetailMap.put("标记相关", 32);
        AbilityDetailMap.put("费用相关", 33);
        AbilityDetailMap.put("力量相关", 34);
        AbilityDetailMap.put("原力相关", 35);
        AbilityDetailMap.put("伤害相关", 40);
        AbilityDetailMap.put("玩家相关", 41);
        AbilityDetailMap.put("联动相关", 42);
        AbilityDetailMap.put("规则相关", 43);
        AbilityDetailMap.put("状态调整", 50);
        AbilityDetailMap.put("牌库调整", 51);
    }
}
