package com.app.workflowmanager;

import android.app.Dialog;
import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;

public class BottomMenuDialogControl {
    Dialog mDialog;

    private static BottomMenuDialogControl sInstance;

    public BottomMenuDialogControl() {
    }

    public static BottomMenuDialogControl getInstance() {
        if (sInstance == null) {
            sInstance = new BottomMenuDialogControl();
        }
        return sInstance;
    }

    public void showMoreDialogWorkflowRun(Context context, BottomMenuAdapter.Callback callback) {
        if (mDialog != null && mDialog.isShowing()) return;
        mDialog = new BottomMenuDialogBuilder(context, new BottomMenuAdapter(
                new ArrayList<>(Arrays.asList(R.string.delete_workflow_run, R.string.info)),
                new ArrayList<>(Arrays.asList(R.drawable.ic_delete, R.drawable.ic_info)),
                position -> {
                    mDialog.dismiss();
                    callback.onClick(position);
                })).build();
        mDialog.show();
    }

    public void showMoreDialogWorkflow(Context context, BottomMenuAdapter.Callback callback) {
        if (mDialog != null && mDialog.isShowing()) return;
        mDialog = new BottomMenuDialogBuilder(context, new BottomMenuAdapter(
                new ArrayList<>(Arrays.asList(R.string.disable_workflow, R.string.enable_workflow, R.string.info)),
                new ArrayList<>(Arrays.asList(R.drawable.ic_cancel, R.drawable.ic_delete, R.drawable.ic_info)),
                position -> {
                    mDialog.dismiss();
                    callback.onClick(position);
                })).build();
        mDialog.show();
    }

    public void showMoreDialogWorkflowJob(Context context, BottomMenuAdapter.Callback callback) {
        if (mDialog != null && mDialog.isShowing()) return;
        mDialog = new BottomMenuDialogBuilder(context, new BottomMenuAdapter(
                new ArrayList<>(Arrays.asList(R.string.info)),
                new ArrayList<>(Arrays.asList(R.drawable.ic_info)),
                position -> {
                    mDialog.dismiss();
                    callback.onClick(position);
                })).build();
        mDialog.show();
    }

    public void showMoreDialogStep(Context context, BottomMenuAdapter.Callback callback) {
        if (mDialog != null && mDialog.isShowing()) return;
        mDialog = new BottomMenuDialogBuilder(context, new BottomMenuAdapter(
                new ArrayList<>(Arrays.asList(R.string.info)),
                new ArrayList<>(Arrays.asList(R.drawable.ic_info)),
                position -> {
                    mDialog.dismiss();
                    callback.onClick(position);
                })).build();
        mDialog.show();
    }
}

