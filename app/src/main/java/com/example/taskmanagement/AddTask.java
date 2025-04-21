package com.example.taskmanagement;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class AddTask extends BaseActivity {

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
            @Override
            public void onClick(View v) {
                String category = editTextCategory.getText().toString();
                String description = editTextDescription.getText().toString();

                // יצירת משימה עם התאריך והשעה שנבחרו
                Task newTask = new Task(category, description, selectedDateTime);
                databaseHelper.addTask(newTask);

                Intent intent = new Intent(AddTask.this, TaskListActivity.class);
                startActivity(intent);
                finish(); // סוגר את המסך הנוכחי
            }
        });
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
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
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // עדכון אובייקט LocalDateTime עם השעה שנבחרה
                selectedDateTime = selectedDateTime.withHour(hourOfDay).withMinute(minute);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                String formattedDateTime = selectedDateTime.format(formatter);
                tvDate.setText(formattedDateTime);            }
        }, hour, minute, true);

        timePickerDialog.show();
    }
}
