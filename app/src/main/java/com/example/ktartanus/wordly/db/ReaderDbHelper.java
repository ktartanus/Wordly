package com.example.ktartanus.wordly.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ktartanus.wordly.model.GlobalParam;

public class ReaderDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "WordReader.db";

    public ReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(WordReaderContract.SQL_CREATE_WORD_ENTRY);
        db.execSQL(GlobalParamReaderContract.SQL_CREATE_GLOBAL_PARAM_ENTRY);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(WordReaderContract.SQL_DELETE_WORD_ENTRY);
        db.execSQL(GlobalParamReaderContract.SQL_DELETE_GLOBAL_PARAM_ENTRY);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
