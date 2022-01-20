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
import com.app.workflowmanager.activity.WorkflowJobActivity;
import com.app.workflowmanager.activity.WorkflowRunActivity;
import com.app.workflowmanager.dialog.BottomMenuDialogControl;
import com.app.workflowmanager.entity.WorkflowRun;
import com.app.workflowmanager.utils.Configs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

public class WorkflowRunAdapter extends RecyclerView.Adapter<WorkflowRunAdapter.Holder> {

    private Context mContext;
    private List<WorkflowRun> workflowRunList;
    private LayoutInflater layoutInflater;
    private String owner;
    private String repo;
    private Callback mCallback;
    private int sortMode = Configs.SortType.DATE;
    private boolean ascending = false;

    public interface Callback {
        void onWorkflowRunOptionSelect(int option, int position);
    }

    public WorkflowRunAdapter(Context context, List<WorkflowRun> workflowRunList) {
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
        this.workflowRunList = workflowRunList;
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
        if (workflowRunList.get(position).getEvent().equals("push")) {
            holder.textViewWorkflowRunName.setText(workflowRunList.get(position).getHeadCommit().getMessage());
        } else
            holder.textViewWorkflowRunName.setText(workflowRunList.get(position).getName());
        holder.textViewWorkflowRunNumber.setText(workflowRunList.get(position).getName() + " #" + workflowRunList.get(position).getRun_number());
        if (workflowRunList.get(position).getStatus().equals("completed"))
            holder.imageViewStatus.setImageDrawable(mContext.getDrawable(R.drawable.check));
        else if (workflowRunList.get(position).getStatus().equals("in_progress"))
            holder.imageViewStatus.setImageDrawable(mContext.getDrawable(R.drawable.work_in_progress));
        else if (workflowRunList.get(position).getStatus().equals("queued"))
            holder.imageViewStatus.setImageDrawable(mContext.getDrawable(R.drawable.queue));
        holder.imageViewShowMoreRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof WorkflowRunActivity)
                    BottomMenuDialogControl.getInstance().showMoreDialogWorkflowRun(mContext, selection -> mCallback.onWorkflowRunOptionSelect(selection, position));
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof WorkflowRunActivity) {
                    Intent intent = new Intent(holder.itemView.getContext(), WorkflowJobActivity.class);
                    intent.putExtra("owner", owner);
                    intent.putExtra("repo", repo);
                    intent.putExtra("run_id", workflowRunList.get(position).getId());
                    holder.itemView.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (workflowRunList != null) ? workflowRunList.size() : 0;
    }

    public void sortList(int sortMode, boolean ascending) {
        switch (sortMode) {
            case Configs.SortType.DATE:
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                if (ascending)
                    Collections.sort(workflowRunList, (workflowRun1, workflowRun2) -> workflowRun1.getUpdated_at().compareTo(workflowRun2.getUpdated_at()));
                else
                    Collections.sort(workflowRunList, (workflow1, workflow2) -> workflow2.getUpdated_at().compareTo(workflow1.getUpdated_at()));
                break;
            case Configs.SortType.NAME:
                if (ascending)
                    Collections.sort(workflowRunList, (workflow1, workflow2) -> workflow1.getName().toUpperCase().compareTo(workflow2.getName().toUpperCase()));
                else
                    Collections.sort(workflowRunList, (workflow1, workflow2) -> workflow2.getName().toUpperCase().compareTo(workflow1.getName().toUpperCase()));
                break;
        }
        notifyDataSetChanged();
    }

    public void updateWorkflowRunDataList(List<WorkflowRun> workflowRuns) {
        workflowRunList = workflowRuns;
        sortList(sortMode, ascending);
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder {
        public final TextView textViewWorkflowRunName;
        public final TextView textViewWorkflowRunNumber;
        public final ImageView imageViewShowMoreRun;
        public final ImageView imageViewStatus;
        final WorkflowRunAdapter workflowRunAdapter;

        public Holder(View itemView, WorkflowRunAdapter workflowRunAdapter) {
            super(itemView);
            this.workflowRunAdapter = workflowRunAdapter;
            textViewWorkflowRunName = itemView.findViewById(R.id.tv_workflow_run_name);
            textViewWorkflowRunNumber = itemView.findViewById(R.id.tv_workflow_run_number);
            imageViewShowMoreRun = itemView.findViewById(R.id.iv_show_more_run);
            imageViewStatus = itemView.findViewById(R.id.iv_status);
        }
    }
}