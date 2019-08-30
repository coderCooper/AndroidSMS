package lollipop.com.sms;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import lollipop.com.sms.adapter.SingleMsgAdapter;
import lollipop.com.sms.adapter.SmsFatherModel;
import lollipop.com.sms.adapter.SmsModel;
import lollipop.com.sms.utils.DataBaseHelper;
import lollipop.com.sms.utils.ToastUtil;
import lollipop.com.sms.utils.Utils;

public class SingleUserActivity extends BaseActivity {

    private SwipeRefreshLayout swipeRefreshLayout;

    private ListView mListView;

    private EditText contentEd;

    private SingleMsgAdapter adapter;

    private ArrayList<SmsModel> allsms;

    private String curPhone;

    private SentMsgReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);
        initView();
        curPhone = getIntent().getStringExtra("phone");
        setTitle(curPhone);


        IntentFilter filter = new IntentFilter();
        filter.addAction(Utils.SMS_ACTION);
        receiver = new SentMsgReceiver();
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData(curPhone);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void initView(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData(curPhone);
            }
        });

        mListView = findViewById(R.id.listview);

        contentEd = findViewById(R.id.ed00);

        findViewById(R.id.tv00).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                send();
            }
        });
    }

    private void initData(String phone){
        if (TextUtils.isEmpty(curPhone)){
            return;
        }
        if (allsms == null){
            allsms = new ArrayList<>();
        } else {
            allsms.clear();
        }

        ArrayList<SmsModel> smsModels = new DataBaseHelper(this).getAllSmsWithPhone(phone);

        allsms.addAll(smsModels);

        if (allsms != null){
            setAdapter();
        }
        swipeRefreshLayout.setRefreshing(false);//设置不刷新
    }

    public void setAdapter() {
        if (adapter == null){
            adapter = new SingleMsgAdapter(this, allsms);
            mListView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        mListView.setSelection(adapter.getCount() - 1);
    }

    private void send() {
        if (TextUtils.isEmpty(curPhone)) {
            ToastUtil.showShortText("您的手机号为空");
            return;
        }

        String msgStr = contentEd.getText().toString().trim();
        if (TextUtils.isEmpty(msgStr)) {
            ToastUtil.showShortText("请输入您要发送的内容");
            return;
        }


        SmsManager sms = SmsManager.getDefault();

        Intent i = new Intent(Utils.SMS_ACTION);

        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);

        sms.sendTextMessage(curPhone, null, msgStr, pi, null);

        long time = System.currentTimeMillis();
        SmsModel model = new SmsModel();
        model.setContent(msgStr);
        model.setTime(Utils.getDataTime(time));
        model.setPhone(curPhone);
        model.setSend(true);
        allsms.add(model);
        setAdapter();

        new DataBaseHelper(this).insert("", curPhone, time, true, msgStr);

        contentEd.setText(null);
        InputMethodManager manager = ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE));
        if (manager != null)
            manager.hideSoftInputFromWindow(contentEd.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private class SentMsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(Utils.SMS_ACTION)) {

                int code = getResultCode();

                //短消息发送成功
                if (code == Activity.RESULT_OK) {
                    ToastUtil.showShortText("短信发送成功");
                } else {
                    ToastUtil.showShortText("短信发送失败");
                }
            }
        }
    }
}
