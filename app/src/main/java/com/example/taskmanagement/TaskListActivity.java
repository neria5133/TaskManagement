package com.example.taskmanagement;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TaskListActivity extends AppCompatActivity {
    private ArrayList<String> taskList;
    private ListView listView;
    private DatabaseHelper databaseHelper;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_list);

        listView = findViewById(R.id.listView);
        databaseHelper = new DatabaseHelper(this);

        loadTasks();
    }
    private void loadTasks() {
        taskList = new ArrayList<>();
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

        // יצירת מתאם להצגת המשימות
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskList);
        listView.setAdapter(adapter);
    }
}