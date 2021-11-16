package com.app.workflowmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GithubRepoAdapter extends RecyclerView.Adapter<GithubRepoAdapter.Holder> {

    private List<GithubRepo> githubRepoList;
    private LayoutInflater layoutInflater;

    public GithubRepoAdapter(Context context, List<GithubRepo> githubRepoList) {
        layoutInflater = LayoutInflater.from(context);
        this.githubRepoList = githubRepoList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = layoutInflater.inflate(R.layout.repo_item, parent, false);
        return new Holder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.textViewRepo.setText(githubRepoList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return (githubRepoList != null) ? githubRepoList.size() : 0;
    }

    class Holder extends RecyclerView.ViewHolder {
        public final TextView textViewRepo;
        final GithubRepoAdapter githubRepoAdapter;

        public Holder(View itemView, GithubRepoAdapter githubRepoAdapter) {
            super(itemView);
            this.githubRepoAdapter = githubRepoAdapter;
            textViewRepo = itemView.findViewById(R.id.tv_repo);
        }
    }
}
