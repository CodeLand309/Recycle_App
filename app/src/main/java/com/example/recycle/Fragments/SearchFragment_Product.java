package com.example.recycle.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.recycle.Adapters.ProductsAdapter;
import com.example.recycle.Model.ProductsItem;
import com.example.recycle.R;
import com.example.recycle.Network.RestApiInterface;
import com.example.recycle.Network.RestClient;
import com.example.recycle.SubActivity.ProductDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchFragment_Product extends Fragment {

    private RestApiInterface restApiInterface;
    private RecyclerView mRecyclerView;
    private ProductsAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    private ProgressBar progressBar;
    private ArrayList<ProductsItem> products;

    private String user_name, product_name, description, image, date;
    private int user_id, product_id, price, year;

    private SharedPreferences sp;

    public SearchFragment_Product() {
        // Required empty public constructor
    }

    public static SearchFragment_Product newInstance() {
        return new SearchFragment_Product();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_product, container, false);

        setRetainInstance(true);
        Bundle bundle = getArguments();
        String search_word = bundle.getString("Search Word");

        progressBar = view.findViewById(R.id.progressBar);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        //mAdapter = new ProductsAdapter(products, getContext());
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        sp = getContext().getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        user_id = sp.getInt("User ID",0);

        restApiInterface = RestClient.getRetrofit().create(RestApiInterface.class);

        progressBar.setVisibility(View.VISIBLE);
//        Call<ArrayList<DataResponse>> call = restApiInterface.searchProductName(user_id, search_word);
//        call.enqueue(new Callback<ArrayList<DataResponse>>() {
//            @Override
//            public void onResponse(@NonNull Call<ArrayList<DataResponse>> call, @NonNull Response<ArrayList<DataResponse>> response) {
//                if(response.body().get(0).getStatus().equals("found")) {
//                    products = response.body().get(1).getItems();
//                    mAdapter = new ProductsAdapter(products, getContext());
//                    mRecyclerView.setAdapter(mAdapter);
//                    progressBar.setVisibility(View.GONE);
//                    mAdapter.setOnItemClickListener(new ProductsAdapter.OnItemClickListener() {
//                        @Override
//                        public void onItemCLick(int position) {
//                            changeActivity(position, products);
//                        }
//                    });
//                }else{
//                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotFoundFragment()).commit();
//                }
//            }
//            @Override
//            public void onFailure(Call<ArrayList<DataResponse>> call, Throwable t) {
//                Toast.makeText(getContext(), "Cannot Access Server", Toast.LENGTH_SHORT).show();
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(1)).commit();
//            }
//        });
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
}