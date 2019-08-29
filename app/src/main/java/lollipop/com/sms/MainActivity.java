package lollipop.com.sms;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import lollipop.com.sms.adapter.AddressAdapter;
import lollipop.com.sms.adapter.SmsFatherModel;
import lollipop.com.sms.adapter.SmsModel;
import lollipop.com.sms.utils.DataBaseHelper;

public class MainActivity extends BaseActivity {

    private SwipeRefreshLayout swipeRefreshLayout;

    private ListView mListView;

    private AddressAdapter adapter;

    private ArrayList<SmsFatherModel> allsms;

    private final String[] permissionsSMS = new String[]{
            Manifest.permission.RECEIVE_SMS};
    private ArrayList<String> permissionsList = new ArrayList<String>();
    private final int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        checkPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initView(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SendActivity.class));
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

        mListView = findViewById(R.id.listview);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SmsFatherModel smsFatherModel = allsms.get(position);

                SmsModel model = smsFatherModel.getSms().get(0);

                Intent intent = new Intent(MainActivity.this, SingleUserActivity.class);

                intent.putExtra("phone", model.getPhone());

                startActivity(intent);

            }
        });
    }

    private void initData(){
        if (allsms == null){
            allsms = new ArrayList<>();
        } else {
            allsms.clear();
        }
        ArrayList secsms = new DataBaseHelper(this).getAllSms();

        allsms.addAll(secsms);

        if (allsms != null){
            setAdapter();
        }
        swipeRefreshLayout.setRefreshing(false);//设置不刷新
    }

    public void setAdapter() {
        if (adapter == null){
            adapter = new AddressAdapter(this, allsms);
            mListView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void checkPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            // 请求权限
            checkRequiredPermission(this);

            if (permissionsList.size() > 0) {
                ActivityCompat.requestPermissions(this,permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_PERMISSIONS);
            } else {
                // 已有权限
            }
        } else {
            // 已有权限
        }
    }

    private void checkRequiredPermission(final Activity activity){
        permissionsList.clear();
        String[] permissionsArray = permissionsSMS;
        for (String permission : permissionsArray) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            if (grantResults.length > 0) {
                boolean requestResult = true;

                for (int i : grantResults) {
                    if (i != PackageManager.PERMISSION_GRANTED) {
                        requestResult = false;
                    }
                }

                if (requestResult) {
                    // 权限申请成功
                } else {
                    requestResult = true;   // 标识符
                    for (String str : permissions) {
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,str)) {   // 是否点击了不再提醒
                            requestResult = false;
                        }
                    }
                    if (requestResult) {  // 允许继续请求权限
                        showAlertDialog("为了使用相机,需要开启存储和相机权限", "取消", "开启授权", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                checkPermission();
                            }
                        });
                        return;  // 有不再询问就不需要再去请求权限了，直接弹窗
                    }
                    showAlertDialog("请在-应用设置-权限-中，允许冰团e购使用相机和存储权限", "取消", "手动授权", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Uri uri = Uri.parse("package:" + getPackageName());//包名
                            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", uri);
                            startActivity(intent);
                        }
                    });
                }
            }
        }
    }


//    public String getSmsInPhone() {
//        final String SMS_URI_ALL = "content://sms/"; // 所有短信
//        final String SMS_URI_INBOX = "content://sms/inbox"; // 收件箱
//        final String SMS_URI_SEND = "content://sms/sent"; // 已发送
//        final String SMS_URI_DRAFT = "content://sms/draft"; // 草稿
//        final String SMS_URI_OUTBOX = "content://sms/outbox"; // 发件箱
//        final String SMS_URI_FAILED = "content://sms/failed"; // 发送失败
//        final String SMS_URI_QUEUED = "content://sms/queued"; // 待发送列表
//
//        StringBuilder smsBuilder = new StringBuilder();
//
//        try {
//            Uri uri = Uri.parse(SMS_URI_INBOX);
//            String[] projection = new String[] { "_id", "address", "person",
//                    "body", "date", "type", };
//            Cursor cur = getContentResolver().query(uri, projection, null,
//                    null, "date desc"); // 获取手机内部短信
//            // 获取短信中最新的未读短信
//            // Cursor cur = getContentResolver().query(uri, projection,
//            // "read = ?", new String[]{"0"}, "date desc");
//            if (cur.moveToFirst()) {
//                int index_Address = cur.getColumnIndex("address");
//                int index_Person = cur.getColumnIndex("person");
//                int index_Body = cur.getColumnIndex("body");
//                int index_Date = cur.getColumnIndex("date");
//                int index_Type = cur.getColumnIndex("type");
//
//                do {
//                    String strAddress = cur.getString(index_Address);
//                    int intPerson = cur.getInt(index_Person);
//                    String strbody = cur.getString(index_Body);
//                    long longDate = cur.getLong(index_Date);
//                    int intType = cur.getInt(index_Type);
//
//                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//                    Date d = new Date(longDate);
//                    String strDate = dateFormat.format(d);
//
//                    String strType = "";
//                    if (intType == 1) {
//                        strType = "接收";
//                    } else if (intType == 2) {
//                        strType = "发送";
//                    } else if (intType == 3) {
//                        strType = "草稿";
//                    } else if (intType == 4) {
//                        strType = "发件箱";
//                    } else if (intType == 5) {
//                        strType = "发送失败";
//                    } else if (intType == 6) {
//                        strType = "待发送列表";
//                    } else if (intType == 0) {
//                        strType = "所以短信";
//                    } else {
//                        strType = "null";
//                    }
//
//                    smsBuilder.append("[ ");
//                    smsBuilder.append(strAddress + ", ");
//                    smsBuilder.append(intPerson + ", ");
//                    smsBuilder.append(strbody + ", ");
//                    smsBuilder.append(strDate + ", ");
//                    smsBuilder.append(strType);
//                    smsBuilder.append(" ]\n\n");
//                } while (cur.moveToNext());
//
//                if (!cur.isClosed()) {
//                    cur.close();
//                    cur = null;
//                }
//            } else {
//                smsBuilder.append("no result!");
//            }
//
//            smsBuilder.append("getSmsInPhone has executed!");
//
//        } catch (SQLiteException ex) {
//            Log.d("SQLiteException", ex.getMessage());
//        }
//
//        return smsBuilder.toString();
//    }
}
