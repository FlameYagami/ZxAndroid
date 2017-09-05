package com.zx.bean;

import java.io.Serializable;

/**
 * Created by 时空管理局 on 2016/9/26.
 */
public class CardBean implements Serializable
{
    private String Md5;
    private String Key;
    private String AbilityType;
    private String Type;
    private String Camp;
    private String Race;
    private String Sign;
    private String Rare;
    private String Pack;
    private String Restrict;
    private String CName;
    private String JName;
    private String Illust;
    private String Number;
    private String Cost;
    private String Power;
    private String Ability;
    private String Lines;
    private String AbilityDetail;
    private String Image;

    public CardBean(String key, String type, String camp, String race, String sign, String rare, String pack, String illust, String restrict, String cost, String power, String abilityType, String abilityDetail) {
        Key = key;
        Type = type;
        Camp = camp;
        Race = race;
        Sign = sign;
        Rare = rare;
        Pack = pack;
        Illust = illust;
        Cost = cost;
        Power = power;
        Restrict = restrict;
        AbilityType = abilityType;
        AbilityDetail = abilityDetail;
    }

    /**
     * 全数据缓存构造方法
     */
    public CardBean(String md5, String type, String race, String camp, String sign, String rare, String pack, String restrict, String cname, String jname, String illust, String number, String cost, String power, String ability, String lines, String image) {
        Md5 = md5;
        Type = type;
        Race = race;
        Camp = camp;
        Sign = sign;
        Rare = rare;
        Pack = pack;
        Restrict = restrict;
        CName = cname;
        JName = jname;
        Illust = illust;
        Number = number;
        Cost = cost;
        Power = power;
        Ability = ability;
        Lines = lines;
        Image = image;
    }

    public CardBean(String number) {
        Number = number;
    }

    public String getKey() {
        return Key;
    }

    public String getType() {
        return Type;
    }

    public CardBean setType(String type) {
        Type = type;
        return this;
    }

    public String getCamp() {
        return Camp;
    }

    public CardBean setCamp(String camp) {
        Camp = camp;
        return this;
    }

    public String getRace() {
        return Race;
    }

    public String getRare() {
        return Rare;
    }

    public String getSign() {
        return Sign;
    }

    public String getPack() {
        return Pack;
    }

    public String getRestrict() {
        return Restrict;
    }

    public String getCName() {
        return CName;
    }

    public String getJName() {
        return JName;
    }

    public String getIllust() {
        return Illust;
    }

    public String getNumber() {
        return Number;
    }

    public String getCost() {
        return Cost;
    }

    public String getAbility() {
        return Ability;
    }

    public String getPower() {
        return Power;
    }

    public String getLines() {
        return Lines;
    }

    public String getAbilityType() {
        return AbilityType;
    }

    public String getAbilityDetail() {
        return AbilityDetail;
    }

    public String getImage() {
        return Image;
    }

    public String getMd5() {
        return Md5;
    }
}
