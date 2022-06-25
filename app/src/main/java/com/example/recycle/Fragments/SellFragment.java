package com.example.recycle.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.recycle.Activities.MainActivity;
import com.example.recycle.Adapters.SellAdapter;
import com.example.recycle.Model.ProductsItem;
import com.example.recycle.Pagination.ProductDataSource;
import com.example.recycle.R;
import com.example.recycle.Network.RestApiInterface;
import com.example.recycle.Network.RestClient;
import com.example.recycle.SubActivity.EditProduct;
import com.example.recycle.SubActivity.MyProductDetails;
import com.example.recycle.SubActivity.UploadProduct;
import com.example.recycle.ViewModels.ProductsViewModel;
import com.example.recycle.ViewModels.ProductsViewModelFactory;
import com.example.recycle.databinding.FragmentSellBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

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
    private PagedList<ProductsItem> products;
    private FloatingActionButton AddButton;
    private Button Options;
    private FragmentSellBinding fragmentSellBinding = null;
    private ProductsViewModel model;

    private String user_name, product_name, description, image, Result, date;
    private int page_number = 1, user_id, product_id, price, year, status;
    private int item_count = 6;

    private SharedPreferences sp;
    //Variables for Pagination
    private boolean isLoading = true;
    private int pastVisibleItems, visibleItemCount, totalItemCount, previous_total=0;
    private int view_threshold = 3;

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
        menu.removeItem(R.id.search);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        fragmentSellBinding = FragmentSellBinding.inflate(inflater, container, false);
        FragmentSellBinding binding = fragmentSellBinding;

        sp = getContext().getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        user_id = sp.getInt("User ID",0);

        //ProductDataSource.setType("Self");
        //model = new ViewModelProvider(this).get(ProductsViewModel.class);
        model = new ViewModelProvider(this, new ProductsViewModelFactory(user_id, "Self")).get(ProductsViewModel.class);


        progressBar = binding.progressBar;
        mRecyclerView = binding.recyclerView;
        AddButton = binding.addButton;
//        Log.d("TAG", "Reached Here1");
//        mAdapter = new SellAdapter(products, getContext());
//        mRecyclerView.setHasFixedSize(true);
//        mLayoutManager = new LinearLayoutManager(getContext());
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(mAdapter);
//        Log.d("TAG", "Reached Here2");
//        sp = getContext().getSharedPreferences("Credentials", Context.MODE_PRIVATE);
//        user_id = sp.getInt("User ID",0);
//        Log.d("TAG", String.valueOf(user_id));

//        restApiInterface = getRest();

//        progressBar.setVisibility(View.VISIBLE);
//        showProgress();
        observeStatus();

        if(!((MainActivity)getActivity()).isNetworkAvailable()){
            hideRecyclerView();
            ((MainActivity) getActivity()).showHideErrorMessages(0);
        }else{
            initRecyclerView();
            ((MainActivity) getActivity()).showProgress();
            observeProductsData();
        }

        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(((MainActivity)getActivity()).isNetworkAvailable()) {
//                    showRecyclerView();
//                    if(mAdapter == null || mRecyclerView.getLayoutManager() == null || mRecyclerView.getAdapter() == null) {
//                        initRecyclerView();
//                        observeProductsData();
//                    }
                    checkRecyclerViewInit();
                    refreshRecyclerView();
//                    model.refresh();
//                    products = model.getProducts().getValue();
//                    mAdapter.submitList(products);
                }else {
                    ((MainActivity) getActivity()).showHideErrorMessages(0);
                    hideRecyclerView();
                    binding.swipeRefresh.setRefreshing(false);
                    status = 0;
                }
//                binding.swipeRefresh.setRefreshing(false);
            }
        });

//        productsViewModel.getUserProducts(page_number, item_count, user_id).observe(this,
//                new Observer<ArrayList<ProductsItem>>() {
//                    @Override
//                    public void onChanged(ArrayList<ProductsItem> productsItems) {
//                        setUI(productsItems);
//                    }
//                });

