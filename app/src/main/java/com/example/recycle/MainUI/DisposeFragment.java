package com.example.recycle.MainUI;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recycle.R;

import java.util.ArrayList;

public class DisposeFragment extends Fragment {


 /*   String  [] listviewitems= new String[]{
            "Mattanchery Plastic Recycling center"
            , "Palarivattam Plastic Recycling Unit",
            "Palluruthy Scrap item collection unit ",
            "Aluva E-waste collection Centre", "Aroor E-waste Recycling Centre",
            "Fort Kochi Scrap item", "Biju's plastic Recycling unit",
            "Tthevara wasete Management centre ", "Edapally Scrap item collection unit"

    };*/

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view= inflater.inflate(R.layout.fragment_dispose, container, false);

      /*  final String[] listviewitems = new String[]{
                "Mattanchery Plastic Recycling center"
                , "Palarivattam Plastic Recycling Unit",
                "Palluruthy Scrap item collection unit ",
                "Aluva E-waste collection Centre", "Aroor E-waste Recycling Centre",
                "Fort Kochi Scrap item", "Biju's plastic Recycling unit",
                "Thevara wasete Management centre ", "Edapally Scrap item collection unit"

        };*/
//
//        listView = (ListView) view.findViewById(R.id.list);
//        final Resources res = getResources();
//        final String[] listviewitems = res.getStringArray(R.array.listviewitems);
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                getActivity(), android.R.layout.simple_list_item_1,listviewitems
//
//        );
//
//        listView.setAdapter((adapter));
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(MainActivity.this, secondActivity.class);
//                if(position == 0)
//                    intent.putExtra("listViewClickValues", res.getStringArray(R.array.Mattanchery Plastic Recycling center));
//                else if(position==1)
//                    intent.putExtra("listViewClickValues", res.getStringArray(R.array.Palarivattam Plastic Recycling Unit));
//                else if(position==2)
//                    intent.putExtra("listViewClickValues", res.getStringArray(R.array.Palluruthy Scrap item collection unit));
//                else if(position==3)
//                    intent.putExtra("listViewClickValues", res.getStringArray(R.array.Aluva E-waste collection Centre));
//                else if(position==4)
//                    intent.putExtra("listViewClickValues", res.getStringArray(R.array.Aroor E-waste Recycling Centre));
//
//
//                else if(position==4)
//                    intent.putExtra("listViewClickValues", res.getStringArray(R.array.Thevara wasete Management centre ));
//                else if(position==4)
//                    intent.putExtra("listViewClickValues", res.getStringArray(R.array.Edapally Scrap item collection unit));
//
//                startActivity(intent);
//            }
//        });


        ArrayList<DisposeCenter> disposeList = new ArrayList<>();
        disposeList.add(new DisposeCenter("Mattanchery Plastic Recycling center"));
        disposeList.add(new DisposeCenter("Palarivattam Plastic Recycling Unit"));
        disposeList.add(new DisposeCenter("Palluruthy Scrap item collection unit"));
        disposeList.add(new DisposeCenter("Aluva E-waste collection Centre"));
        disposeList.add(new DisposeCenter("Aroor E-waste Recycling Centre"));
        disposeList.add(new DisposeCenter("Fort Kochi Scrap item"));
        disposeList.add(new DisposeCenter("Biju's plastic Recycling unit"));
        disposeList.add(new DisposeCenter("Thevara wasete Management centre"));
        disposeList.add(new DisposeCenter("Edapally Scrap item collection unit"));
//        disposeList.add(new DisposeCenter(""));
//        disposeList.add(new DisposeCenter(""));
//        disposeList.add(new DisposeCenter(""));
//        disposeList.add(new DisposeCenter(""));
//        disposeList.add(new DisposeCenter(""));
//        disposeList.add(new DisposeCenter(""));

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new DisposeAdapter(disposeList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        return view;
    }
}
