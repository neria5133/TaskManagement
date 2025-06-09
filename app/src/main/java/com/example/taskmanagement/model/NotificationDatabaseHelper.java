package com.example.taskmanagement.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * מחלקה שמנהלת את מסד הנתונים SQLite של ההתראות.
 * יוצרת טבלה לשמירת התראות הכוללת מזהה, כותרת ותאריך.
 */
public class NotificationDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "notifications_db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_NAME = "notifications";
    public static final String COL_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_DATE = "date";

    /**
     * בנאי עבור NotificationDatabaseHelper.
     *
     * @param context ההקשר (Context) של האפליקציה.
     */
    public NotificationDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT, " +
                COL_DATE + " TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
