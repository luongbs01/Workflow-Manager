package com.app.workflowmanager.entity;

import java.util.List;

public class GithubWorkflow {
    private int total_count;
    private List<Workflow> workflows;

    public int getTotal_count() {
        return total_count;
    }

    public List<Workflow> getWorkflows() {
        return workflows;
    }
}