//        productsViewModel.getUserProducts(page_number, item_count, user_id).observe(this,
//            new Observer<Resource<ArrayList<DataResponse>>>() {
//            @Override
//            public void onChanged(Resource<ArrayList<DataResponse>> response) {
//                if(response.isSuccess()){
//                    products = response.getResource().get(1).getItems();
//                    mAdapter = new SellAdapter(products, getContext());
//                    mRecyclerView.setAdapter(mAdapter);
//                    progressBar.setVisibility(View.GONE);
//                    mAdapter.setOnItemClickListener(new SellAdapter.OnItemClickListener() {
//                        @Override
//                        public void onItemCLick(int position) {
//                            if(((MainActivity)getActivity()).isNetworkAvailable()) {
//                                changeActivity(position, products);
//                            }else{
//                                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(0)).commit();
//                                ((MainActivity)getActivity()).ModifyFragment(0);
//                            }
//                        }
//
//                        @Override
//                        public void onOptionsCLick(int position, Button options) {
//                            dropDownMenu = new PopupMenu(getContext(), options);
//                            dropDownMenu.getMenuInflater().inflate(R.menu.options, dropDownMenu.getMenu());
//                            dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//
//                                @Override
//                                public boolean onMenuItemClick(MenuItem menuItem) {
//                                    switch(menuItem.getItemId()) {
//                                        case R.id.edit_item:
//                                            product_id = products.get(position).getProductID();
//                                            product_name = products.get(position).getProductName();
//                                            image = products.get(position).getImage();
//                                            user_id = products.get(position).getUserID();
//                                            user_name = products.get(position).getUserName();
//                                            description = products.get(position).getDescription();
//                                            price = products.get(position).getPrice();
//                                            year = products.get(position).getYears();
//                                            if(((MainActivity)getActivity()).isNetworkAvailable()) {
//                                                EditData(product_id, product_name, image, user_id, description, price, year);
//                                            }else{
//    //                                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(0)).commit();
//                                                ((MainActivity)getActivity()).ModifyFragment(0);
//                                            }
//                                            return true;
//                                        case R.id.delete_item:
//                                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                                            builder.setTitle("Delete");
//                                            builder.setMessage("Confirm Delete operation?");
//                                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialogInterface, int i) {
//                                                    product_id = products.get(position).getProductID();
//                                                    product_name = products.get(position).getProductName();
//                                                    if (((MainActivity) getActivity()).isNetworkAvailable()) {
//                                                        DeleteData(product_id, product_name);
//                                                    } else {
//                                                        // getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(0)).commit();
//                                                        ((MainActivity) getActivity()).ModifyFragment(0);
//                                                    }
//                                                }
//                                            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    dialog.dismiss();
//                                                }
//                                            });
//                                            AlertDialog alert = builder.create();
//                                            alert.show();
//                                            return true;
//                                    }
//                                    return true;
//                                }
//                            });
//                            dropDownMenu.show();
//                        }
//                    });
//                }else{
//                    Toast.makeText(getContext(), "Cannot Access Server", Toast.LENGTH_SHORT).show();
//                    //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(1)).commit();
//                    ((MainActivity)getActivity()).ModifyFragment(0);
//                }
//            }
//        });

