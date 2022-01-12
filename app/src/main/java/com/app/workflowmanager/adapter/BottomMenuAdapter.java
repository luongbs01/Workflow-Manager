package com.app.workflowmanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.workflowmanager.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BottomMenuAdapter extends RecyclerView.Adapter<BottomMenuAdapter.ViewHolder> {
    private List<Integer> mSelections;
    private List<Integer> mIcons;
    private Callback mCallback;

    public BottomMenuAdapter(List<Integer> selections, List<Integer> icons, Callback callback) {
        mSelections = selections;
        mCallback = callback;
        mIcons = icons;
    }

    public interface Callback {
        void onClick(int position);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_option_bottom, parent, false);
        return new BottomMenuAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BottomMenuAdapter.ViewHolder holder, int position) {
        holder.tvSelectionName.setText(mSelections.get(position));
        holder.ivIcon.setImageResource(mIcons.get(position));
        holder.itemView.setOnClickListener(view -> mCallback.onClick(position));
    }

    @Override
    public int getItemCount() {
        return mSelections.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSelectionName;
        ImageView ivIcon;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvSelectionName = itemView.findViewById(R.id.tv_selection_name);
            ivIcon = itemView.findViewById(R.id.iv_icon_option);
        }
    }
}

