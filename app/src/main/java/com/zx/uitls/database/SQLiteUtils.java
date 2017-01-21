package com.zx.uitls.database;

import android.database.Cursor;

import com.zx.bean.CardBean;
import com.zx.config.SQLitConst;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/30.
 */
public class SQLiteUtils implements SQLitConst
{
    private static final String TAG = SQLiteUtils.class.getSimpleName();

    private static List<CardBean> allCardList = new ArrayList<>();

    /**
     * 返回设备数据集合
     *
     * @return DeviceList
     */
    public static List<CardBean> getAllCardList() {
        if (allCardList.size() == 0) {
            allCardList = getCardList(SqlUtils.getQueryAllSql());
        }
        return allCardList;
    }

    /**
     * 返回设备数据集合
     *
     * @return DeviceList
     */
    public static List<CardBean> getCardList(String sql) {
        SQLiteHelper   helper     = DBManager.getInstance().getSQLiteHelper();
        List<CardBean> deviceList = new ArrayList<>();
        Cursor         result     = helper.getCursor(DBManager.getInstance().openDatabase(), sql);
        while (result.moveToNext()) {
            String Type     = result.getString(result.getColumnIndex(ColumnType));
            String Camp     = result.getString(result.getColumnIndex(ColumnCamp));
            String Race     = result.getString(result.getColumnIndex(ColumnRace));
            String Sign     = result.getString(result.getColumnIndex(ColumnSign));
            String Rare     = result.getString(result.getColumnIndex(ColumnRare));
            String Pack     = result.getString(result.getColumnIndex(ColumnPack));
            String Restrict = result.getString(result.getColumnIndex(ColumnRestrict));
            String CName    = result.getString(result.getColumnIndex(ColumnCName));
            String JName    = result.getString(result.getColumnIndex(ColumnJName));
            String Illust   = result.getString(result.getColumnIndex(ColumnIllust));
            String Number   = result.getString(result.getColumnIndex(ColumnNumber));
            String Cost     = result.getString(result.getColumnIndex(ColumnCost));
            String Power    = result.getString(result.getColumnIndex(ColumnPower));
            String Ability  = result.getString(result.getColumnIndex(ColumnAbility));
            String Lines    = result.getString(result.getColumnIndex(ColumnLines));
            String Faq      = result.getString(result.getColumnIndex(ColumnFaq));
            String Image    = result.getString(result.getColumnIndex(ColumnImage));

            CardBean cardBean = new CardBean(Type, Race, Camp, Sign, Rare, Pack, Restrict, CName, JName, Illust, Number, Cost, Power, Ability, Lines, Faq, Image);
            deviceList.add(cardBean);
        }
        result.close();
        DBManager.getInstance().closeDatabase();
        return deviceList;
    }
}
