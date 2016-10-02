package com.itheima.mobilesafe.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 短信工具
 * Created by billow on 2016/10/1.
 */

public class SmsUtil {

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
     *
     * @param context
     */
    public static void smsBackup(Context context, BackUpCallBack callBack) throws Exception {
        String encode = "UTF-8";
        File file = new File(Environment.getExternalStorageDirectory(), "backup.xml");
        FileOutputStream fos = new FileOutputStream(file);
        XmlSerializer sz = Xml.newSerializer();
        sz.setOutput(fos, encode);
        sz.startDocument(encode, true);
        sz.startTag(null, "smss");
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://sms/");
        Cursor cursor = resolver.query(uri, new String[] { "body", "address", "type", "date" }, null, null, null);
        int max = cursor.getCount();
        //短信最大数量
        callBack.beforeBackUp(max);
        int progress = 0;
        while (cursor.moveToNext()) {
            sz.startTag(null, "sms");
            //<![CDATA[&]]>
            sz.startTag(null, "body");
            String body = cursor.getString(0);
            sz.text("<![CDATA[" + body + "]]>");
            sz.endTag(null, "body");

            sz.startTag(null, "address");
            sz.text(cursor.getString(1));
            sz.endTag(null, "address");

            sz.startTag(null, "type");
            sz.text(cursor.getString(2));
            sz.endTag(null, "type");

            sz.startTag(null, "date");
            sz.text(cursor.getString(3));
            sz.endTag(null, "date");

            sz.endTag(null, "sms");
            progress++;
            //短信备份中改变进度条的ui
            callBack.onSmsBackUp(progress);
        }
        cursor.close();
        sz.endTag(null, "smss");
        sz.endDocument();
        fos.close();
    }
}




























