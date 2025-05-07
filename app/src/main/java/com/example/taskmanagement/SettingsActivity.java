package com.example.taskmanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationManagerCompat;

public class SettingsActivity extends BaseActivity {
    private Button btnToggleNotifications;
    private Switch switchTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnToggleNotifications = findViewById(R.id.btnToggleNotifications);
        switchTheme = findViewById(R.id.darkModeSwitch);

        // 🔔 לחצן לניהול התראות
        btnToggleNotifications.setOnClickListener(v -> {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            boolean areEnabled = notificationManager.areNotificationsEnabled();

            if (areEnabled) {
                Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                        .putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                startActivity(intent);
                Toast.makeText(this, "העבר להגדרות להתראות", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "נראה שהתראות כבר כבויות", Toast.LENGTH_SHORT).show();
            }
        });

        // 🌙 מצב כהה / בהיר
        SharedPreferences sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean("DarkMode", false);
        switchTheme.setChecked(isDarkMode);

        // החלת מצב התצוגה הנוכחי בעת יצירת האקטיביטי
        applyTheme(isDarkMode);

        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("DarkMode", isChecked);
            editor.apply();

            applyTheme(isChecked);

            // הפעלה מחדש קלה של האקטיביטי כדי להחיל את העיצוב החדש
            new Handler().postDelayed(this::recreate, 100);
        });
    }

    private void applyTheme(boolean isDarkMode) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}