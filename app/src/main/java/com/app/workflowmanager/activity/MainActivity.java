package com.app.workflowmanager.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.workflowmanager.R;
import com.app.workflowmanager.adapter.GithubRepoAdapter;
import com.app.workflowmanager.entity.GithubClient;
import com.app.workflowmanager.entity.GithubRepo;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout noRepoConstraintLayout;
    private Button signInAgainButton;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private RecyclerView repoListRecyclerView;
    private SharedPreferences sharedPreferences;
    private List<GithubRepo> repoList;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        noRepoConstraintLayout = findViewById(R.id.layout_no_repo);
        signInAgainButton = findViewById(R.id.bt_sign_in_again);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawerLayout = findViewById(R.id.activity_main_drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem view) {
                view.setCheckable(false);
                drawerLayout.closeDrawers();
                Intent intent;
                switch (view.getItemId()) {
                    case R.id.log_out:
                        intent = new Intent(MainActivity.this, SignInActivity.class);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("access_token", null);
                        editor.apply();
                        startActivity(intent);
                        break;
                    case R.id.list_mode:
                        intent = new Intent(MainActivity.this, ListActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putParcelableArrayList("repoList", (ArrayList) repoList);
//                        intent.putExtras(bundle);
                        startActivity(intent);
                }
                return true;
            }
        });

        repoListRecyclerView = findViewById(R.id.rv_repo_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        repoListRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(repoListRecyclerView.getContext(), 1);
        repoListRecyclerView.addItemDecoration(mDividerItemDecoration);

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
        Call<List<GithubRepo>> call = client.repos();
        call.enqueue(new Callback<List<GithubRepo>>() {
            @Override
            public void onResponse(Call<List<GithubRepo>> call, Response<List<GithubRepo>> response) {
                repoList = response.body();
                repoListRecyclerView.setAdapter(new GithubRepoAdapter(MainActivity.this, repoList));
                if (repoList == null) {
                    DisplayLayout();
                }
            }

            @Override
            public void onFailure(Call<List<GithubRepo>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void DisplayLayout() {
        noRepoConstraintLayout.setVisibility(View.VISIBLE);
        signInAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
}