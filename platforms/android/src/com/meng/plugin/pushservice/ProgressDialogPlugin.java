package com.meng.plugin.pushservice;

import android.util.Log;
import android.widget.Toast;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.content.ContentValues;
import io.socket.client.Socket;
import android.content.Intent;

/**
 * Created by liumeng on 2016/8/17.
 */
public class ProgressDialogPlugin extends CordovaPlugin {
    CallbackContext callbackContext;
    private Socket mSocket;
    SQLiteDatabase db;

    public ProgressDialogPlugin(){

    }
    @Override
    public void pluginInitialize(){
        db=SQLiteDatabase.openOrCreateDatabase(cordova.getActivity().getFilesDir().toString()+"/sfDB.db3",null);
    }
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException{
        this.callbackContext = callbackContext;

        if (action.equals("start")) {
            JSONObject message=args.getJSONObject(0);
            Toast.makeText(cordova.getActivity(), message.toString(), Toast.LENGTH_SHORT).show();

            //判断表是否存在
            boolean tableExist=DbTools.isExistTable(db,"users");
            if(!tableExist){
                //不存在表，就建立一个users表
                db.execSQL("create table users(_id varchar(255) primary key,name varchar(50),active int,image blob,token blob)");
            }
            db.beginTransaction();
            try{
                //将已经存在的user激活状态全部置否
                ContentValues values=new ContentValues();
                values.put("active",false);
                db.update("users",values,null,null);
                //新值变成激活状态
                String sqlUserExist="select * from users where name='"+message.getString("name")+"'";
                Cursor cursor1=db.rawQuery(sqlUserExist,null);
                if(cursor1.getCount()!=0){
                    //存在的话，就把这个user激活
                    ContentValues cv=new ContentValues();
                    cv.put("active",1);
                    db.update("users",cv,"name=?",new String[]{message.getString("name")});
                }
                else{
                    //不存在，就新增新数据
                    ContentValues cvChats = new ContentValues();
                    cvChats.put("_id", messageJson.getString("_id"));
                    cvChats.put("from", messageJson.getString("from"));
                    cvChats.put("to", messageJson.getString("to"));
                    cvChats.put("content", messageJson.getString("content"));
                    JSONObject meta=messageJson.getJSONObject("meta");
                    cvChats.put("createAt", meta.getString("createAt"));
                    cvChats.put("saw", 0);
                    db.insert("chats",null,cvChats);
                }
                db.setTransactionSuccessful();
            }
            catch (SQLiteException e){
                //Toast.makeText(MainActivity.this,R.string.error_sqlite, Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            db.endTransaction();
            //db.close();
            //启动服务
            Intent intent =new Intent(cordova.getActivity(),ChatService.class);
            intent.putExtra("username",message.getString("name"));
            intent.putExtra("_id",message.getString("_id"));
            cordova.getActivity().startService(intent);
            return true;
        }
        return false;
    }
}
