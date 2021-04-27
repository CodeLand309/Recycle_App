package com.example.recycle.MainUI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recycle.R;

public class NotFoundFragment extends Fragment {

    public NotFoundFragment() {
        // Required empty public constructor
    }

    public static NotFoundFragment newInstance(String param1, String param2) {
        NotFoundFragment fragment = new NotFoundFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_not_found, container, false);
        return view;
    }
}