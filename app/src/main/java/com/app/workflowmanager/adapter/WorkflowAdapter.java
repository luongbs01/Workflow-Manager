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
import com.app.workflowmanager.activity.WorkflowActivity;
import com.app.workflowmanager.activity.WorkflowRunActivity;
import com.app.workflowmanager.dialog.BottomMenuDialogControl;
import com.app.workflowmanager.entity.Workflow;
import com.app.workflowmanager.utils.Configs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

public class WorkflowAdapter extends RecyclerView.Adapter<WorkflowAdapter.Holder> {

    private Context mContext;
    private List<Workflow> workflowList;
    private LayoutInflater layoutInflater;
    private String owner;
    private String repo;
    private Callback mCallback;
    private int sortMode = Configs.SortType.DATE;
    private boolean ascending = false;

    public interface Callback {
        void onWorkflowOptionSelect(int option, int position);
    }

    public WorkflowAdapter(Context context, List<Workflow> workflowList) {
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
        this.workflowList = workflowList;
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
                if (mContext instanceof WorkflowActivity)
                    BottomMenuDialogControl.getInstance().showMoreDialogWorkflow(mContext, selection -> mCallback.onWorkflowOptionSelect(selection, position));
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof WorkflowActivity) {
                    Intent intent = new Intent(holder.itemView.getContext(), WorkflowRunActivity.class);
                    intent.putExtra("owner", owner);
                    intent.putExtra("repo", repo);
                    intent.putExtra("workflow_id", workflowList.get(position).getId());
                    holder.itemView.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (workflowList != null) ? workflowList.size() : 0;
    }

    public void sortList(int sortMode, boolean ascending) {
        switch (sortMode) {
            case Configs.SortType.DATE:
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                if (ascending)
                    Collections.sort(workflowList, (workflow1, workflow2) -> workflow1.getUpdated_at().compareTo(workflow2.getUpdated_at()));
                else
                    Collections.sort(workflowList, (workflow1, workflow2) -> workflow2.getUpdated_at().compareTo(workflow1.getUpdated_at()));
                break;
            case Configs.SortType.NAME:
                if (ascending)
                    Collections.sort(workflowList, (workflow1, workflow2) -> workflow1.getName().toUpperCase().compareTo(workflow2.getName().toUpperCase()));
                else
                    Collections.sort(workflowList, (workflow1, workflow2) -> workflow2.getName().toUpperCase().compareTo(workflow1.getName().toUpperCase()));
                break;
        }
        notifyDataSetChanged();
    }

    public void updateWorkflowDataList(List<Workflow> workflows) {
        workflowList = workflows;
        sortList(sortMode, ascending);
        notifyDataSetChanged();
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
