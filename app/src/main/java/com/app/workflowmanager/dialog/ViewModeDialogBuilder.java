package com.app.workflowmanager.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.app.workflowmanager.R;

public class ViewModeDialogBuilder {

    private final Dialog mDialog;
    private RadioButton rbRepository, rbWorkflow, rbWorkflowRun;
    private RadioGroup rgUnit;

    public interface OkButtonClickListener {
        void onClick(int viewMode);
    }

    public ViewModeDialogBuilder(Context context, OkButtonClickListener listener, int viewMode) {
        mDialog = new Dialog(context, R.style.CustomDialog);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.view_mode);

        rbRepository = mDialog.findViewById(R.id.rb_repository);
        rbWorkflow = mDialog.findViewById(R.id.rb_workflow);
        rbWorkflowRun = mDialog.findViewById(R.id.rb_workflow_run);

        rgUnit = mDialog.findViewById(R.id.rg_unit);

        switch (viewMode) {
            case 0:
                rbRepository.setChecked(true);
                break;
            case 1:
                rbWorkflow.setChecked(true);
                break;
            case 3:
                rbWorkflowRun.setChecked(true);
                break;
        }

        mDialog.findViewById(R.id.tv_dialog_cancel).setOnClickListener(view -> mDialog.dismiss());
        mDialog.findViewById(R.id.tv_dialog_ok).setOnClickListener(view -> {
            mDialog.dismiss();
            int typeUnit = 0;
            switch (rgUnit.getCheckedRadioButtonId()) {
                case R.id.rb_repository:
                    typeUnit = 0;
                    break;
                case R.id.rb_workflow:
                    typeUnit = 1;
                    break;
                case R.id.rb_workflow_run:
                    typeUnit = 2;
                    break;
            }
            listener.onClick(typeUnit);
        });
    }

    public Dialog build() {
        return mDialog;
    }
}
