package com.app.workflowmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.workflowmanager.entity.WorkflowJob;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WorkflowJobAdapter extends RecyclerView.Adapter<WorkflowJobAdapter.Holder> {

    private Context mContext;
    private List<WorkflowJob> workflowJobList;
    private LayoutInflater layoutInflater;
    private Callback mCallback;

    public interface Callback {
        void onWorkflowJobOptionSelect(int option, int position);
    }

    public WorkflowJobAdapter(Context context, List<WorkflowJob> workflowJobList, Callback callback) {
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
        mCallback = callback;
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
        if (workflowJobList.get(position).getStatus().equals("completed"))
            holder.imageViewStatus.setImageDrawable(mContext.getDrawable(R.drawable.check));
        else if (workflowJobList.get(position).getStatus().equals("in_progress"))
            holder.imageViewStatus.setImageDrawable(mContext.getDrawable(R.drawable.work_in_progress));
        else if (workflowJobList.get(position).getStatus().equals("queued"))
            holder.imageViewStatus.setImageDrawable(mContext.getDrawable(R.drawable.queue));

        holder.imageViewShowMoreRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomMenuDialogControl.getInstance().showMoreDialogWorkflowJob(mContext, selection -> mCallback.onWorkflowJobOptionSelect(selection, position));
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), StepActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("step", (ArrayList) workflowJobList.get(position).getSteps());
                intent.putExtras(bundle);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (workflowJobList != null) ? workflowJobList.size() : 0;
    }

    class Holder extends RecyclerView.ViewHolder {
        public final TextView textViewWorkflowJobName;
        public final ImageView imageViewStatus;
        public final ImageView imageViewShowMoreRun;
        final WorkflowJobAdapter workflowJobAdapter;

        public Holder(View itemView, WorkflowJobAdapter workflowJobAdapter) {
            super(itemView);
            this.workflowJobAdapter = workflowJobAdapter;
            textViewWorkflowJobName = itemView.findViewById(R.id.tv_workflow_job);
            imageViewStatus = itemView.findViewById(R.id.iv_job_status);
            imageViewShowMoreRun = itemView.findViewById(R.id.iv_show_more_workflow_job);
        }
    }
}
