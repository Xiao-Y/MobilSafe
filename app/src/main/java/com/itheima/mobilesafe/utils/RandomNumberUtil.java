package com.itheima.mobilesafe.utils;

/**
 * 生成随机数
 * Created by Administrator on 2016/9/22.
 */
public class RandomNumberUtil {

    /**
     * 生成指定长度的数据
     *
     * @param len
     */
    public static void getRandomNumber(int len) {
        int max = 0;
        int min = 0;
        System.out.print("产生随机数: ");
        for (int index = 0; index < len; index++) {
            int num = (int) (Math.random() * 100);// [0,99]之间的整数
            System.out.print(num + " , ");
            if (index == 0) {
                max = num;
                min = num;
            } else if (num > max) {
                max = num;
            } else if (num < min) {
                min = num;
            }
        }
        System.out.println("\nMax: " + max + "   Min: " + min);
    }
}
