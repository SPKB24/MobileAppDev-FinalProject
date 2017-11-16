# Team Mark - Week 2

## Team

Researched and decided on a OCR based API to use - Microsoft Azure

## Individual

### Dave

![Main Menu]("menu.png")

### Mark

### Sohit
* Created Object classes and SQLite database for the information that we want to store.

```Java
@Override
public void onCreate(SQLiteDatabase db) {
    String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_BUDGETS + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_LOCATION + " TEXT,"
            + KEY_DATE + " TEXT" + ")";
    db.execSQL(CREATE_CONTACTS_TABLE);
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
```
