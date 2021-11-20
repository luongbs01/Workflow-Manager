package com.app.workflowmanager.entity;

public class Workflow {
    private int id;
    private String name;
    private String path;
    private String created_at;
    private String updated_at;
    private String html_url;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getHtml_url() {
        return html_url;
    }
}
