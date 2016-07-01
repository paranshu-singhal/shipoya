package com.android.shipoya.shipoya2;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String TAGlog = "logTag";
    private static int DATABASE_VERSION = 1;
    private static String DATABASE_NAME = "ordersDB";

    private static final String TABLE_NAME_ORDERS = "ViewOrders";
    private static final String COLUMN_NAME_ORDERS_1 ="order_id";
    private static final String COLUMN_NAME_ORDERS_2 ="source";
    private static final String COLUMN_NAME_ORDERS_3 ="destination";
    private static final String COLUMN_NAME_ORDERS_4 ="date";

    public DatabaseHelper(Context context,int version) {
        super(context, DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME_ORDERS + "( " + COLUMN_NAME_ORDERS_1 + " TEXT, " +
                COLUMN_NAME_ORDERS_2 + " TEXT, "+
                COLUMN_NAME_ORDERS_3 + " TEXT, "+
                COLUMN_NAME_ORDERS_4 + " TEXT); ";
        Log.d(TAGlog, query);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ORDERS);
        onCreate(db);
    }

    public long onInsert(ContentValues values){
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(TABLE_NAME_ORDERS, null, values);
    }
}
