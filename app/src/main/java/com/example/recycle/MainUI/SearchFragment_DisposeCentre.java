package com.example.recycle.MainUI;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

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

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link SearchFragment_DisposeCentre#newInstance} factory method to
// * create an instance of this fragment.
// */
public class SearchFragment_DisposeCentre extends Fragment {

    private RestApiInterface restApiInterface;
    private RecyclerView mRecyclerView;
    private DisposeAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private ProgressBar progressBar;
    private ArrayList<DisposeCentre> centres;

    private int page_number = 1;
    private int item_count = 7;

    //Variables for Pagination
    private boolean isLoading = true;
    private String image, centre_name, address, phone;
    private int centre_id;
    private int pastVisibleItems, visibleItemCount, totalItemCount, previous_total=0;
    private int view_threshold= 7;

//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    public SearchFragment_DisposeCentre() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment SearchFragment_DisposeCentre.
//     */
//    // TODO: Rename and change types and number of parameters
    public static SearchFragment_DisposeCentre newInstance() {
        SearchFragment_DisposeCentre fragment = new SearchFragment_DisposeCentre();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_dispose, container, false);

        Bundle bundle = getArguments();
        String search_word = bundle.getString("Search Word");

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
        Call<ArrayList<CentreListResponse>> call = restApiInterface.searchCentreName(page_number, item_count, search_word);
        call.enqueue(new Callback<ArrayList<CentreListResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<CentreListResponse>> call, @NonNull Response<ArrayList<CentreListResponse>> response) {
                centres = response.body().get(1).getCentres();
                mAdapter = new DisposeAdapter(centres, getContext());
                Log.d("TAG", "Reached Here3");
                mRecyclerView.setAdapter(mAdapter);
                Log.d("Tag", String.valueOf(response.body()));
                Toast.makeText(getContext(), "First Page Loading", Toast.LENGTH_SHORT).show();
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
                        performPagination(search_word);
                        isLoading = true;
                    }
                }
            }
        });

        return view;
    }

    private void changeActivity(int position, ArrayList<DisposeCentre> centres) {
        Toast.makeText(getContext(), "Clicked on a Item", Toast.LENGTH_SHORT).show();
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

    private void performPagination(String search_word){
        progressBar.setVisibility(View.VISIBLE);
        Call<ArrayList<CentreListResponse>> call = restApiInterface.searchCentreName(page_number, item_count, search_word);
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
            }
        });
    }
}