package com.example.taskmanagement.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.taskmanagement.R;

public class MainActivity extends BaseActivity {
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button addButton = findViewById(R.id.buttonAddTask);
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(intent);
        });
    }
}