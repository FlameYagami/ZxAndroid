package com.zx.uitls.database;

import android.database.Cursor;
import android.text.TextUtils;

import com.zx.R;
import com.zx.bean.CardBean;
import com.zx.bean.RestrictBean;
import com.zx.config.SQLitConst;
import com.zx.game.utils.RestrictUtils;

import java.util.ArrayList;
import java.util.List;

import static br.com.zbra.androidlinq.Linq.stream;
import static com.zx.config.MyApp.context;

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
            String Md5      = result.getString(result.getColumnIndex(ColumnMd5));
            String Type     = result.getString(result.getColumnIndex(ColumnType));
            String Camp     = result.getString(result.getColumnIndex(ColumnCamp));
            String Race     = result.getString(result.getColumnIndex(ColumnRace));
            String Sign     = result.getString(result.getColumnIndex(ColumnSign));
            String Rare     = result.getString(result.getColumnIndex(ColumnRare));
            String Pack     = result.getString(result.getColumnIndex(ColumnPack));
            String CName    = result.getString(result.getColumnIndex(ColumnCName));
            String JName    = result.getString(result.getColumnIndex(ColumnJName));
            String Illust   = result.getString(result.getColumnIndex(ColumnIllust));
            String Number   = result.getString(result.getColumnIndex(ColumnNumber));
            String Cost     = result.getString(result.getColumnIndex(ColumnCost));
            String Power    = result.getString(result.getColumnIndex(ColumnPower));
            String Ability  = result.getString(result.getColumnIndex(ColumnAbility));
            String Lines    = result.getString(result.getColumnIndex(ColumnLines));
            String Image    = result.getString(result.getColumnIndex(ColumnImage));
            String Restrict = String.valueOf(stream(RestrictUtils.getRestrictList()).firstOrDefault(bean -> bean.getMd5().equals(Md5), new RestrictBean()).getRestrict());
            Cost = TextUtils.isEmpty(Cost) || Cost.equals("0") ? context.getString(R.string.hyphen) : Cost;
            Power = TextUtils.isEmpty(Power) || Power.equals("0") ? context.getString(R.string.hyphen) : Power;
            CardBean cardBean = new CardBean(Md5, Type, Race, Camp, Sign, Rare, Pack, Restrict, CName, JName, Illust, Number, Cost, Power, Ability, Lines, Image);
            deviceList.add(cardBean);
        }
        result.close();
        DBManager.getInstance().closeDatabase();
        return deviceList;
    }
}
