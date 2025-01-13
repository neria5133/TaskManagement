package com.example.taskmanagement;

import java.time.LocalDate;

public class Task {
    private LocalDate deadline;
    private String category;
    private String description;





    public Task(LocalDate deadline, String category, String description) {
        this.deadline = deadline;
        this.category = category;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }
}
