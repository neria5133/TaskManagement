package com.example.taskmanagement;


public class Notification {
    public static int count = 0;
    private int id;
    private String title;
    private String date;

    public Notification(int id, String title, String date) {
        this.id = id;
        this.title = title;
        this.date = date;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDate() { return date; }
}