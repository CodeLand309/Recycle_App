package com.example.recycle.MainUI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recycle.R;

import java.util.ArrayList;

public class SellFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_sell, container, false);


      // View view = inflater.inflate(R.layout.fragment_home, container, false);
        ArrayList<SellItem> sellList = new ArrayList<>();
        sellList.add(new SellItem(R.drawable.ic_round_home_24, "Line 1", "Line 2"));
        sellList.add(new SellItem(R.drawable.ic_round_chat_24, "Line 3", "Line 4"));
        sellList.add(new SellItem(R.drawable.ic_round_add_circle_24, "Line 5", "Line 6"));
        sellList.add(new SellItem(R.drawable.ic_round_dispose_24, "Line 7", "Line 8"));
        sellList.add(new SellItem(R.drawable.ic_round_settings_24, "Line 9", "Line 10"));
        sellList.add(new SellItem(R.drawable.ic_round_home_24, "Line 11", "Line 12"));
        sellList.add(new SellItem(R.drawable.ic_round_chat_24, "Line 13", "Line 14"));
        sellList.add(new SellItem(R.drawable.ic_round_add_circle_24, "Line 15", "Line 16"));
        sellList.add(new SellItem(R.drawable.ic_round_dispose_24, "Line 17", "Line 18"));
        sellList.add(new SellItem(R.drawable.ic_round_settings_24, "Line 19", "Line 20"));
        sellList.add(new SellItem(R.drawable.ic_round_home_24, "Line 21", "Line 22"));
        sellList.add(new SellItem(R.drawable.ic_round_chat_24, "Line 23", "Line 24"));
        sellList.add(new SellItem(R.drawable.ic_round_add_circle_24, "Line 25", "Line 26"));
        sellList.add(new SellItem(R.drawable.ic_round_dispose_24, "Line 27", "Line 28"));
        sellList.add(new SellItem(R.drawable.ic_round_settings_24, "Line 29", "Line 30"));

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new SellAdapter(sellList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);




        return view;
    }
}
