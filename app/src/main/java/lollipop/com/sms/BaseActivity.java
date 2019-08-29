package lollipop.com.sms;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

public class BaseActivity extends AppCompatActivity {


    protected void showAlertDialog(String msg, String cancel, String ok, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("系统提示");
        builder.setMessage(msg);
        if (!TextUtils.isEmpty(cancel)) {
            builder.setNegativeButton(cancel, null);
        }
        //确定
        builder.setPositiveButton(ok, listener);

        builder.show();
    }
}
