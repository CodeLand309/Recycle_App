package com.example.recycle.SubActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

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

//        ArrayList<HistoryItem> historyList = new ArrayList<>();
//        historyList.add(new HistoryItem(R.drawable.ic_round_chat_24, "Line 1", "Line 2"));
//        historyList.add(new HistoryItem(R.drawable.ic_round_home_24, "Line 3", "Line 4"));
//        historyList.add(new HistoryItem(R.drawable.ic_round_dispose_24, "Line 5", "Line 6"));

//        mRecyclerView = findViewById(R.id.recyclerView);
//        mRecyclerView.setHasFixedSize(true);
//        mLayoutManager = new LinearLayoutManager(this);
//        mAdapter = new HistoryAdapter(historyList);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(mAdapter);


    }
}