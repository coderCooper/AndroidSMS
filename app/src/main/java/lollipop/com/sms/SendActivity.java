package lollipop.com.sms;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import lollipop.com.sms.utils.DataBaseHelper;
import lollipop.com.sms.utils.ToastUtil;
import lollipop.com.sms.utils.Utils;

public class SendActivity extends BaseActivity {

    private EditText phoneEd;
    private EditText contentEd;
    private SentMsgReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        initView();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Utils.SMS_ACTION);
        receiver = new SentMsgReceiver();
        registerReceiver(receiver, filter);

        Intent intent = getIntent();

        String phone = intent.getStringExtra("phone");

        if (!TextUtils.isEmpty(phone)){
            phoneEd.setText(phone);
            phoneEd.setEnabled(false);
            contentEd.requestFocus();
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        phoneEd = findViewById(R.id.et00);
        contentEd = findViewById(R.id.et01);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });
    }

    private void send() {
        String phoneNumber = phoneEd.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNumber)) {
            ToastUtil.showShortText("请输入手机号");
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

        sms.sendTextMessage(phoneNumber, null, msgStr, pi, null);

        new DataBaseHelper(this).insert("", phoneNumber, System.currentTimeMillis(), true, msgStr);
    }

    private class SentMsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(Utils.SMS_ACTION)) {

                int code = getResultCode();

                //短消息发送成功
                if (code == Activity.RESULT_OK) {
                    ToastUtil.showShortText("短信发送成功");
                    finish();
                } else {
                    ToastUtil.showShortText("短信发送失败");
                }
            }
        }
    }
}
