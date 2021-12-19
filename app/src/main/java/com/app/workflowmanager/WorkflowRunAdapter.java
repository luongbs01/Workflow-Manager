package com.app.workflowmanager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.workflowmanager.entity.WorkflowRun;

import java.util.List;

public class WorkflowRunAdapter extends RecyclerView.Adapter<WorkflowRunAdapter.Holder> {

    private Context mContext;
    private List<WorkflowRun> workflowRunList;
    private LayoutInflater layoutInflater;
    private String owner;
    private String repo;
    private Callback mCallback;

    public interface Callback {
        void onWorkflowRunOptionSelect(int option, int position);
    }

    public WorkflowRunAdapter(Context context, List<WorkflowRun> workflowRunList, String owner, String repo, Callback callback) {
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
        mCallback = callback;
        this.workflowRunList = workflowRunList;
        this.owner = owner;
        this.repo = repo;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = layoutInflater.inflate(R.layout.workflow_run_item, parent, false);
        return new WorkflowRunAdapter.Holder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.textViewWorkflowRunName.setText(workflowRunList.get(position).getName() + " #" + workflowRunList.get(position).getRun_number());

        holder.imageViewShowMoreRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomMenuDialogControl.getInstance().showMoreDialogWorkflowRun(mContext, selection -> mCallback.onWorkflowRunOptionSelect(selection, position));
            }
        });

        holder.textViewWorkflowRunName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), WorkflowJobActivity.class);
                intent.putExtra("owner", owner);
                intent.putExtra("repo", repo);
                intent.putExtra("run_id", workflowRunList.get(position).getId());
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (workflowRunList != null) ? workflowRunList.size() : 0;
    }

    class Holder extends RecyclerView.ViewHolder {
        public final TextView textViewWorkflowRunName;
        public final ImageView imageViewShowMoreRun;
        final WorkflowRunAdapter workflowRunAdapter;

        public Holder(View itemView, WorkflowRunAdapter workflowRunAdapter) {
            super(itemView);
            this.workflowRunAdapter = workflowRunAdapter;
            textViewWorkflowRunName = itemView.findViewById(R.id.tv_workflow_run_name);
            imageViewShowMoreRun = itemView.findViewById(R.id.iv_show_more_run);
        }
    }
}