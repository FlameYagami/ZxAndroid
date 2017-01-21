package com.zx.uitls.database;

import android.text.TextUtils;

import com.zx.R;
import com.zx.bean.CardBean;
import com.zx.bean.KeySearchBean;
import com.zx.config.MapConst;
import com.zx.config.SpConst;
import com.zx.config.SQLitConst;
import com.zx.uitls.LogUtils;
import com.zx.uitls.SpUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static br.com.zbra.androidlinq.Linq.stream;
import static com.zx.config.MyApp.context;

/**
 * Created by 八神火焰 on 2016/12/13.
 */

public class SqlUtils implements SQLitConst
{
    private static final String TAG = SqlUtils.class.getSimpleName();

    public static String getKeyQuerySql(String key) {
        StringBuilder builder = new StringBuilder();
        builder.append(getHeaderSql());// 基础查询语句
        builder.append(SqlUtils.getAllKeySql(key)); // 关键字
        builder.append(getFooterSql()); // 排序
        LogUtils.i(TAG, builder.toString());
        return builder.toString(); // 完整的查询语句
    }

    public static String getPackQuerySql(String key) {
        StringBuilder builder = new StringBuilder();
        builder.append(getHeaderSql());// 基础查询语句
        builder.append(SqlUtils.getSimilarSql(key, ColumnPack)); // 关键字
        builder.append(getFooterSql()); // 排序
        LogUtils.i(TAG, builder.toString());
        return builder.toString(); // 完整的查询语句
    }

    public static String getQuerySql(CardBean cardBean) {
        StringBuilder builder = new StringBuilder();
        builder.append(getHeaderSql());// 基础查询语句
        builder.append(SqlUtils.getAllKeySql(cardBean.getKey())); // 关键字
        builder.append(SqlUtils.getAccurateSql(cardBean.getType(), ColumnType)); // 种类
        builder.append(SqlUtils.getSimilarSql(cardBean.getCamp(), ColumnCamp)); // 阵营
        builder.append(SqlUtils.getAccurateSql(cardBean.getRace(), ColumnRace)); // 种族
        builder.append(SqlUtils.getAccurateSql(cardBean.getSign(), ColumnSign)); // 标记
        builder.append(SqlUtils.getAccurateSql(cardBean.getRare(), ColumnRare)); // 罕贵
        builder.append(SqlUtils.getAccurateSql(cardBean.getIllust(), ColumnIllust)); // 画师
        builder.append(SqlUtils.getPackSql(cardBean.getPack(), ColumnPack)); // 卡包
        builder.append(SqlUtils.getIntervalSql(cardBean.getCost(), ColumnCost)); // 费用
        builder.append(SqlUtils.getIntervalSql(cardBean.getPower(), ColumnPower)); // 力量
        builder.append(SqlUtils.getAccurateSql(cardBean.getRestrict(), ColumnRestrict)); // 限制
        builder.append(cardBean.getAbilityType()); //  能力类型
        builder.append(cardBean.getAbilityDetail()); // 详细能力
        builder.append(getFooterSql()); // 排序
        LogUtils.i(TAG, builder.toString());
        return builder.toString(); // 完整的查询语句
    }

    public static String getQueryAllSql()
    {
        return "SELECT * FROM " + TableName + " ORDER BY " + ColumnNumber + " ASC";
    }

    /**
     * 获取头部查询语句
     *
     * @return sql
     */
    private static String getHeaderSql()
    {
        return "SELECT * FROM " + TableName + " WHERE 1=1";
    }

    /**
     * 获取尾部查询语句
     *
     * @return sql
     */
    private static String getFooterSql() {
        return SpUtil.getInstances().getString(SpConst.OrderPattern).equals(context.getString(R.string.order_by_number)) ? getOrderNumberSql() : getOrderValueSql();
    }

    /**
     * 获取卡编排序方式查询语句
     *
     * @return sql
     */
    private static String getOrderNumberSql() {
        return " ORDER BY " + ColumnNumber + " ASC";
    }

