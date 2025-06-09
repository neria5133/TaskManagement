package com.example.taskmanagement.view;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.taskmanagement.R;
import com.example.taskmanagement.model.Notification;
import com.example.taskmanagement.model.NotificationRepository;

import java.util.List;

/**
 * פעילות להצגת רשימת ההתראות מהמסד.
 * מציגה כל התראה עם אפשרות למחיקה.
 */
public class NotificationListActivity extends BaseActivity {

    private LinearLayout containerLayout;

    /**
     * נקרא ביצירת הפעילות.
     * מגדיר את התצוגה ונטען את כל ההתראות להצגה.
     *
     * @param savedInstanceState מצב שמור של הפעילות
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);

        containerLayout = findViewById(R.id.notification_container);

        // טוען את כל ההתראות מהמאגר
        List<Notification> notifications = NotificationRepository.getAll(this);

        for (Notification notification : notifications) {
            NotificationView view = new NotificationView(this);
            view.setData(notification);

            // מאזין ללחיצה על כפתור מחיקה בהתראה
            view.setOnDeleteClickListener(v -> {
                NotificationRepository.delete(this, notification.getId());
                containerLayout.removeView(view);
            });

            containerLayout.addView(view);
        }
    }
}
