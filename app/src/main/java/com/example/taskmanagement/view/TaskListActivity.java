package com.example.taskmanagement.view;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

import com.example.taskmanagement.R;
import com.example.taskmanagement.controller.TaskAdapter;
import com.example.taskmanagement.model.DatabaseHelper;

import java.util.ArrayList;

/**
 * Activity להצגת רשימת המטלות מהמסד נתונים.
 * אחראי על טעינת המטלות מה-Database והצגתן ברשימה עם מתאם.
 */
public class TaskListActivity extends BaseActivity {
    private ListView listView;
    private DatabaseHelper databaseHelper;
    private TaskAdapter adapter;

    /**
     * נקרא ביצירת האקטיביטי.
     * מגדיר את התצוגה, מאתחל את מסד הנתונים וטעינת המטלות.
     *
     * @param savedInstanceState מצב שמור של האקטיביטי, אם קיים
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_list);

        listView = findViewById(R.id.listView);
        databaseHelper = new DatabaseHelper(this);

        loadTasks();
    }

    /**
     * טוען את כל המטלות מה-Database ומציגן ברשימה.
     * שולף את הנתונים מה-Cursor, מעבד את התאריך,
     * ויוצר רשימה של מחרוזות להצגה עם מתאם מותאם אישית.
     */
    private void loadTasks() {
        ArrayList<String> taskList = new ArrayList<>();
        Cursor cursor = databaseHelper.getAllTasks();

        if (cursor.moveToFirst()) {
            do {
                String category = cursor.getString(1); // קטגוריה
                String description = cursor.getString(2); // תיאור
                String date = cursor.getString(3); // תאריך

                // עיבוד מחרוזת התאריך להסרת התו 'T' לשם תצוגה נוחה יותר
                date = date.replace("T", " ");
                taskList.add(category + " - " + description + "\n" + " (עד: " + date + ")");
            } while (cursor.moveToNext());
        }

        cursor.close();

        // יצירת מתאם רשימה והצגת המטלות
        adapter = new TaskAdapter(this, taskList);
        listView.setAdapter(adapter);
    }
}
