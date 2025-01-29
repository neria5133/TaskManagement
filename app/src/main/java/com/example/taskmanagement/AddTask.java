package com.example.taskmanagement;

import android.app.DatePickerDialog;
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

    private TextView tvDate;
    private LocalDate selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // חיבור ה-TextView
        tvDate = findViewById(R.id.tvDate);
        Button btnAddTask = findViewById(R.id.button);
        EditText editCategory = findViewById(R.id.editText);
        EditText editDescription = findViewById(R.id.editText2);

        // הוספת מאזין ללחיצה על תאריך
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // כפתור להוספת מטלה (בדיקה לדוגמה)
        btnAddTask.setOnClickListener(v -> {
            String category = editCategory.getText().toString();
            String description = editDescription.getText().toString();
            if (selectedDate != null) {
                // כאן תוכל לשמור את הנתונים ל-SQL או להציג הודעה
                tvDate.setText("תאריך שנבחר: " + selectedDate.toString());
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