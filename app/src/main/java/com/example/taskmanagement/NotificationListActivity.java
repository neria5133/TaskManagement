package com.example.taskmanagement;

import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class NotificationListActivity extends AppCompatActivity {

    private LinearLayout containerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);

        containerLayout = findViewById(R.id.notification_container);

        // כאן תקרא ל-Database שלך. דוגמה:
        List<Notification> notifications = NotificationRepository.getAll(this);

        for (Notification notification : notifications) {
            NotificationView view = new NotificationView(this);
            view.setData(notification);
            containerLayout.addView(view);
        }
    }
}