package com.app.workflowmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.app.workflowmanager.entity.GithubWorkflowJob;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WorkflowJobActivity extends AppCompatActivity {

    private RecyclerView workflowJobListRecyclerView;
    private int run_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workflow_job);
        workflowJobListRecyclerView = findViewById(R.id.rv_workflow_job_list);
        workflowJobListRecyclerView.setLayoutManager(new LinearLayoutManager(WorkflowJobActivity.this));

        run_id = getIntent().getIntExtra("run_id", 0);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        GithubClient client = retrofit.create(GithubClient.class);

        Log.d("luong", "getExtra "+run_id);

        Call<GithubWorkflowJob> workflowJobCall = client.workflowJob(getIntent().getStringExtra("owner"),
                getIntent().getStringExtra("repo"),
                run_id);

        workflowJobCall.enqueue(new Callback<GithubWorkflowJob>() {
            @Override
            public void onResponse(Call<GithubWorkflowJob> call, Response<GithubWorkflowJob> response) {
                assert response.body() != null;
                workflowJobListRecyclerView.setAdapter(new WorkflowJobAdapter(WorkflowJobActivity.this, response.body().getJobs()));
                Toast.makeText(WorkflowJobActivity.this, "" + response.body().getTotal_count(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<GithubWorkflowJob> call, Throwable t) {
                Log.d("luong", t.getMessage());
                Toast.makeText(WorkflowJobActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}