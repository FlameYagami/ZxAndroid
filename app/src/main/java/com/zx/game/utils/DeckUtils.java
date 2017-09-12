package com.zx.game.utils;

import android.text.TextUtils;

import com.zx.R;
import com.zx.bean.CardBean;
import com.zx.bean.DeckBean;
import com.zx.bean.DeckPreviewBean;
import com.zx.config.Enum;
import com.zx.game.DeckManager;
import com.zx.uitls.FileUtils;
import com.zx.uitls.JsonUtils;
import com.zx.uitls.PathManager;

import java.util.ArrayList;
import java.util.List;

import br.com.zbra.androidlinq.Linq;

import static br.com.zbra.androidlinq.Linq.stream;
import static com.zx.config.MyApp.context;
import static com.zx.game.utils.CardUtils.getAreaType;
import static com.zx.uitls.PathManager.deckDir;

/**
 * Created by 八神火焰 on 2016/12/22.
 */

public class DeckUtils
{
    private static final String TAG = DeckUtils.class.getSimpleName();

    /**
     * 检测卡组名称是否已被使用
     *
     * @param deckName 卡组名称
     * @return true|false
     */
    public static boolean checkDeckName(String deckName) {
        return stream(getDeckNameList()).any(deckName::equals);
    }

    /**
     * 返回卡组名称集合
     *
     * @return 卡组名称集合
     */
    public static List<String> getDeckNameList() {
        String       deckExtension = context.getString(R.string.deck_extension);
        List<String> deckNameList  = new ArrayList<>();
        deckNameList.addAll(Linq.stream(FileUtils.getFileNameExList(deckDir))
                .where(fileNameEx -> fileNameEx.endsWith(deckExtension))
                .select(fileNameEx -> fileNameEx.replace(deckExtension, context.getString(R.string.empty)))
                .toList());
        return deckNameList;
    }

    /**
     * 获取卡组路径
     *
     * @param deckName 卡组名称
     * @return 卡组路径
     */
    public static String getDeckPath(String deckName) {
        return PathManager.deckDir + deckName + context.getString(R.string.deck_extension);
    }

    /**
     * 获取卡组预览集合
     *
     * @return 卡组预览集合
     */
    public static List<DeckPreviewBean> getDeckPreviewList() {
        List<String>          deckPathList    = new ArrayList<>();
        List<DeckPreviewBean> deckPreviewList = new ArrayList<>();
        deckPathList.addAll(stream(FileUtils.getFilePathList(deckDir)).where(deckPath -> deckPath.endsWith(context.getString(R.string.deck_extension))).toList());
        for (String deckPath : deckPathList) {
            String deckName       = FileUtils.getFileName(deckPath);
            String numberListJson = FileUtils.getFileContent(deckPath);
            numberListJson = TextUtils.isEmpty(numberListJson) ? JsonUtils.serializer(new ArrayList<String>()) : numberListJson;
            List<String>   numberListEx = JsonUtils.deserializerArray(numberListJson, String[].class);
            List<CardBean> cardBeanList = CardUtils.getCardBeanList(numberListEx);
            String         playerPath   = CardUtils.getPlayerPath(cardBeanList);
            String         startPath    = CardUtils.getStartPath(cardBeanList);
            String         statusMain   = context.getString(getMainCount(cardBeanList) == 50 ? R.string.deck_pre_complete : R.string.deck_pre_not_complete);
            String         statusExtra  = context.getString(getExtraCount(cardBeanList) == 10 ? R.string.deck_pre_complete : R.string.deck_pre_not_complete);
            deckPreviewList.add(new DeckPreviewBean(deckName, statusMain, statusExtra, playerPath, startPath, numberListEx));
        }
        return deckPreviewList;
    }

    /**
     * 获取玩家卡组的数量
     *
     * @param cardBeanList 卡牌信息集合
     * @return 数量
     */
    private static int getPlayerCount(List<CardBean> cardBeanList) {
        return stream(cardBeanList)
                .where(cardBean -> getAreaType(cardBean).equals(Enum.AreaType.Player))
                .count();
    }

    /**
     * 获取主卡组的数量
     *
     * @param cardBeanList 卡牌信息集合
     * @return 数量
     */
    private static int getMainCount(List<CardBean> cardBeanList) {
        return stream(cardBeanList)
                .where(cardBean -> getAreaType(cardBean).equals(Enum.AreaType.Ig) || getAreaType(cardBean).equals(Enum.AreaType.Ug))
                .count();
    }

    /**
     * 获取额外卡组的数量
     *
     * @param cardBeanList 卡牌信息集合
     * @return 数量
     */
    private static int getExtraCount(List<CardBean> cardBeanList) {
        return stream(cardBeanList)
                .where(cardBean -> getAreaType(cardBean).equals(Enum.AreaType.Ex))
                .count();
    }

    /**
     * 判断卡组是否符合标准
     *
     * @param numberList 卡编集合
     * @return ture|false
     */
    public static boolean checkDeck(List<String> numberList) {
        List<CardBean> cardBeanList = CardUtils.getCardBeanList(numberList);
        return 1 <= getPlayerCount(cardBeanList) &&
                50 == getMainCount(cardBeanList) &&
                4 >= stream(cardBeanList).where(bean -> CardUtils.isLife(bean.getNumber())).count() &&
                4 >= stream(cardBeanList).where(bean -> CardUtils.isVoid(bean.getNumber())).count();
    }

    /**
     * 保存卡组
     *
     * @return ture|false
     */
    public static boolean saveDeck(DeckManager mDeckManager) {
        List<String> numberList = new ArrayList<>();
        numberList.addAll(stream(mDeckManager.getIgList()).select(DeckBean::getNumberEx).toList());
        numberList.addAll(stream(mDeckManager.getUgList()).select(DeckBean::getNumberEx).toList());
        numberList.addAll(stream(mDeckManager.getExList()).select(DeckBean::getNumberEx).toList());
        numberList.addAll(stream(mDeckManager.getPlayerList()).select(DeckBean::getNumberEx).toList());
        String numberListJson = JsonUtils.serializer(numberList);
        String deckPath       = DeckUtils.getDeckPath(mDeckManager.getDeckName());
        return FileUtils.writeFile(numberListJson, deckPath);
    }
}
