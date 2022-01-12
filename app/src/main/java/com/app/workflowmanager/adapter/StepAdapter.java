package com.app.workflowmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.workflowmanager.R;
import com.app.workflowmanager.dialog.BottomMenuDialogControl;
import com.app.workflowmanager.entity.Step;

import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.Holder> {

    private Context mContext;
    private List<Step> stepList;
    private LayoutInflater layoutInflater;
    private Callback mCallback;

    public interface Callback {
        void onStepOptionSelect(int option, int position);
    }

    public StepAdapter(Context context, List<Step> stepList, Callback callback) {
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
        mCallback = callback;
        this.stepList = stepList;
    }

    @NonNull
    @Override
    public StepAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = layoutInflater.inflate(R.layout.workflow_step_item, parent, false);
        return new StepAdapter.Holder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull StepAdapter.Holder holder, int position) {
        holder.textViewStepName.setText(stepList.get(position).getName());
        if (stepList.get(position).getStatus().equals("completed"))
            holder.imageViewStatus.setImageDrawable(mContext.getDrawable(R.drawable.check));
        else if (stepList.get(position).getStatus().equals("in_progress"))
            holder.imageViewStatus.setImageDrawable(mContext.getDrawable(R.drawable.work_in_progress));
        else if (stepList.get(position).getStatus().equals("queued"))
            holder.imageViewStatus.setImageDrawable(mContext.getDrawable(R.drawable.queue));

        holder.imageViewShowMoreRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomMenuDialogControl.getInstance().showMoreDialogStep(mContext, selection -> mCallback.onStepOptionSelect(selection, position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return (stepList != null) ? stepList.size() : 0;
    }

    class Holder extends RecyclerView.ViewHolder {
        public final TextView textViewStepName;
        public final ImageView imageViewStatus;
        public final ImageView imageViewShowMoreRun;
        final StepAdapter stepAdapter;

        public Holder(View itemView, StepAdapter stepAdapter) {
            super(itemView);
            this.stepAdapter = stepAdapter;
            textViewStepName = itemView.findViewById(R.id.tv_step);
            imageViewStatus = itemView.findViewById(R.id.iv_step_status);
            imageViewShowMoreRun = itemView.findViewById(R.id.iv_show_more_step);
        }
    }
}
