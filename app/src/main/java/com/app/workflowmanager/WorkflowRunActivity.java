package com.app.workflowmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.app.workflowmanager.entity.GithubWorkflowRun;
import com.app.workflowmanager.entity.WorkflowRun;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WorkflowRunActivity extends AppCompatActivity implements WorkflowRunAdapter.Callback {

    private RecyclerView workflowRunListRecyclerView;
    private CardView cardViewWorkflowRun;
    private List<WorkflowRun> workflowRunList;

    private Retrofit.Builder builder;
    private Retrofit retrofit;
    private GithubClient client;

    private String owner;
    private String repo;
    private int workflow_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workflow_run);

        workflowRunListRecyclerView = findViewById(R.id.rv_workflow_run_list);
        workflowRunListRecyclerView.setLayoutManager(new LinearLayoutManager(WorkflowRunActivity.this));
        cardViewWorkflowRun = findViewById(R.id.cv_workflow_runs);
        cardViewWorkflowRun.setActivated(true);

        owner = getIntent().getStringExtra("owner");
        repo = getIntent().getStringExtra("repo");
        workflow_id = getIntent().getIntExtra("workflow_id", 0);

        builder = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create());

        retrofit = builder.build();

        client = retrofit.create(GithubClient.class);

        Call<GithubWorkflowRun> workflowRunCall = client.workflowRun(owner, repo, workflow_id);
        workflowRunCall.enqueue(new Callback<GithubWorkflowRun>() {
            @Override
            public void onResponse(Call<GithubWorkflowRun> call, Response<GithubWorkflowRun> response) {
                workflowRunList = response.body().getWorkflow_runs();
                workflowRunListRecyclerView.setAdapter(new WorkflowRunAdapter(WorkflowRunActivity.this, workflowRunList,
                        owner, repo, WorkflowRunActivity.this));
            }

            @Override
            public void onFailure(Call<GithubWorkflowRun> call, Throwable t) {

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

    @Override
    public void onWorkflowRunOptionSelect(int option, int position) {
        switch (option) {
            case 0:
                Call<String> cancelWorkflowRunCall = client.cancelWorkflowRun(owner, repo, workflowRunList.get(position).getId());
                cancelWorkflowRunCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.d("luong", response.toString());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("luong", t.toString());
                    }
                });
                break;
            case 1:
                Call<String> deleteWorkflowRunCall = client.deleteWorkflowRun(owner, repo, workflowRunList.get(position).getId());
                deleteWorkflowRunCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.d("luong", response.toString());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("luong", t.toString());
                    }
                });
                break;
            case 2:

        }
    }
}