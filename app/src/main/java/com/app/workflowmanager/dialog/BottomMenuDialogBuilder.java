package com.app.workflowmanager.dialog;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.workflowmanager.R;
import com.app.workflowmanager.adapter.BottomMenuAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class BottomMenuDialogBuilder {
    BottomSheetDialog mDialog;
    RecyclerView mRvContent;

    public BottomMenuDialogBuilder(Context context, BottomMenuAdapter adapter) {
        mDialog = new BottomSheetDialog(context, R.style.CustomDialog);
        mDialog.setContentView(R.layout.custom_bottom_dialog);
        mRvContent = mDialog.findViewById(R.id.rv_content_dialog);
        mRvContent.setLayoutManager(new LinearLayoutManager(context));
        mRvContent.setAdapter(adapter);
    }

    public BottomSheetDialog build() {
        return mDialog;
    }
}
