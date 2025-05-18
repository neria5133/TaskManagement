package com.example.taskmanagement.view;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.taskmanagement.R;
import com.example.taskmanagement.model.Notification;
import com.example.taskmanagement.model.NotificationRepository;

import java.util.List;

public class NotificationListActivity extends BaseActivity {

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
            view.setOnDeleteClickListener(v -> {
                NotificationRepository.delete(this, notification.getId());
                containerLayout.removeView(view);
            });

            containerLayout.addView(view);
        }
    }
}