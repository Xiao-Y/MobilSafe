package com.itheima.mobilesafe.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.lastfind.SelectContactActivity;

import java.util.List;
import java.util.Map;

/**
 * 显示联系人适配器
 * Created by billow on 2016/8/22.
 */
public class ContactAdapter extends BaseAdapter {

    public Context context;
    public Map<String, Object> map;
    /**
     * 联系人
     */
    public List<String> contactsNameList;
    /**
     * 电话号码
     */
    public List<String> phoneNumberList;
    /**
     * 头像
     */
    public List<Bitmap> contactPhotoList;
    /**
     * 绘图的数量
     */
    public int count = 0;

    public ContactAdapter(Context context, Map<String, Object> map) {
        this.context = context;
        this.map = map;
        if (!map.isEmpty()) {
            contactsNameList = (List<String>) map.get(SelectContactActivity.CONTACTS_NAME);
            phoneNumberList = (List<String>) map.get(SelectContactActivity.PHONE_NUMBER);
            contactPhotoList = (List<Bitmap>) map.get(SelectContactActivity.CONTACT_PHOTO);
            count = contactsNameList.size();
        }
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView = null;
        TextView title = null;
        TextView text = null;
        if (view == null || i < count) {
            //为每一条数据加载布局
            view = LayoutInflater.from(context).inflate(R.layout.contact_item_view, null);
            imageView = (ImageView) view.findViewById(R.id.color_image);
            title = (TextView) view.findViewById(R.id.color_title);
            text = (TextView) view.findViewById(R.id.color_text);
        }
        imageView.setImageBitmap(contactPhotoList.get(i));
        title.setText(contactsNameList.get(i));
        text.setText(phoneNumberList.get(i));
        return view;
    }
}
