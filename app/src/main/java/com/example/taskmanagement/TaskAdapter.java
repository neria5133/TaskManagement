package com.example.taskmanagement;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> tasks;
    private DatabaseHelper databaseHelper;

    public TaskAdapter(Context context, List<String> tasks) {
        super(context, R.layout.list_item_task, tasks);
        this.context = context;
        this.tasks = tasks;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_task, null);
        }

        TextView tvTaskDescription = convertView.findViewById(R.id.tvTaskDescription);
        Button btnDelete = convertView.findViewById(R.id.btnDelete);
        Button btnNotify = convertView.findViewById(R.id.btnNotify);

        String task = tasks.get(position);
        tvTaskDescription.setText(task);

        // פיצול המידע מתוך המשימה
        String splitDescDate = "\\)\\s*עד:";
        String[] taskParts = task.split(" - ");
        String category = taskParts[0]; // קטגוריה
        String description = taskParts[1].split(splitDescDate)[0]; // תיאור המשימה
        String dateString = taskParts[1].split(splitDescDate)[1].replace(")", ""); // תאריך סיום המשימה

        // כפתור מחיקה
        btnDelete.setOnClickListener(v -> {
            deleteTask(description, category);
            tasks.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, "המשימה נמחקה", Toast.LENGTH_SHORT).show();
        });

        // כפתור התראות
        btnNotify.setOnClickListener(v -> showNotificationDialog(dateString, description));

        return convertView;
    }

    // מחיקת משימה מהמאגר
    private void deleteTask(String description, String category) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete("tasks", "category = ? AND description = ?", new String[]{category, description});
        db.close();
    }

    // הצגת דיאלוג בחירת תזכורת
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
                case 0: // שבוע לפני
                    timeOffset = -7 * 24 * 60 * 60 * 1000;
                    break;
                case 1: // יום לפני
                    timeOffset = -24 * 60 * 60 * 1000;
                    break;
                case 2: // שעה לפני
                    timeOffset = -60 * 60 * 1000;
                    break;
            }
            scheduleNotification(dateString, timeOffset, description);
        });

        builder.setNegativeButton("ביטול", null);
        builder.show();
    }

    // תזמון ההתראה
    private void scheduleNotification(String dateString, int offset, String description) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault());
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