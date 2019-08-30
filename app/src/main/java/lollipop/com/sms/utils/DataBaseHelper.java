package lollipop.com.sms.utils;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;

import lollipop.com.sms.adapter.SmsFatherModel;
import lollipop.com.sms.adapter.SmsModel;

/**
 * Created by lollipop on 2017/7/28.
 */

public class DataBaseHelper {
    /**
     * 数据库打开的帮助类
     */
    private MyDBHelper helper;

    public DataBaseHelper(Context context) {
        helper = new MyDBHelper(context);
    }

//
//    public boolean isExistWithPhone(String phone){
//        SQLiteDatabase db = helper.getReadableDatabase();
//
//        Cursor  cursor = db.rawQuery("select phone from loginInfo where phone=?", new String[]{phone});
//
//        if (cursor != null && cursor.getCount() > 0) {  // 该用户已经存在
//            return true;
//        }
//        return false;
//    }
//
//    public long addNewIfExcite(String phone, String token, String name, boolean update){
//        SQLiteDatabase db = helper.getWritableDatabase();
//
//        Cursor cursor = db.rawQuery("select phone from loginInfo where phone=?", new String[]{phone});
//
//        if (cursor != null && cursor.getCount() > 0) {  // 该用户已经存在
//            int rowcount = 0;
//            if (update){  // 存在就更新数据，一般丛登录返回
//                ContentValues values = new ContentValues();
//                values.put("name", name);
//                values.put("token", token);
//                rowcount =  db.update("loginInfo", values, "phone=?", new String[]{phone});
//            }
//            db.close();
//            return rowcount;
//        } else { //不存在该用户，直接增加一条数据内部是组拼sql语句实现的.
//            ContentValues values = new ContentValues();
//            values.put("name", name);
//            values.put("phone", phone);
//            values.put("token", token);
//            long rowid = db.insert("loginInfo", null, values);
//            //记得释放数据库资源
//            db.close();
//            return rowid;
//        }
//    }
//
//    public long addNewfromOld(String phone, String token, String name){
//        SQLiteDatabase db = helper.getWritableDatabase();
//
//        Cursor  cursor = db.rawQuery("select phone from loginInfo where phone=?", new String[]{phone});
//
//        if (cursor != null && cursor.getCount() > 0) {  // 该用户已经存在
//            ContentValues values = new ContentValues();
//            values.put("name", name);
//            int rowcount =  db.update("loginInfo", values, "phone=?", new String[]{phone});
//            db.close();
//            return rowcount;
//        } else { //不存在该用户，直接增加一条数据内部是组拼sql语句实现的.
//            ContentValues values = new ContentValues();
//            values.put("name", name);
//            values.put("phone", phone);
//            values.put("token", token);
//            long rowid = db.insert("loginInfo", null, values);
//            //记得释放数据库资源
//            db.close();
//            return rowid;
//        }
//    }
//    /**
//     * 根据手机号删除一条记录
//     * @param phone 要删除的联系人的姓名
//     * @return 返回0代表的是没有删除任何的记录 返回整数int值代表删除了几条数据
//     */
//    public int delete(String phone){
//        //判断这个数据是否存在.
//        SQLiteDatabase db = helper.getWritableDatabase();
//        //db.execSQL("delete from contactinfo where name=?", new Object[]{name});
//        int rowcount = db.delete("loginInfo", "phone=?", new String[]{phone});
//        db.close();
//        //再从数据库里面查询一遍,看name是否还在
//        return rowcount;
//    }
//
//    // 删除旧版本的所有数据
//    public void deleteWhereTokenNull(){
//        //判断这个数据是否存在.
//        SQLiteDatabase db = helper.getWritableDatabase();
//        db.execSQL("delete from loginInfo where token is null");
//        db.close();
//    }
//
//
//    /**
//     * 根据手机号修改用户的密码或者用户名
//     * @param phone 电话号码
//     * @param name 要修改的联系人姓名
//     * @return 0代表一行也没有更新成功, >0 整数代表的是更新了多少行记录
//     */
//    public int update(String phone, String name, String token){
//        SQLiteDatabase db = helper.getWritableDatabase();
//        //db.execSQL("update contactinfo set phone =? where name=?", new Object[]{newphone,name});
//        ContentValues values = new ContentValues();
//        values.put("name", name);
//        values.put("token", token);
//        int rowcount =  db.update("loginInfo", values, "phone=?", new String[]{phone});
//        if (rowcount == 0) { // 说明该用户不存在
//
//        }
//        db.close();
//        return rowcount;
//    }
//    /**
//     * 查询联系人的电话号码
//     * @return 电话号码
//     */
//    public ArrayList<UserModel> getAllUser(){
//        SQLiteDatabase db = helper.getReadableDatabase();
//        //Cursor  cursor = db.rawQuery("select phone from contactinfo where name=?", new String[]{name});
//        Cursor  cursor =  db.query("loginInfo", null, null, null, null, null, null);
//        if (cursor == null || !cursor.moveToFirst()) {
//            return null;
//        }
//        ArrayList<UserModel> userModels = new ArrayList<>();
//        UserModel model;
//        do {
//            model = new UserModel();
//            model.setName(cursor.getString(cursor.getColumnIndex("name")));
//            model.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
//            model.setToken(cursor.getString(cursor.getColumnIndex("token")));
//            userModels.add(model);
//        } while (cursor.moveToNext());
//        cursor.close();//关闭掉游标,释放资源
//        db.close();//关闭数据库,释放资源
//        return userModels;
//    }


