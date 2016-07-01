package com.android.shipoya.shipoya2;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;


public class contentProvider extends ContentProvider {

    private static final String TAGlog = "logTag";
    DatabaseHelper db;

    @Override
    public boolean onCreate() {
        db = new DatabaseHelper(getContext(), 1);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id =  db.onInsert(values);
        return ContentUris.withAppendedId(uri, id);
    }
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
