package com.example.hp.jiandan.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.hp.jiandan.SQLite.Mode.Entiy;
import com.example.hp.jiandan.SQLite.Mode.EntiyValue;

import java.util.ArrayList;

/**
 * Created by hp on 2016/3/18.
 */
public class SQLiteManager {

    public static SQLiteManager manager;

    public DevSQLiteOpenHelper openHelper;
    public SQLiteDatabase db;
    public ContentValues values = new ContentValues();
    public Cursor cursor;


    public static SQLiteManager getManager() {
        if (manager == null) {

            synchronized (SQLiteManager.class) {

                manager = new SQLiteManager();
            }

        }


        return manager;
    }

    public void CreateDataBase(Context context, String DatebaseName, int version, Entiy entiy, String tablename) {

        openHelper = new DevSQLiteOpenHelper(context, DatebaseName, null, version, entiy, tablename);

        db = openHelper.getWritableDatabase();

    }

    public void saveDataBase(EntiyValue entityvalue, String tablename) {

        if (db != null) {
        }

        values.clear();
        values.put(Entiy.Sresult, entityvalue.getResultValue());
        values.put(Entiy.Spage, entityvalue.getPageValue());
        values.put(Entiy.Stime, entityvalue.getTimeValue());
        db.insert(tablename, null, values);
    }

    public ArrayList<Entiy> query(String tablename) {

           ArrayList<Entiy> arrayList=new ArrayList<>();
        if (openHelper != null && db != null) {
            do {
                Entiy entiy = new Entiy();
                cursor = db.query(tablename, null, null, null, null, null, null);
                entiy.setResult(cursor.getString(cursor.getColumnIndex(Entiy.Sresult)));
                entiy.setPage(cursor.getInt(cursor.getColumnIndex(Entiy.Spage)));
                entiy.setTime(cursor.getLong(cursor.getColumnIndex(Entiy.Stime)));
                arrayList.add(entiy);
            }while (cursor.moveToNext());

            return arrayList;

        }
        return null;
    }


    public class DevSQLiteOpenHelper extends SQLiteOpenHelper {

        private Context mContext;
        public final String CREATE_TABLE;


        public DevSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, Entiy entiy, String tablename) {
            super(context, name, factory, version);
            CREATE_TABLE = "create table" + tablename + "(" + entiy.getKey() + "integer primary key autoincrement" + entiy.getResult() + "text" + entiy.getPage() + "integer" +
                    entiy.getTime() + "Long" + ")";
            mContext = context;


        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }


}
