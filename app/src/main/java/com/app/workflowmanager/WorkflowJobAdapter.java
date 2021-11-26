package com.app.workflowmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.workflowmanager.entity.WorkflowJob;

import java.util.List;

public class WorkflowJobAdapter extends RecyclerView.Adapter<WorkflowJobAdapter.Holder> {

    private List<WorkflowJob> workflowJobList;
    private LayoutInflater layoutInflater;

    public WorkflowJobAdapter(Context context, List<WorkflowJob> workflowJobList) {
        layoutInflater = LayoutInflater.from(context);
        this.workflowJobList = workflowJobList;
    }

    @NonNull
    @Override
    public WorkflowJobAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = layoutInflater.inflate(R.layout.workflow_job_item, parent, false);
        return new WorkflowJobAdapter.Holder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkflowJobAdapter.Holder holder, int position) {
        holder.textViewWorkflowJobName.setText(workflowJobList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return (workflowJobList != null) ? workflowJobList.size() : 0;
    }

    class Holder extends RecyclerView.ViewHolder {
        public final TextView textViewWorkflowJobName;
        final WorkflowJobAdapter workflowJobAdapter;

        public Holder(View itemView, WorkflowJobAdapter workflowJobAdapter) {
            super(itemView);
            this.workflowJobAdapter = workflowJobAdapter;
            textViewWorkflowJobName = itemView.findViewById(R.id.tv_workflow_job_name);
        }
    }
}
