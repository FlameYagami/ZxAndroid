package com.dab.zx.game.utils;

import android.text.TextUtils;

import com.dab.zx.R;
import com.dab.zx.bean.CardBean;
import com.dab.zx.config.Enum;
import com.dab.zx.config.MapConst;
import com.dab.zx.uitls.JsonUtils;
import com.dab.zx.uitls.PathManager;
import com.dab.zx.uitls.database.SQLiteUtils;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static br.com.zbra.androidlinq.Linq.stream;
import static com.dab.zx.config.MyApp.context;
import static com.dab.zx.uitls.PathManager.pictureDir;

/**
 * Created by 八神火焰 on 2016/12/13.
 */

public class CardUtils {
    private static final String TAG = CardUtils.class.getSimpleName();

    /**
     * 获取标记的图片资源Id
     *
     * @param sign 标记类型
     * @return Id
     */
    public static int getSignResId(String sign) {
        return stream(MapConst.SignMap).firstOrDefault(entry -> entry.getKey().equals(sign), new AbstractMap.SimpleEntry<>(context.getString(R.string.empty), -1)).getValue();
    }

    /**
     * 获取阵营的图片资源Id
     *
     * @param camp 阵营类型
     * @return Id
     */
    public static List<Integer> getCampResIdList(String camp) {
        List<Integer> campResIdList = new ArrayList<>();
        for (String tempCamp : camp.split(context.getString(R.string.slash))) {
            campResIdList.add(stream(MapConst.CampMap).firstOrNull(entry -> entry.getKey().equals(tempCamp)).getValue());
        }
        while (campResIdList.size() < 5) {
            campResIdList.add(-1);
        }
        return campResIdList;
    }

    /**
     * 获取罕贵的图片资源Id
     *
     * @param rare 阵营类型
     * @return Id
     */
    public static int getRareResIdList(String rare) {
        return stream(MapConst.RareMap).firstOrDefault(entry -> entry.getKey().equals(rare), new AbstractMap.SimpleEntry<>(context.getString(R.string.empty), -1)).getValue();
    }

    /**
     * 获取画师集合
     *
     * @return 画师集合
     */
    public static List<String> getIllust() {
        List<String> list = stream(SQLiteUtils.getAllCardList())
                .select(CardBean::getIllust)
                .distinct()
                .toList();
        Collections.sort(list);
        list.add(0, context.getString(R.string.not_applicable));
        return list;
    }

    /**
     * 获取卡包集合
     *
     * @return 卡包集合
     */
    public static List<String> getAllPack() {
        List<String> list = new ArrayList<>();
        list.add(0, context.getString(R.string.not_applicable));
        List<String> packSeriesList = Arrays.asList(context.getResources().getStringArray(R.array.pack_series));
        list.addAll(stream(packSeriesList).selectMany(CardUtils::getPartPack).toList());
        return list;
    }

    /**
     * 获取部分卡包集合
     *
     * @param packType 卡包类型
     * @return 部分卡包集合
     */
    private static List<String> getPartPack(String packType) {
        List<String> list = stream(SQLiteUtils.getAllCardList())
                .where(bean -> bean.getPack().contains(packType))
                .select(CardBean::getPack)
                .distinct()
                .orderBy(pack -> pack)
                .toList();
        list.add(0, packType + context.getString(R.string.series));
        return list;
    }

    /**
     * 获取部分种族集合
     *
     * @param camp 阵营
     * @return 部分种族集合
     */
    public static List<String> getPartRace(String camp) {
        List<String> list = stream(SQLiteUtils.getAllCardList())
                .where(bean -> bean.getCamp().equals(camp))
                .select(CardBean::getRace)
                .distinct()
                .orderBy(String::length)
                .toList();
        list.add(0, context.getString(R.string.not_applicable));
        return list;
    }

    /**
     * 获取卡片进入的区域
     *
     * @param cardBean 卡牌信息
     * @return 枚举类型
     */
    public static Enum.AreaType getAreaType(CardBean cardBean) {
        if (context.getString(R.string.SignIg).equals(cardBean.getSign())) {
            return Enum.AreaType.Ig;
        }
        if (context.getString(R.string.TypeZxEx).equals(cardBean.getType())) {
            return Enum.AreaType.Ex;
        }
        if (context.getString(R.string.TypePlayer).equals(cardBean.getType())) {
            return Enum.AreaType.Player;
        }
        return Enum.AreaType.Ug;
    }


    /**
     * 获取卡片进入的区域
     *
     * @param number 编号
     * @return 枚举类型
     */
    public static Enum.AreaType getAreaType(String number) {
        CardBean cardBean = stream(SQLiteUtils.getAllCardList()).first(bean -> bean.getNumber().equals(number));
        return getAreaType(cardBean);
    }

    /**
     * 获取中文卡名
     *
     * @param number 卡编
     * @return 卡名
     */
    public static String getCName(String number) {
        return stream(SQLiteUtils.getAllCardList()).first(bean -> number.contains(bean.getNumber())).getCName();
    }

    /**
     * 获取卡片的最大数量
     *
     * @param number 卡编
     * @return 数量
     */
    public static int getMaxCount(String number) {
        String restrict = stream(SQLiteUtils.getAllCardList()).first(bean -> bean.getNumber().equals(number)).getRestrict();
        return TextUtils.isEmpty(restrict) ? 4 : Integer.valueOf(restrict);
    }

    /**
     * 获取卡片在点燃区的枚举类型
     *
     * @param number 卡编
     * @return Life|Void|Normal
     */
    public static Enum.IgType getIgType(String number) {
        return getIgType(stream(SQLiteUtils.getAllCardList()).first(bean -> number.contains(bean.getNumber())));
    }

