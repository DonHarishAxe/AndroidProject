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
    private static final String DATABASE_NAME = "k16";

    // Tables name
    private static final String TABLE_LISTNAMES = "listnames";
    private static final String TABLE_LISTS_NEW = "new_list";
    private static final String TABLE_LISTS_DONE = "done_list";

    // Table Columns names
    private static final String ID = "_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_TASK = "task";
    private static final String KEY_COUNT = "count";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_1 = "CREATE TABLE " + TABLE_LISTNAMES + "("
                + ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT" + ")";
        // + KEY_COUNT + " INTEGER" + ")";
        String CREATE_TABLE_2 = "CREATE TABLE " + TABLE_LISTS_NEW + "("
                + ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_TASK + " TEXT" + ")";

        String CREATE_TABLE_3 = "CREATE TABLE " + TABLE_LISTS_DONE + "("
                + ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_TASK + " TEXT" + ")";

        db.execSQL(CREATE_TABLE_1);
        db.execSQL(CREATE_TABLE_2);
        db.execSQL(CREATE_TABLE_3);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTNAMES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTS_NEW);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTS_DONE);

        // Create tables again
        onCreate(db);
    }

    public void db_merge(String s1,String s2,String s3)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, s3); // Contact Name
        //values.put(KEY_COUNT, 0);
        // Inserting Row
        db.insert(TABLE_LISTNAMES, null, values);
        db.update(TABLE_LISTS_NEW, values, KEY_NAME + "= ?",new String [] {s1});
        db.update(TABLE_LISTS_NEW, values, KEY_NAME + "= ?",new String [] {s2});
        db.update(TABLE_LISTS_DONE, values, KEY_NAME + "= ?",new String [] {s1});
        db.update(TABLE_LISTS_DONE, values, KEY_NAME + "= ?",new String [] {s2});
        deleteListName(s1);
        deleteListName(s2);
        db.close();
    }
    public void incCount(String s)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<String> temp = new ArrayList<>();

        String[] projection = { KEY_COUNT,KEY_NAME};
        String selection =  KEY_NAME + " LIKE ? ";
        String[] selectionArgs = { s };

        Cursor c = db.query(TABLE_LISTNAMES, projection, selection, selectionArgs, null, null, null);
        int val=0;
        if(c.moveToFirst()) {
            do{
                val=c.getInt(0);
            }while(c.moveToNext());
        }
        val++;
        ContentValues values = new ContentValues();
        values.put(KEY_COUNT, val);
        db.update(TABLE_LISTNAMES, values, KEY_NAME + "= ?",new String [] {s});
        db.close();
    }
    public void createDuplicate(String s,String newname)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, newname); // Contact Name
        //values.put(KEY_COUNT, 0);
        db.insert(TABLE_LISTNAMES, null, values);

        String[] projection = { KEY_NAME , KEY_TASK};
        String selection =  KEY_NAME + " LIKE ? ";
        String[] selectionArgs = { s };

        Cursor c = db.query(TABLE_LISTS_DONE, projection, selection, selectionArgs, null, null, null);

        if(c.moveToFirst()) {
            do{
                //temp.add(c.getString(1));
                String task_name=c.getString(1);
                values = new ContentValues();
                values.put(KEY_NAME, newname);
                values.put(KEY_TASK, task_name);
                db.insert(TABLE_LISTS_DONE,null,values);
            }while(c.moveToNext());
        }
        c = db.query(TABLE_LISTS_NEW, projection, selection, selectionArgs, null, null, null);

        if(c.moveToFirst()) {
            do{
                //temp.add(c.getString(1));
                String task_name=c.getString(1);
                values = new ContentValues();
                values.put(KEY_NAME, newname);
                values.put(KEY_TASK, task_name);
                db.insert(TABLE_LISTS_NEW,null,values);
            }while(c.moveToNext());
        }
        db.close();
    }
    public void addListName(CheckListObject checkListObject) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, checkListObject.getTitle()); // Contact Name
        //values.put(KEY_COUNT , 0);
        // Inserting Row
        db.insert(TABLE_LISTNAMES, null, values);
        db.close(); // Closing database connection
    }

    public void addNewList(CheckListObject checkListObject) {
        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<String> st = checkListObject.getTasks();

        for(int i = 0; i < st.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, checkListObject.getTitle()); // Contact Name
            values.put(KEY_TASK, st.get(i));

            db.insert(TABLE_LISTS_NEW, null, values);
        }

        db.close(); // Closing database connection
    }

    public void addOldList(CheckListObject checkListObject) {
        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<String> st = checkListObject.getTasks();

        for(int i = 0; i < st.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, checkListObject.getTitle()); // Contact Name
            values.put(KEY_TASK, st.get(i));

            db.insert(TABLE_LISTS_DONE, null, values);
        }

        db.close(); // Closing database connection
    }
    public ArrayList<String> getListNames() {
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<String> taskNames = new ArrayList<>();

        String[] projection = { KEY_NAME };
        String yourColumn=KEY_COUNT;
        //yourColumn+" DESC"
        Cursor c = db.query(TABLE_LISTNAMES, projection, null, null, null, null , null );

        if(c.moveToFirst()){
            do{
                taskNames.add(c.getString(0));
            }while(c.moveToNext());
        }

        db.close();
        return taskNames;
    }

    public int getId(String temp) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {ID, KEY_NAME };
        String selection = KEY_NAME + " LIKE ? ";
        String[] selectionArgs = { temp };

        Cursor c = db.query(TABLE_LISTNAMES, projection, selection, selectionArgs, null, null, null);

        if(c.moveToFirst()){
            return c.getInt(1);
        }
        else return 0;
    }

    public CheckListObject getOldList(String title) {
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<String> temp = new ArrayList<>();

        String[] projection = { KEY_NAME, KEY_TASK };
        String selection =  KEY_NAME + " LIKE ? ";
        String[] selectionArgs = { title };

        Cursor c = db.query(TABLE_LISTS_DONE, projection, selection, selectionArgs, null, null, null);

        if(c.moveToFirst()) {
            do{
                temp.add(c.getString(1));
            }while(c.moveToNext());
        }

        db.close();
        return new CheckListObject(title, temp);
    }

    public CheckListObject getNewList(String title) {
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<String> temp = new ArrayList<>();

        String[] projection = { KEY_NAME, KEY_TASK };
        String selection =  KEY_NAME + " LIKE ? ";
        String[] selectionArgs = { title };

        Cursor c = db.query(TABLE_LISTS_NEW, projection, selection, selectionArgs, null, null, null);

        if(c.moveToFirst()) {
            do{
                temp.add(c.getString(1));
            }while(c.moveToNext());
        }

        db.close();
        return new CheckListObject(title, temp);
    }
    public String search(String title) {
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<String> temp = new ArrayList<>();

        String[] projection = { KEY_NAME};
        String selection =  KEY_NAME + " LIKE ? ";
        String[] selectionArgs = { title };

        Cursor c = db.query(TABLE_LISTNAMES, projection, selection, selectionArgs, null, null, null);

        if(c.moveToFirst()) {
            do{
                //temp.add(c.getString(1));
                return "Found checklist";
            }while(c.moveToNext());
        }
        String[] projection1 = { KEY_NAME,KEY_TASK};
        String selection1 =  KEY_TASK + " LIKE ? ";

        c = db.query(TABLE_LISTS_DONE, projection1, selection1, selectionArgs, null, null, null);
        if(c.moveToFirst()) {
            do{
                //temp.add(c.getString(1));
                return "Found completed item in "+c.getString(0)+" checklist";
            }while(c.moveToNext());
        }
        c = db.query(TABLE_LISTS_NEW, projection1, selection1, selectionArgs, null, null, null);

        if(c.moveToFirst()) {
            do{
                //temp.add(c.getString(1));
                return "Found pending item in "+c.getString(0)+" checklist";
            }while(c.moveToNext());
        }

        db.close();
        return "Item/Checklist Not found";
        //return new CheckListObject(title, temp);

    }


    public void deleteNewTask(String task) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = KEY_TASK + " LIKE ?";
        String[] selectionArgs = { task };

        db.delete(TABLE_LISTS_NEW, selection, selectionArgs);
        db.close();
    }

    public void deleteOldTask(String task){
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = KEY_TASK + " LIKE ?";
        String[] selectionArgs = { task };

        db.delete(TABLE_LISTS_DONE, selection, selectionArgs);
        db.close();
    }


    public void deleteListName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = KEY_NAME + " LIKE ?";
        String[] selectionArgs = { name };

        db.delete(TABLE_LISTNAMES, selection, selectionArgs);
        db.delete(TABLE_LISTS_DONE, selection, selectionArgs);
        db.delete(TABLE_LISTS_NEW, selection, selectionArgs);

        db.close();
   }
}