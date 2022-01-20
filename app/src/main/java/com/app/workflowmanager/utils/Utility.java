package com.app.workflowmanager.utils;

import com.app.workflowmanager.entity.GithubRepo;
import com.app.workflowmanager.entity.Workflow;
import com.app.workflowmanager.entity.WorkflowRun;

import java.util.ArrayList;
import java.util.List;

public class Utility {

    public static List<GithubRepo> searchRepoByName(List<GithubRepo> reposInput, String query) {
        List<GithubRepo> result = new ArrayList<>();
        for (GithubRepo githubRepo : reposInput) {
            if (githubRepo.getName().toLowerCase().contains(query.toLowerCase()))
                result.add(githubRepo);
        }
        return result;
    }

    public static List<Workflow> searchWorkflowByName(List<Workflow> reposInput, String query) {
        List<Workflow> result = new ArrayList<>();
        for (Workflow workflow : reposInput) {
            if (workflow.getName().toLowerCase().contains(query.toLowerCase()))
                result.add(workflow);
        }
        return result;
    }

    public static List<WorkflowRun> searchWorkflowRunByName(List<WorkflowRun> reposInput, String query) {
        List<WorkflowRun> result = new ArrayList<>();
        for (WorkflowRun workflowRun : reposInput) {
            if ((workflowRun.getEvent().equals("push")
                    && workflowRun.getHeadCommit().getMessage().toLowerCase().contains(query.toLowerCase()))
                    || workflowRun.getName().toLowerCase().contains(query.toLowerCase()))
                result.add(workflowRun);
        }
        return result;
    }
}
