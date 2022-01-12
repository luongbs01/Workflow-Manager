package com.app.workflowmanager;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        stepListRecyclerView = findViewById(R.id.rv_step_list);
        stepListRecyclerView.setLayoutManager(new LinearLayoutManager(StepActivity.this));
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