package com.app.workflowmanager.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.workflowmanager.R;
import com.app.workflowmanager.adapter.GithubRepoAdapter;
import com.app.workflowmanager.dialog.SortDialogBuilder;
import com.app.workflowmanager.entity.GithubClient;
import com.app.workflowmanager.entity.GithubRepo;
import com.app.workflowmanager.entity.GithubWorkflow;
import com.app.workflowmanager.entity.Step;
import com.app.workflowmanager.entity.Workflow;
import com.app.workflowmanager.entity.WorkflowJob;
import com.app.workflowmanager.entity.WorkflowRun;
import com.app.workflowmanager.utils.Configs;
import com.app.workflowmanager.utils.Utility;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListActivity extends AppCompatActivity {

    private ImageView ivSearch;
    private RelativeLayout rlTitle;
    private RelativeLayout rlSearchView;
    private SearchView searchView;
    private TextView tvCancelSearch;
    private ImageView ivBack;
    private ImageView ivSort;
    private RecyclerView rvList;

    private List<GithubRepo> githubRepoList;
    private GithubRepoAdapter mAdapter;
    private List<Workflow> workflowList;
    private List<WorkflowRun> workflowRunList;
    private List<WorkflowJob> workflowJobList;
    private List<Step> stepList;

    private int mSortType = Configs.SortType.DATE;
    private boolean mIsSortAscending = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ivSearch = findViewById(R.id.iv_search);
        rlTitle = findViewById(R.id.rl_title);
        rlSearchView = findViewById(R.id.rl_search_view);
        searchView = findViewById(R.id.search_view);
        tvCancelSearch = findViewById(R.id.tv_cancel_search);
        ivBack = findViewById(R.id.iv_back);
        ivSort = findViewById(R.id.iv_sort);
        rvList = findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(ListActivity.this));
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(rvList.getContext(), 1);
        rvList.addItemDecoration(mDividerItemDecoration);

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
                        mAdapter.sortList(typeUnit, ascending);
                        mSortType = typeUnit;
                        mIsSortAscending = ascending;
                    }
                }, mSortType, mIsSortAscending).build();
                dialog.show();
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
                List<GithubRepo> queryResult = Utility.searchRepoByName(githubRepoList, query);
                mAdapter.updateRepoDataList(queryResult);
                return false;
            }
        });

        new Task().execute();
//        try {
//            fetchData();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    int i = 0;

    private void fetchData() throws IOException, InterruptedException {

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
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        GithubClient client = retrofit.create(GithubClient.class);

        if (i == 0) {
            Call<List<GithubRepo>> call = client.repos();
            Response<List<GithubRepo>> githubRepoResponse = call.execute();

            if (githubRepoResponse.body().size() != 0)
                githubRepoList = githubRepoResponse.body();
        }

        Log.d("luong", "" + githubRepoList.size());
        if (i < githubRepoList.size()) {
//        for (GithubRepo githubRepo : githubRepoList) {
            Call<GithubWorkflow> workflowCall = client.workflow(githubRepoList.get(i).getOwner().getLogin(), githubRepoList.get(i).getName());
            Response<GithubWorkflow> response = workflowCall.execute();
            assert response.body() != null;
            if (response.body().getWorkflows().size() != 0) {
                Log.d("luong workflow", "" + response.body().getWorkflows().size());
                workflowList.addAll(response.body().getWorkflows());
            }
            i++;
            Thread.sleep(2000);
//            fetchData();
        }
//            break;
    }

//        Observable.range(0, githubRepoList.size()).observeOn(AndroidSchedulers.mainThread())
//                .doOnError(new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Throwable {
//
//                    }
//                })
//                .concatMap(new Function<Integer, Observable<GithubWorkflow>>() {
//                    @Override
//                    public Observable<GithubWorkflow> apply(Integer integer) throws Throwable {
//                        return client.getWorkflow(githubRepoList.get(integer).getOwner().getLogin(), githubRepoList.get(integer).getName());
//                    }
//                })
//                .concatMap(new Function<GithubWorkflow, Observable<Workflow>>() {
//                               @Override
//                               public Observable<Workflow> apply(GithubWorkflow githubWorkflow) throws Throwable {
//                                   return (Observable<Workflow>) githubWorkflow.getWorkflows();
//                               }
//                           }
//                ).toList().subscribe(new Consumer<List<Workflow>>() {
//            @Override
//            public void accept(List<Workflow> workflowLists) throws Throwable {
//                workflowList = workflowLists;
//            }
//        });


    class Task extends AsyncTask<String, Void, Boolean> {

        protected Boolean doInBackground(String... urls) {
            try {
                fetchData();
//                for (GithubRepo githubRepo : githubRepoList) {
//                    Call<GithubWorkflow> workflowCall = client.workflow(githubRepo.getOwner().getLogin(), githubRepo.getName());
//                    Response<GithubWorkflow> response = workflowCall.execute();
//                    assert response.body() != null;
//                    if (response.body().getWorkflows().size() != 0) {
//                        workflowList.addAll(response.body().getWorkflows());
//                    }
//                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            mAdapter = new GithubRepoAdapter(ListActivity.this, githubRepoList);
            mAdapter.sortList(mSortType, mIsSortAscending);
            rvList.setAdapter(mAdapter);
        }
    }


}