//        Call<ArrayList<DataResponse>> call = restApiInterface.getUserProducts(page_number, item_count, user_id);
//        call.enqueue(new Callback<ArrayList<DataResponse>>() {
//            @Override
//            public void onResponse(@NonNull Call<ArrayList<DataResponse>> call, @NonNull Response<ArrayList<DataResponse>> response) {
//                products = response.body().get(1).getItems();
//                mAdapter = new SellAdapter(products, getContext());
//                Log.d("TAG", "Reached Here3");
//                mRecyclerView.setAdapter(mAdapter);
//                Log.d("Tag", String.valueOf(response.body()));
//                progressBar.setVisibility(View.GONE);
//                mAdapter.setOnItemClickListener(new SellAdapter.OnItemClickListener() {
//                    @Override
//                    public void onItemCLick(int position) {
//                        if(isNetworkAvailable()) {
//                            changeActivity(position, products);
//                        }else{
//                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(0)).commit();
//                        }
//                    }
//
//                    @Override
//                    public void onOptionsCLick(int position, Button options) {
//                        dropDownMenu = new PopupMenu(getContext(), options);
//                        dropDownMenu.getMenuInflater().inflate(R.menu.options, dropDownMenu.getMenu());
//                        dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//
//                            @Override
//                            public boolean onMenuItemClick(MenuItem menuItem) {
//                                switch(menuItem.getItemId()) {
//                                    case R.id.edit_item:
//                                        product_id = products.get(position).getProductID();
//                                        product_name = products.get(position).getProductName();
//                                        image = products.get(position).getImage();
//                                        user_id = products.get(position).getUserID();
//                                        user_name = products.get(position).getUserName();
//                                        description = products.get(position).getDescription();
//                                        price = products.get(position).getPrice();
//                                        year = products.get(position).getYears();
//                                        if(isNetworkAvailable()) {
//                                            EditData(product_id, product_name, image, user_id, description, price, year);
//                                        }else{
//                                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(0)).commit();
//                                        }
//                                        return true;
//                                    case R.id.delete_item:
//                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                                        builder.setTitle("Delete");
//                                        builder.setMessage("Confirm Delete operation?");
//                                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialogInterface, int i) {
//                                                product_id = products.get(position).getProductID();
//                                                product_name = products.get(position).getProductName();
//                                                if(isNetworkAvailable()) {
//                                                    DeleteData(product_id, product_name);
//                                                }else{
//                                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(0)).commit();
//                                                }
//                                            }
//                                        });
//                                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                dialog.dismiss();
//                                            }
//                                        });
//                                        AlertDialog alert = builder.create();
//                                        alert.show();
//                                        return true;
//                                }
//                                return true;
//                            }
//                        });
//                        dropDownMenu.show();
//                    }
//                });
//
//            }
//            @Override
//            public void onFailure(Call<ArrayList<DataResponse>> call, Throwable t) {
//                Toast.makeText(getContext(), "Cannot Access Server", Toast.LENGTH_SHORT).show();
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(1)).commit();
//            }
//        });

        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), UploadProduct.class);
                startActivity(i);
            }
        });

