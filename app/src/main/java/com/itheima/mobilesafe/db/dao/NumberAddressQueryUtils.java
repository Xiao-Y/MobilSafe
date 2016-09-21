package com.itheima.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.itheima.mobilesafe.utils.MyApplication;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 号码归属
 * Created by billow on 2016/9/4.
 */
public class NumberAddressQueryUtils {

    // private static String path = "//data/data/com.itheima.mobilesafe/files/address.db";

    /**
     * 查询号码归属地
     *
     * @param number
     * @return
     */
    public static String queryNumber(String number) {
        String address = number;
        Map<Integer, String> map = new HashMap<>();
        map.put(3, "匪警号码");
        map.put(4, "模拟器");
        map.put(5, "客服电话");
        map.put(7, "本地号码");
        map.put(8, "本地号码");

        // path 把address.db这个数据库拷贝到data/data/《包名》/files/address.db
        String path = MyApplication.getContext().getFilesDir().getAbsolutePath() + File.separator + "address.db";
        SQLiteDatabase database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        // 手机号码 13 14 15 16 18
        // 手机号码的正则表达式
        if (number.matches("^1[34568]\\d{9}$")) {
            // 手机号码
            Cursor cursor = database.rawQuery("select location from data2 where id = (select outkey from data1 where id = ?)",
                    new String[] { number.substring(0, 7) });
            while (cursor.moveToNext()) {
                String location = cursor.getString(0);
                address = location;
            }
            cursor.close();
        } else {
            if (number.length() > 10 && number.startsWith("0")) {
                // 010-59790386
                Cursor cursor = database.rawQuery("select location from data2 where area = ?",
                        new String[] { number.substring(1, 3) });
                while (cursor.moveToNext()) {
                    String location = cursor.getString(0);
                    address = location.substring(0, location.length() - 2);
                }
                cursor.close();
                // 0855-59790386
                cursor = database.rawQuery(
                        "select location from data2 where area = ?",
                        new String[] { number.substring(1, 4) });
                while (cursor.moveToNext()) {
                    String location = cursor.getString(0);
                    address = location.substring(0, location.length() - 2);
                }
            } else {
                address = map.get(number.length());
            }
        }
        return address;
    }
}
