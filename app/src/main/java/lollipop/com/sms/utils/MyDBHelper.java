package lollipop.com.sms.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;

import lollipop.com.sms.adapter.SmsFatherModel;
import lollipop.com.sms.adapter.SmsModel;

/**
 * 数据库Helper类，必须继承自 SQLiteOpenHelper
 * 当一个继承自 SQLiteOpenHelper 后需要复写两个方法，分别是 onCreate()  和 onUpgrade()
 * onCreate()： onCreate是在数据库创建的时候调用的，主要用来初始化数据表结构和插入数据初始化的记录
 * onUpgrade()：onUpGrade是在数据库版本升级的时候调用的，主要用来改变表结构
 *
 *
 *  数据库帮助类要做的事情特别简单：
 *  1、复写onCreate()  和 onUpgrade()方法
 *  2、在这两个方法里面填写相关的sql语句
 *
 *
 */
public class MyDBHelper extends SQLiteOpenHelper{

    public static final String DbName = "smsInfo";
    public static final String name = "name";
    public static final String phone = "phone";
    public static final String content = "content";
    public static final String time = "time";
    public static final String type = "type";       // DbName表中 --> 1: 本人发送，2:接受   DbUserName表中 --> 1: 未读，2:已读


    public static final String DbUserName = "smsUserInfo";

    public MyDBHelper(Context context) {
        /**
         * 参数说明：
         *
         * 第一个参数： 上下文
         * 第二个参数：数据库的名称
         * 第三个参数：null代表的是默认的游标工厂
         * 第四个参数：是数据库的版本号  数据库只能升级,不能降级,版本号只能变大不能变小
         */
        super(context, "sms.db", null, 2);
    }


    /**
     * onCreate是在数据库创建的时候调用的，主要用来初始化数据表结构和插入数据初始化的记录
     *
     * 当数据库第一次被创建的时候调用的方法,适合在这个方法里面把数据库的表结构定义出来.
     * 所以只有程序第一次运行的时候才会执行
     * 如果想再看到这个函数执行，必须写在程序然后重新安装这个app
     */

    @Override
    public void onCreate(SQLiteDatabase db) {

        // 版本1加入
        db.execSQL("create table " + DbName + "(id integer primary key autoincrement, " +
                name + " varchar(20), " +
                phone + " varchar(11), " +
                time + " long, " +
                type + " int(1), " +
                content + " varchar) ");


        // 版本2加入
        db.execSQL("create table " + DbUserName + "(id integer primary key autoincrement, " +
                name + " varchar(20), " +
                phone + " varchar(11), " +
                time + " long, " +
                type + " int(1), " +
                content + " varchar) ");
    }


    /**
     * 当数据库更新的时候调用的方法
     * 这个要显示出来得在上面的super语句里面版本号发生改变时才会 打印  （super(context, "itheima.db", null, 2); ）
     * 注意，数据库的版本号只可以变大，不能变小，假设我们当前写的版本号是3，运行，然后又改成1，运行则报错。不能变小
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1 && 2 == newVersion){
            db.execSQL("create table " + DbUserName + "(id integer primary key autoincrement, " +
                    name + " varchar(20), " +
                    phone + " varchar(11), " +
                    time + " long, " +
                    type + " int(1), " +
                    content + " varchar) ");

            Cursor cursor =  db.query(MyDBHelper.DbName, null, null, null, null, null, " time desc");

            if (cursor == null || !cursor.moveToFirst()) {
                return;
            }

            ArrayList<String> smsFathers = new ArrayList<>();

            String phone = null;
            String name = null;
            String content = null;
            long time;
            do {

                phone = cursor.getString(cursor.getColumnIndex(MyDBHelper.phone));

                if (!smsFathers.contains(phone)){
                    name = cursor.getString(cursor.getColumnIndex(MyDBHelper.name));
                    content = cursor.getString(cursor.getColumnIndex(MyDBHelper.content));
                    time = cursor.getLong(cursor.getColumnIndex(MyDBHelper.time));

                    ContentValues values = new ContentValues();
                    values.put(MyDBHelper.name, name);
                    values.put(MyDBHelper.phone, phone);
                    values.put(MyDBHelper.content, content);
                    values.put(MyDBHelper.time, time);
                    values.put(MyDBHelper.type, 2);

                    db.insert(DbUserName, null, values);

                    smsFathers.add(phone);
                }


            } while (cursor.moveToNext());
            cursor.close();//关闭掉游标,释放资源
        }
    }
}