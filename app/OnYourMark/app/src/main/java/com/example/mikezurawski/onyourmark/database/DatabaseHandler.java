package com.example.mikezurawski.onyourmark.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = "budgetTracker";

    // Contacts table name
    private static final String TABLE_BUDGETS = "budgetItems";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_DAY = "day";
    private static final String KEY_MONTH = "month";
    private static final String KEY_YEAR = "year";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_AMOUNT_SPENT = "amount_spent";
    private static final String KEY_SPENDING_LIMIT = "spending_limit";

    // ticker system for unique IDs
    private static int idTicker = 0;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_BUDGETS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_DAY + " INTEGER,"
                + KEY_MONTH + " INTEGER,"
                + KEY_YEAR + " INTEGER,"
                + KEY_CATEGORY + " INTEGER,"
                + KEY_AMOUNT_SPENT + " DOUBLE,"
                + KEY_SPENDING_LIMIT + " DOUBLE" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    public void resetDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGETS);
        onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGETS);

        // Create tables again
        onCreate(db);
    }

    public void addBudgetItem(final BudgetItem budgetItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, idTicker++);
        values.put(KEY_DAY, getFromDate(budgetItem.getDate(), Calendar.DAY_OF_WEEK));
        values.put(KEY_MONTH, getFromDate(budgetItem.getDate(), Calendar.MONTH));
        values.put(KEY_YEAR, getFromDate(budgetItem.getDate(), Calendar.YEAR));
        values.put(KEY_CATEGORY, budgetItem.getCategory());
        values.put(KEY_AMOUNT_SPENT, budgetItem.getCost());
        values.put(KEY_SPENDING_LIMIT, getSpendingLimit());

        db.insert(TABLE_BUDGETS, null, values);
        db.close();
    }

    private Integer getFromDate(final Date date, final int type) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(type);
    }

    private Date createDateObject(final int day, final int month, final int year) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, 0, 0);
        return c.getTime();
    }

    // Get from settings
    private String getSpendingLimit() {
        return "0"; // TODO: do
    }

    private String getCategoryString(int i) {
        switch(i) {
            case 1:
                return "FOOD";
            case 2:
                return "CLOTHES";
            case 3:
                return "GAS";
            case 4:
                return "UTILITIES";
            case 0:
                return "MISC";
            default:
                return "";
        }
    }

    public ArrayList<BudgetItem> getBudgetItems() {
        ArrayList<BudgetItem> items = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_BUDGETS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                BudgetItem budgetItem = new BudgetItem();

                budgetItem.setId(cursor.getString(0));
                budgetItem.setDate(createDateObject(
                        cursor.getInt(1),  // Day
                        cursor.getInt(2),  // Month
                        cursor.getInt(3))); // Year
                budgetItem.setCategory(cursor.getInt(4));
                budgetItem.setCost(cursor.getDouble(5));

                items.add(budgetItem);
            } while (cursor.moveToNext());
        }

        return items;
    }

    public ArrayList<BudgetItem> getBudgetItems(final int month) {
        ArrayList<BudgetItem> items = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_BUDGETS + " "
                + "WHERE " + KEY_MONTH + " = '" + month + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                BudgetItem budgetItem = new BudgetItem();

                budgetItem.setId(cursor.getString(0));
                budgetItem.setDate(createDateObject(
                        cursor.getInt(1),  // Day
                        cursor.getInt(2),  // Month
                        cursor.getInt(3))); // Year
                budgetItem.setCategory(cursor.getInt(4));
                budgetItem.setCost(cursor.getDouble(5));

                items.add(budgetItem);
            } while (cursor.moveToNext());
        }

        return items;
    }

    // TODO: Probably delete?
//    public List<BudgetItem> getAllContacts() {
//        List<BudgetItem> itemsList = new ArrayList<>();
//
//        String selectQuery = "SELECT * FROM " + TABLE_BUDGETS;
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                BudgetItem budgetItem = new BudgetItem();
//                budgetItem.setLocation(cursor.getString(1));
//                budgetItem.setDate(cursor.getString(2));
//
//                itemsList.add(budgetItem);
//            } while (cursor.moveToNext());
//        }
//
//        return itemsList;
//    }

    public int getBudgetItemCount() {
        String countQuery = "SELECT  * FROM " + TABLE_BUDGETS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        return cursor.getCount();
    }

    public int getBudgetItemCount(final int month) {
        String countQuery = "SELECT * FROM " + TABLE_BUDGETS
                + "WHERE " + KEY_MONTH + " = '" + month + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        return cursor.getCount();
    }

//    public void deleteBudgetItem(BudgetItem budgetItem) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_BUDGETS, KEY_ID + " = ?",
//                new String[] { String.valueOf(budgetItem.getID()) });
//        db.close();
//    }

}
