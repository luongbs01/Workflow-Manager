package com.app.workflowmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.workflowmanager.entity.GithubRepo;
import com.bumptech.glide.Glide;

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
        holder.textViewLogin.setText(githubRepoList.get(position).getOwner().getLogin());
        holder.textViewRepoName.setText(githubRepoList.get(position).getName());
        Glide.with(holder.itemView).load(githubRepoList.get(position).getOwner().getAvatar_url()).into(holder.imageViewAvatar);
    }

    @Override
    public int getItemCount() {
        return (githubRepoList != null) ? githubRepoList.size() : 0;
    }

    class Holder extends RecyclerView.ViewHolder {
        public final TextView textViewLogin;
        public final TextView textViewRepoName;
        public final ImageView imageViewAvatar;
        final GithubRepoAdapter githubRepoAdapter;

        public Holder(View itemView, GithubRepoAdapter githubRepoAdapter) {
            super(itemView);
            this.githubRepoAdapter = githubRepoAdapter;
            textViewLogin = itemView.findViewById(R.id.tv_login);
            textViewRepoName = itemView.findViewById(R.id.tv_repo_name);
            imageViewAvatar = itemView.findViewById(R.id.iv_avatar);
        }
    }
}
