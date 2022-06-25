package com.example.recycle.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.recycle.Activities.MainActivity;
import com.example.recycle.Adapters.DisposeAdapter;
import com.example.recycle.Model.DisposeCentre;
import com.example.recycle.Network.RestApiInterface;
import com.example.recycle.R;
import com.example.recycle.SubActivity.DisposeCenterDetails;
import com.example.recycle.SubActivity.InfoActivity;
import com.example.recycle.ViewModels.DisposeCentreViewModel;
import com.example.recycle.databinding.FragmentDisposeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class DisposeFragment extends Fragment {

    private static final String TAG = "Dispose Fragment";
    private RestApiInterface restApiInterface;
    private RecyclerView mRecyclerView;
    private DisposeAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private FloatingActionButton Fab;
    private SearchView searchView;

    private ProgressBar progressBar;
//    private ArrayList<DisposeCentre> centres;
    private PagedList<DisposeCentre> centres;
    private int status=-1;

    private int page_number = 1;
    private int item_count = 15;
    private DisposeCentreViewModel model;
    private FragmentDisposeBinding fragmentDisposeBinding = null;
    private boolean searchPerformed = false;
    private String searchText="";

    //Variables for Pagination
    private boolean isLoading = true;
    private String image, centre_name, address, phone;
    private int centre_id;
    private int pastVisibleItems, visibleItemCount, totalItemCount, previous_total=0;
    private int view_threshold= 10;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem search = menu.findItem(R.id.search);
        searchView = (SearchView) search.getActionView();
        searchView.setQueryHint("Search Here");
//        if("".equals(searchView.getQuery().toString())) {
//            ((MainActivity) getActivity()).hideKeyboard();
//            Snackbar.make(getView(), "Type Something", Snackbar.LENGTH_SHORT).show();
//        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(((MainActivity) getActivity()).isNetworkAvailable()) {
//                    if(((MainActivity) getActivity()).isAnyErrorMessageShowing())
//                    if(status!=1 && status!=0 && status!=3)
//                        ((MainActivity)getActivity()).showHideErrorMessages(-1);
                    ((MainActivity) getActivity()).hideKeyboard();
                    if(!searchText.equals(query)) {
                        checkRecyclerViewInit();
                        searchText = query;
                        if(model!=null)
                            model.setSearchText(query);
                        ((MainActivity) getActivity()).showProgress();
                        searchPerformed = true;
                    }
                    else{
                        ((MainActivity) getActivity()).showSnackbar(fragmentDisposeBinding.fragmentDispose, "No Change in Search Results");
                        searchPerformed = false;
                    }
//                    DisposeCentreDataSource.setSearch(query);
                    //refreshRecyclerView();
                }
//                return true;
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if(((MainActivity) getActivity()).isNetworkAvailable()) {
//                    if(((MainActivity) getActivity()).isAnyErrorMessageShowing())
//                    if(status!=1 && status!=0 && status!=3)
//                        ((MainActivity)getActivity()).showHideErrorMessages(-1);
                    //((MainActivity) getActivity()).hideKeyboard();
//                    if(!searchText.equals(query)) {
                        checkRecyclerViewInit();
                        //searchText = query;
                        if(model!=null)
                            model.setSearchText(query);
                        ((MainActivity) getActivity()).showProgress();
                        //searchPerformed = true;
//                    }
//                    else{
//                        ((MainActivity) getActivity()).showSnackbar(getView(), "Same Search Result");
//                        searchPerformed = false;
//                    }
//                    DisposeCentreDataSource.setSearch(query);
                    //refreshRecyclerView();
                }
                return true;
//
//                return false;
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
//                        DisposeCentreDataSource.setSearch(null);

//                        refreshRecyclerView();
                    }
                }

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        fragmentDisposeBinding = FragmentDisposeBinding.inflate(inflater, container, false);
        FragmentDisposeBinding binding = fragmentDisposeBinding;

        Fab = binding.information;

        model = new ViewModelProvider(this).get(DisposeCentreViewModel.class);
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Current Fragment", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("Name", "DisposeFragment");
//        editor.commit();

        progressBar = binding.progressBar;
        mRecyclerView = binding.recyclerView;
