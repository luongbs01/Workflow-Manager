package com.app.workflowmanager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.workflowmanager.entity.GithubWorkflow;
import com.app.workflowmanager.entity.GithubWorkflowRun;
import com.app.workflowmanager.entity.Workflow;
import com.app.workflowmanager.entity.WorkflowRun;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WorkflowActivity extends AppCompatActivity implements WorkflowAdapter.Callback {

    private RecyclerView workflowListRecyclerView;
    private CardView cardViewWorkflow;
    private List<Workflow> workflowList;

    private OkHttpClient okHttpClient;
    private Retrofit.Builder builder;
    private Retrofit retrofit;
    private GithubClient client;

    private String owner;
    private String repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workflow);
        workflowListRecyclerView = findViewById(R.id.rv_workflow_list);
        workflowListRecyclerView.setLayoutManager(new LinearLayoutManager(WorkflowActivity.this));
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(workflowListRecyclerView.getContext(), 1);
        workflowListRecyclerView.addItemDecoration(mDividerItemDecoration);
        cardViewWorkflow = findViewById(R.id.cv_workflows);
        cardViewWorkflow.setActivated(true);

        owner = getIntent().getStringExtra("owner");
        repo = getIntent().getStringExtra("repo");

        okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "token "
                                + getSharedPreferences(getResources().getString(R.string.preference_file_key),
                                Context.MODE_PRIVATE).getString("access_token", ""))
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();

        builder = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create());
        retrofit = builder.build();
//        Log.d("luong", "workflow: " + getSharedPreferences(getResources().getString(R.string.preference_file_key),
//                Context.MODE_PRIVATE).getString("access_token", ""));

        client = retrofit.create(GithubClient.class);

        Call<GithubWorkflow> workflowCall = client.workflow(owner, repo);
        workflowCall.enqueue(new Callback<GithubWorkflow>() {
            @Override
            public void onResponse(Call<GithubWorkflow> call, Response<GithubWorkflow> response) {
                workflowList = response.body().getWorkflows();
                workflowListRecyclerView.setAdapter(new WorkflowAdapter(WorkflowActivity.this, workflowList, owner, repo, WorkflowActivity.this));
                if (workflowList.size() == 0) {
                    Toast.makeText(WorkflowActivity.this, "No workflow", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GithubWorkflow> call, Throwable t) {
                Toast.makeText(WorkflowActivity.this, "No workflow", Toast.LENGTH_LONG).show();
            }
        });

//        cardViewWorkflow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (cardViewWorkflow.isActivated()) {
//                    cardViewWorkflow.setActivated(false);
//                    workflowListRecyclerView.setVisibility(View.VISIBLE);
//                } else {
//                    cardViewWorkflow.setActivated(true);
//                    workflowListRecyclerView.setVisibility(View.GONE);
//                }
//            }
//        });
    }

    @Override
    public void onWorkflowOptionSelect(int option, int position) {
        switch (option) {
            case 0:
                Call<String> disableWorkflowCall = client.disableWorkflow(owner, repo, workflowList.get(position).getId());
                disableWorkflowCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.code() / 100 == 2) {
                            Toast.makeText(WorkflowActivity.this, "Succeeded", Toast.LENGTH_LONG).show();
                            workflowList.get(position).setState("inactive");
                            workflowListRecyclerView.setAdapter(new WorkflowAdapter(WorkflowActivity.this, workflowList, owner, repo, WorkflowActivity.this));
                        } else {
                            Toast.makeText(WorkflowActivity.this, "Failed", Toast.LENGTH_LONG).show();
                        }
                        Log.d("luong", response.toString());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("luong", t.toString());
                        Toast.makeText(WorkflowActivity.this, "Failed", Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case 1:
                Call<String> enableWorkflowCall = client.enableWorkflow(owner, repo, workflowList.get(position).getId());
                enableWorkflowCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.code() / 100 == 2) {
                            Toast.makeText(WorkflowActivity.this, "Succeeded", Toast.LENGTH_LONG).show();
                            workflowList.get(position).setState("active");
                            workflowListRecyclerView.setAdapter(new WorkflowAdapter(WorkflowActivity.this, workflowList, owner, repo, WorkflowActivity.this));
                        } else {
                            Toast.makeText(WorkflowActivity.this, "Failed", Toast.LENGTH_LONG).show();
                        }
                        Log.d("luong", response.toString());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("luong", t.toString());
                        Toast.makeText(WorkflowActivity.this, "Failed", Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case 2:
                new InfoDialogBuilder<>(this, workflowList.get(position)).build().show();
                break;

        }
    }
}