//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                visibleItemCount = mLayoutManager.getChildCount();
//                totalItemCount = mLayoutManager.getItemCount();
//                pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
//
//                if(dy>0){
//                    if(isLoading){
//                        if(totalItemCount>previous_total){
//                            isLoading = false;
//                            previous_total = totalItemCount;
//                        }
//                    }
//                    if(!isLoading && (totalItemCount-visibleItemCount) <= (pastVisibleItems + view_threshold)){
//                        page_number++;
//                        //performPagination();
//                        isLoading = true;
//                    }
//                }
//            }
//        });

        return binding.getRoot();
    }

    public void showProgress(){
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress(){
        progressBar.setVisibility(View.GONE);
    }

    public void hideRecyclerView(){
        mRecyclerView.setVisibility(View.INVISIBLE);
    }
    public void showRecyclerView(){
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void checkRecyclerViewInit(){
        if (mAdapter == null || mRecyclerView.getLayoutManager() == null || mRecyclerView.getAdapter() == null) {
            initRecyclerView();
            observeProductsData();
        }
    }

    private void refreshRecyclerView() {
        //model.setSearchText(null);
        model.refresh();
        products = model.getProducts().getValue();
        mAdapter.submitList(products);
    }

    private void initRecyclerView() {
        mAdapter = new SellAdapter(getContext());
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void observeStatus(){
        model.getStatus().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
//                Toast.makeText(getActivity(), integer+"", Toast.LENGTH_SHORT).show();
                if(status!=integer) {
                    if (integer != 2 && integer != -1) {
                        hideRecyclerView();
                        ((MainActivity) getActivity()).showHideErrorMessages(integer);
                        Toast.makeText(getActivity(), integer + "", Toast.LENGTH_SHORT).show();
                        ((MainActivity) getActivity()).hideProgress();
                    }
                    if(fragmentSellBinding.swipeRefresh.isRefreshing())
                        fragmentSellBinding.swipeRefresh.setRefreshing(false);
                    status = integer;
                }
            }
        });
    }

    private void observeProductsData(){
        model.getProducts().observe(getViewLifecycleOwner(), new Observer<PagedList<ProductsItem>>() {
            @Override
            public void onChanged(PagedList<ProductsItem> productsItems) {
                if(productsItems!=null && model.getStatus().getValue()==2 ) {
                    products = productsItems;
                    setUI(productsItems);
                    showRecyclerView();
                }
//                    hideProgress();
//                    setUI(productsItems);
//                }else{
//                    if(ProductDataSource.getStatus() == 0) {
//                        hideProgress();
//                        ((MainActivity) getActivity()).showHideErrorMessages(1);
//                    }else {
//                        hideProgress();
//                        ((MainActivity) getActivity()).showHideErrorMessages(2);
//                    }
//                }
            }
        });
    }

    private void setUI(PagedList<ProductsItem> products) {
//        if(products!=null) {
            mAdapter.submitList(products);
//            mRecyclerView.setAdapter(mAdapter);
//            hideProgress();
            ((MainActivity) getActivity()).hideProgress();
            if(((MainActivity) getActivity()).isAnyErrorMessageShowing())
                ((MainActivity) getActivity()).showHideErrorMessages(-1);
            mAdapter.setOnItemClickListener(new SellAdapter.OnItemClickListener() {
                @Override
                public void onItemCLick(int position) {
//                    if (((MainActivity) getActivity()).isNetworkAvailable()) {
                        changeActivity(position, products);
//                    } else {
//                        //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(0)).commit();
//                        ((MainActivity) getActivity()).showHideErrorMessages(0);
//                    }
                }

                @Override
                public void onOptionsCLick(int position, ImageButton options) {
                    dropDownMenu = new PopupMenu(getContext(), options);
                    dropDownMenu.getMenuInflater().inflate(R.menu.options, dropDownMenu.getMenu());
                    dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.edit_item:
                                    product_id = products.get(position).getProductID();
                                    product_name = products.get(position).getProductName();
                                    image = products.get(position).getImage();
                                    user_id = products.get(position).getUserID();
                                    user_name = products.get(position).getUserName();
                                    description = products.get(position).getDescription();
                                    price = products.get(position).getPrice();
                                    year = products.get(position).getYears();
                                    if (((MainActivity) getActivity()).isNetworkAvailable()) {
                                        EditData(product_id, product_name, image, user_id, description, price, year);
                                    } else {
                                        //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(0)).commit();
                                        ((MainActivity) getActivity()).showHideErrorMessages(0);
                                    }
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
                                            if (((MainActivity) getActivity()).isNetworkAvailable()) {
                                                DeleteData(product_id, product_name);
                                            } else {
                                                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(0)).commit();
                                                ((MainActivity) getActivity()).showHideErrorMessages(0);
                                            }
                                        }
                                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
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
//        }
//        }else{
//            Toast.makeText(getContext(), "Cannot Access Server", Toast.LENGTH_SHORT).show();
//            //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(1)).commit();
//            ((MainActivity)getActivity()).ModifyFragment(1);
//        }
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
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SellFragment()).commit();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Result = t.getMessage();
                Toast.makeText(getContext(), Result, Toast.LENGTH_SHORT).show();
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(1)).commit();
                ((MainActivity)getActivity()).showHideErrorMessages(0);
            }
        });
    }

