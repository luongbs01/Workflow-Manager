package com.app.workflowmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.workflowmanager.entity.Workflow;

import java.util.List;

public class WorkflowAdapter extends RecyclerView.Adapter<WorkflowAdapter.Holder> {

    private List<Workflow> workflowList;
    private LayoutInflater layoutInflater;

    public WorkflowAdapter(Context context, List<Workflow> workflowList) {
        layoutInflater = LayoutInflater.from(context);
        this.workflowList = workflowList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = layoutInflater.inflate(R.layout.workflow_item, parent, false);
        return new WorkflowAdapter.Holder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.textViewWorkflowName.setText(workflowList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return (workflowList != null) ? workflowList.size() : 0;
    }

    class Holder extends RecyclerView.ViewHolder {
        public final TextView textViewWorkflowName;
        final WorkflowAdapter workflowAdapter;

        public Holder(View itemView, WorkflowAdapter workflowAdapter) {
            super(itemView);
            this.workflowAdapter = workflowAdapter;
            textViewWorkflowName = itemView.findViewById(R.id.tv_workflow_name);
        }
    }
}
