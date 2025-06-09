package com.example.taskmanagement.view;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.taskmanagement.controller.NotificationReceiver;
import com.example.taskmanagement.R;
import com.example.taskmanagement.model.DatabaseHelper;
import com.example.taskmanagement.model.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

/**
 * Activity להוספת מטלה חדשה.
 *
 * מאפשר למשתמש להזין קטגוריה, תיאור ובחירת תאריך ושעה.
 * שומר את המטלה במסד הנתונים ומגדיר התראת אזעקה מדויקת לזמן שנבחר.
 */
public class AddTaskActivity extends BaseActivity {
    private String formattedDateTime;
    private EditText editTextCategory, editTextDescription;
    private TextView tvDate;
    private DatabaseHelper databaseHelper;
    private LocalDateTime selectedDateTime;

    /**
     * פונקציה המופעלת בעת יצירת האקטיביטי.
     * מאתחלת את רכיבי הממשק ומגדירה מאזינים ללחיצות.
     *
     * @param savedInstanceState - מצב שמור של האקטיביטי
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        tvDate = findViewById(R.id.tvDate);
        tvDate.setOnClickListener(new View.OnClickListener() {
            /**
             * מאזין ללחיצה על TextView תאריך,
             * מציג דיאלוג לבחירת תאריך.
             *
             * @param v - ה-View שנלחץ
             */
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        editTextCategory = findViewById(R.id.editText);
        editTextDescription = findViewById(R.id.editText2);
        Button addButton = findViewById(R.id.button);

        databaseHelper = new DatabaseHelper(this);

        addButton.setOnClickListener(new View.OnClickListener() {
            /**
             * מאזין ללחיצה על כפתור הוספה,
             * יוצר אובייקט מטלה חדש, שומר במסד הנתונים,
             * מגדיר התראת אזעקה בזמנו ומעביר לרשימת מטלות.
             *
             * @param v - ה-View שנלחץ
             */
            @SuppressLint("ScheduleExactAlarm")
            @Override
            public void onClick(View v) {
                String category = editTextCategory.getText().toString();
                String description = editTextDescription.getText().toString();

                // יצירת אובייקט מטלה חדש עם התאריך והשעה שנבחרו
                Task newTask = new Task(category, description, selectedDateTime);
                databaseHelper.addTask(newTask);

                // קביעת אזעקה מדויקת לזמן המטלה
                long triggerMillis = selectedDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();

                Intent notificationIntent = new Intent(AddTaskActivity.this, NotificationReceiver.class);
                notificationIntent.putExtra("task_category", category);
                notificationIntent.putExtra("date", formattedDateTime);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        AddTaskActivity.this,
                        (int) System.currentTimeMillis(),
                        notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                );

                android.app.AlarmManager alarmManager = (android.app.AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.setExact(android.app.AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent);

                // מעבר לרשימת המטלות וסגירת הפעילות הנוכחית
                Intent intent = new Intent(AddTaskActivity.this, TaskListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * מציג דיאלוג לבחירת תאריך.
     * לאחר בחירת התאריך, מוצג דיאלוג לבחירת שעה.
     */
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            /**
             * מטפל בבחירת תאריך.
             * מעדכן את התאריך שנבחר ומשנה את התצוגה.
             *
             * @param view - ה-DatePicker שנבחר
             * @param year - שנה שנבחרה
             * @param month - חודש שנבחר (0-11)
             * @param dayOfMonth - יום בחודש שנבחר
             */
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // יצירת LocalDateTime עם התאריך שנבחר (השעה 00:00)
                selectedDateTime = LocalDateTime.of(year, month + 1, dayOfMonth, 0, 0);
                tvDate.setText(selectedDateTime.toLocalDate().toString());
                showTimePickerDialog();
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    /**
     * מציג דיאלוג לבחירת שעה.
     * מעדכן את הזמן שנבחר ומציג אותו ב-TextView.
     */
    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            /**
             * מטפל בבחירת השעה.
             * מעדכן את הזמן שנבחר בתאריך ומציג אותו.
             *
             * @param view - ה-TimePicker שנבחר
             * @param hourOfDay - שעה שנבחרה (0-23)
             * @param minute - דקות שנבחרו
             */
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // עדכון LocalDateTime עם השעה והדקות שנבחרו
                selectedDateTime = selectedDateTime.withHour(hourOfDay).withMinute(minute);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                formattedDateTime = selectedDateTime.format(formatter);
                tvDate.setText(formattedDateTime);
            }
        }, hour, minute, true);

        timePickerDialog.show();
    }
}
