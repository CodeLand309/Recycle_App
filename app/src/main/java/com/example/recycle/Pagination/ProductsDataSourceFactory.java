package com.example.recycle.Pagination;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.example.recycle.Pagination.ProductDataSource;

import io.reactivex.rxjava3.core.Observer;

public class ProductsDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<ProductDataSource> items = new MutableLiveData<>();
    private ProductDataSource productDataSource;
    private String searchText;
    private String type;
    private Integer searchResultSize;
    private Integer id;
    private Observer observer;

//    private static String TYPE;
//    private static Integer ID;

//    public ProductsDataSourceFactory(Integer id){
//        ID = id;
//    }

    public ProductsDataSourceFactory(Integer id, String type, Observer observer){
        this.id = id;
        this.type = type;
        this.observer = observer;
    }

    @NonNull
    @Override
    public DataSource create() {
        productDataSource = new ProductDataSource(id, searchText, type, observer);
        items.postValue(productDataSource);
        return productDataSource;
    }

    public void setSearchText(String newSearchText){
        searchText = newSearchText;
    }

    public MutableLiveData<ProductDataSource> getProducts() {
        return items;
    }

    public Integer getSearchResultSize(){
        searchResultSize=productDataSource.getSearchResultSize();
        return searchResultSize;
    }

    public void refresh() {
        if(items.getValue()!=null)
            items.getValue().invalidate();
    }
}
