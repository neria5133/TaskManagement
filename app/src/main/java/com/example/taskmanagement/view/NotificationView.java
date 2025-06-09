package com.example.taskmanagement.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.taskmanagement.model.Notification;

/**
 * תצוגה מותאמת אישית להצגת התראה בודדת.
 * מכילה כותרת, תאריך וכפתור מחיקה.
 */
public class NotificationView extends LinearLayout {

    private TextView titleView;
    private TextView dateView;
    private Button deleteButton;

    /**
     * בנאי ליצירת תצוגה מהקוד.
     *
     * @param context ההקשר
     */
    public NotificationView(Context context) {
        super(context);
        init(context);
    }

    /**
     * בנאי ליצירת התצוגה עם פרמטרי מאפיינים XML.
     *
     * @param context ההקשר
     * @param attrs מאפייני XML
     */
    public NotificationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * אתחול התצוגה – יצירת רכיבים, עיצוב וסידורם.
     *
     * @param context ההקשר
     */
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

        deleteButton = new Button(context);
        deleteButton.setText("מחק");
        addView(deleteButton);
    }

    /**
     * הגדרת הנתונים להצגה בהתראה.
     *
     * @param notification ההתראה להצגה
     */
    public void setData(Notification notification) {
        titleView.setText(notification.getTitle());
        dateView.setText(notification.getDate());
    }

    /**
     * קביעת מאזין ללחיצה על כפתור המחיקה.
     *
     * @param listener מאזין ללחיצה
     */
    public void setOnDeleteClickListener(OnClickListener listener) {
        deleteButton.setOnClickListener(listener);
    }
}
