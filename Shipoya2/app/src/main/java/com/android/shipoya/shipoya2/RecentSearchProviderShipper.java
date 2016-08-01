package com.android.shipoya.shipoya2;

import android.app.SearchManager;
import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.net.Uri;

public class RecentSearchProviderShipper extends SearchRecentSuggestionsProvider {

    public static final String AUTHORITY = RecentSearchProviderShipper.class.getName();

    DatabaseHelper helper;

    public static final int MODE = DATABASE_MODE_QUERIES;

    public RecentSearchProviderShipper() {
        setupSuggestions(AUTHORITY, MODE);
    }

    @Override
    public boolean onCreate() {
        helper = new DatabaseHelper(getContext());
        return super.onCreate();
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        System.out.println(uri + "\n" + projection + "\n" + selection + "\n" + selectionArgs);
        helper.open();

        if (selectionArgs != null && selectionArgs.length > 0 && selectionArgs[0].length() > 0) {
            return helper.getResult(selectionArgs);
        } else {
            projection = new String[]{
                    "_id",
                    "display1 AS " + SearchManager.SUGGEST_COLUMN_TEXT_1,
                    "query AS " + SearchManager.SUGGEST_COLUMN_QUERY};
            return super.query(uri, projection, selection, selectionArgs, sortOrder);
        }
    }
}
