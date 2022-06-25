package com.example.recycle.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.recycle.Activities.MainActivity;
import com.example.recycle.Adapters.ProductsAdapter;
import com.example.recycle.Model.ProductsItem;
import com.example.recycle.Pagination.ProductDataSource;
import com.example.recycle.Network.RestApiInterface;
import com.example.recycle.R;
import com.example.recycle.SubActivity.ProductDetails;
import com.example.recycle.ViewModels.ProductsViewModel;
import com.example.recycle.ViewModels.ProductsViewModelFactory;
import com.example.recycle.databinding.FragmentHomeBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {

    private RestApiInterface restApiInterface;
    private RecyclerView mRecyclerView;
    private ProductsAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    private ProgressBar progressBar;
    private PagedList<ProductsItem> products;
    private ProductsViewModel model;
    private FragmentHomeBinding fragmentHomeBinding = null;

    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    private String user_name, product_name, description, image, date;
    private int page_number = 1, user_id, product_id, price, year;
    private final int item_count = 10;
    private Boolean searchPerformed = false;
    private int status=-1;
    private String searchText = "";

    private SharedPreferences sp;
    //Variables for Pagination
    private boolean isLoading = true;
    private int pastVisibleItems, visibleItemCount, totalItemCount, previous_total=0;
    private final int view_threshold = 10;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem search = menu.findItem(R.id.search);
        searchView = (SearchView) search.getActionView();
        searchView.setQueryHint("Search Product");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(((MainActivity) getActivity()).isNetworkAvailable()){
                    ((MainActivity) getActivity()).hideKeyboard();
                    if(!searchText.equals(query)) {
                        checkRecyclerViewInit();
                        searchText=query;
                        if (model != null)
                            model.setSearchText(query);
                        ((MainActivity) getActivity()).showProgress();
                        searchPerformed = true;
                    }else {
                        ((MainActivity) getActivity()).showSnackbar(fragmentHomeBinding.fragmentHome, "No Change in Search Results");
                        searchPerformed = false;
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                checkRecyclerViewInit();
                if(model!=null)
                    model.setSearchText(query);
                ((MainActivity) getActivity()).showProgress();
                return true;
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {
                    if (((MainActivity) getActivity()).isNetworkAvailable()){
                        search.collapseActionView();
                        if(model!=null)
                            model.setSearchText(null);
                        if(status!=1 && status!=0 && status!=3) {
                            ((MainActivity) getActivity()).showHideErrorMessages(-1);
                            status = -1;
                        }
                        checkRecyclerViewInit();
                        ((MainActivity) getActivity()).hideProgress();
                    }
                }

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        FragmentHomeBinding binding = fragmentHomeBinding;

        sp = getContext().getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        user_id = sp.getInt("User ID",0);

        //model = new ViewModelProvider(this, new ProductsViewModelFactory(user_id)).get(ProductsViewModel.class);
        //ProductDataSource.setType("Other");
        model = new ViewModelProvider(this, new ProductsViewModelFactory(user_id, "Other")).get(ProductsViewModel.class);

//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Current Fragment", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("Name", "HomeFragment");
//        editor.commit();

//        if(!((MainActivity)getActivity()).isNetworkAvailable()){
//
//        }

        progressBar = binding.progressBar;
        mRecyclerView = binding.recyclerView;
//        mAdapter = new ProductsAdapter(products, getContext());
//        mRecyclerView.setHasFixedSize(true);
//        mLayoutManager = new GridLayoutManager(getContext(), 2);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(mAdapter);
//        sp = getContext().getSharedPreferences("Credentials", Context.MODE_PRIVATE);
//        user_id = sp.getInt("User ID",0);
        //fetching id before connecting to view model

        //restApiInterface = RestClient.getRetrofit().create(RestApiInterface.class);

//        progressBar.setVisibility(View.VISIBLE);
        observeStatus();

        //Call<ArrayList<DataResponse>> call;
        if(!((MainActivity)getActivity()).isNetworkAvailable()){
            hideRecyclerView();
            ((MainActivity) getActivity()).showHideErrorMessages(0);
        }else {

//            if (sp.contains("User ID") && sp.getInt("Log in Status", 0) == 2) {
                //call = restApiInterface.getProductData(page_number, item_count, user_id);
                //response = Repository.getProductData(page_number, item_count, user_id);
                initRecyclerView();
                ((MainActivity) getActivity()).showProgress();
                observeProductsData();
//            } else {
//                //call = restApiInterface.getProductDataNoID(page_number, item_count);
//                //response = Repository.getProductDataNoID(page_number, item_count);
//                model.getProductsNoID(page_number, item_count).observe(getViewLifecycleOwner(), new Observer<PagedList<ProductsItem>>() {
//                    @Override
//                    public void onChanged(PagedList<ProductsItem> productsItems) {
//                        setUI(productsItems);
//                    }
//                });
//            }
        }

//        call.enqueue(new Callback<ArrayList<DataResponse>>() {
//            @Override
//            public void onResponse(@NonNull Call<ArrayList<DataResponse>> call, @NonNull Response<ArrayList<DataResponse>> response) {
//                products = response.body().get(1).getItems();
//                mAdapter = new ProductsAdapter(products, getContext());
//                mRecyclerView.setAdapter(mAdapter);
//                progressBar.setVisibility(View.GONE);
//                mAdapter.setOnItemClickListener(new ProductsAdapter.OnItemClickListener() {
//                    @Override
//                    public void onItemCLick(int position) {
//                        changeActivity(position, products);
//                    }
//                });
//            }
//            @Override
//            public void onFailure(Call<ArrayList<DataResponse>> call, Throwable t) {
//                Toast.makeText(getContext(), "Cannot Access Server", Toast.LENGTH_SHORT).show();
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(1)).commit();
//            }
//        });

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
                }else{
                    ((MainActivity) getActivity()).showHideErrorMessages(0);
                    hideRecyclerView();
                    binding.swipeRefresh.setRefreshing(false);
                    status=0;
                }
                //binding.swipeRefresh.setRefreshing(false);
            }
        });

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
        searchText="";
        products = model.getProducts().getValue();
        mAdapter.submitList(products);
    }

    private void initRecyclerView() {
        mAdapter = new ProductsAdapter(getContext());
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void observeStatus(){
        model.getStatus().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
//                Toast.makeText(getActivity(), integer+"", Toast.LENGTH_SHORT).show();
                if(status!=integer) {
                    if (integer != 2 && integer != 4 && integer != -1) {
                        hideRecyclerView();
                        ((MainActivity) getActivity()).showHideErrorMessages(integer);
                        Toast.makeText(getActivity(), integer + "", Toast.LENGTH_SHORT).show();
                        ((MainActivity) getActivity()).hideProgress();
                    }
                    if(fragmentHomeBinding.swipeRefresh.isRefreshing())
                        fragmentHomeBinding.swipeRefresh.setRefreshing(false);
                    status = integer;
                }
            }
        });
    }

    private void observeProductsData() {
        model.getProducts().observe(getViewLifecycleOwner(), new Observer<PagedList<ProductsItem>>() {
            @Override
            public void onChanged(PagedList<ProductsItem> productsItems) {
                if(model.getStatus().getValue()==2 || model.getStatus().getValue()==4) {
                    products = productsItems;
                    setUI(productsItems);
                    if(model.getStatus().getValue()==4 && searchPerformed) {
                        ((MainActivity) getActivity()).showSnackbar(getView(), "Found " + model.getSearchResultSize() + " search results");
                        searchPerformed=false;
                    }
                    showRecyclerView();
                }
//                else {
//                    hideProgress();
//                    ((MainActivity) getActivity()).showHideErrorMessages(ProductDataSource.getStatus());
//                }
            }
        });
    }

