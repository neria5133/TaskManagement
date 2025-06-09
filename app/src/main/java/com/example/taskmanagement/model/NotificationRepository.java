package com.example.taskmanagement.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * מחלקת Repository לניהול פעולות CRUD עבור התראות במסד הנתונים.
 * מבצעת הכנסת התראות, שליפת כל ההתראות ומחיקת התראות לפי מזהה.
 */
public class NotificationRepository {

    /**
     * מוסיפה התראה חדשה למסד הנתונים.
     *
     * @param context ההקשר (Context) של האפליקציה.
     * @param notification אובייקט ההתראה להכנסה למסד הנתונים.
     */
    public static void insert(Context context, Notification notification) {
        NotificationDatabaseHelper helper = new NotificationDatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NotificationDatabaseHelper.COL_TITLE, notification.getTitle());
        values.put(NotificationDatabaseHelper.COL_DATE, notification.getDate());

        db.insert(NotificationDatabaseHelper.TABLE_NAME, null, values);
        db.close();
    }

    /**
     * מחזירה רשימה של כל ההתראות הקיימות במסד הנתונים, מסודרות לפי תאריך.
     *
     * @param context ההקשר (Context) של האפליקציה.
     * @return רשימת כל ההתראות (List של Notification).
     */
    public static List<Notification> getAll(Context context) {
        List<Notification> list = new ArrayList<>();
        NotificationDatabaseHelper helper = new NotificationDatabaseHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.query(NotificationDatabaseHelper.TABLE_NAME, null, null, null, null, null, NotificationDatabaseHelper.COL_DATE + " ASC");

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(NotificationDatabaseHelper.COL_ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(NotificationDatabaseHelper.COL_TITLE));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(NotificationDatabaseHelper.COL_DATE));

            list.add(new Notification(id, title, date));
        }

        cursor.close();
        db.close();

        return list;
    }

    /**
     * מוחקת התראה לפי מזהה (id) ממסד הנתונים.
     *
     * @param context ההקשר (Context) של האפליקציה.
     * @param id המזהה של ההתראה למחיקה.
     */
    public static void delete(Context context, int id) {
        NotificationDatabaseHelper helper = new NotificationDatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(NotificationDatabaseHelper.TABLE_NAME, NotificationDatabaseHelper.COL_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
