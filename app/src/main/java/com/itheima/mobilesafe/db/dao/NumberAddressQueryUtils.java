package com.itheima.mobilesafe.db.dao;

/**
 * 号码归属
 * Created by billow on 2016/9/4.
 */
public class NumberAddressQueryUtils {

    private static String path = "/data/data/com.itheimamobilesafe/files/address.db";

    /**
     * 查询号码归属地
     *
     * @param number
     * @return
     */
    public static String queryNumber(String number) {
        String address = number;
        //String sql = "select r.location from data2 r where r.id = (select a.outkey from data1 a where a.id = ?)";
        //        SQLiteDatabase database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        //        Cursor cursor = database.rawQuery(sql, new String[] { number.substring(0, 7) });
        //        while (cursor.moveToNext()) {
        //            String location = cursor.getString(0);
        //            address = location;
        //        }
        //        cursor.close();
        return address;
    }
}
