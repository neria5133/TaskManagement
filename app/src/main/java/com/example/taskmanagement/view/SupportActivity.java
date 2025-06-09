package com.example.taskmanagement.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.taskmanagement.R;

/**
 * מחלקה זו מציגה את מסך התמיכה (Support).
 * במסך זה המשתמש יכול לשלוח אימייל לכתובת התמיכה של האפליקציה.
 */
public class SupportActivity extends BaseActivity {

    /**
     * פונקציית onCreate מופעלת בעת יצירת האקטיביטי.
     * היא מגדירה את פריסת המסך ומאזין לכפתור שליחת מייל.
     *
     * @param savedInstanceState מצב שמור של האקטיביטי
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_support);

        // הגדרת התאמה לגבולות מסך
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button sendEmailButton = findViewById(R.id.sendEmailButton);

        /**
         * מאזין ללחיצה על כפתור שליחת מייל.
         * פותח אפליקציית אימייל לשליחה לכתובת שהוגדרה.
         */
        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:Brhvz5133@gmail.com"));

                try {
                    startActivity(Intent.createChooser(emailIntent, "Choose an Email client:"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(SupportActivity.this, "No email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
