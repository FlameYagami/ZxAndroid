package com.zx.game;

import android.text.TextUtils;

import com.zx.R;
import com.zx.bean.CardBean;
import com.zx.bean.DeckBean;
import com.zx.config.Enum;
import com.zx.game.utils.CardUtils;
import com.zx.uitls.database.SQLiteUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static br.com.zbra.androidlinq.Linq.stream;
import static com.zx.config.MyApp.context;
import static com.zx.game.utils.CardUtils.getAreaType;
import static com.zx.game.utils.CardUtils.isLife;
import static com.zx.game.utils.CardUtils.isVoid;
import static com.zx.uitls.PathManager.pictureCache;

/**
 * Created by 八神火焰 on 2017/1/14.
 */

public class DeckManager
{
    private static final String TAG = DeckManager.class.getSimpleName();

    // 卡组名称
    private String deckName;

    // 卡组Md5集合
    private List<String> Md5List = new ArrayList<>();

    // 卡组扩展名集合
    private List<String> NumberExList = new ArrayList<>();

    // 玩家卡数据缓存
    private List<DeckBean> PlayerList = new ArrayList<>();

    // 起始卡缓存
    private List<DeckBean> StartList = new ArrayList<>();

    // 点燃数据缓存
    private List<DeckBean> IgList = new ArrayList<>();

    // 非点燃数据缓存
    private List<DeckBean> UgList = new ArrayList<>();

    // 额外数据缓存
    private List<DeckBean> ExList = new ArrayList<>();

    /**
     * 初始化
     *
     * @param deckName     卡组名称
     * @param numberExList 编号扩展集合(B01-001A、B01-001B)
     */
    public DeckManager(String deckName, List<String> numberExList) {
        clearDeck();
        this.deckName = deckName;
        this.NumberExList = numberExList;
        loadDeck();
    }

    public List<DeckBean> getPlayerList() {
        return PlayerList;
    }

    public List<DeckBean> getStartList() {
        return StartList;
    }

    public List<DeckBean> getIgList() {
        return IgList;
    }

    public List<DeckBean> getUgList() {
        return UgList;
    }

    public List<DeckBean> getExList() {
        return ExList;
    }

    public List<String> getNumberExList() {
        return NumberExList;
    }

    public List<String> getMd5List() {
        return Md5List;
    }

    public String getDeckName() {
        return deckName;
    }

    /**
     * 清空卡组
     */
    private void clearDeck() {
        Md5List.clear();
        NumberExList.clear();
        PlayerList.clear();
        StartList.clear();
        IgList.clear();
        UgList.clear();
        ExList.clear();
    }

    /**
     * 加载卡组
     */
    private void loadDeck() {
        for (String numberEx : NumberExList) {
            Enum.AreaType areaType  = getAreaType(numberEx);
            String        imagePath = pictureCache + File.separator + numberEx + context.getString(R.string.image_extension);
            addCard(areaType, numberEx, imagePath);
        }
    }

    /**
     * 添加卡片到组卡区
     *
     * @param areaType      卡片添加区域枚举类型
     * @param number        卡编
     * @param thumbnailPath 缩略图路径
     * @return 枚举类型
     */
    public Enum.AreaType addCard(Enum.AreaType areaType, String number, String thumbnailPath) {
        Md5List.add(CardUtils.getMd5(number));
        if (areaType.equals(Enum.AreaType.Player)) {
            PlayerList.clear();
            addBeanToList(number, thumbnailPath, PlayerList);
            return Enum.AreaType.Player;
        } else if (areaType.equals(Enum.AreaType.Ig)) {
            if (checkAreaIg(number)) {
                addBeanToList(number, thumbnailPath, IgList);
                return Enum.AreaType.Ig;
            }
        } else if (areaType.equals(Enum.AreaType.Ug)) {
            if (checkAreaUg(number)) {
                addBeanToList(number, thumbnailPath, UgList);
                if (CardUtils.getUgType(number).equals(Enum.UgType.Start)) {
                    addBeanToList(number, thumbnailPath, StartList);
                }
                return Enum.AreaType.Ug;
            }
        } else if (areaType.equals(Enum.AreaType.Ex)) {
            if (checkAreaEx(number)) {
                addBeanToList(number, thumbnailPath, ExList);
                return Enum.AreaType.Ex;
            }
        }
        return Enum.AreaType.None;
    }

