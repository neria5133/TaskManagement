package com.example.taskmanagement;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NotificationView extends LinearLayout {
    private TextView titleView;
    private TextView dateView;

    public NotificationView(Context context) {
        super(context);
        init(context);
    }

    public NotificationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        setPadding(32, 32, 32, 32);
        setBackgroundColor(Color.parseColor("#E0F7FA"));
        setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        titleView = new TextView(context);
        titleView.setTextSize(18);
        titleView.setTextColor(Color.BLACK);
        addView(titleView);

        dateView = new TextView(context);
        dateView.setTextSize(14);
        dateView.setTextColor(Color.DKGRAY);
        dateView.setGravity(Gravity.END);
        addView(dateView);
    }

    public void setData(Notification notification) {
        titleView.setText(notification.getTitle());
        dateView.setText(notification.getDate());
    }
}