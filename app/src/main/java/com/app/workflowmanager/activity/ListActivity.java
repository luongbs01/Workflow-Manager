package com.app.workflowmanager.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.workflowmanager.GithubClient;
import com.app.workflowmanager.R;
import com.app.workflowmanager.adapter.GithubRepoAdapter;
import com.app.workflowmanager.adapter.WorkflowAdapter;
import com.app.workflowmanager.adapter.WorkflowRunAdapter;
import com.app.workflowmanager.dialog.SortDialogBuilder;
import com.app.workflowmanager.dialog.ViewModeDialogBuilder;
import com.app.workflowmanager.entity.GithubRepo;
import com.app.workflowmanager.entity.Workflow;
import com.app.workflowmanager.entity.WorkflowRun;
import com.app.workflowmanager.entity.WorkflowRunWrapper;
import com.app.workflowmanager.entity.WorkflowWrapper;
import com.app.workflowmanager.utils.Configs;
import com.app.workflowmanager.utils.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListActivity extends AppCompatActivity {

    private ImageView ivSearch;
    private RelativeLayout rlTitle;
    private RelativeLayout rlSearchView;
    private SearchView searchView;
    private TextView tvCancelSearch;
    private ImageView ivBack;
    private ImageView ivSort;
    private ImageView ivViewMode;
    private RecyclerView rvList;

    private List<GithubRepo> githubRepoList;
    private GithubRepoAdapter githubRepoAdapter;
    private List<Workflow> workflowList = new ArrayList<>();
    private WorkflowAdapter workflowAdapter;
    private List<WorkflowRun> workflowRunList = new ArrayList<>();
    private WorkflowRunAdapter workflowRunAdapter;

    private int mSortType = Configs.SortType.DATE;
    private int mViewMode = Configs.ViewMode.REPOSITORY;
    private boolean mIsSortAscending = false;

    private boolean hasWorkflow = false;
    private boolean hasWorkflowRun = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initializeView();

        initializeEvent();

        try {
            fetchData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeView() {
        ivSearch = findViewById(R.id.iv_search);
        rlTitle = findViewById(R.id.rl_title);
        rlSearchView = findViewById(R.id.rl_search_view);
        searchView = findViewById(R.id.search_view);
        tvCancelSearch = findViewById(R.id.tv_cancel_search);
        ivBack = findViewById(R.id.iv_back);
        ivSort = findViewById(R.id.iv_sort);
        ivViewMode = findViewById(R.id.iv_view_mode);
        rvList = findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(ListActivity.this));
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(rvList.getContext(), 1);
        rvList.addItemDecoration(mDividerItemDecoration);
    }

    private void initializeEvent() {
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlSearchView.setVisibility(View.VISIBLE);
                rlTitle.setVisibility(View.INVISIBLE);
                searchView.setFocusable(true);
                searchView.setIconified(false);
                searchView.requestFocusFromTouch();
            }
        });

        tvCancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlSearchView.setVisibility(View.INVISIBLE);
                rlTitle.setVisibility(View.VISIBLE);
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ivSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new SortDialogBuilder(ListActivity.this, (typeUnit, ascending) -> {
                    if (mSortType != typeUnit || mIsSortAscending != ascending) {
                        githubRepoAdapter.sortList(typeUnit, ascending);
                        workflowAdapter.sortList(typeUnit, ascending);
                        workflowRunAdapter.sortList(typeUnit, ascending);
                        mSortType = typeUnit;
                        mIsSortAscending = ascending;
                    }
                }, mSortType, mIsSortAscending).build();
                dialog.show();
            }
        });

        ivViewMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasWorkflow && hasWorkflowRun) {
                    ivViewMode.setAlpha(1.0F);
                    Dialog dialog = new ViewModeDialogBuilder(ListActivity.this, (viewMode) -> {
                        switch (viewMode) {
                            case 0:
                                rvList.setAdapter(githubRepoAdapter);
                                break;
                            case 1:
                                rvList.setAdapter(workflowAdapter);
                                break;
                            case 2:
                                rvList.setAdapter(workflowRunAdapter);
                                break;
                        }
                        mViewMode = viewMode;
                    }, mViewMode).build();
                    dialog.show();
                } else {
                    Toast.makeText(ListActivity.this, "Fetching data, please wait!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (rvList.getAdapter() instanceof GithubRepoAdapter) {
                    List<GithubRepo> queryResult = Utility.searchRepoByName(githubRepoList, query);
                    githubRepoAdapter.updateRepoDataList(queryResult);
                } else if (rvList.getAdapter() instanceof WorkflowAdapter) {
                    List<Workflow> queryResult = Utility.searchWorkflowByName(workflowList, query);
                    workflowAdapter.updateWorkflowDataList(queryResult);
                } else if (rvList.getAdapter() instanceof WorkflowRunAdapter) {
                    List<WorkflowRun> queryResult = Utility.searchWorkflowRunByName(workflowRunList, query);
                    workflowRunAdapter.updateWorkflowRunDataList(queryResult);
                }
                return false;
            }
        });
    }

    private void fetchData() throws IOException {

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "token "
                            + getSharedPreferences(getResources().getString(R.string.preference_file_key),
                            Context.MODE_PRIVATE).getString("access_token", ""))
                    .build();
            return chain.proceed(newRequest);
        }).build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        Retrofit retrofit = builder.build();

        GithubClient client = retrofit.create(GithubClient.class);

        Call<List<GithubRepo>> call = client.repos();
        call.enqueue(new Callback<List<GithubRepo>>() {
            @Override
            public void onResponse(Call<List<GithubRepo>> call, Response<List<GithubRepo>> response) {
                githubRepoList = response.body();
                githubRepoAdapter = new GithubRepoAdapter(ListActivity.this, githubRepoList);
                rvList.setAdapter(githubRepoAdapter);
                getWorkflow(retrofit);
                getWorkflowRun(retrofit);
            }

            @Override
            public void onFailure(Call<List<GithubRepo>> call, Throwable t) {
                Toast.makeText(ListActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getWorkflow(Retrofit retrofit) {
        List<Observable<WorkflowWrapper>> requests = new ArrayList<>();
        for (GithubRepo githubRepo : githubRepoList) {
            requests.add(retrofit.create(GithubClient.class).getWorkflow(githubRepo.getOwner().getLogin(), githubRepo.getName()));
        }

        Log.e("onSubscribe", "START: ");
        Observable.zip(
                requests,
                new Function<Object[], List<WorkflowWrapper>>() {
                    @Override
                    public List<WorkflowWrapper> apply(Object[] objects) throws Exception {
                        Log.e("onSubscribe", "apply: " + objects.length);
                        List<WorkflowWrapper> GithubWorkflows = new ArrayList<>();
                        for (Object o : objects) {
                            GithubWorkflows.add((WorkflowWrapper) o);
                        }
                        return GithubWorkflows;
                    }
                }).subscribeOn(Schedulers.io())
                .subscribe(
                        new Consumer<List<WorkflowWrapper>>() {
                            @Override
                            public void accept(List<WorkflowWrapper> dataResponses) throws Exception {
                                Log.e("onSubscribe", "YOUR DATA IS HERE: " + dataResponses);
                                for (int i = 0; i < dataResponses.size(); i++) {
                                    if (dataResponses.get(i).getTotal_count() > 0) {
                                        workflowList.addAll(dataResponses.get(i).getWorkflows());
                                    }
                                }
                                hasWorkflow = true;
                                workflowAdapter = new WorkflowAdapter(ListActivity.this, workflowList);
                            }
                        },

                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable e) throws Exception {
                                Log.e("onSubscribe", "Throwable: " + e);
                            }
                        }
                );
    }

    private void getWorkflowRun(Retrofit retrofit) {
        List<Observable<WorkflowRunWrapper>> requests = new ArrayList<>();
        for (GithubRepo githubRepo : githubRepoList) {
            requests.add(retrofit.create(GithubClient.class).getWorkflowRun(githubRepo.getOwner().getLogin(), githubRepo.getName()));
        }

        Log.e("onSubscribe", "START: ");
        Observable.zip(
                requests,
                new Function<Object[], List<WorkflowRunWrapper>>() {
                    @Override
                    public List<WorkflowRunWrapper> apply(Object[] objects) throws Exception {
                        Log.e("onSubscribe", "apply: " + objects.length);
                        List<WorkflowRunWrapper> GithubWorkflowRuns = new ArrayList<>();
                        for (Object o : objects) {
                            GithubWorkflowRuns.add((WorkflowRunWrapper) o);
                        }
                        return GithubWorkflowRuns;
                    }
                }).subscribeOn(Schedulers.io())
                .subscribe(
                        new Consumer<List<WorkflowRunWrapper>>() {
                            @Override
                            public void accept(List<WorkflowRunWrapper> dataResponses) throws Exception {
                                Log.e("onSubscribe", "YOUR DATA IS HERE: " + dataResponses);
                                for (int i = 0; i < dataResponses.size(); i++) {
                                    if (dataResponses.get(i).getTotal_count() > 0) {
                                        workflowRunList.addAll(dataResponses.get(i).getWorkflow_runs());
                                    }
                                }
                                hasWorkflowRun = true;
                                workflowRunAdapter = new WorkflowRunAdapter(ListActivity.this, workflowRunList);
                            }
                        },

                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable e) throws Exception {
                                Log.e("onSubscribe", "Throwable: " + e);
                            }
                        }
                );
    }

}

