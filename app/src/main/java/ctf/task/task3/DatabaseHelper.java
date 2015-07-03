package ctf.task.task3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Task3";

    // Tables name
    private static final String TABLE_LISTNAMES = "listnames";
    private static final String TABLE_LISTS = "lists";

    // Table Columns names
    private static final String KEY_ID_1 = "id";
    private static final String KEY_ID_2 = "id";
    private static final String KEY_LISTNAME = "name";
    private static final String KEY_TITLE = "c1_name";
    private static final String KEY_TASK = "task";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_1 = "CREATE TABLE " + TABLE_LISTNAMES + "("
                + KEY_ID_1 + " INTEGER PRIMARY KEY," + KEY_LISTNAME + " TEXT " + ")";

        String CREATE_TABLE_2 = "CREATE TABLE " + TABLE_LISTS + "("
                + KEY_ID_2 + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
                + KEY_TASK + " TEXT" + ")";

        db.execSQL(CREATE_TABLE_1);
        db.execSQL(CREATE_TABLE_2);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTNAMES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTS);

        // Create tables again
        onCreate(db);
    }


    public void addListName(CheckListObject checkListObject) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LISTNAME, checkListObject.getTitle()); // Contact Name

        // Inserting Row
        db.insert(TABLE_LISTNAMES, null, values);
        db.close(); // Closing database connection
    }

    public void addList(CheckListObject checkListObject) {
        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<String> st = checkListObject.getTasks();

        for(int i = 0; i < st.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(KEY_TITLE, checkListObject.getTitle()); // Contact Name
            values.put(KEY_TASK, st.get(i));

            db.insert(TABLE_LISTS, null, values);
        }

        db.close(); // Closing database connection
    }

    public ArrayList<String> getListNames() {
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<String> taskNames = new ArrayList<>();

        String[] projection = { KEY_LISTNAME };

        Cursor c = db.query(TABLE_LISTNAMES, projection, null, null, null, null ,null);

        if(c.moveToFirst()){
            do{
                taskNames.add(c.getString(0));
            }while(c.moveToNext());
        }

        db.close();
        return taskNames;
    }

    public CheckListObject getList(String title) {
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<String> temp = new ArrayList<>();

        String[] projection = { KEY_TITLE, KEY_TASK };
        String selection =  KEY_TITLE + " LIKE ? ";
        String[] selectionArgs = { title };

        Cursor c = db.query( TABLE_LISTS, projection, selection, selectionArgs, null, null, null);

        if(c.moveToFirst()) {
            do{
                temp.add(c.getString(1));
            }while(c.moveToNext());
        }

        db.close();
        return new CheckListObject(title, temp);
    }

    public void deleteListName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = KEY_LISTNAME + " LIKE ?";
        String[] selectionArgs = { name };

        db.delete(TABLE_LISTNAMES, selection, selectionArgs);
        db.close();
    }
}