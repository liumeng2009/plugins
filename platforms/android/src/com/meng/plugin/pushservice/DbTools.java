package com.meng.plugin.pushservice;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONObject;

/**
 * Created by liumeng on 2016/8/16.
 */
public class DbTools {
    public static boolean isExistTable(SQLiteDatabase db,String tablename){
        //判断表是否存在
        boolean tableExist=false;
        String sqlTableExist="select * from sqlite_master where type='table' and name='"+tablename+"'";
        Cursor cursor =db.rawQuery(sqlTableExist,null);
        if(cursor.getCount()!=0){
            tableExist=true;
        }
        else{
            tableExist=false;
        }
        return tableExist;
    }
    public static void saveActor(SQLiteDatabase db,String actorId){
        String sqlUserExist="select * from actor where _id='"+actorId+"'";
        Cursor cursor1=db.rawQuery(sqlUserExist,null);
        if(cursor1.getCount()!=0){
            //存在，不更新信息
        }
        else{
            //不存在这个人，从服务器请求这个人的信息，存入数据库
            try {
                JSONObject jsonObject=netTools.getActorInfo(db,actorId);
                ContentValues cv=new ContentValues();
                cv.put("_id",jsonObject.getString("_id"));
                cv.put("name",jsonObject.getString("_id"));
                cv.put("_id",jsonObject.getString("_id"));
                db.insert("actor",null,cv);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static String GetToken(SQLiteDatabase db) throws Exception{
        String token="";
        Cursor cursor=db.query("users",new String[]{"token"},"active=?",new String[]{"1"},null,null,null);
        if(cursor.moveToFirst()){
            int index=cursor.getColumnIndex("token");
            token=cursor.getString(index);
        }
        return token;
    }
    public static boolean IsExist(SQLiteDatabase db,String tablename,String clomnname,String clomnvalue) throws Exception{
        String sqlExist="select * from "+tablename+" where "+clomnname+"='"+clomnvalue+"'";
        Cursor cursor1=db.rawQuery(sqlExist,null);
        if(cursor1.getCount()>0){
            return true;
        }
        else{
            return false;
        }
    }
}
