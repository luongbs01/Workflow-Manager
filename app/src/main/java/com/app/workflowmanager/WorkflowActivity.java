package com.app.workflowmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.app.workflowmanager.entity.GithubWorkflow;
import com.app.workflowmanager.entity.GithubWorkflowRun;
import com.app.workflowmanager.entity.Workflow;
import com.app.workflowmanager.entity.WorkflowRun;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WorkflowActivity extends AppCompatActivity {

    private RecyclerView workflowListRecyclerView;
    private RecyclerView workflowRunListRecyclerView;
    private TextView textViewWorkflow;
    private TextView textViewWorkflowRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workflow);
        workflowListRecyclerView = findViewById(R.id.rv_workflow_list);
        workflowListRecyclerView.setLayoutManager(new LinearLayoutManager(WorkflowActivity.this));
        workflowRunListRecyclerView = findViewById(R.id.rv_workflow_run_list);
        workflowRunListRecyclerView.setLayoutManager(new LinearLayoutManager(WorkflowActivity.this));
        textViewWorkflow = findViewById(R.id.tv_workflows);
        textViewWorkflowRun = findViewById(R.id.tv_workflow_runs);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        GithubClient client = retrofit.create(GithubClient.class);

        Call<GithubWorkflow> workflowCall = client.workflow(getIntent().getStringExtra("owner"), getIntent().getStringExtra("repo"));
        workflowCall.enqueue(new Callback<GithubWorkflow>() {
            @Override
            public void onResponse(Call<GithubWorkflow> call, Response<GithubWorkflow> response) {
                workflowListRecyclerView.setAdapter(new WorkflowAdapter(WorkflowActivity.this, response.body().getWorkflows()));
            }

            @Override
            public void onFailure(Call<GithubWorkflow> call, Throwable t) {

            }
        });

        Call<GithubWorkflowRun> workflowRunCall = client.workflowRun(getIntent().getStringExtra("owner"), getIntent().getStringExtra("repo"));
        workflowRunCall.enqueue(new Callback<GithubWorkflowRun>() {
            @Override
            public void onResponse(Call<GithubWorkflowRun> call, Response<GithubWorkflowRun> response) {
                workflowRunListRecyclerView.setAdapter(new WorkflowRunAdapter(WorkflowActivity.this, response.body().getWorkflow_runs(),
                        getIntent().getStringExtra("owner"), getIntent().getStringExtra("repo")));
            }

            @Override
            public void onFailure(Call<GithubWorkflowRun> call, Throwable t) {

            }
        });

        textViewWorkflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textViewWorkflow.isActivated()) {
                    textViewWorkflow.setActivated(false);
                    workflowListRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    textViewWorkflow.setActivated(true);
                    workflowListRecyclerView.setVisibility(View.GONE);
                }
            }
        });

        textViewWorkflowRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textViewWorkflowRun.isActivated()) {
                    textViewWorkflowRun.setActivated(false);
                    workflowRunListRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    textViewWorkflowRun.setActivated(true);
                    workflowRunListRecyclerView.setVisibility(View.GONE);
                }
            }
        });
    }
}