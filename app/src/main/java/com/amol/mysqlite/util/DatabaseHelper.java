package com.amol.mysqlite.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by amolmhatre on 9/22/20
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    public static final String DATABASE_NAME = "cart.db";
    public static final String TABLE_NAME = "product_table";
    public static final String COLUMN_1 = "vendor_id";
    public static final String COLUMN_2 = "product_id";
    public static final String COLUMN_3 = "product_name";
    public static final String COLUMN_4 = "product_quantity";
    public static final String COLUMN_5 = "product_price";
    public static final String COLUMN_6 = "product_total";
    public static final String COLUMN_7 = "id";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME + "(" +
                COLUMN_1 + " TEXT, " +
                COLUMN_2 + " TEXT UNIQUE, " +
                COLUMN_3 + " TEXT, " +
                COLUMN_4 + " TEXT, " +
                COLUMN_5 + " TEXT, " +
                COLUMN_6 + " TEXT, " +
                COLUMN_7 + " INTEGER PRIMARY KEY AUTOINCREMENT" + ")");
        Log.d(TAG, "Database Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        Log.d(TAG, "onUpgrade");
        onCreate(sqLiteDatabase);
    }

    public Cursor getData() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + TABLE_NAME, null);
        return cursor;

    }

    public String getVendorId() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            /** Selecting top row, first column from table, which is vendor id*/
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + COLUMN_1 + " FROM " + TABLE_NAME + " LIMIT 1", null);
            cursor.moveToFirst();
            return cursor.getString(0);
        } catch (CursorIndexOutOfBoundsException exception) {
            return "0";
        }
    }

    public boolean insertData(String vendor_id, String product_id, String product_name, String product_quantity, String product_price, String product_total) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_1, vendor_id);
        contentValues.put(COLUMN_2, product_id);
        contentValues.put(COLUMN_3, product_name);
        contentValues.put(COLUMN_4, product_quantity);
        contentValues.put(COLUMN_5, product_price);
        contentValues.put(COLUMN_6, product_total);
        long result = 0;
        try {
            result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
            Log.d(TAG, "insertData Result:" + result);
        } catch (SQLiteConstraintException exception) {
            Log.d(TAG, "Trying to insert same product id:" + result);
        }
        return result == -1 ? false : true;
    }

    public boolean updateData(String vendor_id, String product_id, String product_name, String product_quantity, String product_price, String product_total) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_1, vendor_id);
        contentValues.put(COLUMN_2, product_id);
        contentValues.put(COLUMN_3, product_name);
        contentValues.put(COLUMN_4, product_quantity);
        contentValues.put(COLUMN_5, product_price);
        contentValues.put(COLUMN_6, product_total);
        long result = sqLiteDatabase.update(TABLE_NAME, contentValues, COLUMN_2 + "=?", new String[]{product_id});
        Log.d(TAG, "updateData Result:" + result);
        return result == 0 ? false : true;

    }

    public boolean deleteData(String product_id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int result = sqLiteDatabase.delete(TABLE_NAME, COLUMN_2 + "=?", new String[]{product_id});
        Log.d(TAG, "deleteData Result:" + result);
        return result == 0 ? false : true;
    }

    public boolean deleteTable(String vendor_id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int result = sqLiteDatabase.delete(TABLE_NAME, COLUMN_1 + "=?", new String[]{vendor_id});
        Log.d(TAG, "deleteTable Result:" + result);
        return result == 0 ? false : true;
    }
}
