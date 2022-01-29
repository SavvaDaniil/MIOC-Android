package com.ds.miocnative.component;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "mioc";
    private static final String TABLE_INFO_OF_USER = "info_of_user";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_VALUE = "value";

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable  SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ TABLE_INFO_OF_USER + " (" + KEY_ID
                + " integer primary key autoincrement, " + KEY_NAME
                + " text," + KEY_VALUE+ " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public static String selectData(Context context, String name) {
        String answer = "";

        DBHelper dbHelper = new DBHelper(context,null,null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor res = db.query("info_of_user", null,"name = '"+name+"'", null, null, null, null);
        Log.d("ИЗ БАЗЫ ДАННЫХ", String.valueOf(res));
        StringBuffer buffer = new StringBuffer();
        if (res != null) {
            String str;
            while(res.moveToNext()){
                str = "";
                for (String cn : res.getColumnNames()) {
                    str = str.concat(cn + " = "
                            + res.getString(res.getColumnIndex(cn)) + "; ");
                    answer = res.getString(res.getColumnIndex("value"));
                }
                Log.d("база данных SELECT", str);
            }
            res.close();
        } else {
            Log.d("база данных SELECT", "Cursor is null");
        }

        dbHelper = null;
        db = null;
        res = null;
        buffer = null;
        name = null;
        context = null;
        System.gc();

        return answer;
    }

    public static void updateData(Context context, String name, String value){
        DBHelper dbHelper = new DBHelper(context,null,null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("value", value);
        int updCount = db.update("info_of_user", cv, "name = ?", new String[] { name });
        Log.d("база данных UPDATE", "updated rows count = " + updCount);
        dbHelper.close();

        dbHelper = null;
        db = null;
        cv = null;
        name = null;
        context = null;
        value = null;
        System.gc();
    }

    public static int checkTechRowsCreateIfNoExist(Context context, String name){
        int check_exist = 0;

        DBHelper dbHelper = new DBHelper(context,null,null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor res = db.query("info_of_user", null,"name = '"+name+"'", null, null, null, null);
        Log.d("база данных SELECT", String.valueOf(res));
        StringBuffer buffer = new StringBuffer();
        if (res != null) {
            String str;
            while(res.moveToNext()){
                check_exist++;
                str = "";
                for (String cn : res.getColumnNames()) {
                    str = str.concat(cn + " = "
                            + res.getString(res.getColumnIndex(cn)) + "; ");
                }
                Log.d("база данных SELECT", str);
            }
            res.close();
        } else {
            Log.d("база данных SELECT", "Cursor is null");
        }

        if(check_exist == 0){
            insertIntoDb(context, name, "");
        }

        dbHelper = null;
        db = null;
        res = null;
        buffer = null;
        name = null;
        context = null;
        System.gc();

        return check_exist;
    }

    public static void insertIntoDb(Context context, String name, String value){
        DBHelper dbHelper = new DBHelper(context,null,null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("value", value);
        long rowID = db.insert("info_of_user", null, cv);
        Log.d("база данных INSERT", "row inserted, ID = " + rowID);
        dbHelper.close();

        dbHelper = null;
        db = null;
        cv = null;
        name = null;
        context = null;
        value = null;
        System.gc();
    }


    public static void clearTable(Context context){
        DBHelper dbHelper = new DBHelper(context,null,null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int delCount = db.delete("info_of_user", null, null);
        Log.d("УДАЛЕНИЕ ИЗ БАЗЫ ДАННЫХ", "deleted rows count = " + delCount);
        dbHelper.close();

        dbHelper = null;
        db = null;
        context = null;
        System.gc();
    }
}
