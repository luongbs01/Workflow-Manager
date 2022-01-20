package com.app.workflowmanager;

import com.app.workflowmanager.entity.GithubRepo;
import com.app.workflowmanager.entity.WorkflowJobWrapper;
import com.app.workflowmanager.entity.WorkflowRunWrapper;
import com.app.workflowmanager.entity.WorkflowWrapper;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GithubClient {

    @GET("/user/repos")
    Call<List<GithubRepo>> repos();

    @GET("/repos/{owner}/{repo}/actions/workflows")
    Call<WorkflowWrapper> workflow(@Path("owner") String owner, @Path("repo") String repo);

    @GET("/repos/{owner}/{repo}/actions/workflows")
    Observable<WorkflowWrapper> getWorkflow(@Path("owner") String owner, @Path("repo") String repo);

    @GET("/repos/{owner}/{repo}/actions/workflows/{workflow_id}/runs")
    Call<WorkflowRunWrapper> workflowRun(@Path("owner") String owner, @Path("repo") String repo, @Path("workflow_id") int workflow_id);

    @GET("/repos/{owner}/{repo}/actions/runs")
    Observable<WorkflowRunWrapper> getWorkflowRun(@Path("owner") String owner, @Path("repo") String repo);

    @GET("/repos/{owner}/{repo}/actions/runs/{run_id}/jobs")
    Call<WorkflowJobWrapper> workflowJob(@Path("owner") String owner, @Path("repo") String repo, @Path("run_id") int run_id);

    @PUT("/repos/{owner}/{repo}/actions/workflows/{workflow_id}/disable")
    Call<String> disableWorkflow(@Path("owner") String owner, @Path("repo") String repo, @Path("workflow_id") int workflow_id);

    @PUT("/repos/{owner}/{repo}/actions/workflows/{workflow_id}/enable")
    Call<String> enableWorkflow(@Path("owner") String owner, @Path("repo") String repo, @Path("workflow_id") int workflow_id);

    @DELETE("/repos/{owner}/{repo}/actions/runs/{run_id}")
    Call<String> deleteWorkflowRun(@Path("owner") String owner, @Path("repo") String repo, @Path("run_id") int run_id);

}
