package com.example.ktartanus.wordly.db;

import android.provider.BaseColumns;

public final class WordReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private WordReaderContract() {}

    /* Inner class that defines the table contents */
    public static class WordEntry implements BaseColumns {
        public static final String TABLE_NAME = "words";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_STATE = "state";
        public static final String COLUMN_TRANSLATION = "translation";
        public static final String COLUMN_STATE_LAST_MODYFICATION = "state_last_modyfication";

    }

    static final String SQL_CREATE_WORD_ENTRY =
            "CREATE TABLE " + WordEntry.TABLE_NAME + " (" +
                    WordEntry._ID + " INTEGER PRIMARY KEY," +
                    WordEntry.COLUMN_CONTENT + " TEXT," +
                    WordEntry.COLUMN_STATE + " TEXT," +
                    WordEntry.COLUMN_TRANSLATION + " TEXT," +
                    WordEntry.COLUMN_STATE_LAST_MODYFICATION + " TEXT)";

    static final String SQL_DELETE_WORD_ENTRY =
            "DROP TABLE IF EXISTS " + WordEntry.TABLE_NAME;
}