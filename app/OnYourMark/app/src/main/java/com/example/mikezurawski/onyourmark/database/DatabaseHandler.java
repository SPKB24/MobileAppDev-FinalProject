package com.example.mikezurawski.onyourmark.database;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.example.mikezurawski.onyourmark.other.Constants;
import com.example.mikezurawski.onyourmark.views.MainActivity;
import com.example.mikezurawski.onyourmark.views.SettingsActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 8;

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

    // File Storage Location Path Item Thing
    private static String CURRENT_DB_FILE_PATH;
    private static String EXPORTED_DB_FILE_PATH;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        CURRENT_DB_FILE_PATH = context.getDatabasePath(DATABASE_NAME).getPath();
        EXPORTED_DB_FILE_PATH = Environment.getExternalStorageDirectory() + "/" + DATABASE_NAME + ".db";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_BUDGETS + "("
//                + KEY_ID + " INTEGER PRIMARY KEY,"
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
//        values.put(KEY_ID, idTicker++);
        values.put(KEY_DAY, getFromDate(budgetItem.getDate(), Calendar.DAY_OF_WEEK));
        values.put(KEY_MONTH, getFromDate(budgetItem.getDate(), Calendar.MONTH));
        values.put(KEY_YEAR, getFromDate(budgetItem.getDate(), Calendar.YEAR));
        values.put(KEY_CATEGORY, budgetItem.getCategory());
        values.put(KEY_AMOUNT_SPENT, budgetItem.getCost());
        values.put(KEY_SPENDING_LIMIT, getSpendingLimit());

        System.out.println("ADDING VALUE TO TABLE");
        System.out.println("******************************");
        System.out.println("id: " + budgetItem.getId());
        System.out.println("category: " + budgetItem.getCategory());
        System.out.println("date: " + budgetItem.getDate());
        System.out.println("cost: " + String.format("%.2f", budgetItem.getCost()));
        System.out.println("******************************");

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

                budgetItem.setDate(createDateObject(
                        cursor.getInt(0),   // Day
                        cursor.getInt(1),   // Month
                        cursor.getInt(2))); // Year
                budgetItem.setCategory(cursor.getInt(3));
                budgetItem.setCost(cursor.getDouble(4));

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

                budgetItem.setDate(createDateObject(
                        cursor.getInt(0),   // Day
                        cursor.getInt(1),   // Month
                        cursor.getInt(2))); // Year
                budgetItem.setCategory(cursor.getInt(3));
                budgetItem.setCost(cursor.getDouble(4));

                items.add(budgetItem);
            } while (cursor.moveToNext());
        }

        return items;
    }

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

    public static void importDatabase(Activity context) {

        // Check if user has given READ_EXTERNAL_STORAGE Permission
        boolean hasPermission = (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

        if (!hasPermission) {
            // Send request for permission. Handler is in SettingsActivity
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    Constants.REQUEST_READ_PERMISSION_ID);
        } else {
            // Otherwise, go ahead and import
            try {
                final File importFile = new File(EXPORTED_DB_FILE_PATH);

                if (!importFile.exists()) {
                    Toast.makeText(context, "No Backup file could be found", Toast.LENGTH_SHORT).show();
                    return;
                }
                FileInputStream importFileStream = new FileInputStream(importFile);

                File currDbFile = new File(CURRENT_DB_FILE_PATH);
                OutputStream currDbFileStream = new FileOutputStream(currDbFile);

                // Start fresh
                currDbFile.delete();
                currDbFile.createNewFile();

                copyFile(importFileStream, currDbFileStream);

                currDbFileStream.flush();
                currDbFileStream.close();
                importFileStream.close();

                Toast.makeText(context, "Import completed successfully!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(context, "Something went wrong. File was unable to be imported", Toast.LENGTH_SHORT).show();
            }


        }
    }

    public static void exportDatabase(Activity context) {

        // Check if user has given WRITE_EXTERNAL_STORAGE Permission
        boolean hasPermission = (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

        if (!hasPermission) {
            // Send request for permission. Handler is in SettingsActivity
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constants.REQUEST_WRITE_PERMISSION_ID);
        } else {
            // Otherwise, go ahead and export
            try {
                File currDbFile = new File(CURRENT_DB_FILE_PATH);
                FileInputStream currDbFileStream = new FileInputStream(currDbFile);

                File exportedDbFile = new File(EXPORTED_DB_FILE_PATH);

                if (exportedDbFile.exists())
                    exportedDbFile.delete();
                exportedDbFile.createNewFile();

                OutputStream exportedDbFileStream = new FileOutputStream(EXPORTED_DB_FILE_PATH);

                copyFile(currDbFileStream, exportedDbFileStream);

                exportedDbFileStream.flush();
                exportedDbFileStream.close();
                currDbFileStream.close();

                Toast.makeText(context, "File Saved to " + EXPORTED_DB_FILE_PATH, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(context, "Something went wrong. File was unable to be saved", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static void copyFile(FileInputStream from, OutputStream to) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = from.read(buffer)) > 0){
            to.write(buffer, 0, length);
        }
    }
}
