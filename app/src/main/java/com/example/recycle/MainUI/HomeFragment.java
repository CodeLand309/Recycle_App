package com.example.recycle.MainUI;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recycle.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ArrayList<ExampleItem> exampleList = new ArrayList<>();
        exampleList.add(new ExampleItem(R.drawable.ic_round_home_24, "Line 1", "Line 2"));
        exampleList.add(new ExampleItem(R.drawable.ic_round_chat_24, "Line 3", "Line 4"));
        exampleList.add(new ExampleItem(R.drawable.ic_round_add_circle_24, "Line 5", "Line 6"));
        exampleList.add(new ExampleItem(R.drawable.ic_round_dispose_24, "Line 7", "Line 8")); 
        exampleList.add(new ExampleItem(R.drawable.ic_round_settings_24, "Line 9", "Line 10"));
        exampleList.add(new ExampleItem(R.drawable.ic_round_home_24, "Line 11", "Line 12"));
        exampleList.add(new ExampleItem(R.drawable.ic_round_chat_24, "Line 13", "Line 14"));
        exampleList.add(new ExampleItem(R.drawable.ic_round_add_circle_24, "Line 15", "Line 16"));
        exampleList.add(new ExampleItem(R.drawable.ic_round_dispose_24, "Line 17", "Line 18"));
        exampleList.add(new ExampleItem(R.drawable.ic_round_settings_24, "Line 19", "Line 20"));
        exampleList.add(new ExampleItem(R.drawable.ic_round_home_24, "Line 21", "Line 22"));
        exampleList.add(new ExampleItem(R.drawable.ic_round_chat_24, "Line 23", "Line 24"));
        exampleList.add(new ExampleItem(R.drawable.ic_round_add_circle_24, "Line 25", "Line 26"));
        exampleList.add(new ExampleItem(R.drawable.ic_round_dispose_24, "Line 27", "Line 28"));
        exampleList.add(new ExampleItem(R.drawable.ic_round_settings_24, "Line 29", "Line 30"));

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mAdapter = new ExampleAdapter(exampleList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }
}
