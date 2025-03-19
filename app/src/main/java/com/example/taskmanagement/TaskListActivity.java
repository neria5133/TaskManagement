package com.example.taskmanagement;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TaskListActivity extends BaseActivity {
    private ListView listView;
    private DatabaseHelper databaseHelper;
    private TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_list);

        listView = findViewById(R.id.listView);
        databaseHelper = new DatabaseHelper(this);

        loadTasks();
    }

    private void loadTasks() {
        ArrayList<String> taskList = new ArrayList<>();
        Cursor cursor = databaseHelper.getAllTasks();

        if (cursor.moveToFirst()) {
            do {
                String category = cursor.getString(1); // קטגוריה
                String description = cursor.getString(2); // תיאור
                String date = cursor.getString(3); // תאריך

                taskList.add(category + " - " + description + " (עד: " + date + ")");
            } while (cursor.moveToNext());
        }

        cursor.close();

        // יצירת מתאם עם כפתור מחיקה
        adapter = new TaskAdapter(this, taskList);
        listView.setAdapter(adapter);
    }
}