    /**
     * 从组卡区删除卡牌
     *
     * @param areaType 卡片添加区域枚举类型
     * @param number   卡编
     * @return 枚举类型
     */
    public Enum.AreaType deleteCard(Enum.AreaType areaType, String number) {
        Md5List.remove(CardUtils.getMd5(number));
        if (areaType.equals(Enum.AreaType.Player)) {
            PlayerList.clear();
            return Enum.AreaType.Player;
        } else if (areaType.equals(Enum.AreaType.Ig)) {
            IgList.remove(stream(IgList).first(bean -> bean.getNumberEx().equals(number)));
            return Enum.AreaType.Ig;
        } else if (areaType.equals(Enum.AreaType.Ug)) {
            UgList.remove(stream(UgList).first(bean -> bean.getNumberEx().equals(number)));
            if (CardUtils.getUgType(number).equals(Enum.UgType.Start)) {
                StartList.remove(stream(StartList).first(bean -> bean.getNumberEx().equals(number)));
            }
            return Enum.AreaType.Ug;
        } else if (areaType.equals(Enum.AreaType.Ex)) {
            ExList.remove(stream(ExList).first(bean -> bean.getNumberEx().equals(number)));
            return Enum.AreaType.Ex;
        }
        return Enum.AreaType.None;
    }

    /**
     * 添加卡牌信息到指定的集合
     *
     * @param thumbnailPath 卡片缩略图
     * @param numberEx      扩展卡编
     * @param collection    指定的集合
     */
    private void addBeanToList(String numberEx, String thumbnailPath, List<DeckBean> collection) {
        CardBean cardBean = stream(SQLiteUtils.getAllCardList())
                .first(bean -> bean.getNumber().equals(numberEx));
        String name  = cardBean.getCName();
        String camp  = cardBean.getCamp();
        int    cost  = TextUtils.isEmpty(cardBean.getCost()) ? 0 : Integer.valueOf(cardBean.getCost());
        int    power = TextUtils.isEmpty(cardBean.getPower()) ? 0 : Integer.valueOf(cardBean.getPower());
        collection.add(new DeckBean(thumbnailPath, name, camp, numberEx, cost, power));
    }

    /**
     * 返回卡编是否具有添加到额外区域的权限
     *
     * @param number 卡编
     * @return true|false
     */
    private boolean checkAreaEx(String number) {
        String name = CardUtils.getCName(number);
        return stream(ExList).where(bean -> name.equals(bean.getCName())).count() < CardUtils.getMaxCount(number) && ExList.size() < 10;
    }

    /**
     * 返回卡编是否具有添加到非点燃区域的权限
     *
     * @param number 卡编
     * @return true|false
     */
    private boolean checkAreaUg(String number) {
        String name = CardUtils.getCName(number);
        return stream(UgList).where(bean -> name.equals(bean.getCName())).count() < CardUtils.getMaxCount(number) && UgList.size() < 30;
    }

    /**
     * 返回卡编是否具有添加到点燃区域的权限
     *
     * @param number 卡编
     * @return true|false
     */
    private boolean checkAreaIg(String number) {
        String name = CardUtils.getCName(number);
        // 根据卡编获取卡片在点燃区的枚举类型
        Enum.IgType igType = CardUtils.getIgType(number);
        // 判断卡片是否超出自身添加数量以及点燃区总数量
        boolean canAdd = stream(IgList).where(bean -> name.equals(bean.getCName())).count() < CardUtils.getMaxCount(number) && IgList.size() < 20;
        if (igType.equals(Enum.IgType.Life)) {
            canAdd = canAdd && stream(IgList).where(bean -> isLife(bean.getNumberEx())).count() < 4;
        } else if (igType.equals(Enum.IgType.Void)) {
            canAdd = canAdd && stream(IgList).where(bean -> isVoid(bean.getNumberEx())).count() < 4;
        }
        return canAdd;
    }

    /**
     * 卡组排序
     *
     * @param value 排序枚举类型
     */
    public void orderDeck(Enum.DeckOrderType value) {
        if (value.equals(Enum.DeckOrderType.Value)) {
            orderByValue(IgList);
            orderByValue(UgList);
            orderByValue(ExList);
        } else if (value.equals(Enum.DeckOrderType.Random)) {
            orderByRandom(IgList);
            orderByRandom(UgList);
            orderByRandom(ExList);
        }
    }

    /**
     * 数值排序
     *
     * @param deckList 卡组集合
     */
    private void orderByValue(List<DeckBean> deckList) {
        List<DeckBean> tempDeckList = stream(deckList)
                .orderBy(DeckBean::getCamp)
                .thenByDescending(DeckBean::getCost)
                .thenByDescending(DeckBean::getPower)
                .thenBy(DeckBean::getNumberEx)
                .toList();
        deckList.clear();
        deckList.addAll(tempDeckList);
    }

    /**
     * 随机排序
     *
     * @param deckList 卡组集合
     */
    private void orderByRandom(List<DeckBean> deckList) {
        List<DeckBean> tempDeckList = new ArrayList<>();
        Random         random       = new Random();
        for (DeckBean deck : deckList) {
            tempDeckList.add(random.nextInt(tempDeckList.size() + 1), deck);
        }
        deckList.clear();
        deckList.addAll(tempDeckList);
    }
}
