package com.zx.bean;

/**
 * Created by 八神火焰 on 2016/12/22.
 */

public class DeckBean
{
    private String ImagePath;
    private String CName;
    private String Camp;
    private String NumberEx;
    private int    Cost;
    private int    Power;

    public DeckBean(String imagePath, String cname, String camp, String numberEx, int cost, int power) {
        ImagePath = imagePath;
        CName = cname;
        Camp = camp;
        NumberEx = numberEx;
        Cost = cost;
        Power = power;
    }

    public String getCName() {
        return CName;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public String getCamp() {
        return Camp;
    }

    public String getNumberEx() {
        return NumberEx;
    }

    public int getCost() {
        return Cost;
    }

    public int getPower() {
        return Power;
    }
}
