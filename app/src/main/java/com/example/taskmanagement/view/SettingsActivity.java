package com.example.taskmanagement.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationManagerCompat;

import com.example.taskmanagement.R;

/**
 * פעילות הגדרות האפליקציה.
 * מאפשרת מעבר להגדרות התראות ושינוי מצב תצוגה (כהה/בהיר).
 */
public class SettingsActivity extends BaseActivity {

    private Button btnToggleNotifications;
    private Switch switchTheme;

    /**
     * נקרא ביצירת הפעילות.
     * מגדיר את הכפתורים והמתגים לפעולות השונות.
     *
     * @param savedInstanceState מצב שמור של הפעילות
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnToggleNotifications = findViewById(R.id.btnToggleNotifications);
        switchTheme = findViewById(R.id.darkModeSwitch);

        // לחצן לפתיחת הגדרות התראות המערכת לאפליקציה
        btnToggleNotifications.setOnClickListener(v -> {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            boolean areEnabled = notificationManager.areNotificationsEnabled();

            if (areEnabled) {
                Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                        .putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                startActivity(intent);
                Toast.makeText(this, "העבר להגדרות ההתראות", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "נראה שהתראות כבר כבויות", Toast.LENGTH_SHORT).show();
            }
        });

        // הגדרת מצב התצוגה לפי ההגדרות השמורות
        SharedPreferences sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean("DarkMode", false);
        switchTheme.setChecked(isDarkMode);

        // מאזין לשינוי מצב מתג המצב הכהה
        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("DarkMode", isChecked);
            editor.apply();

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }

            // הפעלה מחדש של הפעילות כדי להחיל את השינוי
            recreate();
        });
    }
}
