package com.itheima.mobilesafe;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.itheima.mobilesafe.db.BlackNumberDBOpenHelper;
import com.itheima.mobilesafe.db.dao.BlackNumberDao;
import com.itheima.mobilesafe.domain.BlackNumberInfo;

import java.util.List;
import java.util.Random;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    public ApplicationTest() {
        super(Application.class);
    }

    public void testCreateDB() {
        BlackNumberDBOpenHelper db = new BlackNumberDBOpenHelper(getContext());
        db.getWritableDatabase();
    }

    public void testAdd() {
        BlackNumberDao dao = new BlackNumberDao(getContext());
        long basenumber = 13500000000l;
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            dao.add(String.valueOf(basenumber + i), String.valueOf(random.nextInt(3) + 1));
        }
    }

    public void testFind() throws Exception {
        BlackNumberDao dao = new BlackNumberDao(getContext());
        boolean b = dao.find("123456789");
        System.out.println(b);
    }

    public void testFindMode() throws Exception {
        BlackNumberDao dao = new BlackNumberDao(getContext());
        String mode = dao.findMode("123456789");
        System.out.println(mode);
    }

    public void testFindAll() throws Exception {
        BlackNumberDao dao = new BlackNumberDao(getContext());
        List<BlackNumberInfo> all = dao.findAll();
        for (BlackNumberInfo info : all) {
            System.out.println(info);
        }
    }

    public void testUpdate() throws Exception {
        BlackNumberDao dao = new BlackNumberDao(getContext());
        dao.update("123456789", "3");
        String mode = dao.findMode("123456789");
        System.out.println(mode);
    }

    public void testDelete() throws Exception {
        BlackNumberDao dao = new BlackNumberDao(getContext());
        dao.delete("123456789");
        boolean b = dao.find("123456789");
        System.out.println(b);
    }

    public void testDeleteAll() {
        BlackNumberDao dao = new BlackNumberDao(getContext());
        dao.deleteAll();
    }
}