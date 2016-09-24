package com.itheima.mobilesafe.callsafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.db.dao.BlackNumberDao;
import com.itheima.mobilesafe.domain.BlackNumberInfo;
import com.itheima.mobilesafe.lastfind.SelectContactActivity;
import com.itheima.mobilesafe.ui.ToastUtils;

import java.util.List;

/**
 * 通讯卫士
 * Created by billow on 2016/9/21.
 */
public class CallSmsSafeActivity extends Activity {

    private ListView lvCallSmsSafe;
    private EditText etBalckNumber;
    private EditText etBlackName;
    private Button tb_ok;
    private Button tb_cancel;
    private CheckBox cb_phone;
    private CheckBox cb_sms;
    private MyAdapter adapter;
    private List<BlackNumberInfo> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_call_sms_safe);
        lvCallSmsSafe = (ListView) findViewById(R.id.lv_call_sms_safe);
        try {
            BlackNumberDao dao = new BlackNumberDao(this);
            list = dao.findAll();
            adapter = new MyAdapter(this, list);
            lvCallSmsSafe.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class MyAdapter extends BaseAdapter {

        private Context context;
        private List<BlackNumberInfo> list;
        private ViewHolder holder;

        public MyAdapter(Context context, List<BlackNumberInfo> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public BlackNumberInfo getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.list_item_black_number, null);
                holder = new ViewHolder();
                holder.blackNumber = (TextView) view.findViewById(R.id.tv_number);
                holder.mode = (TextView) view.findViewById(R.id.tv_mode);
                holder.blackName = (TextView) view.findViewById(R.id.tv_displayName);
                holder.deleteBlackNumber = (ImageView) view.findViewById(R.id.iv_delete);
                view.setTag(holder);
            }
            holder = (ViewHolder) view.getTag();
            final BlackNumberInfo info = getItem(position);
            holder.blackNumber.setText(info.getNumber());
            holder.mode.setText(info.getMode());
            holder.blackName.setText(info.getDisplayName());
            holder.deleteBlackNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CallSmsSafeActivity.this);
                    builder.setTitle("警告");
                    builder.setMessage("确定要删除这条记录么？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            list.remove(position);
                            BlackNumberDao dao = new BlackNumberDao(getApplicationContext());
                            dao.delete(info.getNumber());
                            adapter.notifyDataSetInvalidated();
                        }
                    });
                    builder.setNegativeButton("取消", null);
                    builder.show();
                }
            });
            return view;
        }
    }

    class ViewHolder {
        TextView blackNumber;
        TextView mode;
        TextView blackName;
        ImageView deleteBlackNumber;
    }

    /**
     * 添加黑名单
     */
    public void addBlackNumber(View view1) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_add_blacknumber, null);
        alertDialog.setView(view, 0, 0, 0, 0);
        alertDialog.show();
        etBalckNumber = (EditText) view.findViewById(R.id.et_blackNumber);
        etBlackName = (EditText) view.findViewById(R.id.et_blackName);
        tb_ok = (Button) view.findViewById(R.id.ok);
        tb_cancel = (Button) view.findViewById(R.id.cancel);
        cb_phone = (CheckBox) view.findViewById(R.id.cb_phone);
        cb_sms = (CheckBox) view.findViewById(R.id.cb_sms);

        tb_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        tb_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String balckNumber = etBalckNumber.getText().toString();
                String blackName = etBlackName.getText().toString();
                boolean cbPhone = cb_phone.isChecked();
                boolean cbSms = cb_sms.isChecked();
                if (TextUtils.isEmpty(balckNumber)) {
                    ToastUtils.toastShort("黑名单号码不能为空！");
                    return;
                }
                if (!cbPhone && !cbSms) {
                    ToastUtils.toastShort("拦截模式不能为空！");
                    return;
                }
                String mode = "3";
                if (cbPhone) {
                    mode = "1";
                }
                if (cbSms) {
                    mode = "2";
                }
                if (cbPhone && cbSms) {
                    mode = "3";
                }
                BlackNumberDao dao = new BlackNumberDao(getApplicationContext());
                dao.add(blackName, balckNumber, mode);
                //更新ListView的数据源
                BlackNumberInfo info = new BlackNumberInfo();
                info.setDisplayName(blackName);
                info.setMode(BlackNumberDao.getModeByKey(mode));
                info.setNumber(balckNumber);
                list.add(0, info);
                //通知适配器更新ListView
                adapter.notifyDataSetChanged();

                alertDialog.dismiss();
            }
        });

    }

    /**
     * 打开通讯录选取黑名单
     *
     * @param view
     */
    public void selectContact(View view) {
        Intent intent = new Intent(this, SelectContactActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        String phone = data.getStringExtra("phone").replace("-", "").replace(" ", "").replace("+86", "");
        String displayName = data.getStringExtra("name");
        Toast.makeText(this, displayName + ": " + phone, Toast.LENGTH_SHORT).show();
        etBalckNumber.setText(phone);
        etBlackName.setText(displayName);
    }
}
