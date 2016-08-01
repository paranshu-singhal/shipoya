package com.android.shipoya.shipoya2;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper {
    Context context;
    InnerHelper helper;
    String APP_DATABASE = "SuccessPlayer.db";
    public String TABLE_ORDERS = "orders";
    public String TABLE_TRUCKS = "trucks";
    public String TABLE_LOADS = "loads";
    public String TABLE_SUGGESTIONS = "recent";
    public String TABLE = "suggestions";
    SQLiteDatabase database;

    private static final String TAGlog = "logTag";

    public DatabaseHelper(Context context) {
        this.context = context;
        helper = new InnerHelper(context, APP_DATABASE, null, 1);
    }

    public DatabaseHelper open() {
        database = helper.getWritableDatabase();
        return this;
    }

    public boolean isOpen() {
        return database.isOpen();
    }

    public void close() {
        database.close();
    }

    public long insertData(String table, ContentValues values) {
        return database.insert(table, null, values);
    }

    public void deleteTable(String table) {
        database.execSQL("DELETE from " + table + ";");
    }

    public Cursor selectWhere(String table, String where) {
        return database.rawQuery("select * from " + table + " where label = '" + where + "';", null);
    }

    public Cursor getResult(String query) {
        return database.rawQuery("select label AS " + SearchManager.SUGGEST_COLUMN_TEXT_1 + " from " + TABLE + " where label LIKE '%" + query + "%';", null);
    }

    public Cursor getResult(String[] selectionArgs) {
        return database.rawQuery("select * from " + TABLE_SUGGESTIONS + " where " + SearchManager.SUGGEST_COLUMN_TEXT_1 + " LIKE '%" + selectionArgs[0] + "%';", null);
    }

    public Cursor getData(String table) {
        return database.query(table, null, null, null, null, null, null);
    }

    public class InnerHelper extends SQLiteOpenHelper {

        public InnerHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                           int version) {
            super(context, name, factory, version);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table " + TABLE + "(" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "label string not null" +
                    ");");

            db.execSQL("create table " + TABLE_SUGGESTIONS + "(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SearchManager.SUGGEST_COLUMN_TEXT_1 + " string not null unique," +
                    SearchManager.SUGGEST_COLUMN_QUERY + " string not null unique," +
                    SearchManager.EXTRA_DATA_KEY + " long not null" +
                    ");");

            db.execSQL("create table " + TABLE_LOADS + "(" +
                    "order_id string not null," +
                    "order_name string not null," +
                    "material_type string not null," +
                    "shipper_name string not null" +
                    ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
            // TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOADS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUGGESTIONS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRUCKS);
        }

    }

}
