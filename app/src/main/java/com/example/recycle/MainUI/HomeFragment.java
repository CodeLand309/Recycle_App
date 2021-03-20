package com.example.recycle.MainUI;

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

import com.example.recycle.R;
import com.example.recycle.RetrofitFolder.RestApiInterface;
import com.example.recycle.RetrofitFolder.RestClient;
import com.example.recycle.SignUPActivity;
import com.example.recycle.SubActivity.ProductDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    private int item_count = 6;

    private SharedPreferences sp;
    //Variables for Pagination
    private boolean isLoading = true;
    private int pastVisibleItems, visibleItemCount, totalItemCount, previous_total=0;
    private int view_threshold= 10;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        Log.d("TAG", "Reached Here1");
        mAdapter = new ProductsAdapter(products, getContext());
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        Log.d("TAG", "Reached Here2");
        sp = getContext().getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        user_id = sp.getInt("User ID",0);
        Log.d("TAG", String.valueOf(user_id));


        restApiInterface = RestClient.getRetrofit().create(RestApiInterface.class);

        progressBar.setVisibility(View.VISIBLE);
        Call<ArrayList<DataResponse>> call = restApiInterface.getProductData(page_number, item_count, user_id);
        call.enqueue(new Callback<ArrayList<DataResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<DataResponse>> call, @NonNull Response<ArrayList<DataResponse>> response) {
                products = response.body().get(1).getItems();
                mAdapter = new ProductsAdapter(products, getContext());
                Log.d("TAG", "Reached Here3");
                mRecyclerView.setAdapter(mAdapter);
                Log.d("Tag", String.valueOf(response.body()));
                Toast.makeText(getContext(), "First Page Loading", Toast.LENGTH_SHORT).show();
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

//        ArrayList<ExampleItem> exampleList = new ArrayList<>();
//        exampleList.add(new ExampleItem(R.drawable.ic_round_home_24, "Line 1", "Line 2"));
//        exampleList.add(new ExampleItem(R.drawable.ic_round_chat_24, "Line 3", "Line 4"));
//        exampleList.add(new ExampleItem(R.drawable.ic_round_add_circle_24, "Line 5", "Line 6"));
//        exampleList.add(new ExampleItem(R.drawable.ic_round_dispose_24, "Line 7", "Line 8"));
//        exampleList.add(new ExampleItem(R.drawable.ic_round_settings_24, "Line 9", "Line 10"));
//        exampleList.add(new ExampleItem(R.drawable.ic_round_home_24, "Line 11", "Line 12"));
//        exampleList.add(new ExampleItem(R.drawable.ic_round_chat_24, "Line 13", "Line 14"));
//        exampleList.add(new ExampleItem(R.drawable.ic_round_add_circle_24, "Line 15", "Line 16"));
//        exampleList.add(new ExampleItem(R.drawable.ic_round_dispose_24, "Line 17", "Line 18"));
//        exampleList.add(new ExampleItem(R.drawable.ic_round_settings_24, "Line 19", "Line 20"));
//        exampleList.add(new ExampleItem(R.drawable.ic_round_home_24, "Line 21", "Line 22"));
//        exampleList.add(new ExampleItem(R.drawable.ic_round_chat_24, "Line 23", "Line 24"));
//        exampleList.add(new ExampleItem(R.drawable.ic_round_add_circle_24, "Line 25", "Line 26"));
//        exampleList.add(new ExampleItem(R.drawable.ic_round_dispose_24, "Line 27", "Line 28"));
//        exampleList.add(new ExampleItem(R.drawable.ic_round_settings_24, "Line 29", "Line 30"));

//        mRecyclerView.setHasFixedSize(true);
//        mLayoutManager = new GridLayoutManager(getContext(), 2);
//        mAdapter = new ProductsAdapter(exampleList);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    private void changeActivity(int position, ArrayList<ProductsItem> products) {
        Toast.makeText(getContext(), "Clicked on a Item", Toast.LENGTH_SHORT).show();
        image = products.get(position).getImage();
        product_name = products.get(position).getProductName();
        product_id = products.get(position).getProductID();
        user_id = products.get(position).getUserID();
        user_name = products.get(position).getUserName();
        description = products.get(position).getDescription();
        price = products.get(position).getPrice();
        year = products.get(position).getYears();
        date = products.get(position).getDate();
        Log.d("Item", "changeActivity: ");
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
        progressBar.setVisibility(View.VISIBLE);
        Call<ArrayList<DataResponse>> call = restApiInterface.getProductData(page_number, item_count, user_id);
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
            }
        });
    }
}
