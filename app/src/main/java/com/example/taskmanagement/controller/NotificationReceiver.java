package com.example.taskmanagement.controller;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.taskmanagement.R;

/**
 * {@code NotificationReceiver} היא מחלקה שמטפלת בקבלת התראות מתוזמנות
 * (למשל באמצעות AlarmManager) ומציגה התראה למשתמש.
 *
 * ההתראה מציגה את קטגוריית המטלה ואת התאריך שלה.
 *
 * המחלקה פועלת כברודקאסט רסיבר, כלומר מופעלת כאשר מתקבל אינטנט מתאים,
 * למשל בתזמון מוגדר מראש.
 */
public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String taskTitleAndDate = intent.getStringExtra("task_category") + "  " + intent.getStringExtra("date");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // יצירת ערוץ התראות במערכות הפעלה Android O ומעלה
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("task_channel", "תזכורות משימות", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        // בניית התראה עם אייקון, כותרת ותוכן
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "task_channel")
                .setSmallIcon(R.drawable.baseline_circle_notifications_24)
                .setContentTitle("תזכורת למשימה")
                .setContentText(taskTitleAndDate)
                .setAutoCancel(true); // ההודעה תיסגר כשלוחצים עליה

        // הצגת ההתראה עם מזהה ייחודי (מבוסס על הזמן הנוכחי)
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}