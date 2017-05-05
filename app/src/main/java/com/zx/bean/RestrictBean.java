package com.zx.bean;

/**
 * Created by 八神火焰 on 2017/5/4.
 */

public class RestrictBean
{
    public String md5;
    public int    restrict;

    public RestrictBean() {
        restrict = 4;
    }

    public String getMd5() {
        return md5;
    }

    public int getRestrict() {
        return restrict;
    }
}
