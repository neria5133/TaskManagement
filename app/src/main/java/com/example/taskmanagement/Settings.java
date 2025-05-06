package com.example.taskmanagement;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

public class Settings extends BaseActivity {
    private Button btnToggleNotifications, btnManagePermissions;
    private boolean notificationsEnabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnToggleNotifications = findViewById(R.id.btnToggleNotifications);
        btnManagePermissions = findViewById(R.id.btnManagePermissions);

        btnToggleNotifications.setOnClickListener(v -> {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            boolean areEnabled = notificationManager.areNotificationsEnabled();

            if (areEnabled) {
                // לא ניתן לכבות התראות ישירות - מפנה את המשתמש להגדרות
                Intent intent = new Intent(android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                        .putExtra(android.provider.Settings.EXTRA_APP_PACKAGE, getPackageName());
                startActivity(intent);
                Toast.makeText(this, "העבר לכיבוי ההתראות בהגדרות", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "נראה שהתראות כבר כבויות", Toast.LENGTH_SHORT).show();
            }
        });

        btnManagePermissions.setOnClickListener(v -> {
            Intent intent = new Intent(android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    .putExtra(android.provider.Settings.EXTRA_APP_PACKAGE, getPackageName());
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
            Toast.makeText(this, "פתח מסך ניהול הרשאות", Toast.LENGTH_SHORT).show();
        });
    }
}