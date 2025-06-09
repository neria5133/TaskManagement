package com.example.taskmanagement.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.taskmanagement.R;
import com.example.taskmanagement.model.DatabaseHelper;
import com.example.taskmanagement.model.Notification;
import com.example.taskmanagement.model.NotificationRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * {@code TaskAdapter} אחראית להצגת רשימת המטלות בתוך ListView.
 * לכל מטלה מוצגים תיאור, כפתור מחיקה וכפתור לקביעת תזכורת.
 *
 * האדפטר משתמש בפריסת {@code list_item_task.xml} להצגת כל מטלה.
 */
public class TaskAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> tasks;
    private DatabaseHelper databaseHelper;

    /**
     * בנאי של TaskAdapter.
     *
     * @param context ההקשר של האקטיביטי או הפרגמנט.
     * @param tasks   רשימת המטלות בפורמט טקסטואלי.
     */
    public TaskAdapter(Context context, List<String> tasks) {
        super(context, R.layout.list_item_task, tasks);
        this.context = context;
        this.tasks = tasks;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // לא מתועד לפי הבקשה — פעולה עם @Override
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_task, null);
        }

        TextView tvTaskDescription = convertView.findViewById(R.id.taskTextView);
        Button btnDelete = convertView.findViewById(R.id.deleteButton);
        Button btnNotify = convertView.findViewById(R.id.reminderButton);

        String task = tasks.get(position);
        tvTaskDescription.setText(task);

        String pattern = "^(.*?) - (.*?)\\s*\\(עד: (.*?)\\)$";
        java.util.regex.Pattern r = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = r.matcher(task.replace("\n", " "));

        if (m.find()) {
            String category = m.group(1).trim();
            String description = m.group(2).trim();
            String dateString = m.group(3).trim();

            btnDelete.setOnClickListener(v -> {
                deleteTask(description, category);
                tasks.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "המשימה נמחקה", Toast.LENGTH_SHORT).show();
            });

            btnNotify.setOnClickListener(v -> showNotificationDialog(dateString, description));
        }

        return convertView;
    }

    /**
     * מוחקת מטלה מתוך מסד הנתונים לפי תיאור וקטגוריה.
     *
     * @param description תיאור המטלה למחיקה.
     * @param category    קטגוריית המטלה למחיקה.
     */
    private void deleteTask(String description, String category) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete("tasks", "category = ? AND description = ?", new String[]{category, description});
        db.close();
    }

    /**
     * מציג חלונית (דיאלוג) לבחירת זמן התראה לפני מועד המטלה.
     * לאחר בחירה, מתוזמנת התראה בהתאם.
     *
     * @param dateString  מחרוזת תאריך בפורמט "yyyy-MM-dd HH:mm".
     * @param description תיאור המטלה (לצורך ההודעה בהתראה).
     */
    private void showNotificationDialog(String dateString, String description) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this.context, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions((Activity) this.context,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null && !alarmManager.canScheduleExactAlarms()) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                this.context.startActivity(intent);
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("בחר זמן להתראה");

        String[] options = {"שבוע לפני", "יום לפני", "שעה לפני"};
        builder.setItems(options, (dialog, which) -> {
            int timeOffset = 0;
            switch (which) {
                case 0:
                    timeOffset = -7 * 24 * 60 * 60 * 1000;
                    break;
                case 1:
                    timeOffset = -24 * 60 * 60 * 1000;
                    break;
                case 2:
                    timeOffset = -60 * 60 * 1000;
                    break;
            }

            NotificationRepository.insert(context, new Notification(Notification.count++, description, dateString));

            scheduleNotification(dateString, timeOffset, description);
        });

        builder.setNegativeButton("ביטול", null);
        builder.show();
    }

    /**
     * מתזמן התראה (Notification) למטלה בזמן הרצוי לפי תאריך ו-offset.
     *
     * @param dateString  מחרוזת תאריך היעד (בפורמט "yyyy-MM-dd HH:mm").
     * @param offset      הזמן ביחס לתאריך (במילישניות; שלילי לציון זמן מוקדם יותר).
     * @param description תוכן ההתראה.
     */
    private void scheduleNotification(String dateString, int offset, String description) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(dateString));
            calendar.setTimeInMillis(calendar.getTimeInMillis() + offset);

            if (calendar.getTimeInMillis() > System.currentTimeMillis()) {
                Intent intent = new Intent(context, NotificationReceiver.class);
                intent.putExtra("task_description", description);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        context,
                        (int) System.currentTimeMillis(),
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                );

                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                Toast.makeText(context, "התראה נקבעה", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "אי אפשר להגדיר התראה לזמן שעבר", Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(context, "תאריך לא תקין", Toast.LENGTH_SHORT).show();
        }
    }
}
