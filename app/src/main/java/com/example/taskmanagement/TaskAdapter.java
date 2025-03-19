package com.example.taskmanagement;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cursoradapter.widget.SimpleCursorAdapter;

import java.util.List;

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

        String task = tasks.get(position);
        tvTaskDescription.setText(task);

        // מקבל את המזהה של המשימה מהמילים שבין התיאור והקטגוריה
        String[] taskParts = task.split(" - ");
        String description = taskParts[1].split(" ")[0]; // תיאור המשימה
        String category = taskParts[0]; // קטגוריה

        // הגדרת לחצן מחיקה
        btnDelete.setOnClickListener(v -> {
            deleteTask(description, category);
            tasks.remove(position); // מסיר את המשימה מהרשימה
            notifyDataSetChanged(); // מעדכן את ה-Adapter
            Toast.makeText(context, "המשימה נמחקה", Toast.LENGTH_SHORT).show();
        });

        return convertView;
    }

    // מחיקת המשימה מהמאגר
    private void deleteTask(String description, String category) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete("tasks", "category = ? AND description = ?", new String[]{category, description});
        db.close();
    }
}
