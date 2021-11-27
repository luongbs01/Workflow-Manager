package com.app.workflowmanager;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.workflowmanager.entity.GithubWorkflow;
import com.app.workflowmanager.entity.GithubWorkflowRun;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WorkflowActivity extends AppCompatActivity {

    private RecyclerView workflowListRecyclerView;
    private RecyclerView workflowRunListRecyclerView;
    private CardView cardViewWorkflow;
    private CardView cardViewWorkflowRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workflow);
        workflowListRecyclerView = findViewById(R.id.rv_workflow_list);
        workflowListRecyclerView.setLayoutManager(new LinearLayoutManager(WorkflowActivity.this));
        workflowRunListRecyclerView = findViewById(R.id.rv_workflow_run_list);
        workflowRunListRecyclerView.setLayoutManager(new LinearLayoutManager(WorkflowActivity.this));
        cardViewWorkflow = findViewById(R.id.cv_workflows);
        cardViewWorkflowRun = findViewById(R.id.cv_workflow_runs);
        cardViewWorkflow.setActivated(true);
        cardViewWorkflowRun.setActivated(true);

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

        cardViewWorkflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cardViewWorkflow.isActivated()) {
                    cardViewWorkflow.setActivated(false);
                    workflowListRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    cardViewWorkflow.setActivated(true);
                    workflowListRecyclerView.setVisibility(View.GONE);
                }
            }
        });

        cardViewWorkflowRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cardViewWorkflowRun.isActivated()) {
                    cardViewWorkflowRun.setActivated(false);
                    workflowRunListRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    cardViewWorkflowRun.setActivated(true);
                    workflowRunListRecyclerView.setVisibility(View.GONE);
                }
            }
        });
    }
}