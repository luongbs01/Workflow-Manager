package com.app.workflowmanager.entity;

import java.util.List;

public class WorkflowJobWrapper {
    private int total_count;
    private List<WorkflowJob> jobs;

    public int getTotal_count() {
        return total_count;
    }

    public List<WorkflowJob> getJobs() {
        return jobs;
    }
}
