package com.example.recycle.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.recycle.R;

public class ConnectionFragment extends Fragment {

    private TextView text;
    private ImageView image;
    private int flag=0;

    public ConnectionFragment(int flag){
        this.flag = flag;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connection, container, false);

        setHasOptionsMenu(true);

        image=view.findViewById(R.id.imageView4);
        text = view.findViewById(R.id.no);

        if(flag == 1) {
            text.setText("Server Not Responding");
            image.setImageResource(R.drawable.server_error);
        }
        return view;
    }
}
