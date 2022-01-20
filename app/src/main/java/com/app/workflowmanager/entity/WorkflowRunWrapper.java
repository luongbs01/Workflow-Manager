package com.app.workflowmanager.entity;

import java.util.List;

public class WorkflowRunWrapper {
    private int total_count;
    private List<WorkflowRun> workflow_runs;

    public int getTotal_count() {
        return total_count;
    }

    public List<WorkflowRun> getWorkflow_runs() {
        return workflow_runs;
    }
}
