package lollipop.com.sms;

import android.app.Application;

/**
 *
 * Created by xieshengqi on 15/12/2.
 * Updated by lollipop on 18/10/29.
 *
 */
public class BTApplication extends Application {

    private static BTApplication mApp;

    public BTApplication() {
        mApp = this;
    }

    public static BTApplication getInstance() {
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}