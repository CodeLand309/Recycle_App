package com.example.recycle.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.recycle.Adapters.DisposeAdapter;
import com.example.recycle.Model.DisposeCentre;
import com.example.recycle.R;
import com.example.recycle.Network.RestApiInterface;
import com.example.recycle.Network.RestClient;
import com.example.recycle.SubActivity.DisposeCenterDetails;
import com.example.recycle.SubActivity.InfoActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchFragment_DisposeCentre extends Fragment {

    private RestApiInterface restApiInterface;
    private RecyclerView mRecyclerView;
    private DisposeAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private ProgressBar progressBar;
    private ArrayList<DisposeCentre> centres;
    private FloatingActionButton Fab;

    private String image, centre_name, address, phone;
    private int centre_id;


    public SearchFragment_DisposeCentre() {
        // Required empty public constructor
    }

    public static SearchFragment_DisposeCentre newInstance() {
        return new SearchFragment_DisposeCentre();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_dispose, container, false);

        Fab = view.findViewById(R.id.information);
        setRetainInstance(true);

        Bundle bundle = getArguments();
        String search_word = bundle.getString("Search Word");

        progressBar = view.findViewById(R.id.progressBar);
        mRecyclerView = view.findViewById(R.id.recyclerView);
       // mAdapter = new DisposeAdapter(centres, getContext());
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        restApiInterface = RestClient.getRetrofit().create(RestApiInterface.class);

        progressBar.setVisibility(View.VISIBLE);
//        Call<ArrayList<CentreListResponse>> call = restApiInterface.searchCentreName(search_word);
//        call.enqueue(new Callback<ArrayList<CentreListResponse>>() {
//            @Override
//            public void onResponse(@NonNull Call<ArrayList<CentreListResponse>> call, @NonNull Response<ArrayList<CentreListResponse>> response) {
//                if(response.body().get(0).getStatus().equals("found")){
//                    centres = response.body().get(1).getCentres();
//                    mAdapter = new DisposeAdapter(centres, getContext());
//                    mRecyclerView.setAdapter(mAdapter);
//                    progressBar.setVisibility(View.GONE);
//                    mAdapter.setOnItemClickListener(new DisposeAdapter.OnCentreClickListener() {
//                        @Override
//                        public void onCentreCLick(int position) {
//                            changeActivity(position, centres);
//                        }
//                    });
//                }else{
//                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotFoundFragment()).commit();
//                }
//            }
//            @Override
//            public void onFailure(Call<ArrayList<CentreListResponse>> call, Throwable t) {
//                Toast.makeText(getContext(), "Cannot Access Server", Toast.LENGTH_SHORT).show();
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(1)).commit();
//            }
//        });

        Fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), InfoActivity.class);
                startActivity(i);
            }
        });

        return view;
    }

    private void changeActivity(int position, ArrayList<DisposeCentre> centres) {
        image = centres.get(position).getCentreImage();
        centre_name = centres.get(position).getCentreName();
        centre_id = centres.get(position).getCentreID();
        address = centres.get(position).getCentreAddress();
        phone = centres.get(position).getCentrePhone();
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("Centre ID", centre_id);
            jsonData.put("Name", centre_name);
            jsonData.put("Image", image);
            jsonData.put("Address", address);
            jsonData.put("Phone", phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent i = new Intent(getContext(), DisposeCenterDetails.class);
        i.putExtra("Centre", jsonData.toString());
        startActivity(i);
    }
}