package com.app.workflowmanager.entity;

public class WorkflowRun {
    private int id;
    private String name;
    private int run_number;
    private String event;
    private String status;
    private String conclusion;
    private String html_url;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getRun_number() {
        return run_number;
    }

    public String getEvent() {
        return event;
    }

    public String getStatus() {
        return status;
    }

    public String getConclusion() {
        return conclusion;
    }

    public String getHtml_url() {
        return html_url;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
