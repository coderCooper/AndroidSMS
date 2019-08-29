package lollipop.com.sms.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import lollipop.com.sms.MainActivity;
import lollipop.com.sms.R;

public class SMSReceiver extends BroadcastReceiver {

    final int NOTIFICATION_ID = 0x1123;

    @Override
    public void onReceive(Context arg0, Intent arg1) {

        Log.e("SMSReceiver", arg1.toString());

        Bundle bundle = arg1.getExtras();

        Object messages[] = (Object[]) bundle.get("pdus");// Bundle从意图中获得数据

        int length = messages.length;

        String number = null;

        String content = null;

        long time = 0;

        for (int i = 0; i < length; i++) {

            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) messages[i]);// 赋值

            //发送方的电话号码
            number = smsMessage.getDisplayOriginatingAddress();

            //获取短信的内容
            content = smsMessage.getDisplayMessageBody();

            time = smsMessage.getTimestampMillis();

            new DataBaseHelper(arg0).insert("", number, time, false, content);

        }

        //获取推送大图标
        Bitmap bitmap = BitmapFactory.decodeResource(arg0.getResources(), R.mipmap.smsicon);
        //构建打开界面
        Intent intent1 = new Intent(arg0, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(arg0, NOTIFICATION_ID, intent1, PendingIntent.FLAG_ONE_SHOT);
        //构建推送对象
        Notification.Builder builder = new Notification.Builder(arg0);
        builder.setSmallIcon(R.mipmap.smsicon);
        builder.setLargeIcon(bitmap);
        builder.setTicker(number);
        builder.setContentTitle(number);
        builder.setContentText(content);
        builder.setContentIntent(pendingIntent);
        //设置默认声音
        builder.setDefaults(Notification.DEFAULT_SOUND);
        //设置点击自动清除状态栏显示
        builder.setAutoCancel(true);
        NotificationManager manager = (NotificationManager) arg0.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, builder.build());
    }
}