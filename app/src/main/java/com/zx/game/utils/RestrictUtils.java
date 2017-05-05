package com.zx.game.utils;

import com.zx.bean.CardBean;
import com.zx.bean.RestrictBean;
import com.zx.uitls.FileUtils;
import com.zx.uitls.JsonUtils;

import java.util.ArrayList;
import java.util.List;

import static br.com.zbra.androidlinq.Linq.stream;

/**
 * Created by 八神火焰 on 2017/5/4.
 */

public class RestrictUtils
{
    private static final String TAG = RestrictUtils.class.getSimpleName();

    private static List<RestrictBean> restrictList = new ArrayList<>();

    private enum RestrictType
    {
        NONE, RESTRICT_0, RESTRICT_4, RESTRICT_20, RESTRICT_30
    }

    /**
     * 获取制限枚举类型
     *
     * @param restrict 制限数量字符串
     * @return 限制枚举类型
     */
    private static RestrictType getRestrictType(String restrict) {
        switch (restrict) {
            case "0":
                return RestrictType.RESTRICT_0;
            case "4":
                return RestrictType.RESTRICT_4;
            case "20":
                return RestrictType.RESTRICT_20;
            case "30":
                return RestrictType.RESTRICT_30;
            default:
                return RestrictType.NONE;
        }
    }

    /**
     * 获取制限列表
     *
     * @return 限制列表
     */
    public static List<RestrictBean> getRestrictList() {
        if (restrictList.size() == 0) {
            String restrictJson = FileUtils.getAssetsContent("restrict");
            restrictList = JsonUtils.deserializerList(restrictJson, RestrictBean.class);
        }
        return restrictList;
    }

    /**
     * 获取制限后的卡牌集合
     *
     * @param cardList 源卡牌集合
     * @param cardBean 查询条件实例
     * @return 制限后的卡牌集合
     */
    public static List<CardBean> getRestrictCardList(List<CardBean> cardList, CardBean cardBean) {
        RestrictType mRestrictType = getRestrictType(cardBean.getRestrict());
        if (mRestrictType.equals(RestrictType.NONE)) {
            return cardList;
        }
        List<CardBean> restrictCardList = new ArrayList<>();
        List<CardBean> tempCardList     = stream(cardList).where(bean -> getRestrictType(bean.getRestrict()).equals(mRestrictType)).toList();
        restrictCardList.addAll(tempCardList);
        return restrictCardList;
    }
}
