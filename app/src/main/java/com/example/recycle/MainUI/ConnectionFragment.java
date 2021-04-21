package com.example.recycle.MainUI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.recycle.R;

public class ConnectionFragment extends Fragment {

    private TextView text;
    private int flag=0;

    ConnectionFragment(int flag){
        this.flag = flag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connection, container, false);

        text = view.findViewById(R.id.no);
        if(flag == 1)
            text.setText("Server Not Responding");
        return view;
    }
}
