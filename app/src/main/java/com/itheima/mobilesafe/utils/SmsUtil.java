package com.itheima.mobilesafe.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 短信工具
 * Created by billow on 2016/10/1.
 */

public class SmsUtil {

    static String encode = "UTF-8";
    static File file = new File(Environment.getExternalStorageDirectory(), "backup.xml");
    static Uri uri = Uri.parse("content://sms/");
    static ContentResolver resolver = MyApplication.getContext().getContentResolver();

    /**
     * 短信备份接口
     */
    public interface BackUpCallBack {

        /**
         * 短信备份前
         *
         * @param max 短信最大数量
         */
        void beforeBackUp(int max);

        /**
         * 短信备份中改变进度条的ui
         *
         * @param progress
         */
        void onSmsBackUp(int progress);

    }

    /**
     * 短信备份
     */
    public static void smsBackup(BackUpCallBack callBack) throws Exception {
        FileOutputStream fos = new FileOutputStream(file);
        XmlSerializer serializer = Xml.newSerializer();
        serializer.setOutput(fos, encode);
        serializer.startDocument(encode, true);
        serializer.startTag(null, "smss");
        Cursor cursor = resolver.query(uri, new String[] { "body", "address", "type", "date" }, null, null, null);
        int max = cursor.getCount();
        serializer.attribute(null, "max", max + "");
        //短信最大数量
        callBack.beforeBackUp(max);
        int progress = 0;
        while (cursor.moveToNext()) {
            serializer.startTag(null, "sms");
            //<![CDATA[&]]>
            serializer.startTag(null, "body");
            String body = cursor.getString(0);
            serializer.text("<![CDATA[" + body + "]]>");
            serializer.endTag(null, "body");

            serializer.startTag(null, "address");
            serializer.text(cursor.getString(1));
            serializer.endTag(null, "address");

            serializer.startTag(null, "type");
            serializer.text(cursor.getString(2));
            serializer.endTag(null, "type");

            serializer.startTag(null, "date");
            serializer.text(cursor.getString(3));
            serializer.endTag(null, "date");

            serializer.endTag(null, "sms");
            progress++;
            //短信备份中改变进度条的ui
            callBack.onSmsBackUp(progress);
        }
        cursor.close();
        serializer.endTag(null, "smss");
        serializer.endDocument();
        fos.close();
    }

    /**
     * 短信还原
     *
     * @param flag 是否清除原来的短信
     */
    public static void smsRestore(boolean flag, BackUpCallBack callBack) throws Exception {
        //清除数据库中所有短信
        if (flag) {
            resolver.delete(uri, null, null);
        }
        List<ContentValues> values = null;
        ContentValues value = null;
        FileInputStream is = new FileInputStream(file);
        XmlPullParser pullParser = Xml.newPullParser();
        pullParser.setInput(is, encode);
        int max = 0;
        int eventType = pullParser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    max = Integer.parseInt(pullParser.getAttributeValue(null, "max"));
                    values = new ArrayList<>(max);
                    break;
                case XmlPullParser.START_TAG:
                    String name = pullParser.getName();
                    if (name.equalsIgnoreCase("body")) {
                        value = new ContentValues();
                        value.put("body", pullParser.getText().trim());
                    } else if (name.equalsIgnoreCase("address")) {
                        value = new ContentValues();
                        value.put("address", pullParser.getText().trim());
                    } else if (name.equalsIgnoreCase("type")) {
                        value = new ContentValues();
                        value.put("type", pullParser.getText().trim());
                    } else if (name.equalsIgnoreCase("date")) {
                        value = new ContentValues();
                        value.put("date", pullParser.getText().trim());
                    }
                case XmlPullParser.END_TAG:
                    name = pullParser.getName();
                    if (name.equalsIgnoreCase("sms")) {
                        values.add(value);
                    }
                    break;
            }
            eventType = pullParser.next();
        }
        is.close();

        //用于显示短信还原进度
        callBack.beforeBackUp(max);
        if (values != null && values.size() > 0) {
            for (int i = 0; i < max; i++) {
                ContentValues v = values.get(i);
                //插入数据到数据库
                resolver.insert(uri, v);
                callBack.onSmsBackUp(i);
            }
        }
    }
}




























