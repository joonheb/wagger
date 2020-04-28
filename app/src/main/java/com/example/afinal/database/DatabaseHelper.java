package com.example.afinal.database;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private static DatabaseHelper databaseHelper;

    private static SQLiteDatabase database;
    private static String dbName = "together";
    private static String tableName = "kingGame";

    public static DatabaseHelper getInstance() {
        if(databaseHelper == null) databaseHelper = new DatabaseHelper();
        return databaseHelper;
    }
    public void freeInstance() {
        if(databaseHelper!=null) {
            databaseHelper = null;
        }
    }

    private DatabaseHelper() {
    }

    public void openDatabase(Activity activity) {
        if(database == null) database = activity.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
    }

    public void createTable() {
        String query = "CREATE TABLE IF NOT EXISTS " + tableName + " ( gameNum INT, gameTitle VARCHAR2(30), menuNum INT );";
        database.execSQL(query);
    }

    public void insertTable(Activity activity) {
        InputStream is = null;
        byte[] b = null;
        try {
            is = activity.getAssets().open("kingGame.txt");
            b = new byte[is.available()];
            is.read(b);

            String temp = new String(b, "utf-8");
            String[] data = temp.split("\n");
            Log.d("TEST", "data size : "+data.length);


            String query = "SELECT count(*) AS cnt FROM "+ tableName;
            Cursor cursor = database.rawQuery(query, null);
            int cnt = 0;
            if(cursor.moveToNext()) {
                cnt = cursor.getInt(0);
                Log.d("TEST", cnt+"");
            }

            if(cnt!=data.length) {
                query = "DELETE FROM " + tableName;
                database.execSQL(query);

                // 데이터 삽입
                for(int i=0; i<data.length; i++) {
                    database.execSQL(data[i]);
                    Log.d("TEST", data[i]);
                }


            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // 데이터 패치나 추가할때 사용
    public List<String> selectTable(int menuNum) {
        List<String> list = new ArrayList<>();
        String query = "SELECT * FROM "+ tableName + " WHERE menuNum = "+ menuNum +";";

        Cursor cursor = database.rawQuery(query, null);
        Log.d("TEST" ,"cursor : "+cursor.getCount());
        Log.d("TEST" ,"query : "+query);
        while(cursor.moveToNext()) {
            String title = cursor.getString(1);
            Log.d("TEST", "title : "+title);
            list.add(title);
        }

        return list;
    }

}

