package com.itheima.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itheima.mobilesafe.R;


/**
 * 自定义的组合控件，它里面有两个TextView ，还有一个ImageView,还有一个View
 *
 * @author XiaoY
 * @date: 2016年8月19日 下午10:54:09
 */
public class SettingClickView extends RelativeLayout {

    public final static String ITHEIMA_SCHEMAS = "http://schemas.android.com/apk/res-auto";

    private TextView tv_title;
    private TextView tv_desc;

    //选种显示的信息
    private String desc_on;
    //未选种显示的信息
    private String desc_off;

    public SettingClickView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        iniView(context);
    }

    public SettingClickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        iniView(context);
        this.initInfo(attrs);
    }

    public SettingClickView(Context context) {
        super(context);
        iniView(context);
    }

    /**
     * 初始化布局文件
     *
     * @param context
     */
    private void iniView(Context context) {
        // 把一个布局文件---》View 并且加载在SettingItemView
        View.inflate(context, R.layout.setting_click_view, this);
        tv_desc = (TextView) this.findViewById(R.id.tv_desc);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
    }

    /**
     * 获取自定义的标签的属性
     *
     * @param attrs
     */
    private void initInfo(AttributeSet attrs) {
        String title = attrs.getAttributeValue(ITHEIMA_SCHEMAS, "title");
        tv_title.setText(title);
        desc_on = attrs.getAttributeValue(ITHEIMA_SCHEMAS, "desc_on");
        desc_off = attrs.getAttributeValue(ITHEIMA_SCHEMAS, "desc_off");
        setDesc(desc_off);
    }

    /**
     * 设置 组合控件的描述信息
     */
    public void setDesc(String text) {
        tv_desc.setText(text);
    }

    /**
     * 设置组合控件的标题
     *
     * @param title
     */
    public void setTitle(String title) {
        tv_title.setText(title);
    }
}
