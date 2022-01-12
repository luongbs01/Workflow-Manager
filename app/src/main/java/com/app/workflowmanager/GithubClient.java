package com.app.workflowmanager;

import android.database.Observable;

import com.app.workflowmanager.entity.GithubRepo;
import com.app.workflowmanager.entity.GithubWorkflow;
import com.app.workflowmanager.entity.GithubWorkflowJob;
import com.app.workflowmanager.entity.GithubWorkflowRun;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GithubClient {

    @GET("/user/repos")
    Call<List<GithubRepo>> repos();

    @GET("/repos/{owner}/{repo}/actions/workflows")
    Call<GithubWorkflow> workflow(@Path("owner") String owner, @Path("repo") String repo);

    @GET("/repos/{owner}/{repo}/actions/workflows/{workflow_id}/runs")
    Call<GithubWorkflowRun> workflowRun(@Path("owner") String owner, @Path("repo") String repo, @Path("workflow_id") int workflow_id);

    @GET("/repos/{owner}/{repo}/actions/runs")
    Call<GithubWorkflowRun> workflowRunForRepo(@Path("owner") String owner, @Path("repo") String repo);

    @GET("/repos/{owner}/{repo}/actions/runs/{run_id}/jobs")
    Call<GithubWorkflowJob> workflowJob(@Path("owner") String owner, @Path("repo") String repo, @Path("run_id") int run_id);

    @PUT("/repos/{owner}/{repo}/actions/workflows/{workflow_id}/disable")
    Call<String> disableWorkflow(@Path("owner") String owner, @Path("repo") String repo, @Path("workflow_id") int workflow_id);

    @PUT("/repos/{owner}/{repo}/actions/workflows/{workflow_id}/enable")
    Call<String> enableWorkflow(@Path("owner") String owner, @Path("repo") String repo, @Path("workflow_id") int workflow_id);

    @DELETE("/repos/{owner}/{repo}/actions/runs/{run_id}")
    Call<String> deleteWorkflowRun(@Path("owner") String owner, @Path("repo") String repo, @Path("run_id") int run_id);

    @POST("/repos/{owner}/{repo}/actions/runs/{run_id}/rerun")
    Call<String> rerunWorkflowRun(@Path("owner") String owner, @Path("repo") String repo, @Path("run_id") int run_id);
}
