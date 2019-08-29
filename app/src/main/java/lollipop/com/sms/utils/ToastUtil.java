package lollipop.com.sms.utils;

import android.text.TextUtils;
import android.widget.Toast;

import lollipop.com.sms.BTApplication;

/**
 * Created by xieshengqi on 15/11/17.
 */
public class ToastUtil {
    private static Toast mToast;

    public static void showLongText(String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        if (mToast == null) {
            mToast = Toast.makeText(BTApplication.getInstance(), content, Toast.LENGTH_LONG);
//            mToast.setGravity(Gravity.TOP, 0, 128);
        } else {
            mToast.setText(content);
        }
        mToast.show();
    }
    public static void showShortText(String text) {
        showLongText(text);
        mToast.setDuration(Toast.LENGTH_SHORT);
    }
}
