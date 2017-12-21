package com.dab.zx.uitls.database;

/**
 * Created by 八神火焰 on 2017/1/9.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dab.zx.uitls.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 数据库操作类.
 */
class SQLiteHelper extends SQLiteOpenHelper {

    private final String TAG = SQLiteHelper.class.getSimpleName();

    /**
     * 构造方法.
     *
     * @param context 上下文环境.
     * @param name    数据库名称.
     * @param version 数据库版本号.
     */
    public SQLiteHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * 向数据库的指定表中插入数据.
     *
     * @param db      数据库对象.
     * @param table   表名.
     * @param columns 字段名.
     * @param values  数据值.
     * @return 若传入的字段名与插入值的长度不等则返回false, 否则执行成功则返回true.
     */
    public boolean insert(SQLiteDatabase db, String table, String[] columns, String[] values) {
        if (columns.length != values.length) // 判断传入的字段名数量与插入数据的数量是否相等
        {
            LogUtils.e(TAG, "columns != values");
            return false;
        }
        long result; // 添加语句影响的行数
        try {
            ContentValues contentValues = new ContentValues(); // 将插入值与对应字段放入ContentValues实例中
            for (int i = 0; i < columns.length; i++) {
                contentValues.put(columns[i], values[i]);
            }
            result = db.insert(table, null, contentValues); // 执行插入操作
        } catch (Exception e) {
            LogUtils.e(TAG, "insert->" + e.getMessage());
            return false;
        }
        return -1 != result;
    }


    /**
     * 删除数据库的指定表中的指定数据.
     *
     * @param db          数据库对象.
     * @param table       表名.
     * @param conditions  条件字段.
     * @param whereValues 条件值.
     */
    public boolean delete(SQLiteDatabase db, String table, String conditions, String[] whereValues) {
        int result; // 删除语句影响的行数
        try {
            result = db.delete(table, conditions, whereValues); //执行删除操作
        } catch (Exception e) {
            LogUtils.e(TAG, "delete->" + e.getMessage());
            return false;
        }
        return -1 != result;
    }


    /**
     * 修改数据库的指定表中的指定数据.
     *
     * @param table       表名.
     * @param titles      字段名.
     * @param values      数据值.
     * @param conditions  条件字段.
     * @param whereValues 条件值.
     * @return 若传入的字段名与插入值的长度不等则返回false, 否则执行成功则返回true.
     */
    public boolean update(SQLiteDatabase db, String table, String[] titles, String[] values, String conditions, String[] whereValues) {
        if (titles.length != values.length) {
            LogUtils.e(TAG, "columns != values");
            return false;
        }
        int result; // 更新语句影响的行数
        try {
            // 将插入值与对应字段放入ContentValues实例中
            ContentValues contentValues = new ContentValues();
            for (int i = 0; i != titles.length; i++) {
                contentValues.put(titles[i], values[i]);
            }
            result = db.update(table, contentValues, conditions, whereValues); // 执行修改操作
        } catch (Exception e) {
            LogUtils.e(TAG, "update->" + e.getMessage());
            return false;
        }
        return -1 != result;
    }

    /**
     * 删除数据库的指定表中的所有数据.
     *
     * @param db    数据库对象
     * @param table 表名
     */
    public boolean clear(SQLiteDatabase db, String table) {
        try {
            db.execSQL("delete from " + table);
        } catch (Exception e) {
            LogUtils.e(TAG, "clear->" + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 查询数据库的指定表中的指定数据.
     *
     * @param table         表名.
     * @param columns       查询字段.
     * @param selection     条件字段.
     * @param selectionArgs 条件值.
     * @param groupBy       分组名称.
     * @param having        分组条件.与groupBy配合使用.
     * @param orderBy       排序字段.
     * @param limit         分页.
     * @return 查询结果游标
     */
    public Cursor getCursor(SQLiteDatabase db, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        return db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit); // 执行查询操作.
    }

    /**
     * 查询数据库的指定表中的指定数据.
     *
     * @param sql 查询语句
     * @return 查询结果游标
     */
    public Cursor getCursor(SQLiteDatabase db, String sql) {
        return db.rawQuery(sql, null); // 执行查询操作.
    }

    /**
     * 查询数据库的指定表中的指定数据.
     *
     * @param table         表名.
     * @param columns       查询字段.
     * @param selection     条件字段.
     * @param selectionArgs 条件值.
     * @param groupBy       分组名称.
     * @param having        分组条件.与groupBy配合使用.
     * @param orderBy       排序字段.
     * @param limit         分页.
     * @return 查询的数据.
     */
    public ArrayList<HashMap<String, String>> select(SQLiteDatabase db, String table, String[] columns, String selection, String[] selectionArgs, String groupBy,
                                                     String having, String orderBy, String limit) {
        Cursor                             cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit); // 执行查询操作.
        int                                count  = cursor.getColumnCount();
        ArrayList<HashMap<String, String>> list   = new ArrayList<>();
        while (cursor.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            for (int i = 0; i < count; i++) {
                map.put(cursor.getColumnName(i), cursor.getString(i));
            }
            list.add(map);
        }
        cursor.close();
        return list;
    }

    /**
     * 事务处理,调用OperationTransaction接口中的executeTransaction的方法根据返回判断事务是否执行成功.
     * 若事务执行成功则进行数据提交,否则进行滚回操作.
     *
     * @param ot 操作数据库事务对象.
     * @return 若事务执行成功则返回true, 否则滚回返回false.
     */
    public boolean transaction(SQLiteDatabase db, OnTransaction ot) {
        db.beginTransaction(); // 开始事务.
        boolean isSuccess = ot.executeTransaction(db);
        if (isSuccess) {
            db.setTransactionSuccessful(); // 设置事务处理成功,不设置会自动回滚不提交.
        }
        db.endTransaction(); // 事务结束.
        return isSuccess;
    }

    /**
     * 事务操作接口.
     * 提供数据库事务操作接口.
     */
    interface OnTransaction {

        /**
         * 执行事务.
         *
         * @param db 数据库操作对象.
         * @return 若事务执行完成并成功返回true, 若失败则返回false.
         */
        boolean executeTransaction(SQLiteDatabase db);

    }
}