    /**
     * 获取数值排序方式查询语句
     *
     * @return sql
     */
    private static String getOrderValueSql() {
        return " ORDER BY " + ColumnCamp + " DESC," + ColumnType + " DESC," +
                ColumnCName + " DESC," + ColumnRace + " ASC," + ColumnCost + " DESC," + ColumnPower + " DESC";
    }

    /**
     * 获取精确查询语句
     *
     * @param value  查询的值
     * @param column 数据库字段
     * @return sql
     */
    private static String getAccurateSql(String value, String column) {
        return !context.getString(R.string.not_applicable).equals(value) ? " AND " + column + "='" + value + "'" : "";
    }

    /**
     * 获取模糊查询语句
     *
     * @param column 数据库字段
     * @param value  查询的值
     * @return sql
     */
    public static String getSimilarSql(String value, String column) {
        return TextUtils.isEmpty(value) || context.getString(R.string.not_applicable).equals(value) ? "" : " AND " + column + " LIKE '%" + value + "%'";
    }

    /**
     * 获取数值查询语句（适用范围:费用、力量）
     *
     * @param value  查询的值
     * @param column 数据库字段
     * @return sql
     */
    private static String getIntervalSql(String value, String column) {
        return !TextUtils.isEmpty(value)
                ? (value.contains(context.getString(R.string.hyphen))
                ? " AND " + column + ">=" + value.split("-")[0] + " AND " + column + "<=" + value.split("-")[1]
                : " AND " + column + "=" + value)
                : "";
    }

    /**
     * 获取卡包查询语句
     *
     * @param value  查询的值
     * @param column 数据库字段
     * @return sql
     */
    private static String getPackSql(String value, String column) {
        if (context.getString(R.string.not_applicable).equals(value)) {
            return "";
        }
        value = value.contains(context.getString(R.string.series)) ? value.substring(0, 1) : value;
        return " AND " + column + " LIKE '%" + value + "%'";
    }

    /**
     * 获取卡片能力类型语句
     *
     * @param checkboxMap checkbox对应的Map
     * @return sql
     */
    public static String getAbilityTypeSql(LinkedHashMap<String, Boolean> checkboxMap) {
        StringBuilder sql         = new StringBuilder();
        List<String>  tempKeyList = stream(checkboxMap).where(Map.Entry::getValue).select(Map.Entry::getKey).toList();
        for (String tempKey : tempKeyList) {
            String ability = stream(MapConst.AbilityTypeMap).first(entry -> entry.getKey().equals(tempKey)).getValue();
            sql.append(" AND " + ColumnAbility + " LIKE '%" + ability + "%'");
        }
        return sql.toString();
    }

    /**
     * 获取卡片能力分类语句
     *
     * @param checkboxMap checkbox对应的Map
     * @return sql
     */
    public static String getAbilityDetailSql(LinkedHashMap<String, Boolean> checkboxMap) {
        StringBuilder sql         = new StringBuilder();
        List<String>  tempKeyList = stream(checkboxMap).where(Map.Entry::getValue).select(Map.Entry::getKey).toList();
        for (String tempKey : tempKeyList) {
            String ability = stream(MapConst.AbilityDetailMap).first(entry -> entry.getKey().equals(tempKey)).getKey();
            sql.append(" AND " + ColumnAbilityDetail + " LIKE '%\"" + ability + "\":1%'");
        }
        return sql.toString();
    }

    /**
     * 获取关键字段取值
     *
     * @param value 查询的值
     * @return sql
     */
    private static String getAllKeySql(String value) {
        if (TextUtils.isEmpty(value)) {
            return "";
        }
        StringBuilder tempValue = new StringBuilder();
        String[]      keyList   = value.split(" "); // 以空格分割关键字
        for (String key : keyList) {
            tempValue.append(" AND ( JName LIKE '%" + key + "%' " + getPartKeySql(key) + ")");
        }
        return tempValue.toString();
    }

    private static String getPartKeySql(String value) {
        StringBuilder tempValue = new StringBuilder();
        for (String keySearch : KeySearchBean.getSelectKeySearchList()) {
            String column = stream(MapConst.KeySearchMap).first(entry -> keySearch.equals(entry.getKey())).getValue();
            tempValue.append(" OR " + column + " LIKE '%" + value + "%'");
        }
        return tempValue.toString();
    }
}
