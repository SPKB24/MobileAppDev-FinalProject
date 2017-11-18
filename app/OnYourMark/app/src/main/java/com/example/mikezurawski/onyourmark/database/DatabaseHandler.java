package com.example.mikezurawski.onyourmark.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sohit on 11/10/17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "budgetTracker";

    // Contacts table name
    private static final String TABLE_BUDGETS = "budgetItems";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_DATE = "date";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_BUDGETS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_LOCATION + " TEXT,"
                + KEY_DATE + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGETS);

        // Create tables again
        onCreate(db);
    }

    public void addBudgetItem(BudgetItem budgetItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, budgetItem.getID()); // TODO: Definitely bad...
        values.put(KEY_DATE, budgetItem.getDate().toString()); // TODO: Probably bad...
        values.put(KEY_LOCATION, budgetItem.getLocation());
        //values.put(KEY_ITEMS, budgetItem.getItems()); // TODO: Figure this out

        db.insert(TABLE_BUDGETS, null, values);
        db.close(); // Closing com.example.mikezurawski.onyourmark.database connection
    }

    public BudgetItem getBudgetItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_BUDGETS, new String[] { KEY_ID,
                        KEY_LOCATION, KEY_DATE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        int _id = Integer.parseInt(cursor.getString(0));
        String _location = cursor.getString(1);
        String _date = cursor.getString(2);

        BudgetItem budgetItem = new BudgetItem();
        budgetItem.setDate(_date).setLocation(_location); // TODO: Items
        return budgetItem;
    }

    public List<BudgetItem> getAllContacts() {
        List<BudgetItem> itemsList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_BUDGETS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                BudgetItem budgetItem = new BudgetItem();
                budgetItem.setLocation(cursor.getString(1));
                budgetItem.setDate(cursor.getString(2)); // TODO: Items and id and sdjfhksjhf

                itemsList.add(budgetItem);
            } while (cursor.moveToNext());
        }

        return itemsList;
    }

    public int getBudgetItemCount() {
        String countQuery = "SELECT  * FROM " + TABLE_BUDGETS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    public int updateBudgetItem(BudgetItem budgetItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, "GET ID FROM SOMEWHERE"); // TODO: Definitely bad...
        values.put(KEY_DATE, budgetItem.getDate().toString()); // TODO: Probably bad...
        values.put(KEY_LOCATION, budgetItem.getLocation());
        //values.put(KEY_ITEMS, budgetItem.getItems()); // TODO: Figure this out

        // updating row
        return db.update(TABLE_BUDGETS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(budgetItem.getID()) });
    }

    public void deleteBudgetItem(BudgetItem budgetItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BUDGETS, KEY_ID + " = ?",
                new String[] { String.valueOf(budgetItem.getID()) });
        db.close();
    }

}
