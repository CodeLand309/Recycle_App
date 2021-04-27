package com.example.recycle.SubActivity;

import android.content.Context;
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
import com.example.recycle.ServerErrorActivity;

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
    private int user_id, product_id, price, year, status;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        SharedPreferences sp = getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        user_id = sp.getInt("User ID", 0);
        progressBar = findViewById(R.id.progressBar);
        mRecyclerView = findViewById(R.id.history_container);
        Log.d("TAG", "Reached Here1");
        mAdapter = new HistoryAdapter(products, History.this);
        mLayoutManager = new LinearLayoutManager(History.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        Log.d("TAG", "Reached Here2");

        restApiInterface = RestClient.getRetrofit().create(RestApiInterface.class);

        progressBar.setVisibility(View.VISIBLE);
        Call<ArrayList<HistoryResponse>> call = restApiInterface.getHistory(user_id);
        call.enqueue(new Callback<ArrayList<HistoryResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<HistoryResponse>> call, @NonNull Response<ArrayList<HistoryResponse>> response) {
                if(response.body().get(0).getStatus().equals("found")) {
                    products = response.body().get(1).getItems();
                    mAdapter = new HistoryAdapter(products, History.this);
                    Log.d("TAG", "Reached Here3");
                    mRecyclerView.setAdapter(mAdapter);
                    Log.d("Tag", String.valueOf(response.body()));
                    progressBar.setVisibility(View.GONE);
                    mAdapter.setOnItemClickListener(new HistoryAdapter.OnItemClickListener() {
                        @Override
                        public void onItemCLick(int position) {
                            changeActivity(position, products);
                        }
                    });
                }else{
                    Intent i = new Intent(History.this, EmptyActivity.class);
                    startActivity(i);
                }
            }
            @Override
            public void onFailure(Call<ArrayList<HistoryResponse>> call, Throwable t) {
                Intent i = new Intent(History.this, ServerErrorActivity.class);
                startActivity(i);
                Toast.makeText(History.this, "Cannot Access Server", Toast.LENGTH_SHORT).show();
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
        status = products.get(position).getStatus();
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
            jsonData.put("Status", status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent i = new Intent(History.this, MyProductDetails.class);
        i.putExtra("Product", jsonData.toString());
        startActivity(i);
    }
}