package com.example.taskmanagement;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Task {
    private int id;
    private String category;
    private String description;
    private LocalDateTime date;

    public Task(String category, String description, LocalDateTime date) {
        this.category = category;
        this.description = description;
        this.date = date;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}