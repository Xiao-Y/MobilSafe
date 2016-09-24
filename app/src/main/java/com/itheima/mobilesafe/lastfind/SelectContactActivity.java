package com.itheima.mobilesafe.lastfind;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.adapter.ContactAdapter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 读取联系人
 * <p/>
 * Created by billow on 2016/8/21.
 */
public class SelectContactActivity extends Activity {

    /**
     * 联系人
     */
    public final static String CONTACTS_NAME = "contactsName";
    /**
     * 电话号码
     */
    public final static String PHONE_NUMBER = "phoneNumber";
    /**
     * 头像
     */
    public final static String CONTACT_PHOTO = "contactPhoto";

    /**
     * 联系人显示名称
     **/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;

    /**
     * 电话号码
     **/
    private static final int PHONES_NUMBER_INDEX = 1;

    /**
     * 头像ID
     **/
    private static final int PHONES_PHOTO_ID_INDEX = 2;

    /**
     * 联系人的ID
     **/
    private static final int PHONES_CONTACT_ID_INDEX = 3;

    /**
     * 获取库Phon表字段
     */
    String[] PHONES_PROJECTION = new String[] {
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Photo.PHOTO_ID,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID };

    private ListView listSelectContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_select_contact);
        listSelectContact = (ListView) findViewById(R.id.list_select_view);
        //List<Map<String, String>> data = getContactInfo();
        //        listSelectContact.setAdapter(new SimpleAdapter(this, data, R.layout.contact_item_view,
        //                new String[] { "name", "phone" }, new int[] { R.id.tv_name, R.id.tv_phone }));
        final Map<String, Object> map = this.getContactInfo();
        listSelectContact.setAdapter(new ContactAdapter(this, map));
        listSelectContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                List<String> phoneNumberList = (List<String>) map.get(SelectContactActivity.PHONE_NUMBER);
                List<String> contactsNameList = (List<String>) map.get(SelectContactActivity.CONTACTS_NAME);
                String phone = phoneNumberList.get(i);
                String displayName = contactsNameList.get(i);
                Intent intent = new Intent();
                intent.putExtra("phone", phone);
                intent.putExtra("name", displayName);
                //返回给上一个页面
                setResult(0, intent);
                finish();
            }
        });
    }

    /**
     * 读取通讯录数据
     *
     * @return 适配器数据
     */
    public Map<String, Object> getContactInfo() {
        //适配器数据
        Map<String, Object> map = new HashMap<>();
        //联系人
        List<String> contactsNameList = new ArrayList<>();
        //电话号码
        List<String> phoneNumberList = new ArrayList<>();
        //头像
        List<Bitmap> contactPhotoList = new ArrayList<>();

        //获取内容解析器
        ContentResolver resolver = this.getContentResolver();
        //联系人表
        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                PHONES_PROJECTION, null, null, null);
        while (phoneCursor.moveToNext()) {
            //得到手机号码
            String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
            if (TextUtils.isEmpty(phoneNumber)) {
                continue;
            }
            phoneNumberList.add(phoneNumber);
            //得到联系人名称
            String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
            contactsNameList.add(contactName);

            //得到联系人ID
            long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);
            //得到联系人头像ID
            long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);
            //得到联系人头像Bitamp
            Bitmap contactPhoto;
            //photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
            if (photoid > 0) {
                Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactid);
                InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);
                contactPhoto = BitmapFactory.decodeStream(input);
            } else {
                contactPhoto = BitmapFactory.decodeResource(getResources(), R.drawable.contact_photo);
            }
            contactPhotoList.add(contactPhoto);
        }
        //切记：一定要关闭游标集
        phoneCursor.close();
        map.put(CONTACTS_NAME, contactsNameList);
        map.put(PHONE_NUMBER, phoneNumberList);
        map.put(CONTACT_PHOTO, contactPhotoList);
        return map;
    }
}
