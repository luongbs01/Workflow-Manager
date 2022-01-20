package com.app.workflowmanager.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.app.workflowmanager.R;

public class SortDialogBuilder {
    private final Dialog mDialog;
    private RadioButton rbName, rbDate, rbAscending, rbDescending;
    private RadioGroup rgUnit, rgSort;

    public interface OkButtonClickListener {
        void onClick(int typeUnit, boolean ascending);
    }

    public SortDialogBuilder(Context context, OkButtonClickListener listener, int sortType, boolean ascending) {
        mDialog = new Dialog(context, R.style.CustomDialog);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_sort);

        initializeView();

        switch (sortType) {
            case 0:
                rbDate.setChecked(true);
                break;
            case 1:
                rbName.setChecked(true);
                break;
        }
        if (ascending) rbAscending.setChecked(true);
        else rbDescending.setChecked(true);

        mDialog.findViewById(R.id.tv_dialog_cancel).setOnClickListener(view -> mDialog.dismiss());
        mDialog.findViewById(R.id.tv_dialog_ok).setOnClickListener(view -> {
            mDialog.dismiss();
            int typeUnit = 0; // date
            if (rgUnit.getCheckedRadioButtonId() == R.id.rb_name) {
                typeUnit = 1;
            }
            boolean sort = rgSort.getCheckedRadioButtonId() == R.id.rb_ascending;
            listener.onClick(typeUnit, sort);
        });
    }

    private void initializeView() {
        rbName = mDialog.findViewById(R.id.rb_name);
        rbDate = mDialog.findViewById(R.id.rb_date);
        rgUnit = mDialog.findViewById(R.id.rg_unit);
        rgSort = mDialog.findViewById(R.id.rg_sort);
        rbAscending = mDialog.findViewById(R.id.rb_ascending);
        rbDescending = mDialog.findViewById(R.id.rb_descending);
    }

    public Dialog build() {
        return mDialog;
    }
}
