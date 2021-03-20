package com.example.recycle.MainUI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recycle.R;
import com.example.recycle.RetrofitFolder.RestApiInterface;
import com.example.recycle.RetrofitFolder.RestClient;
import com.example.recycle.SubActivity.EditProduct;
import com.example.recycle.SubActivity.MyProductDetails;
import com.example.recycle.SubActivity.ProductDetails;
import com.example.recycle.SubActivity.UploadProduct;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private SellAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private RestApiInterface restApiInterface;

    private PopupMenu dropDownMenu;
    private ProgressBar progressBar;
    private ArrayList<ProductsItem> products;
    private FloatingActionButton AddButton;
    private Button Options;

    private String user_name, product_name, description, image, Result, date;
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
        View view= inflater.inflate(R.layout.fragment_sell, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        AddButton = view.findViewById(R.id.addButton);
        Log.d("TAG", "Reached Here1");
        mAdapter = new SellAdapter(products, getContext());
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        Log.d("TAG", "Reached Here2");
        sp = getContext().getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        user_id = sp.getInt("User ID",0);
        Log.d("TAG", String.valueOf(user_id));

      // View view = inflater.inflate(R.layout.fragment_home, container, false);
//        ArrayList<SellItem> sellList = new ArrayList<>();
//        sellList.add(new SellItem(R.drawable.ic_round_home_24, "Line 1", "Line 2"));
//        sellList.add(new SellItem(R.drawable.ic_round_chat_24, "Line 3", "Line 4"));
//        sellList.add(new SellItem(R.drawable.ic_round_add_circle_24, "Line 5", "Line 6"));
//        sellList.add(new SellItem(R.drawable.ic_round_dispose_24, "Line 7", "Line 8"));
//        sellList.add(new SellItem(R.drawable.ic_round_settings_24, "Line 9", "Line 10"));
//        sellList.add(new SellItem(R.drawable.ic_round_home_24, "Line 11", "Line 12"));
//        sellList.add(new SellItem(R.drawable.ic_round_chat_24, "Line 13", "Line 14"));
//        sellList.add(new SellItem(R.drawable.ic_round_add_circle_24, "Line 15", "Line 16"));
//        sellList.add(new SellItem(R.drawable.ic_round_dispose_24, "Line 17", "Line 18"));
//        sellList.add(new SellItem(R.drawable.ic_round_settings_24, "Line 19", "Line 20"));
//        sellList.add(new SellItem(R.drawable.ic_round_home_24, "Line 21", "Line 22"));
//        sellList.add(new SellItem(R.drawable.ic_round_chat_24, "Line 23", "Line 24"));
//        sellList.add(new SellItem(R.drawable.ic_round_add_circle_24, "Line 25", "Line 26"));
//        sellList.add(new SellItem(R.drawable.ic_round_dispose_24, "Line 27", "Line 28"));
//        sellList.add(new SellItem(R.drawable.ic_round_settings_24, "Line 29", "Line 30"));
//
//        mRecyclerView = view.findViewById(R.id.recyclerView);
//        mRecyclerView.setHasFixedSize(true);
//        mLayoutManager = new LinearLayoutManager(getContext());
//        mAdapter = new SellAdapter(sellList);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(mAdapter);

        restApiInterface = getRest();

        progressBar.setVisibility(View.VISIBLE);
        Call<ArrayList<DataResponse>> call = restApiInterface.getUserProducts(page_number, item_count, user_id);
        call.enqueue(new Callback<ArrayList<DataResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<DataResponse>> call, @NonNull Response<ArrayList<DataResponse>> response) {
                products = response.body().get(1).getItems();
                mAdapter = new SellAdapter(products, getContext());
                Log.d("TAG", "Reached Here3");
                mRecyclerView.setAdapter(mAdapter);
                Log.d("Tag", String.valueOf(response.body()));
                Toast.makeText(getContext(), "First Page Loading", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                mAdapter.setOnItemClickListener(new SellAdapter.OnItemClickListener() {
                    @Override
                    public void onItemCLick(int position) {
                        changeActivity(position, products);
                    }

                    @Override
                    public void onOptionsCLick(int position, Button options) {
                        Toast.makeText(getContext(), "Clicked on Options", Toast.LENGTH_SHORT).show();
                        dropDownMenu = new PopupMenu(getContext(), options);
                        dropDownMenu.getMenuInflater().inflate(R.menu.options, dropDownMenu.getMenu());
                        dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch(menuItem.getItemId()) {
                                    case R.id.edit_item:
                                        product_id = products.get(position).getProductID();
                                        product_name = products.get(position).getProductName();
                                        image = products.get(position).getImage();
                                        user_id = products.get(position).getUserID();
                                        user_name = products.get(position).getUserName();
                                        description = products.get(position).getDescription();
                                        price = products.get(position).getPrice();
                                        year = products.get(position).getYears();

                                        Toast.makeText(getContext(), product_name, Toast.LENGTH_SHORT).show();
                                        EditData(product_id, product_name, image, user_id, description, price, year);
                                        return true;
                                    case R.id.delete_item:
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                        builder.setTitle("Delete");
                                        builder.setMessage("Confirm Delete operation?");
                                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                product_id = products.get(position).getProductID();
                                                product_name = products.get(position).getProductName();
                                                Log.d("Product ID", product_id+"");
                                                Log.d("Product Name", product_name);
                                                Toast.makeText(getContext(), product_name, Toast.LENGTH_SHORT).show();
                                                DeleteData(product_id, product_name);
                                            }
                                        });
                                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                        return true;
                                }
                                return true;
                            }
                        });
                        dropDownMenu.show();
                    }
                });

            }
            @Override
            public void onFailure(Call<ArrayList<DataResponse>> call, Throwable t) {
                Toast.makeText(getContext(), "Cannot Access Server", Toast.LENGTH_SHORT).show();
            }
        });

        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), UploadProduct.class);
                startActivity(i);
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

    private void EditData(int product_id, String product_name, String image, int user_id, String description, int price, int year) {
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("User ID", user_id);
            jsonData.put("Product ID", product_id);
            jsonData.put("Product Name", product_name);
            jsonData.put("Description", description);
            jsonData.put("Price", price);
            jsonData.put("Years", year);
            jsonData.put("Image", image);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent i = new Intent(getContext(), EditProduct.class);
        i.putExtra("Product", jsonData.toString());
        startActivity(i);
    }

    private RestApiInterface getRest(){
        restApiInterface = RestClient.getRetrofit().create(RestApiInterface.class);
        return restApiInterface;
    }

    private void DeleteData(int product_id, String product_name) {
        restApiInterface = getRest();
        Toast.makeText(getContext(), "Hello", Toast.LENGTH_SHORT).show();
        Call<JsonElement> call = restApiInterface.deleteProduct(product_id, product_name);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (!response.isSuccessful()) {
                    Result = "Code: " + response.code();
                    Toast.makeText(getContext(), Result, Toast.LENGTH_SHORT).show();
                    return;
                }
                JsonObject jsonObject = response.body().getAsJsonObject();
                String content = jsonObject.get("status").getAsString();
                Toast.makeText(getContext(), content, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Result = t.getMessage();
                Toast.makeText(getContext(), Result, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeActivity(int position, ArrayList<ProductsItem> products) {
        Toast.makeText(getContext(), "Clicked on a Item", Toast.LENGTH_SHORT).show();
        image = products.get(position).getImage();
        product_name = products.get(position).getProductName();
        product_id = products.get(position).getProductID();
        user_id = products.get(position).getUserID();
//        user_name = products.get(position).getUserName();
        description = products.get(position).getDescription();
        price = products.get(position).getPrice();
        year = products.get(position).getYears();
        date = products.get(position).getDate();

        Log.d("image", image);
        Log.d("Product", product_name);
        Log.d("Product ID", product_id+"");
        Log.d("User ID", user_id+"");
//        Log.d("User Name", user_name);
        Log.d("Description", description);
        Log.d("Price", price+"");
        Log.d("Year", year+"");
        Log.d("Date", date);


        Log.d("Item", "changeActivity: ");
        JSONObject jsonData = new JSONObject();
        try {
//            jsonData.put("Name", user_name);
            jsonData.put("User ID", user_id);
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
        Intent i = new Intent(getContext(), MyProductDetails.class);
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
                    mAdapter.setOnItemClickListener(new SellAdapter.OnItemClickListener() {
                        @Override
                        public void onItemCLick(int position) {
                            changeActivity(position, products);
                        }

                        @Override
                        public void onOptionsCLick(int position, Button options) {
                            Toast.makeText(getContext(), "Clicked on Options", Toast.LENGTH_SHORT).show();
                            dropDownMenu = new PopupMenu(getContext(), options);
                            dropDownMenu.getMenuInflater().inflate(R.menu.options, dropDownMenu.getMenu());
                            dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                                @Override
                                public boolean onMenuItemClick(MenuItem menuItem) {
                                    switch(menuItem.getItemId()) {
                                        case R.id.edit_item:
                                            product_id = products.get(position).getProductID();
                                            product_name = products.get(position).getProductName();
                                            image = products.get(position).getImage();
                                            user_id = products.get(position).getUserID();
                                            user_name = products.get(position).getUserName();
                                            description = products.get(position).getDescription();
                                            price = products.get(position).getPrice();
                                            year = products.get(position).getYears();

                                            Toast.makeText(getContext(), product_name, Toast.LENGTH_SHORT).show();
                                            EditData(product_id, product_name, image, user_id, description, price, year);
                                            return true;
                                        case R.id.delete_item:
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                            builder.setTitle("Delete");
                                            builder.setMessage("Confirm Delete operation?");
                                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    product_id = products.get(position).getProductID();
                                                    product_name = products.get(position).getProductName();
                                                    Toast.makeText(getContext(), product_name, Toast.LENGTH_SHORT).show();
                                                    DeleteData(product_id, product_name);
                                                }
                                            });
                                            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                            AlertDialog alert = builder.create();
                                            alert.show();
                                            return true;
                                    }
                                    return true;
                                }
                            });
                            dropDownMenu.show();
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
