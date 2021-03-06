package com.itheima.mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.itheima.mobilesafe.db.BlackNumberDBOpenHelper;
import com.itheima.mobilesafe.domain.BlackNumberInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 黑名单表操作
 * Created by billow on 2016/9/21.
 */
public class BlackNumberDao {

    private BlackNumberDBOpenHelper blackNumberDBOpenHelper;
    private SQLiteDatabase db;
    private static Map<String, String> map;

    public BlackNumberDao(Context context) {
        blackNumberDBOpenHelper = new BlackNumberDBOpenHelper(context);
        map = new HashMap<>();
        map.put("1", "来电拦截");
        map.put("2", "短信拦截");
        map.put("3", "拦截所有");
    }

    /**
     * 通过key获取拦截模式
     *
     * @param key
     * @return
     */
    public static String getModeByKey(String key) {
        return map.get(key);
    }

    /**
     * 查询号码是否存在
     *
     * @param number
     * @return
     */
    public boolean find(String number) throws Exception {
        boolean result = false;
        db = blackNumberDBOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from blackNumber where number = ?", new String[] { number });
        if (cursor.moveToNext()) {
            result = true;
        }
        cursor.close();
        db.close();
        return result;
    }

    /**
     * 通过号码查询拦截模式
     *
     * @param number
     * @return
     * @throws Exception
     */
    public String findMode(String number) throws Exception {
        String result = null;
        SQLiteDatabase db = blackNumberDBOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select mode from blackNumber where number = ?", new String[] { number });
        if (cursor.moveToNext()) {
            result = map.get(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return result;
    }

    /**
     * 查询所有的黑名单
     *
     * @return
     * @throws Exception
     */
    public List<BlackNumberInfo> findAll() throws Exception {
        //Thread.sleep(4000);
        List<BlackNumberInfo> list = new ArrayList<>();
        SQLiteDatabase db = blackNumberDBOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select number, mode, displayName from blackNumber order by _id desc ", null);
        while (cursor.moveToNext()) {
            BlackNumberInfo info = new BlackNumberInfo();
            info.setNumber(cursor.getString(0));
            info.setMode(map.get(cursor.getString(1)));
            info.setDisplayName(cursor.getString(2));
            list.add(info);
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * 添加黑名单
     *
     * @param number
     * @param mode   拦截模式 1.电话拦截 2.短信拦截 3.全部拦截
     */
    public void add(String number, String mode) {
        db = blackNumberDBOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("number", number);
        values.put("mode", mode);
        db.insert("blackNumber", null, values);
        db.close();
    }

    /**
     * 添加黑名单
     *
     * @param displayName
     * @param number
     * @param mode        拦截模式 1.电话拦截 2.短信拦截 3.全部拦截
     */
    public void add(String displayName, String number, String mode) {
        db = blackNumberDBOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("number", number);
        values.put("mode", mode);
        values.put("displayName", displayName);
        db.insert("blackNumber", null, values);
        db.close();
    }

    /**
     * 根据电话号删除黑名单
     *
     * @param number
     */
    public void delete(String number) {
        db = blackNumberDBOpenHelper.getWritableDatabase();
        db.delete("blackNumber", "number = ?", new String[] { number });
        db.close();
    }

    /**
     * 根据电话号码更新拦截模式
     *
     * @param number
     * @param newmode 拦截模式 1.电话拦截 2.短信拦截 3.全部拦截
     */
    public void update(String number, String newmode) {
        db = blackNumberDBOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mode", newmode);
        db.update("blackNumber", values, "number = ?", new String[] { number });
        db.close();
    }

    /**
     * 清空黑名单
     */
    public void deleteAll() {
        db = blackNumberDBOpenHelper.getWritableDatabase();
        db.execSQL("delete from blackNumber");
        db.close();
    }


    /**
     * 查询部分的黑名单
     *
     * @param offset    从那个地方获取
     * @param maxnumber 最多获取多少个
     * @return
     * @throws Exception
     */
    public List<BlackNumberInfo> findPart(int offset, int maxnumber) throws Exception {
        //Thread.sleep(4000);
        List<BlackNumberInfo> list = new ArrayList<>();
        SQLiteDatabase db = blackNumberDBOpenHelper.getReadableDatabase();
        //limit maxnumber offset offset 相当于 limit offset , maxnumber
        Cursor cursor = db.rawQuery("select number, mode, displayName from blackNumber order by _id desc limit ? offset ? ",
                new String[] { String.valueOf(maxnumber), String.valueOf(offset) });
        while (cursor.moveToNext()) {
            BlackNumberInfo info = new BlackNumberInfo();
            info.setNumber(cursor.getString(0));
            info.setMode(map.get(cursor.getString(1)));
            info.setDisplayName(cursor.getString(2));
            list.add(info);
        }
        cursor.close();
        db.close();
        return list;
    }
}