//    public boolean isNetworkAvailable() {
//        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//    }

    private void changeActivity(int position, PagedList<ProductsItem> products) {
        image = products.get(position).getImage();
        product_name = products.get(position).getProductName();
        product_id = products.get(position).getProductID();
        user_id = products.get(position).getUserID();
//        user_name = products.get(position).getUserName();
        description = products.get(position).getDescription();
        price = products.get(position).getPrice();
        year = products.get(position).getYears();
        date = products.get(position).getDate();
        status = products.get(position).getStatus();

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
            jsonData.put("Status", status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent i = new Intent(getContext(), MyProductDetails.class);
        i.putExtra("Product", jsonData.toString());
        startActivity(i);
    }
//    private void performPagination(){
//        progressBar.setVisibility(View.VISIBLE);
//        Call<ArrayList<DataResponse>> call = restApiInterface.getUserProducts(page_number, item_count, user_id);
//        call.enqueue(new Callback<ArrayList<DataResponse>>() {
//            @Override
//            public void onResponse(@NonNull Call<ArrayList<DataResponse>> call, @NonNull Response<ArrayList<DataResponse>> response) {
//
//                if(response.body().get(0).getStatus().equals("ok")){
//                    products = response.body().get(1).getItems();
//                    mAdapter.addProduct(products);
//                    mAdapter.setOnItemClickListener(new SellAdapter.OnItemClickListener() {
//                        @Override
//                        public void onItemCLick(int position) {
//                            changeActivity(position, products);
//                        }
//
//                        @Override
//                        public void onOptionsCLick(int position, Button options) {
//                            Toast.makeText(getContext(), "Clicked on Options", Toast.LENGTH_SHORT).show();
//                            dropDownMenu = new PopupMenu(getContext(), options);
//                            dropDownMenu.getMenuInflater().inflate(R.menu.options, dropDownMenu.getMenu());
//                            dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//
//                                @Override
//                                public boolean onMenuItemClick(MenuItem menuItem) {
//                                    switch(menuItem.getItemId()) {
//                                        case R.id.edit_item:
//                                            product_id = products.get(position).getProductID();
//                                            product_name = products.get(position).getProductName();
//                                            image = products.get(position).getImage();
//                                            user_id = products.get(position).getUserID();
//                                            user_name = products.get(position).getUserName();
//                                            description = products.get(position).getDescription();
//                                            price = products.get(position).getPrice();
//                                            year = products.get(position).getYears();
//                                            if(isNetworkAvailable()) {
//                                                EditData(product_id, product_name, image, user_id, description, price, year);
//                                            }else{
//                                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(0)).commit();
//                                            }
//                                            return true;
//                                        case R.id.delete_item:
//                                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                                            builder.setTitle("Delete");
//                                            builder.setMessage("Confirm Delete operation?");
//                                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialogInterface, int i) {
//                                                    product_id = products.get(position).getProductID();
//                                                    product_name = products.get(position).getProductName();
//                                                    if(isNetworkAvailable()) {
//                                                        DeleteData(product_id, product_name);
//                                                    }else{
//                                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(0)).commit();
//                                                    }
//                                                }
//                                            });
//                                            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    dialog.dismiss();
//                                                }
//                                            });
//                                            AlertDialog alert = builder.create();
//                                            alert.show();
//                                            return true;
//                                    }
//                                    return true;
//                                }
//                            });
//                            dropDownMenu.show();
//                        }
//                    });
//                }
//                else{
//                    //Toast.makeText(ReadActivity.this, "No more Data", Toast.LENGTH_SHORT).show();
//                }
//                progressBar.setVisibility(View.GONE);
//            }
//            @Override
//            public void onFailure(Call<ArrayList<DataResponse>> call, Throwable t) {
//                Toast.makeText(getContext(), "Cannot Access Server", Toast.LENGTH_SHORT).show();
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(1)).commit();
//            }
//        });
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentSellBinding = null;
    }
}
