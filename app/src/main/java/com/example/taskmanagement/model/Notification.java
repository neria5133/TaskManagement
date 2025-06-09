package com.example.taskmanagement.model;

/**
 * מחלקה המייצגת התראה (Notification) עם מזהה, כותרת ותאריך.
 */
public class Notification {
    /**
     * משתנה סטטי לספירת התראות חדשות.
     */
    public static int count = 0;

    private int id;
    private String title;
    private String date;

    /**
     * בונה אובייקט התראה חדש עם פרטי התראה.
     *
     * @param id מזהה ייחודי של ההודעה.
     * @param title כותרת ההודעה.
     * @param date תאריך ההודעה בפורמט טקסטואלי.
     */
    public Notification(int id, String title, String date) {
        this.id = id;
        this.title = title;
        this.date = date;
    }

    /**
     * מחזיר את מזהה ההתראה.
     *
     * @return מזהה ייחודי של ההתראה.
     */
    public int getId() {
        return id;
    }

    /**
     * מחזיר את כותרת ההתראה.
     *
     * @return כותרת ההודעה.
     */
    public String getTitle() {
        return title;
    }

    /**
     * מחזיר את תאריך ההתראה.
     *
     * @return תאריך ההתראה כמחרוזת.
     */
    public String getDate() {
        return date;
    }
}