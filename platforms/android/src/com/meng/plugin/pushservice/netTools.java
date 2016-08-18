package com.meng.plugin.pushservice;

import android.database.sqlite.SQLiteDatabase;

import org.json.JSONObject;

/**
 * Created by liumeng on 2016/8/16.
 */
public class netTools {
    public static JSONObject getActorInfo(SQLiteDatabase db, String _id) throws Exception{
        String token=DbTools.GetToken(db);
        String url=HttpUtil.BASE_URL+"user_by_id?token="+token+"&id="+_id;
        return new JSONObject(HttpUtil.getRequest(url));
    }
}
