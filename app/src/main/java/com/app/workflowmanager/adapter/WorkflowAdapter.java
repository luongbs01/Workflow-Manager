package com.app.workflowmanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.workflowmanager.R;
import com.app.workflowmanager.activity.WorkflowRunActivity;
import com.app.workflowmanager.dialog.BottomMenuDialogControl;
import com.app.workflowmanager.entity.Workflow;

import java.util.List;

public class WorkflowAdapter extends RecyclerView.Adapter<WorkflowAdapter.Holder> {

    private Context mContext;
    private List<Workflow> workflowList;
    private LayoutInflater layoutInflater;
    private String owner;
    private String repo;
    private Callback mCallback;

    public interface Callback {
        void onWorkflowOptionSelect(int option, int position);
    }

    public WorkflowAdapter(Context context, List<Workflow> workflowList, String owner, String repo, Callback callback) {
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
        mCallback = callback;
        this.workflowList = workflowList;
        this.owner = owner;
        this.repo = repo;
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
        if (workflowList.get(position).getState().equals("active")) {
            holder.imageViewBadge.setImageDrawable(mContext.getResources().getDrawable(R.drawable.workflow));
        } else {
            holder.imageViewBadge.setImageDrawable(mContext.getResources().getDrawable(R.drawable.caution));
        }

        holder.imageViewShowMoreWorkflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomMenuDialogControl.getInstance().showMoreDialogWorkflow(mContext, selection -> mCallback.onWorkflowOptionSelect(selection, position));
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), WorkflowRunActivity.class);
                intent.putExtra("owner", owner);
                intent.putExtra("repo", repo);
                intent.putExtra("workflow_id", workflowList.get(position).getId());
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (workflowList != null) ? workflowList.size() : 0;
    }

    class Holder extends RecyclerView.ViewHolder {
        public final TextView textViewWorkflowName;
        public final ImageView imageViewBadge;
        public final ImageView imageViewShowMoreWorkflow;
        final WorkflowAdapter workflowAdapter;

        public Holder(View itemView, WorkflowAdapter workflowAdapter) {
            super(itemView);
            this.workflowAdapter = workflowAdapter;
            textViewWorkflowName = itemView.findViewById(R.id.tv_workflow_name);
            imageViewBadge = itemView.findViewById(R.id.iv_badge);
            imageViewShowMoreWorkflow = itemView.findViewById(R.id.iv_show_more_workflow);
        }
    }
}
