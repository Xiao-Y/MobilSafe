package com.itheima.mobilesafe.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import java.io.IOException;
import java.io.InputStream;

/**
 * 用于手机定位
 * Created by billow on 2016/8/27.
 */
public class GPSService extends Service {

    //位置服务管理器
    LocationManager lm;
    //定位方式
    String provider = null;

    SharedPreferences sp;

    @Override
    public void onCreate() {
        super.onCreate();
        sp = getSharedPreferences("config", MODE_PRIVATE);
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        //选取定位方式的查询条件
        Criteria criteria = new Criteria();
        //设置定义的精确度
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //选取一个最优的可用的定位方式
        provider = lm.getBestProvider(criteria, true);
        //android高版本动态受权
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //注册监听位置服务
        lm.requestLocationUpdates(provider, 0L, 0F, listener);
    }

    LocationListener listener = new LocationListener() {

        /**
         * 位置改变时
         *
         * @param location
         */
        @Override
        public void onLocationChanged(Location location) {
            //经度
            String latitude = null;
            //纬度
            String longitude = null;
            //精度
            String accuracy = "a:" + location.getAccuracy();
            //把标准的GPS坐标转换成火星坐标
            try {
                InputStream in = getAssets().open("axisoffset.dat");
                ModifyOffset offset = ModifyOffset.getInstance(in);
                PointDouble pointDouble = offset.s2c(new PointDouble(location.getLatitude(), location.getLongitude()));
                latitude = "j:" + pointDouble.x + "\n";
                longitude = "w:" + pointDouble.y + "\n";
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //保存最后一次位置的变化
            SharedPreferences.Editor edit = sp.edit();
            edit.putString("lastlocation", latitude + longitude + accuracy);
            edit.commit();
        }

        /**
         * 定位状态改变时
         *
         * @param s
         */
        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        /**
         * 定位方式可用时
         *
         * @param s
         */
        @Override
        public void onProviderEnabled(String s) {

        }

        /**
         * 定位方式禁用时
         *
         * @param s
         */
        @Override
        public void onProviderDisabled(String s) {

        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //android高版本动态受权
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (lm != null) {
            //取消监听位置服务
            lm.removeUpdates(listener);
            listener = null;
        }
    }
}
