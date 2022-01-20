package com.app.workflowmanager.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.workflowmanager.R;
import com.app.workflowmanager.adapter.StepAdapter;
import com.app.workflowmanager.dialog.InfoDialogBuilder;
import com.app.workflowmanager.entity.Step;

import java.util.List;


public class StepActivity extends AppCompatActivity implements StepAdapter.Callback {

    private RecyclerView stepListRecyclerView;
    private List<Step> stepList;
    private StepAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        initializeView();
    }

    private void initializeView() {
        stepListRecyclerView = findViewById(R.id.rv_step_list);
        stepListRecyclerView.setLayoutManager(new LinearLayoutManager(StepActivity.this));
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(stepListRecyclerView.getContext(), 1);
        stepListRecyclerView.addItemDecoration(mDividerItemDecoration);
        stepList = this.getIntent().getExtras().getParcelableArrayList("step");
        adapter = new StepAdapter(this, stepList, StepActivity.this);
        stepListRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onStepOptionSelect(int option, int position) {
        switch (option) {
            case 0:
                new InfoDialogBuilder<>(this, stepList.get(position)).build().show();
                break;
        }
    }
}