    public void updateRead(String phone){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MyDBHelper.type, 2);
        db.update(MyDBHelper.DbUserName,values,"phone=?", new String[]{phone});
        db.close();
    }

    /**
     * 添加一条记录
     * @param name 联系人姓名
     * @param phone 联系人电话
     * @return 返回的是添加后在数据库的行号  -1代表添加失败
     */
    public long insert(String name, String phone, long time, boolean send, String content){
        SQLiteDatabase db = helper.getWritableDatabase();

        // 更新短信表
        ContentValues values = new ContentValues();
        values.put(MyDBHelper.name, name);
        values.put(MyDBHelper.phone, phone);
        values.put(MyDBHelper.content, content);
        values.put(MyDBHelper.time, time);
        values.put(MyDBHelper.type, send ? 1 : 2);
        //内部是组拼sql语句实现的.
        long rowid = db.insert(MyDBHelper.DbName, null, values);

        // 更新短信用户表
        values = new ContentValues();
        values.put(MyDBHelper.name, name);
        values.put(MyDBHelper.phone, phone);
        values.put(MyDBHelper.content, content);
        values.put(MyDBHelper.time, time);
        values.put(MyDBHelper.type, send ? 2 : 1);

        Cursor cursor = db.rawQuery("select * from " + MyDBHelper.DbUserName +" where "+ MyDBHelper.phone +"=?", new String[]{phone});
        if (cursor != null && cursor.getCount() > 0) {  // 表示存在，更新
            db.update(MyDBHelper.DbUserName,values,"phone=?", new String[]{phone});
        } else {
            db.insert(MyDBHelper.DbUserName, null, values);
        }

        //记得释放数据库资源
        db.close();
        return rowid;
    }

    public ArrayList<SmsModel> getAllSmsWithPhone(String phone){

        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from " + MyDBHelper.DbName +" where "+ MyDBHelper.phone +"=?", new String[]{phone});

        if (cursor == null || !cursor.moveToFirst()) {
            return null;
        }

        ArrayList<SmsModel> smsModels = new ArrayList<>();

        SmsModel model;
        do {
            model = new SmsModel();
            model.setName(cursor.getString(cursor.getColumnIndex(MyDBHelper.name)));
            model.setPhone(cursor.getString(cursor.getColumnIndex(MyDBHelper.phone)));
            model.setContent(cursor.getString(cursor.getColumnIndex(MyDBHelper.content)));
            model.setTime(Utils.formatDateTime(cursor.getLong(cursor.getColumnIndex(MyDBHelper.time))));
            model.setSend(cursor.getInt(cursor.getColumnIndex(MyDBHelper.type)) == 1);
            smsModels.add(model);

        } while (cursor.moveToNext());
        cursor.close();//关闭掉游标,释放资源
        db.close();//关闭数据库,释放资源

        return smsModels;
    }

    public ArrayList<SmsFatherModel> getAllSms(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor  cursor =  db.query(MyDBHelper.DbUserName, null, null, null, null, null, " time desc");

        if (cursor == null || !cursor.moveToFirst()) {
            return null;
        }

        ArrayList<SmsFatherModel> fatherModels = new ArrayList<>();

        SmsFatherModel model;
        do {
            model = new SmsFatherModel();
            model.setName(cursor.getString(cursor.getColumnIndex(MyDBHelper.name)));
            model.setPhone(cursor.getString(cursor.getColumnIndex(MyDBHelper.phone)));
            model.setContent(cursor.getString(cursor.getColumnIndex(MyDBHelper.content)));
            model.setTime(Utils.formatDateTime(cursor.getLong(cursor.getColumnIndex(MyDBHelper.time))));
            model.setUnRead(cursor.getInt(cursor.getColumnIndex(MyDBHelper.type)) == 1);
            fatherModels.add(model);
        } while (cursor.moveToNext());
        cursor.close();//关闭掉游标,释放资源
        db.close();//关闭数据库,释放资源

        return fatherModels;
    }
}