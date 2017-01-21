package com.zx.bean;

import java.io.Serializable;

/**
 * Created by 时空管理局 on 2016/9/26.
 */
public class CardBean implements Serializable
{
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
    private String Faq;
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

    public CardBean(String type, String race, String camp, String sign, String rare, String pack, String restrict, String cname, String jname, String illust, String number, String cost, String power, String ability, String lines, String faq, String image) {
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
        Faq = faq;
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

    public CardBean setRace(String race) {
        Race = race;
        return this;
    }

    public String getRare() {
        return Rare;
    }

    public CardBean setRare(String rare) {
        Rare = rare;
        return this;
    }

    public String getSign() {
        return Sign;
    }

    public CardBean setSign(String sign) {
        Sign = sign;
        return this;
    }

    public String getPack() {
        return Pack;
    }

    public CardBean setPack(String pack) {
        Pack = pack;
        return this;
    }

    public String getRestrict() {
        return Restrict;
    }

    public CardBean setRestrict(String restrict) {
        Restrict = restrict;
        return this;
    }

    public String getCName() {
        return CName;
    }

    public CardBean setCName(String CName) {
        this.CName = CName;
        return this;
    }

    public String getJName() {
        return JName;
    }

    public CardBean setJName(String JName) {
        this.JName = JName;
        return this;
    }

    public String getIllust() {
        return Illust;
    }

    public CardBean setIllust(String illust) {
        Illust = illust;
        return this;
    }

    public String getNumber() {
        return Number;
    }

    public CardBean setNumber(String number) {
        Number = number;
        return this;
    }

    public String getCost() {
        return Cost;
    }

    public CardBean setCost(String cost) {
        Cost = cost;
        return this;
    }

    public String getAbility() {
        return Ability;
    }

    public CardBean setAbility(String ability) {
        Ability = ability;
        return this;
    }

    public String getPower() {
        return Power;
    }

    public CardBean setPower(String power) {
        Power = power;
        return this;
    }

    public String getLines() {
        return Lines;
    }

    public CardBean setLines(String lines) {
        Lines = lines;
        return this;
    }

    public String getFaq() {
        return Faq;
    }

    public CardBean setFaq(String faq) {
        Faq = faq;
        return this;
    }

    public String getAbilityType() {
        return AbilityType;
    }

    public CardBean setAbilityType(String abilityType) {
        AbilityType = abilityType;
        return this;
    }

    public String getAbilityDetail() {
        return AbilityDetail;
    }

    public CardBean setAbilityDetail(String abilityDetail) {
        AbilityDetail = abilityDetail;
        return this;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }
}
