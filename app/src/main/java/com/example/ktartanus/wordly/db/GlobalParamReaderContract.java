package com.example.ktartanus.wordly.db;

import android.provider.BaseColumns;

public class GlobalParamReaderContract {
    public static class GlobalParamEntry implements BaseColumns {
        public static final String TABLE_NAME = "GLOBAL_PARAM";
        public static final String COLUMN_KEY = "KEY";
        public static final String COLUMN_VALUE = "VALUE";
    }

    static final String SQL_CREATE_GLOBAL_PARAM_ENTRY =
            "CREATE TABLE " + GlobalParamEntry.TABLE_NAME + " (" +
//                    WordReaderContract.WordEntry._ID + " INTEGER PRIMARY KEY," +
                    GlobalParamEntry.COLUMN_KEY  + " TEXT PRIMARY_KEY," +
                    GlobalParamEntry.COLUMN_VALUE+ " TEXT)";

    static final String SQL_DELETE_GLOBAL_PARAM_ENTRY =
            "DROP TABLE IF EXISTS " + GlobalParamEntry.TABLE_NAME;

}
