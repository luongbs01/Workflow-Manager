package com.app.workflowmanager;

import com.app.workflowmanager.entity.AccessToken;
import com.app.workflowmanager.entity.GithubRepo;
import com.app.workflowmanager.entity.GithubWorkflow;
import com.app.workflowmanager.entity.GithubWorkflowJob;
import com.app.workflowmanager.entity.GithubWorkflowRun;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GithubClient {

    @Headers("Accept: application/json")
    @POST("login/oauth/access_token")
    @FormUrlEncoded
    Call<AccessToken> getAccessToken(
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("code") String code
    );

    @GET("/user/repos")
    Call<List<GithubRepo>> repos();

    @GET("/repos/{owner}/{repo}/actions/workflows")
    Call<GithubWorkflow> workflow(@Path("owner") String owner, @Path("repo") String repo);

    @GET("/repos/{owner}/{repo}/actions/workflows/{workflow_id}/runs")
    Call<GithubWorkflowRun> workflowRun(@Path("owner") String owner, @Path("repo") String repo, @Path("workflow_id") int workflow_id);

    @GET("/repos/{owner}/{repo}/actions/runs/{run_id}/jobs")
    Call<GithubWorkflowJob> workflowJob(@Path("owner") String owner, @Path("repo") String repo, @Path("run_id") int run_id);

    @PUT("/repos/{owner}/{repo}/actions/workflows/{workflow_id}/disable")
    Call<String> disableWorkflow(@Path("owner") String owner, @Path("repo") String repo, @Path("workflow_id") int workflow_id);

    @PUT("/repos/{owner}/{repo}/actions/workflows/{workflow_id}/enable")
    Call<String> enableWorkflow(@Path("owner") String owner, @Path("repo") String repo, @Path("workflow_id") int workflow_id);

    @POST("/repos/{owner}/{repo}/actions/runs/{run_id}/cancel")
    Call<String> cancelWorkflowRun(@Path("owner") String owner, @Path("repo") String repo, @Path("run_id") int run_id);

    @DELETE("/repos/{owner}/{repo}/actions/runs/{run_id}")
    Call<String> deleteWorkflowRun(@Path("owner") String owner, @Path("repo") String repo, @Path("run_id") int run_id);

}
