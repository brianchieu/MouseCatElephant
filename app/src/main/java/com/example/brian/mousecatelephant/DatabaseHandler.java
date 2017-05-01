package com.example.brian.mousecatelephant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "usersManager";

    // Users table name
    private static final String TABLE_USERS = "users";

    // Users Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PASS = "password";
    private static final String KEY_WINS = "wins";
    private static final String KEY_LOSSES = "losses";
    private static final String KEY_DRAWS = "draws";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                //+ KEY_ID + " INTEGER PRIMARY KEY"
                + KEY_NAME + " TEXT PRIMARY KEY,"
                + KEY_PASS + " TEXT,"
                + KEY_WINS + " INTEGER,"
                + KEY_LOSSES + " INTEGER,"
                + KEY_DRAWS + " INTEGER"
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        // Create tables again
        onCreate(db);
    }

    //CRUD
    //Add new user
    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.getName());
        values.put(KEY_PASS, user.getPassword());
        values.put(KEY_WINS, "0");
        values.put(KEY_LOSSES, "0");
        values.put(KEY_DRAWS, "0");

        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    //Get single user
    public User getUser(String username){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_NAME,KEY_PASS, KEY_WINS, KEY_LOSSES, KEY_DRAWS},
                KEY_NAME + "=?", new String[] { String.valueOf(username) }, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        User user = new User(cursor.getString(0),
                cursor.getString(1));

        return user;
    }

    //Get all users
    public List<User> getAllUsers(){
        List<User> userList = new ArrayList<User>();

        String sql_select = "SELECT * FROM "+ TABLE_USERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql_select, null);

        if(cursor.moveToFirst()){
            while(cursor.moveToNext()){
                User user = new User();
                user.setName(cursor.getString(0));
                user.setPassword(cursor.getString(1));
                // Adding user to list
                userList.add(user);
            }
        }
        db.close();
        return userList;
    }
    public int[] getScores(String user) {
        SQLiteDatabase db = this.getReadableDatabase();
        int[] scores = {0,0,0};
        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_NAME, KEY_PASS, KEY_WINS, KEY_LOSSES, KEY_DRAWS},
                KEY_NAME + "=?", new String[] { String.valueOf(user) }, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        scores[0] = Integer.parseInt(cursor.getString(2));
        scores[1] = Integer.parseInt(cursor.getString(3));
        scores[2] = Integer.parseInt(cursor.getString(4));

        return scores;
    }
    public int updateScores(String user, String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_NAME, KEY_PASS, KEY_WINS, KEY_LOSSES, KEY_DRAWS},
                KEY_NAME + "=?", new String[] { String.valueOf(user) }, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        int wins = Integer.parseInt(cursor.getString(2));
        int losses = Integer.parseInt(cursor.getString(3));
        int draws = Integer.parseInt(cursor.getString(4));

        if (status.equals("w")) wins++;
        else if (status.equals("l")) losses++;
        else draws++;

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user);
        values.put(KEY_WINS, Integer.toString(wins));
        values.put(KEY_LOSSES, Integer.toString(losses));
        values.put(KEY_DRAWS, Integer.toString(draws));

        return db.update(TABLE_USERS, values, KEY_NAME + " =?",
                new String[] {String.valueOf(user)});
    }

    public int clearScores(User user){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_NAME,KEY_PASS, KEY_WINS, KEY_LOSSES, KEY_DRAWS},
                KEY_NAME + "=?", new String[] { String.valueOf(user) }, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.getName());
        values.put(KEY_PASS, user.getPassword());
        values.put(KEY_WINS, "0");
        values.put(KEY_LOSSES, "0");
        values.put(KEY_DRAWS, "0");

        return db.update(TABLE_USERS, values, KEY_NAME + " =?",
                new String[] {String.valueOf(user.getName())});
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_USERS);
        db.close();
    }
    //Update single user
/*    public int updateUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.getName());
        values.put(KEY_PASS, user.getPassword());

        return db.update(TABLE_USERS, values, KEY_ID + " =?",
                new String[] {String.valueOf(user.getID())});
    }

    //Delete single user
    public void deleteUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, KEY_ID + " =?",
                new String[]{String.valueOf(user.getID())});
        db.close();
    }*/
}
