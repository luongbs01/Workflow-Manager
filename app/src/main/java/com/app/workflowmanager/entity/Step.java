package com.app.workflowmanager.entity;

public class Step {
    private String name;
    private String status;
    private String conclusion;
    private int number;
    private String started_at;
    private String completed_at;

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getConclusion() {
        return conclusion;
    }

    public int getNumber() {
        return number;
    }

    public String getStarted_at() {
        return started_at;
    }

    public String getCompleted_at() {
        return completed_at;
    }
}
