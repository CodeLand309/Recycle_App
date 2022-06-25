package com.example.recycle.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.recycle.Model.ProductsItem;
import com.example.recycle.Pagination.ProductDataSource;
import com.example.recycle.Pagination.ProductsDataSourceFactory;

import java.util.ArrayList;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class ProductsViewModel extends ViewModel {

    private LiveData<PagedList<ProductsItem>> products;
    private LiveData<PagedList<ProductsItem>> productsNoID;
    private LiveData<PagedList<ProductsItem>> userProducts;
    private LiveData<ProductDataSource> liveDataSource;
    private ProductsDataSourceFactory productsDataSourceFactory;
//    private MutableLiveData<PagedList<ProductsItem>> searchProducts;
    private MutableLiveData<Integer> status;

    private Disposable disposable;
    private Observer<Integer> observer = new io.reactivex.rxjava3.core.Observer<Integer>() {
        @Override
        public void onSubscribe(@NonNull Disposable d) {
            disposable = d;
        }

        @Override
        public void onNext(@NonNull Integer integer) {
            status.postValue(integer);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            status.postValue(1);
        }

        @Override
        public void onComplete() {
            disposable.dispose();
        }
    };

    public ProductsViewModel(Integer id, String type){

        if(status==null){
            status = new MutableLiveData<>(-1);
        }

        productsDataSourceFactory = new ProductsDataSourceFactory(id, type, observer);
        liveDataSource = productsDataSourceFactory.getProducts();

        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setPageSize(ProductDataSource.PAGE_SIZE)
                .build();

        products = (new LivePagedListBuilder<Integer, ProductsItem>(productsDataSourceFactory, config)).build();
    }

    public void refresh(){
        productsDataSourceFactory.refresh();
    }

    public void setSearchText(String text) {
        productsDataSourceFactory.setSearchText(text);
        productsDataSourceFactory.refresh();
    }

    public LiveData<Integer> getStatus() {
        return status;
    }

    public Integer getSearchResultSize(){
        return productsDataSourceFactory.getSearchResultSize();
    }

    public LiveData<PagedList<ProductsItem>> getProducts(){
//        Repository.getProductData(page_number, item_count, id);
//        if(Objects.requireNonNull(response.getValue()).isSuccess()){
//            products.setValue(Objects.requireNonNull(response.getValue().getResource()).get(1).getItems());
//            return products;
//        }
//        else{
//            status = 0;
//            return null;
//        }
        return products;
    }

    public LiveData<PagedList<ProductsItem>> getProductsNoID(int page_number, int items){
//        LiveData<Resource<ArrayList<DataResponse>>> response = Repository.getProductDataNoID(page_number, items);
//        if(Objects.requireNonNull(response.getValue()).isSuccess()){
//            productsNoID.setValue(Objects.requireNonNull(response.getValue().getResource()).get(1).getItems());
//            return productsNoID;
//        }
//        else{
//            status = 0;
//            return null;
//        }
        return null;
    }

    public LiveData<ArrayList<ProductsItem>> getUserProducts(int page_number, int items, int id){
//        LiveData<Resource<ArrayList<DataResponse>>> response = Repository.getUserProducts(page_number, items, id);
//        if(Objects.requireNonNull(response.getValue()).isSuccess()){
//            userProducts.setValue(response.getValue().getResource().get(1).getItems());
//            return userProducts;
//        }
//        else{
//            status = 0;
//            return null;
//        }
        return null;
        
    }

//    public LiveData<ArrayList<ProductsItem>> searchProductName(int id, String name){
//        LiveData<Resource<ArrayList<DataResponse>>> response = Repository.searchProductName(id, name);
//        if(Objects.requireNonNull(response.getValue()).isSuccess()){
//            if(Objects.requireNonNull(response.getValue().getResource()).get(0).getStatus().equals("found")){
//                searchProducts.setValue(response.getValue().getResource().get(0).getItems());
//                return searchProducts;
//            }
//            else{
//                status = 1;
//                return null;
//            }
//        }
//        else{
//            status = 0;
//            return null;
//        }
//    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }
}
