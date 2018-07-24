package com.example.ktartanus.wordly.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.ktartanus.wordly.db.GlobalParamReaderContract;
import com.example.ktartanus.wordly.db.ReaderDbHelper;

public class GlobalParamDAO {

    private Context context;
    private SQLiteDatabase db;
    private ReaderDbHelper readerDbHelper;

    public GlobalParamDAO(Context context) {
        this.context = context;
        this.readerDbHelper = new ReaderDbHelper(context);
    }

    public GlobalParamDAO open(){
        this.db = readerDbHelper.getWritableDatabase();
    return this;
    }

    public void close()
    {
        db.close();
    }

    public String insertEntry(String key,String value)
    {
        try {
            ContentValues newValues = new ContentValues();
            // Assign values for each column.
            newValues.put(GlobalParamReaderContract.GlobalParamEntry.COLUMN_KEY, key);
            newValues.put(GlobalParamReaderContract.GlobalParamEntry.COLUMN_VALUE, value);
            // Insert the row into your table
            long result=db.insert(GlobalParamReaderContract.GlobalParamEntry.TABLE_NAME, null, newValues);
            System.out.print(result);
//            Toast.makeText(context, "User Info Saved", Toast.LENGTH_LONG).show();
        }catch(Exception ex) {
            System.out.println("Exceptions " +ex);
            Log.e("Note", "One row entered");
        }
        return "success";
    }
    // method to delete a Record of UserName
    public int deleteEntry(String key)
    {
        String where= GlobalParamReaderContract.GlobalParamEntry.COLUMN_KEY + "=?";
        int numberOFEntriesDeleted= db.delete(GlobalParamReaderContract.GlobalParamEntry.TABLE_NAME, where, new String[]{key}) ;
//        Toast.makeText(context, "Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
        return numberOFEntriesDeleted;
    }
    // method to get the password  of userName
    public String getSinlgeEntry(String key)
    {
        Cursor cursor=db.query(GlobalParamReaderContract.GlobalParamEntry.TABLE_NAME, null, GlobalParamReaderContract.GlobalParamEntry.COLUMN_KEY+ "=?", new String[]{key}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
            return "NOT EXIST";
        cursor.moveToFirst();
        String getValue= cursor.getString(cursor.getColumnIndex(GlobalParamReaderContract.GlobalParamEntry.COLUMN_VALUE));
        return getValue;
    }
    // Method to Update an Existing
    public void  updateEntry(String key,String value)
    {
        //  create object of ContentValues
        ContentValues updatedValues = new ContentValues();
        // Assign values for each Column.
        updatedValues.put(GlobalParamReaderContract.GlobalParamEntry.COLUMN_KEY, key);
        updatedValues.put(GlobalParamReaderContract.GlobalParamEntry.COLUMN_VALUE, value);
        String where= GlobalParamReaderContract.GlobalParamEntry.COLUMN_KEY + " = ?";
        db.update( GlobalParamReaderContract.GlobalParamEntry.TABLE_NAME ,updatedValues, where, new String[]{key});
    }
}
