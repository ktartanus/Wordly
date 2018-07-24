package com.example.ktartanus.wordly.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android. widget.Toast;

import com.example.ktartanus.wordly.db.WordReaderContract;
import com.example.ktartanus.wordly.db.ReaderDbHelper;
import com.example.ktartanus.wordly.model.Word.WORD_STATE;

import java.util.ArrayList;
import java.util.List;

public class WordDAO {
    private ReaderDbHelper readerDbHelper;

    private SQLiteDatabase db;
    private final Context context;

    public WordDAO(Context context) {
        this.readerDbHelper = new ReaderDbHelper(context);
        this.context = context;
    }

    public WordDAO open() throws SQLException{
        this.db = readerDbHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        db.close();
    }

    public String insertWord(Word word)
    {
        try {
            ContentValues newValues = new ContentValues();
            newValues.put(WordReaderContract.WordEntry.COLUMN_CONTENT, word.getContent());
            newValues.put(WordReaderContract.WordEntry.COLUMN_STATE, word.getState().name());
            newValues.put(WordReaderContract.WordEntry.COLUMN_STATE_LAST_MODYFICATION, word.getContent());
            newValues.put(WordReaderContract.WordEntry.COLUMN_TRANSLATION, word.getTranslation());

            // Insert the row into your table
            long result=db.insert(WordReaderContract.WordEntry.TABLE_NAME, null, newValues);
        }catch(Exception ex) {
            System.out.println("Exceptions " +ex);
        }
        return "success";
    }

    public int deleteWord(Word word)
    {
        String where= WordReaderContract.WordEntry.COLUMN_CONTENT + "=?";
        int numberOFEntriesDeleted= db.delete(WordReaderContract.WordEntry.TABLE_NAME, where, new String[]{word.getContent()}) ;
//        Toast.makeText(context, "Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
        return numberOFEntriesDeleted;
    }

    public Word getSinlgeEntry(String word)
    {
        String where= WordReaderContract.WordEntry.COLUMN_CONTENT + "=?";
        Cursor cursor=db.query(WordReaderContract.WordEntry.TABLE_NAME, null, where, new String[]{word}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
            return null;
        cursor.moveToFirst();
        String rowContent = cursor.getString(cursor.getColumnIndex(WordReaderContract.WordEntry.COLUMN_CONTENT));
        WORD_STATE rowState = WORD_STATE.valueOf( cursor.getString(cursor.getColumnIndex(WordReaderContract.WordEntry.COLUMN_STATE)) );
        String rowDate = cursor.getString(cursor.getColumnIndex(WordReaderContract.WordEntry.COLUMN_STATE_LAST_MODYFICATION));
        String rowTranslation = cursor.getString(cursor.getColumnIndex(WordReaderContract.WordEntry.COLUMN_TRANSLATION));

        return new Word(rowContent,rowState, rowDate, rowTranslation);
    }


    public void  updateEntry(Word word)
    {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(WordReaderContract.WordEntry.COLUMN_STATE, word.getState().name());
        updatedValues.put(WordReaderContract.WordEntry.COLUMN_CONTENT, word.getContent() );
        updatedValues.put(WordReaderContract.WordEntry.COLUMN_STATE_LAST_MODYFICATION, word.getState_last_modyfication_date() );
        updatedValues.put(WordReaderContract.WordEntry.COLUMN_TRANSLATION, word.getTranslation() );
        String where= WordReaderContract.WordEntry.COLUMN_CONTENT+ "= ?";
        db.update(WordReaderContract.WordEntry.TABLE_NAME ,updatedValues, where, new String[]{word.getContent()});
    }

    public List<Word> getAllWordsByState(WORD_STATE word_state){
        String where= WordReaderContract.WordEntry.COLUMN_STATE + "=?";
        Cursor cursor=db.query(WordReaderContract.WordEntry.TABLE_NAME, null, where, new String[]{word_state.name()}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
            return null;
        List<Word> words = new ArrayList<>();
        cursor.moveToFirst();
        while(!cursor.isLast()){
            String rowContent = cursor.getString(cursor.getColumnIndex(WordReaderContract.WordEntry.COLUMN_CONTENT));
            WORD_STATE rowState = WORD_STATE.valueOf( cursor.getString(cursor.getColumnIndex(WordReaderContract.WordEntry.COLUMN_STATE)) );
            String rowDate = cursor.getString(cursor.getColumnIndex(WordReaderContract.WordEntry.COLUMN_STATE_LAST_MODYFICATION));
            String rowTranslation = cursor.getString(cursor.getColumnIndex(WordReaderContract.WordEntry.COLUMN_TRANSLATION));
            Word singleWord = new Word(rowContent,rowState, rowDate,rowTranslation);
            words.add(singleWord);
            cursor.moveToNext();
        }
        return words;
    }

    public Word selectRandomWordByState(WORD_STATE word_state){

        String where= WordReaderContract.WordEntry.COLUMN_STATE + "=?";
        Cursor cursor=db.query(WordReaderContract.WordEntry.TABLE_NAME, null, where, new String[]{word_state.name()}, null, null, "random()", "1");
        if(cursor.getCount()<1) // UserName Not Exist
            return null;
        cursor.moveToFirst();
            String rowContent = cursor.getString(cursor.getColumnIndex(WordReaderContract.WordEntry.COLUMN_CONTENT));
            WORD_STATE rowState = WORD_STATE.valueOf( cursor.getString(cursor.getColumnIndex(WordReaderContract.WordEntry.COLUMN_STATE)) );
            String rowDate = cursor.getString(cursor.getColumnIndex(WordReaderContract.WordEntry.COLUMN_STATE_LAST_MODYFICATION));
            String rowTranslation = cursor.getString(cursor.getColumnIndex(WordReaderContract.WordEntry.COLUMN_TRANSLATION));

        Word singleWord = new Word(rowContent,rowState, rowDate, rowTranslation);
        return singleWord;

    }

}
