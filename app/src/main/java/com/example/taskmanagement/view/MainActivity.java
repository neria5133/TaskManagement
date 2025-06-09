package com.example.taskmanagement.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.taskmanagement.R;

/**
 * הפעילות הראשית של האפליקציה.
 * מכילה כפתור ליציאה למסך הוספת מטלה חדשה.
 */
public class MainActivity extends BaseActivity {

    /**
     * נקרא ביצירת הפעילות.
     * מגדיר את התצוגה ומאזין ללחיצה על הכפתור.
     *
     * @param savedInstanceState מצב שמור של הפעילות
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addButton = findViewById(R.id.buttonAddTask);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivity(intent);
            }
        });
    }
}
