package com.app.workflowmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.workflowmanager.entity.GithubWorkflowRun;
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

public class WorkflowRunActivity extends AppCompatActivity implements WorkflowRunAdapter.Callback {

    private RecyclerView workflowRunListRecyclerView;
    private WorkflowRunAdapter adapter;
    private CardView cardViewWorkflowRun;
    private List<WorkflowRun> workflowRunList;

    private OkHttpClient okHttpClient;
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
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(workflowRunListRecyclerView.getContext(), 1);
        workflowRunListRecyclerView.addItemDecoration(mDividerItemDecoration);
        cardViewWorkflowRun = findViewById(R.id.cv_workflow_runs);
        cardViewWorkflowRun.setActivated(true);

        owner = getIntent().getStringExtra("owner");
        repo = getIntent().getStringExtra("repo");
        workflow_id = getIntent().getIntExtra("workflow_id", 0);

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
//        Log.d("luong", "run: " + getSharedPreferences(getResources().getString(R.string.preference_file_key),
//                Context.MODE_PRIVATE).getString("access_token", ""));
        builder = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create());
        retrofit = builder.build();

        client = retrofit.create(GithubClient.class);

        Call<GithubWorkflowRun> workflowRunCall = client.workflowRun(owner, repo, workflow_id);
        workflowRunCall.enqueue(new Callback<GithubWorkflowRun>() {
            @Override
            public void onResponse(Call<GithubWorkflowRun> call, Response<GithubWorkflowRun> response) {
                workflowRunList = response.body().getWorkflow_runs();
                adapter = new WorkflowRunAdapter(WorkflowRunActivity.this, workflowRunList,
                        owner, repo, WorkflowRunActivity.this);
                workflowRunListRecyclerView.setAdapter(adapter);
                if (workflowRunList.size() == 0) {
                    Toast.makeText(WorkflowRunActivity.this, "No workflow run", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GithubWorkflowRun> call, Throwable t) {
                Toast.makeText(WorkflowRunActivity.this, "No workflow", Toast.LENGTH_LONG).show();
            }
        });

//        cardViewWorkflowRun.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (cardViewWorkflowRun.isActivated()) {
//                    cardViewWorkflowRun.setActivated(false);
//                    workflowRunListRecyclerView.setVisibility(View.VISIBLE);
//                } else {
//                    cardViewWorkflowRun.setActivated(true);
//                    workflowRunListRecyclerView.setVisibility(View.GONE);
//                }
//            }
//        });
    }

    @Override
    public void onWorkflowRunOptionSelect(int option, int position) {
        switch (option) {
            case 0:
                Call<String> deleteWorkflowRunCall = client.deleteWorkflowRun(owner, repo, workflowRunList.get(position).getId());
                deleteWorkflowRunCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.code() / 100 == 2) {
                            workflowRunList.remove(workflowRunList.get(position));
                            adapter.notifyItemRemoved(position);
                            adapter.notifyItemRangeChanged(position, workflowRunList.size());
                            Toast.makeText(WorkflowRunActivity.this, "Succeeded", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(WorkflowRunActivity.this, "Failed", Toast.LENGTH_LONG).show();
                        }
                        Log.d("luong", response.toString());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(WorkflowRunActivity.this, "Failed", Toast.LENGTH_LONG).show();
                        Log.d("luong", t.toString());
                    }
                });
                break;
//            case 1:
//                Call<String> cancelWorkflowRunCall = client.cancelWorkflowRun(owner, repo, workflowRunList.get(position).getId());
//                cancelWorkflowRunCall.enqueue(new Callback<String>() {
//                    @Override
//                    public void onResponse(Call<String> call, Response<String> response) {
//                        if (response.code() / 100 == 2) {
//                            Toast.makeText(WorkflowRunActivity.this, "Succeeded", Toast.LENGTH_LONG).show();
//                        } else {
//                            Toast.makeText(WorkflowRunActivity.this, "Failed", Toast.LENGTH_LONG).show();
//                        }
//                        Log.d("luong", response.toString());
//                    }
//
//                    @Override
//                    public void onFailure(Call<String> call, Throwable t) {
//                        Toast.makeText(WorkflowRunActivity.this, "Failed", Toast.LENGTH_LONG).show();
//                        Log.d("luong", t.toString());
//                    }
//                });
//                break;
//            case 2:
//                Call<String> rerunWorkflowRunCall = client.rerunWorkflowRun(owner, repo, workflowRunList.get(position).getId());
//                rerunWorkflowRunCall.enqueue(new Callback<String>() {
//                    @Override
//                    public void onResponse(Call<String> call, Response<String> response) {
//                        if (response.code() / 100 == 2) {
//                            Toast.makeText(WorkflowRunActivity.this, "Succeeded", Toast.LENGTH_LONG).show();
//                        } else {
//                            Toast.makeText(WorkflowRunActivity.this, "Failed", Toast.LENGTH_LONG).show();
//                        }
//                        Log.d("luong", response.toString());
//                    }
//
//                    @Override
//                    public void onFailure(Call<String> call, Throwable t) {
//                        Toast.makeText(WorkflowRunActivity.this, "Failed", Toast.LENGTH_LONG).show();
//                        Log.d("luong", t.toString());
//                    }
//                });
//                break;
            case 1:
                new InfoDialogBuilder<>(this, workflowRunList.get(position)).build().show();
                break;

        }
    }
}