//    @Override
//    public void onPrepareOptionsMenu(@NonNull Menu menu) {
//        super.onPrepareOptionsMenu(menu);
//        MenuItem searchItem = menu.findItem(R.id.search);
//        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
//        if(searchItem!=null){
//            searchView = (SearchView) searchItem.getActionView();
//            if(searchView!=null){
//                searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
//                queryTextListener = new SearchView.OnQueryTextListener() {
//                    @Override
//                    public boolean onQueryTextSubmit(String query) {
//                        performSearch(query);
//                        return true;
//                    }
//
//                    @Override
//                    public boolean onQueryTextChange(String newText) {
//                        return false;
//                    }
//                };
//            }
//        }
//    }

//    private void performSearch(String query) {
//        model.searchProductName(user_id, query).observe(this, new Observer<ArrayList<ProductsItem>>() {
//            @Override
//            public void onChanged(ArrayList<ProductsItem> productsItems) {
//                setUI(productsItems);
//            }
//        });

//        model.searchProductName(user_id, query).observe(this, new Observer<Resource<ArrayList<DataResponse>>>() {
//            @Override
//            public void onChanged(Resource<ArrayList<DataResponse>> response) {
//                if(response.isSuccess()){
//                    if(response.getResource().get(0).getStatus().equals("found")) {
//                        products = response.getResource().get(0).getItems();
//                        mAdapter = new ProductsAdapter(products, getContext());
//                        mRecyclerView.setAdapter(mAdapter);
//                        progressBar.setVisibility(View.GONE);
//                        mAdapter.setOnItemClickListener(new ProductsAdapter.OnItemClickListener() {
//                            @Override
//                            public void onItemCLick(int position) {
//                                changeActivity(position, products);
//                            }
//                        });
//                    }else{
//                        //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotFoundFragment()).commit();
//                        ((MainActivity)getActivity()).ModifyFragment(2);
//                    }
//                }else{
//                    Toast.makeText(getContext(), "Cannot Access Server", Toast.LENGTH_SHORT).show();
//                    //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(1)).commit();
//                    ((MainActivity)getActivity()).ModifyFragment(0);
//                }
//            }
//        });

    private void changeActivity(int position, PagedList<ProductsItem> products) {
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

//    private void performPagination(){
//        sp = getContext().getSharedPreferences("Credentials", Context.MODE_PRIVATE);
//        progressBar.setVisibility(View.VISIBLE);
//        Call<ArrayList<DataResponse>> call;
//        if(sp.contains("User ID") && sp.getInt("Log in Status",0) == 2) {
//            call = restApiInterface.getProductData(page_number, item_count, user_id);
//        }else{
//            call = restApiInterface.getProductDataNoID(page_number, item_count);
//        }
//        call.enqueue(new Callback<ArrayList<DataResponse>>() {
//            @Override
//            public void onResponse(@NonNull Call<ArrayList<DataResponse>> call, @NonNull Response<ArrayList<DataResponse>> response) {
//                if(response.body().get(0).getStatus().equals("ok")){
//                    products = response.body().get(1).getItems();
//                    mAdapter.addProduct(products);
//                    mAdapter.setOnItemClickListener(new ProductsAdapter.OnItemClickListener() {
//                        @Override
//                        public void onItemCLick(int position) {
//                            changeActivity(position, products);
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

    private void setUI(PagedList<ProductsItem> products){
        mAdapter.submitList(products);
        //hideProgress();
        ((MainActivity) getActivity()).hideProgress();
        if(((MainActivity) getActivity()).isAnyErrorMessageShowing())
            ((MainActivity) getActivity()).showHideErrorMessages(-1);
        mAdapter.setOnItemClickListener(new ProductsAdapter.OnItemClickListener() {
            @Override
            public void onItemCLick(int position) {
                changeActivity(position, products);
            }
        });
//        else{
//            if(ProductsViewModel.getStatus()==0) {
//                Toast.makeText(getContext(), "Cannot Access Server", Toast.LENGTH_SHORT).show();
//                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(1)).commit();
//                ((MainActivity) getActivity()).ModifyFragment(1);
//            }else{
//                ((MainActivity) getActivity()).ModifyFragment(2);
//            }
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentHomeBinding = null;
        status =-1;
    }
}
