package com.app.workflowmanager.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.workflowmanager.GithubClient;
import com.app.workflowmanager.R;
import com.app.workflowmanager.adapter.WorkflowJobAdapter;
import com.app.workflowmanager.dialog.InfoDialogBuilder;
import com.app.workflowmanager.entity.WorkflowJob;
import com.app.workflowmanager.entity.WorkflowJobWrapper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WorkflowJobActivity extends AppCompatActivity implements WorkflowJobAdapter.Callback {

    private RecyclerView workflowJobListRecyclerView;
    private WorkflowJobAdapter workflowJobAdapter;
    private List<WorkflowJob> workflowJobList;
    private int run_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workflow_job);
        initializeView();

        run_id = getIntent().getIntExtra("run_id", 0);

        fetchData();
    }

    private void initializeView() {
        workflowJobListRecyclerView = findViewById(R.id.rv_workflow_job_list);
        workflowJobListRecyclerView.setLayoutManager(new LinearLayoutManager(WorkflowJobActivity.this));
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(workflowJobListRecyclerView.getContext(), 1);
        workflowJobListRecyclerView.addItemDecoration(mDividerItemDecoration);
    }

    private void fetchData() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        GithubClient client = retrofit.create(GithubClient.class);
        Call<WorkflowJobWrapper> workflowJobCall = client.workflowJob(getIntent().getStringExtra("owner"),
                getIntent().getStringExtra("repo"),
                run_id);
        workflowJobCall.enqueue(new Callback<WorkflowJobWrapper>() {
            @Override
            public void onResponse(Call<WorkflowJobWrapper> call, Response<WorkflowJobWrapper> response) {
                assert response.body() != null;
                workflowJobList = response.body().getJobs();
                workflowJobAdapter = new WorkflowJobAdapter(WorkflowJobActivity.this, workflowJobList, WorkflowJobActivity.this);
                workflowJobListRecyclerView.setAdapter(workflowJobAdapter);
            }

            @Override
            public void onFailure(Call<WorkflowJobWrapper> call, Throwable t) {

            }
        });
    }

    @Override
    public void onWorkflowJobOptionSelect(int option, int position) {
        switch (option) {
            case 0:
                new InfoDialogBuilder<>(this, workflowJobList.get(position)).build().show();
                break;
        }
    }
}