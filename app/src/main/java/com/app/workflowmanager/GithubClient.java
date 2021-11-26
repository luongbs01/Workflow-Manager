package com.app.workflowmanager;

import com.app.workflowmanager.entity.GithubRepo;
import com.app.workflowmanager.entity.GithubWorkflow;
import com.app.workflowmanager.entity.GithubWorkflowJob;
import com.app.workflowmanager.entity.GithubWorkflowRun;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GithubClient {

    @GET("/users/{user}/repos")
    Call<List<GithubRepo>> reposForUser(@Path("user") String user);

    @GET("/repos/{owner}/{repo}/actions/workflows")
    Call<GithubWorkflow> workflow(@Path("owner") String owner, @Path("repo") String repo);

    @GET("/repos/{owner}/{repo}/actions/runs")
    Call<GithubWorkflowRun> workflowRun(@Path("owner") String owner, @Path("repo") String repo);

    @GET("/repos/{owner}/{repo}/actions/runs/{run_id}/jobs")
    Call<GithubWorkflowJob> workflowJob(@Path("owner") String owner, @Path("repo") String repo, @Path("run_id") String run_id);
}
