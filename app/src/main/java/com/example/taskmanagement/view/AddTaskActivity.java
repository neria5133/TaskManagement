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

public class AddTaskActivity extends BaseActivity {
    private String formattedDateTime;
    private EditText editTextCategory, editTextDescription;
    private TextView tvDate;
    private DatabaseHelper databaseHelper;
    private LocalDateTime selectedDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        tvDate = findViewById(R.id.tvDate);
        tvDate.setOnClickListener(new View.OnClickListener() {
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
            @SuppressLint("ScheduleExactAlarm")
            @Override
            public void onClick(View v) {
                String category = editTextCategory.getText().toString();
                String description = editTextDescription.getText().toString();

                // יצירת משימה עם התאריך והשעה שנבחרו
                Task newTask = new Task(category, description, selectedDateTime);
                databaseHelper.addTask(newTask);

// ===== התחלת קוד ההתראה =====
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
// ===== סוף קוד ההתראה =====
                Intent intent = new Intent(AddTaskActivity.this, TaskListActivity.class);
                startActivity(intent);
                finish(); // סוגר את המסך הנוכחי
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // יצירת אובייקט LocalDateTime עם התאריך הנבחר
                selectedDateTime = LocalDateTime.of(year, month + 1, dayOfMonth, 0, 0);
                tvDate.setText(selectedDateTime.toLocalDate().toString());
                showTimePickerDialog();
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // עדכון אובייקט LocalDateTime עם השעה שנבחרה
                selectedDateTime = selectedDateTime.withHour(hourOfDay).withMinute(minute);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                formattedDateTime = selectedDateTime.format(formatter);
                tvDate.setText(formattedDateTime);
            }
        }, hour, minute, true);

        timePickerDialog.show();
    }
}
