package com.itheima.mobilesafe.db;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by billow on 2016/9/4.
 */
public class DBUtils {

    /**
     * 复制号码归属地离线查询的数据库到指定的位置
     */
    public static void copyDB(Context context, String fileName) {
        try {
            String filePath = context.getFilesDir().getAbsolutePath();
            File file = new File(filePath, fileName);
            //如果文件存在，就不复制
            if (file.exists() && file.length() > 0) {
                return;
            }
            InputStream is = context.getAssets().open(fileName);
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            is.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
