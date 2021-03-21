package com.example.recycle.SubActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recycle.R;
import com.example.recycle.RetrofitFolder.RestApiInterface;
import com.example.recycle.RetrofitFolder.RestClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class History extends AppCompatActivity {


    private RestApiInterface restApiInterface;
    private RecyclerView mRecyclerView;
    private HistoryAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private ProgressBar progressBar;
    private ArrayList<HistoryItem> products;

    private String user_name, product_name, description, image, date;
    private int page_number = 1, user_id, product_id, price, year;
    private int item_count = 6;

    private SharedPreferences sp;
    //Variables for Pagination
    private boolean isLoading = true;
    private int pastVisibleItems, visibleItemCount, totalItemCount, previous_total=0;
    private int view_threshold= 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        progressBar = findViewById(R.id.progressBar);
        mRecyclerView = findViewById(R.id.history_container);
        Log.d("TAG", "Reached Here1");
        mAdapter = new HistoryAdapter(products, History.this);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(History.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        Log.d("TAG", "Reached Here2");
//        sp = getContext().getSharedPreferences("Credentials", Context.MODE_PRIVATE);
//        user_id = sp.getInt("User ID",0);
//        Log.d("TAG", String.valueOf(user_id));


        restApiInterface = RestClient.getRetrofit().create(RestApiInterface.class);

        progressBar.setVisibility(View.VISIBLE);
        Call<ArrayList<HistoryResponse>> call = restApiInterface.getHistory(page_number, item_count, user_id);
        call.enqueue(new Callback<ArrayList<HistoryResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<HistoryResponse>> call, @NonNull Response<ArrayList<HistoryResponse>> response) {
                products = response.body().get(1).getItems();
                mAdapter = new HistoryAdapter(products, History.this);
                Log.d("TAG", "Reached Here3");
                mRecyclerView.setAdapter(mAdapter);
                Log.d("Tag", String.valueOf(response.body()));
                Toast.makeText(History.this, "First Page Loading", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                mAdapter.setOnItemClickListener(new HistoryAdapter.OnItemClickListener() {
                    @Override
                    public void onItemCLick(int position) {
                        changeActivity(position, products);
                    }
                });
            }
            @Override
            public void onFailure(Call<ArrayList<HistoryResponse>> call, Throwable t) {
                Toast.makeText(History.this, "Cannot Access Server", Toast.LENGTH_SHORT).show();
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
    }

    private void changeActivity(int position, ArrayList<HistoryItem> products) {
        Toast.makeText(History.this, "Clicked on a Item", Toast.LENGTH_SHORT).show();
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
        Intent i = new Intent(History.this, MyProductDetails.class);
        i.putExtra("Product", jsonData.toString());
        startActivity(i);
    }

    private void performPagination(){
        progressBar.setVisibility(View.VISIBLE);
        Call<ArrayList<HistoryResponse>> call = restApiInterface.getHistory(page_number, item_count, user_id);
        call.enqueue(new Callback<ArrayList<HistoryResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<HistoryResponse>> call, @NonNull Response<ArrayList<HistoryResponse>> response) {

                if(response.body().get(0).getStatus().equals("ok")){
                    products = response.body().get(1).getItems();
                    mAdapter.addProduct(products);
                    mAdapter.setOnItemClickListener(new HistoryAdapter.OnItemClickListener() {
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
            public void onFailure(Call<ArrayList<HistoryResponse>> call, Throwable t) {
                Toast.makeText(History.this, "Cannot Access Server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}