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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recycle.Adapters.ProductsAdapter;
import com.example.recycle.Model.DataResponse;
import com.example.recycle.Model.ProductsItem;
import com.example.recycle.R;
import com.example.recycle.RetrofitFolder.RestApiInterface;
import com.example.recycle.RetrofitFolder.RestClient;
import com.example.recycle.SubActivity.ProductDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RestApiInterface restApiInterface;
    private RecyclerView mRecyclerView;
    private ProductsAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    private ProgressBar progressBar;
    private ArrayList<ProductsItem> products;

    private String user_name, product_name, description, image, date;
    private int page_number = 1, user_id, product_id, price, year;
    private int item_count = 8;

    private SharedPreferences sp;
    //Variables for Pagination
    private boolean isLoading = true;
    private int pastVisibleItems, visibleItemCount, totalItemCount, previous_total=0;
    private int view_threshold= 10;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Current Fragment", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Name", "HomeFragment");
        editor.commit();

        progressBar = view.findViewById(R.id.progressBar);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mAdapter = new ProductsAdapter(products, getContext());
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        sp = getContext().getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        user_id = sp.getInt("User ID",0);


        restApiInterface = RestClient.getRetrofit().create(RestApiInterface.class);

        progressBar.setVisibility(View.VISIBLE);
        Call<ArrayList<DataResponse>> call;
        if(sp.contains("User ID") && sp.getInt("Log in Status",0) == 2) {
            call = restApiInterface.getProductData(page_number, item_count, user_id);
        }
        else{
            call = restApiInterface.getProductDataNoID(page_number, item_count);
        }
        call.enqueue(new Callback<ArrayList<DataResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<DataResponse>> call, @NonNull Response<ArrayList<DataResponse>> response) {
                products = response.body().get(1).getItems();
                mAdapter = new ProductsAdapter(products, getContext());
                mRecyclerView.setAdapter(mAdapter);
                progressBar.setVisibility(View.GONE);
                mAdapter.setOnItemClickListener(new ProductsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemCLick(int position) {
                        changeActivity(position, products);
                    }
                });
            }
            @Override
            public void onFailure(Call<ArrayList<DataResponse>> call, Throwable t) {
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

    private void changeActivity(int position, ArrayList<ProductsItem> products) {
        image = products.get(position).getImage();
        product_name = products.get(position).getProductName();
        product_id = products.get(position).getProductID();
        user_id = products.get(position).getUserID();
        user_name = products.get(position).getUserName();
        description = products.get(position).getDescription();
        price = products.get(position).getPrice();
        year = products.get(position).getYears();
        date = products.get(position).getDate();
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("User ID", user_id);
            jsonData.put("Name", user_name);
            jsonData.put("Product ID", product_id);
            jsonData.put("Product Name", product_name);
            jsonData.put("Description", description);
            jsonData.put("Price", price);
            jsonData.put("Years", year);
            jsonData.put("Image", image);
            jsonData.put("Date", date);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent i = new Intent(getContext(), ProductDetails.class);
        i.putExtra("Product", jsonData.toString());
        startActivity(i);
    }

    private void performPagination(){
        sp = getContext().getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        progressBar.setVisibility(View.VISIBLE);
        Call<ArrayList<DataResponse>> call;
        if(sp.contains("User ID") && sp.getInt("Log in Status",0) == 2) {
            call = restApiInterface.getProductData(page_number, item_count, user_id);
        }else{
            call = restApiInterface.getProductDataNoID(page_number, item_count);
        }
        call.enqueue(new Callback<ArrayList<DataResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<DataResponse>> call, @NonNull Response<ArrayList<DataResponse>> response) {
                if(response.body().get(0).getStatus().equals("ok")){
                    products = response.body().get(1).getItems();
                    mAdapter.addProduct(products);
                    mAdapter.setOnItemClickListener(new ProductsAdapter.OnItemClickListener() {
                        @Override
                        public void onItemCLick(int position) {
                            changeActivity(position, products);
                        }
                    });
                }
                else{
                    //Toast.makeText(ReadActivity.this, "No more Data", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<ArrayList<DataResponse>> call, Throwable t) {
                Toast.makeText(getContext(), "Cannot Access Server", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(1)).commit();
            }
        });
    }
}
