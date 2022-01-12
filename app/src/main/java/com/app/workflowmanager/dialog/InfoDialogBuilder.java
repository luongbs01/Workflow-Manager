package com.app.workflowmanager.dialog;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.app.workflowmanager.R;
import com.app.workflowmanager.entity.Step;
import com.app.workflowmanager.entity.Workflow;
import com.app.workflowmanager.entity.WorkflowJob;
import com.app.workflowmanager.entity.WorkflowRun;
import com.google.android.material.bottomsheet.BottomSheetDialog;


public class InfoDialogBuilder<T> {
    private final BottomSheetDialog mDialog;

    public InfoDialogBuilder(Context context, T t) {
        mDialog = new BottomSheetDialog(context, R.style.CustomDialog);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_info);
        TextView tvName = mDialog.findViewById(R.id.tv_info_name);
        TextView tvCreatedAt = mDialog.findViewById(R.id.tv_info_created);
        TextView tvUpdatedAt = mDialog.findViewById(R.id.tv_info_updated);
        TextView tvPath = mDialog.findViewById(R.id.tv_info_path);
        TextView tvState = mDialog.findViewById(R.id.tv_info_state);
        TextView tvEvent = mDialog.findViewById(R.id.tv_info_event);
        TextView tvStatus = mDialog.findViewById(R.id.tv_info_status);
        TextView tvConclusion = mDialog.findViewById(R.id.tv_info_conclusion);
        TextView tvStartedAt = mDialog.findViewById(R.id.tv_info_started);
        TextView tvCompletedAt = mDialog.findViewById(R.id.tv_info_completed);

        TextView tvTitleCreatedAt = mDialog.findViewById(R.id.tv_title_created);
        TextView tvTitleUpdatedAt = mDialog.findViewById(R.id.tv_title_updated);
        TextView tvTitlePath = mDialog.findViewById(R.id.tv_title_path);
        TextView tvTitleState = mDialog.findViewById(R.id.tv_title_state);
        TextView tvTitleEvent = mDialog.findViewById(R.id.tv_title_event);
        TextView tvTitleStatus = mDialog.findViewById(R.id.tv_title_status);
        TextView tvTitleConclusion = mDialog.findViewById(R.id.tv_title_conclusion);
        TextView tvTitleStartedAt = mDialog.findViewById(R.id.tv_title_started);
        TextView tvTitleCompletedAt = mDialog.findViewById(R.id.tv_title_completed);

        if (t instanceof Workflow) {
            Workflow instance = (Workflow) t;
            tvName.setText(instance.getName());
            tvCreatedAt.setText(instance.getCreated_at());
            tvUpdatedAt.setText(instance.getUpdated_at());
            tvPath.setText(instance.getPath());
            tvState.setText(instance.getState());
            tvEvent.setVisibility(View.GONE);
            tvStatus.setVisibility(View.GONE);
            tvConclusion.setVisibility(View.GONE);
            tvStartedAt.setVisibility(View.GONE);
            tvCompletedAt.setVisibility(View.GONE);
            tvTitleEvent.setVisibility(View.GONE);
            tvTitleStatus.setVisibility(View.GONE);
            tvTitleConclusion.setVisibility(View.GONE);
            tvTitleStartedAt.setVisibility(View.GONE);
            tvTitleCompletedAt.setVisibility(View.GONE);
        } else if (t instanceof WorkflowRun) {
            WorkflowRun instance = (WorkflowRun) t;
            tvName.setText(instance.getName());
            tvCreatedAt.setText(instance.getCreated_at());
            tvUpdatedAt.setText(instance.getUpdated_at());
            tvEvent.setText(instance.getEvent());
            tvStatus.setText(instance.getStatus());
            tvConclusion.setText(instance.getConclusion());
            tvPath.setVisibility(View.GONE);
            tvState.setVisibility(View.GONE);
            tvStartedAt.setVisibility(View.GONE);
            tvCompletedAt.setVisibility(View.GONE);
            tvTitlePath.setVisibility(View.GONE);
            tvTitleState.setVisibility(View.GONE);
            tvTitleStartedAt.setVisibility(View.GONE);
            tvTitleCompletedAt.setVisibility(View.GONE);
        } else if (t instanceof WorkflowJob) {
            WorkflowJob instance = (WorkflowJob) t;
            tvName.setText(instance.getName());
            tvStatus.setText(instance.getStatus());
            tvConclusion.setText(instance.getConclusion());
            tvStartedAt.setText(instance.getStarted_at());
            tvCompletedAt.setText(instance.getCompleted_at());
            tvCreatedAt.setVisibility(View.GONE);
            tvUpdatedAt.setVisibility(View.GONE);
            tvPath.setVisibility(View.GONE);
            tvState.setVisibility(View.GONE);
            tvEvent.setVisibility(View.GONE);
            tvTitleCreatedAt.setVisibility(View.GONE);
            tvTitleUpdatedAt.setVisibility(View.GONE);
            tvTitlePath.setVisibility(View.GONE);
            tvTitleState.setVisibility(View.GONE);
            tvTitleEvent.setVisibility(View.GONE);
        } else {
            Step instance = (Step) t;
            tvName.setText(instance.getName());
            tvStatus.setText(instance.getStatus());
            tvConclusion.setText(instance.getConclusion());
            tvStartedAt.setText(instance.getStarted_at());
            tvCompletedAt.setText(instance.getCompleted_at());
            tvCreatedAt.setVisibility(View.GONE);
            tvUpdatedAt.setVisibility(View.GONE);
            tvPath.setVisibility(View.GONE);
            tvState.setVisibility(View.GONE);
            tvEvent.setVisibility(View.GONE);
            tvTitleCreatedAt.setVisibility(View.GONE);
            tvTitleUpdatedAt.setVisibility(View.GONE);
            tvTitlePath.setVisibility(View.GONE);
            tvTitleState.setVisibility(View.GONE);
            tvTitleEvent.setVisibility(View.GONE);
        }
    }

    public BottomSheetDialog build() {
        return mDialog;
    }
}