    /**
     * 获取卡片在点燃区的枚举类型
     *
     * @param cardBean 卡牌信息
     * @return Life|Void|Normal
     */
    public static Enum.IgType getIgType(CardBean cardBean) {
        String ability = cardBean.getAbility();
        if (ability.startsWith(context.getString(R.string.AbilityLife))) {
            return Enum.IgType.Life;
        }
        if (ability.startsWith(context.getString(R.string.AbilityVoid))) {
            return Enum.IgType.Void;
        }
        return Enum.IgType.Normal;
    }

    /**
     * 获取卡片在非点燃区的枚举类型
     *
     * @param number 卡编
     * @return Start|Normal
     */
    public static Enum.UgType getUgType(String number) {
        return getUgType(stream(SQLiteUtils.getAllCardList()).first(bean -> number.contains(bean.getNumber())));
    }

    /**
     * 获取卡片在非点燃区的枚举类型
     *
     * @param cardBean 卡牌信息
     * @return Start|Normal
     */
    public static Enum.UgType getUgType(CardBean cardBean) {
        String ability = cardBean.getAbility();
        if (ability.startsWith(context.getString(R.string.AbilityStart))) {
            return Enum.UgType.Start;
        }
        return Enum.UgType.Normal;
    }

    /**
     * 判断卡片是否为生命恢复
     *
     * @param number 卡编
     * @return Ture|Flase
     */
    public static boolean isLife(String number) {
        return isLife(stream(SQLiteUtils.getAllCardList()).first(bean -> number.contains(bean.getNumber())));
    }

    /**
     * 判断卡片是否为生命恢复
     *
     * @param cardBean 卡牌信息
     * @return Ture|Flase
     */
    public static boolean isLife(CardBean cardBean) {
        return cardBean.getAbility().startsWith(context.getString(R.string.AbilityLife));
    }

    /**
     * 判断卡片是否为虚空使者
     *
     * @param number 卡编
     * @return Ture|Flase
     */
    public static boolean isVoid(String number) {
        return isVoid(stream(SQLiteUtils.getAllCardList()).first(bean -> number.contains(bean.getNumber())));
    }

    /**
     * 判断卡片是否为虚空使者
     *
     * @param cardBean 卡牌信息
     * @return Ture|Flase
     */
    public static boolean isVoid(CardBean cardBean) {
        return cardBean.getAbility().startsWith(context.getString(R.string.AbilityVoid));
    }

    /**
     * 判断卡片是否为起始卡
     *
     * @param number 卡编
     * @return Ture|Flase
     */
    public static boolean isStart(String number) {
        return isStart(stream(SQLiteUtils.getAllCardList()).first(bean -> number.contains(bean.getNumber())));
    }

    /**
     * 判断卡片是否为起始卡
     *
     * @param cardBean 卡牌信息
     * @return Ture|Flase
     */
    public static boolean isStart(CardBean cardBean) {
        return cardBean.getAbility().contains(context.getString(R.string.AbilityStart));
    }


    /**
     * 获取卡牌对应图片的路经集合
     *
     * @param imageJson 卡牌图片编号Json
     * @return 卡牌对应图片的路经集合
     */
    public static List<String> getImagePathList(String imageJson) {
        return stream(JsonUtils.deserializerArray(imageJson, String[].class))
                .select(image -> pictureDir + image + context.getString(R.string.image_extension))
                .toList();
    }


    /**
     * 获取卡编相关卡编集合
     *
     * @param imageJson 图片Json
     * @return 卡牌扩展编号集合
     */
    public static List<String> getNumberExList(String imageJson) {
        return JsonUtils.deserializerArray(imageJson, String[].class);
    }

    /**
     * 获取玩家卡牌路径
     *
     * @param cardBeanList 卡牌信息集合
     * @return 玩家卡牌路径
     */
    public static String getPlayerPath(List<CardBean> cardBeanList) {
        return PathManager.pictureDir
                + stream(cardBeanList).firstOrDefault(cardBean -> getAreaType(cardBean).equals(Enum.AreaType.Player), new CardBean(context.getString(R.string.empty))).getNumber()
                + context.getString(R.string.image_extension);
    }

    /**
     * 获取玩家卡牌路径
     *
     * @param cardBeanList 卡牌信息集合
     * @return 玩家卡牌路径
     */
    public static String getStartPath(List<CardBean> cardBeanList) {
        return PathManager.pictureDir
                + stream(cardBeanList).firstOrDefault(CardUtils::isStart, new CardBean(context.getString(R.string.empty))).getNumber()
                + context.getString(R.string.image_extension);
    }

    /**
     * 获取玩家卡牌路径集合
     *
     * @param numberEx 拍拍扩展编号
     * @return 玩家卡牌路径
     */
    public static String getPlayerPath(String numberEx) {
        return PathManager.pictureDir + numberEx + context.getString(R.string.image_extension);
    }

    /**
     * 获取卡牌信息
     *
     * @param number 卡编
     * @return 卡牌信息
     */
    public static CardBean getCardBean(String number) {
        return stream(SQLiteUtils.getAllCardList()).first(bean -> number.contains(bean.getNumber()));
    }

    /**
     * 获取卡牌信息集合
     *
     * @param numberList 卡编集合
     * @return 卡牌信息集合
     */
    public static List<CardBean> getCardBeanList(List<String> numberList) {
        return stream(numberList).select(CardUtils::getCardBean).toList();
    }

    /**
     * 获取卡牌的Md5
     *
     * @param numberEx 卡编
     * @return Md5
     */
    public static String getMd5(String numberEx) {
        return stream(SQLiteUtils.getAllCardList()).firstOrNull(bean -> numberEx.contains(bean.getNumber())).getMd5();
    }
}