//        mAdapter = new DisposeAdapter(getContext());
//        mLayoutManager = new LinearLayoutManager(getContext());
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setAdapter(mAdapter);
//        restApiInterface = RestClient.getRetrofit().create(RestApiInterface.class);


        observeStatus();

        if(!((MainActivity)getActivity()).isNetworkAvailable()){
            hideRecyclerView();
            ((MainActivity) getActivity()).showHideErrorMessages(0);
        }else {
            //model.getDisposeCentres(page_number, item_count);
            initRecyclerView();
            ((MainActivity) getActivity()).showProgress();
            observeDisposeCentreData();

            //Call<ArrayList<CentreListResponse>> call = restApiInterface.getCentreData(page_number, item_count);
//        call.enqueue(new Callback<ArrayList<CentreListResponse>>() {
//            @Override
//            public void onResponse(@NonNull Call<ArrayList<CentreListResponse>> call, @NonNull Response<ArrayList<CentreListResponse>> response) {
//                centres = response.body().get(1).getCentres();
//                mAdapter = new DisposeAdapter(centres, getContext());
//                mRecyclerView.setAdapter(mAdapter);
//                progressBar.setVisibility(View.GONE);
//                mAdapter.setOnItemClickListener(new DisposeAdapter.OnCentreClickListener() {
//                    @Override
//                    public void onCentreCLick(int position) {
//                        changeActivity(position, centres);
//                    }
//                });
//            }
//            @Override
//            public void onFailure(Call<ArrayList<CentreListResponse>> call, Throwable t) {
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
//                        //fetchData();
//                        isLoading = true;
//                    }
//                }
//            }
//        });
        }

        Fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), InfoActivity.class);
                startActivity(i);
            }
        });

        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(((MainActivity)getActivity()).isNetworkAvailable()) {
//                    if(((MainActivity) getActivity()).isAnyErrorMessageShowing()) {
//                        ((MainActivity) getActivity()).showHideErrorMessages(-1);
//                        status=-1;
//                    }
                    checkRecyclerViewInit();
//                    DisposeCentreDataSource.setSearch(null);
//                    model.setSearchText(null);
                    Toast.makeText(getActivity(), "RecyclerView Refreshed", Toast.LENGTH_SHORT).show();
                    refreshRecyclerView();
                }
                else{
                    ((MainActivity) getActivity()).showHideErrorMessages(0);
                    hideRecyclerView();
                    binding.swipeRefresh.setRefreshing(false);
                    status = 0;
                }
            }
        });

        return binding.getRoot();
    }

    private void checkRecyclerViewInit(){
        if (mAdapter == null || mRecyclerView.getLayoutManager() == null || mRecyclerView.getAdapter() == null) {
            initRecyclerView();
            observeDisposeCentreData();
        }
    }

    private void refreshRecyclerView() {
        //model.setSearchText(null);
        model.refresh();
        searchText="";
        centres = model.getDisposeCentres().getValue();
        mAdapter.submitList(centres);
    }

    public void hideRecyclerView(){
        mRecyclerView.setVisibility(View.INVISIBLE);
    }
    public void showRecyclerView(){
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void initRecyclerView() {
        mAdapter = new DisposeAdapter(getContext());
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
                    if (integer != 2 && integer != 4 && integer != -1) {
                        hideRecyclerView();
                        ((MainActivity) getActivity()).showHideErrorMessages(integer);
                        Toast.makeText(getActivity(), integer + "", Toast.LENGTH_SHORT).show();
                        ((MainActivity) getActivity()).hideProgress();
                    }
                    if(fragmentDisposeBinding.swipeRefresh.isRefreshing())
                        fragmentDisposeBinding.swipeRefresh.setRefreshing(false);
                    status = integer;
                }
            }
        });
    }

    private void observeDisposeCentreData(){
        model.getDisposeCentres().observe(getViewLifecycleOwner(), new Observer<PagedList<DisposeCentre>>() {
            @Override
            public void onChanged(PagedList<DisposeCentre> disposeCentres) {
                if (disposeCentres != null){
                /*DisposeCentreDataSource.getStatus()==2*/

                }
                if(model.getStatus().getValue()==2 || model.getStatus().getValue()==4){
                    centres = disposeCentres;
                    setUI(disposeCentres);
                    if(model.getStatus().getValue()==4 && searchPerformed) {
                        ((MainActivity) getActivity()).showSnackbar(getView(), "Found " + model.getSearchResultSize() + " search results");
                        searchPerformed=false;
                    }
                    showRecyclerView();
                }
//                else{
//                    //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(1)).commit();
////                    if(model.getStatus().getValue()!=-1) {
//                        hideRecyclerView();
//                        Toast.makeText(getActivity(), model.getStatus().getValue() + "", Toast.LENGTH_SHORT).show();
//                        ((MainActivity) getActivity()).showHideErrorMessages(model.getStatus().getValue());
////                    }else{
////                        refreshRecyclerView();
////                        showRecyclerView();
////                    }
//                }
            }
        });
    }

    private void setUI(PagedList<DisposeCentre> disposeCentres) {
        mAdapter.submitList(disposeCentres);
//        mAdapter.notifyDataSetChanged();
        ((MainActivity) getActivity()).hideProgress();
        if(((MainActivity) getActivity()).isAnyErrorMessageShowing())
            ((MainActivity) getActivity()).showHideErrorMessages(-1);
        mAdapter.setOnItemClickListener(new DisposeAdapter.OnCentreClickListener() {
            @Override
            public void onCentreCLick(int position) {
                changeActivity(position, disposeCentres);
            }
        });
    }

//    private void performSearch(String query){
//
//    }

    private void changeActivity(int position, PagedList<DisposeCentre> centres) {
        image = centres.get(position).getCentreImage();
        centre_name = centres.get(position).getCentreName();
        centre_id = centres.get(position).getCentreID();
        address = centres.get(position).getCentreAddress();
        phone = centres.get(position).getCentrePhone();
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

//    private void performPagination(){
//        progressBar.setVisibility(View.VISIBLE);
//        model.getDisposeCentres(page_number, item_count).

//        Call<ArrayList<CentreListResponse>> call = restApiInterface.getCentreData(page_number, item_count);
//        call.enqueue(new Callback<ArrayList<CentreListResponse>>() {
//            @Override
//            public void onResponse(@NonNull Call<ArrayList<CentreListResponse>> call, @NonNull Response<ArrayList<CentreListResponse>> response) {
//
//                if(response.body().get(0).getStatus().equals("ok")){
//                    centres = response.body().get(1).getCentres();
//                    mAdapter.addCentre(centres);
//                    mAdapter.setOnItemClickListener(new DisposeAdapter.OnCentreClickListener() {
//                        @Override
//                        public void onCentreCLick(int position) {
//                            changeActivity(position, centres);
//                        }
//                    });
//                }
//                else{
//                    //Toast.makeText(ReadActivity.this, "No more Data", Toast.LENGTH_SHORT).show();
//                }
//                progressBar.setVisibility(View.GONE);
//            }
//            @Override
//            public void onFailure(Call<ArrayList<CentreListResponse>> call, Throwable t) {
//                Toast.makeText(getContext(), "Cannot Access Server", Toast.LENGTH_SHORT).show();
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(1)).commit();
//            }
//        });
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentDisposeBinding = null;
        status =-1;
    }
}
