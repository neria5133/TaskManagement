package com.example.taskmanagement;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.util.Calendar;

public class AddTask extends AppCompatActivity {

    private EditText editTextCategory, editTextDescription;
    private DatePicker datePicker;
    private DatabaseHelper databaseHelper;
    private TextView tvDate ;
    private LocalDate selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        DatePicker datePicker = new DatePicker(this);
        tvDate=findViewById(R.id.tvDate);
        tvDate.setOnClickListener(new View.OnClickListener()
        {
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

                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1; // חודשים ב-`DatePicker` מתחילים מ-0
                int year = datePicker.getYear();

                LocalDate date = LocalDate.of(year, month, day);

                Task newTask = new Task(category, description, date);
                databaseHelper.addTask(newTask);

                Intent intent = new Intent(AddTask.this, TaskListActivity.class);
                startActivity(intent);
                finish(); // סוגר את המסך הנוכחי כדי שלא יוכל לחזור אליו עם כפתור ה"חזור"
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
                selectedDate = LocalDate.of(year, month + 1, dayOfMonth);
                tvDate.setText(selectedDate.toString());
            }
        }, year, month, day);

        datePickerDialog.show();
    }
}