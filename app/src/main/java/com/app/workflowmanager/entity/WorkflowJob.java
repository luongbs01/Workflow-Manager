package com.app.workflowmanager.entity;

import java.util.List;

public class WorkflowJob {
    private int id;
    private int run_id;
    private String html_url;
    private String status;
    private String conclusion;
    private String started_at;
    private String completed_at;
    private String name;
    private List<Step> steps;

    public int getId() {
        return id;
    }

    public int getRun_id() {
        return run_id;
    }

    public String getHtml_url() {
        return html_url;
    }

    public String getStatus() {
        return status;
    }

    public String getConclusion() {
        return conclusion;
    }

    public String getStarted_at() {
        return started_at;
    }

    public String getCompleted_at() {
        return completed_at;
    }

    public String getName() {
        return name;
    }

    public List<Step> getSteps() {
        return steps;
    }
}
