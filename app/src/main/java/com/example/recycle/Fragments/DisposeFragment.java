package com.example.recycle.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recycle.Adapters.DisposeAdapter;
import com.example.recycle.Model.CentreListResponse;
import com.example.recycle.Model.DisposeCentre;
import com.example.recycle.R;
import com.example.recycle.RetrofitFolder.RestApiInterface;
import com.example.recycle.RetrofitFolder.RestClient;
import com.example.recycle.SubActivity.DisposeCenterDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisposeFragment extends Fragment {

    private RestApiInterface restApiInterface;
    private RecyclerView mRecyclerView;
    private DisposeAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private ProgressBar progressBar;
    private ArrayList<DisposeCentre> centres;

    private int page_number = 1;
    private int item_count = 10;

    //Variables for Pagination
    private boolean isLoading = true;
    private String image, centre_name, address, phone;
    private int centre_id;
    private int pastVisibleItems, visibleItemCount, totalItemCount, previous_total=0;
    private int view_threshold= 10;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view= inflater.inflate(R.layout.fragment_dispose, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Current Fragment", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Name", "DisposeFragment");
        editor.commit();

        progressBar = view.findViewById(R.id.progressBar);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        Log.d("TAG", "Reached Here1");
        mAdapter = new DisposeAdapter(centres, getContext());
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        Log.d("TAG", "Reached Here2");
        restApiInterface = RestClient.getRetrofit().create(RestApiInterface.class);

        progressBar.setVisibility(View.VISIBLE);
        Call<ArrayList<CentreListResponse>> call = restApiInterface.getCentreData(page_number, item_count);
        call.enqueue(new Callback<ArrayList<CentreListResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<CentreListResponse>> call, @NonNull Response<ArrayList<CentreListResponse>> response) {
                centres = response.body().get(1).getCentres();
                mAdapter = new DisposeAdapter(centres, getContext());
                Log.d("TAG", "Reached Here3");
                mRecyclerView.setAdapter(mAdapter);
                Log.d("Tag", String.valueOf(response.body()));
                progressBar.setVisibility(View.GONE);
                mAdapter.setOnItemClickListener(new DisposeAdapter.OnCentreClickListener() {
                    @Override
                    public void onCentreCLick(int position) {
                        changeActivity(position, centres);
                    }
                });
            }
            @Override
            public void onFailure(Call<ArrayList<CentreListResponse>> call, Throwable t) {
                Toast.makeText(getContext(), "Cannot Access Server", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(1)).commit();
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

                if(dy>0){
                    if(isLoading){
                        if(totalItemCount>previous_total){
                            isLoading = false;
                            previous_total = totalItemCount;
                        }
                    }
                    if(!isLoading && (totalItemCount-visibleItemCount) <= (pastVisibleItems + view_threshold)){
                        page_number++;
                        performPagination();
                        isLoading = true;
                    }
                }
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
        Log.d("Item", "changeActivity: ");
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

    private void performPagination(){
        progressBar.setVisibility(View.VISIBLE);
        Call<ArrayList<CentreListResponse>> call = restApiInterface.getCentreData(page_number, item_count);
        call.enqueue(new Callback<ArrayList<CentreListResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<CentreListResponse>> call, @NonNull Response<ArrayList<CentreListResponse>> response) {

                if(response.body().get(0).getStatus().equals("ok")){
                    centres = response.body().get(1).getCentres();
                    mAdapter.addCentre(centres);
                    mAdapter.setOnItemClickListener(new DisposeAdapter.OnCentreClickListener() {
                        @Override
                        public void onCentreCLick(int position) {
                            changeActivity(position, centres);
                        }
                    });
                }
                else{
                    //Toast.makeText(ReadActivity.this, "No more Data", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<ArrayList<CentreListResponse>> call, Throwable t) {
                Toast.makeText(getContext(), "Cannot Access Server", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(1)).commit();
            }
        });
    }
}
