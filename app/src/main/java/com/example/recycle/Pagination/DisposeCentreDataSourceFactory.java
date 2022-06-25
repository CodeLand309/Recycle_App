package com.example.recycle.Pagination;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.example.recycle.Pagination.DisposeCentreDataSource;

import io.reactivex.rxjava3.core.Observer;

public class DisposeCentreDataSourceFactory extends DataSource.Factory{

    private MutableLiveData<DisposeCentreDataSource> centres = new MutableLiveData<>();
    private LiveData<Integer> status;
    private DisposeCentreDataSource disposeCentreDataSource;
    private String searchText;
    private Integer searchResultSize=0;
    private Observer observer;

    public DisposeCentreDataSourceFactory(Observer<Integer> observer){
        this.observer = observer;
    }

    @NonNull
    @Override
    public DataSource create() {
        //return null;
        disposeCentreDataSource = new DisposeCentreDataSource(searchText, observer);
        centres.postValue(disposeCentreDataSource);
        return disposeCentreDataSource;
    }

    public MutableLiveData<DisposeCentreDataSource> getCentres() {
        return centres;
    }

    public Integer getSearchResultSize(){
        searchResultSize=disposeCentreDataSource.getSearchResultSize();
        return searchResultSize;
    }

    public void refresh() {
        if(centres.getValue()!=null)
            centres.getValue().invalidate();
    }

    public void setSearchText(String newSearchText){
        searchText = newSearchText;
    }

//    public LiveData<Integer> getStatus(){
//        if(disposeCentreDataSource!=null) {
//            status = disposeCentreDataSource.getStatus();
//            return status;
//        }
//        MutableLiveData<Integer> temp = new MutableLiveData<>();
//        temp.postValue(-1);
//        return temp;
//    }

//    public Observer<Integer> getStatus(){
//        return disposeCentreDataSource.getStatus();
//    }
}
