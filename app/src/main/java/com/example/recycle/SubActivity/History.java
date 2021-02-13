package com.example.recycle.SubActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.recycle.R;

import java.util.ArrayList;

public class History extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);




        ArrayList<HistoryItem> historyList = new ArrayList<>();
        historyList.add(new HistoryItem(R.drawable.ic_android, "Line 1", "Line 2"));
        historyList.add(new HistoryItem(R.drawable.ic_audio, "Line 3", "Line 4"));
        historyList.add(new HistoryItem(R.drawable.ic_sun, "Line 5", "Line 6"));



        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new HistoryAdapter(historyList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }


}