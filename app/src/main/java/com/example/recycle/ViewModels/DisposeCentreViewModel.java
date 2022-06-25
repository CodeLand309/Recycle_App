package com.example.recycle.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;


import com.example.recycle.Model.DisposeCentre;
import com.example.recycle.Pagination.DisposeCentreDataSource;
import com.example.recycle.Pagination.DisposeCentreDataSourceFactory;

import java.util.ArrayList;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class DisposeCentreViewModel extends ViewModel {

    private LiveData<PagedList<DisposeCentre>> centres;
    private MutableLiveData<ArrayList<DisposeCentre>> searchResult;
    private LiveData<DisposeCentreDataSource> liveDataSource;
    private DisposeCentreDataSourceFactory disposeCentreDataSourceFactory;
    private int i = 1;
    private MutableLiveData<Integer> status1;

    private Disposable disposable;
    private Observer<Integer> observer = new io.reactivex.rxjava3.core.Observer<Integer>() {
        @Override
        public void onSubscribe(@NonNull Disposable d) {
            disposable = d;
        }

        @Override
        public void onNext(@NonNull Integer integer) {
            status1.postValue(integer);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            status1.postValue(1);
        }

        @Override
        public void onComplete() {
            disposable.dispose();
        }
    };

//    private Subscriber<Integer> subscriber = new FlowableSubscriber<Integer>() {
//        @Override
//        public void onSubscribe(@NonNull Subscription s) {
//
//        }
//
//        @Override
//        public void onNext(Integer integer) {
//            status1.postValue(integer);
//        }
//
//        @Override
//        public void onError(Throwable t) {
//
//        }
//
//        @Override
//        public void onComplete() {
//
//        }
//    };

//    private static Repository repository;

    public DisposeCentreViewModel(){
//        if(repository==null){
//            repository = Repository.getRepository();
//        }
//        centres = new MutableLiveData<>();
        if(status1==null){
            status1=new MutableLiveData<>(-1);
        }

        disposeCentreDataSourceFactory = new DisposeCentreDataSourceFactory(observer);
        liveDataSource = disposeCentreDataSourceFactory.getCentres();

//        observer = new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer integer) {
//                status1.postValue(integer);
//            }
//        };
//        disposeCentreDataSourceFactory.getStatus().observeForever(observer);


        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setPageSize(DisposeCentreDataSource.PAGE_SIZE)
                .build();

        centres = (new LivePagedListBuilder<Integer, DisposeCentre>(disposeCentreDataSourceFactory, config)).build();

    }

//    public LiveData<Integer> getStatus(){
////        new Thread(new Runnable() {
////            @Override
////            public void run() {
////                int count=0;
////                for (int i = 0; i < 1000; i++) {
////                    count++;
////                    try {
////                        Thread.sleep(1000);
////                    } catch (InterruptedException e) {
////                        e.printStackTrace();
////                    }
////                    if(count%10==0){
////                        status1.postValue(3);
////                    }
////                    else if(count%5==0){
////                        status1.postValue(1);
////                    }
////                }
////            }
////        }).start();
//        return status1;
//    }

//    public static void setStatus(){
//        status.
//    }

//    public LiveData<ArrayList<DisposeCentre>> getDisposeCentreLiveData(){
//        return centres;
//    }

    public Integer getSearchResultSize(){
        return disposeCentreDataSourceFactory.getSearchResultSize();
    }

    public LiveData<PagedList<DisposeCentre>> getDisposeCentres(){
//        repository.getCentreData(page_number, item_count).enqueue(new Callback<ArrayList<CentreListResponse>>() {
//            @Override
//            public void onResponse(@NonNull Call<ArrayList<CentreListResponse>> call, @NonNull Response<ArrayList<CentreListResponse>> response) {
//                centres.setValue(response.body().get(1).getCentres());
//                status = 2;
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<CentreListResponse>> call, Throwable t) {
//                status = 0;
//                centres.setValue(null);
//            }
//        });
//
//        repository.getCentreData(page_number, item_count).observeForever(new Observer<Resource<ArrayList<CentreListResponse>>>() {
//            @Override
//            public void onChanged(Resource<ArrayList<CentreListResponse>> response) {
//                if(response.isSuccess()){
//                    centres.setValue(Objects.requireNonNull(response.getResource()).get(1).getCentres());
//                    status = 2;
//                }else{
//                    centres.setValue(null);
//                    status = 0;
//                }
//            }
//        });
        return centres;
    }

    public void refresh() {
//        centres.getValue().getDataSource().invalidate();
        disposeCentreDataSourceFactory.refresh();
    }

    public void setSearchText(String text){
        disposeCentreDataSourceFactory.setSearchText(text);
        //centres.getValue().getDataSource().invalidate();
        disposeCentreDataSourceFactory.refresh();
    }

    public LiveData<Integer> getStatus() {
//        observer = new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer integer) {
//                status1.postValue(integer);
//            }
//        }
        return status1;

    }

    //    public LiveData<ArrayList<DisposeCentre>> searchDisposeCentre(String word){
//        repository.searchCentreName(search_word).observeForever(new Observer<Resource<ArrayList<CentreListResponse>>>() {
//            @Override
//            public void onChanged(Resource<ArrayList<CentreListResponse>> response) {
//                if(response.isSuccess()){
//                    if(Objects.requireNonNull(response.getResource()).get(0).getStatus().equals("found")){
//                        centres.setValue(response.getResource().get(1).getCentres());
//                        status = 2;
//                    }else{
//                        status = 1;
//                    }
//                }else{
//                    status = 0;
//                }
//            }
//        });
//        if(status==2){
//            return centres;
//        }else{
//            return null;
//        }

//        Repository.getRepository().searchCentreName(word).enqueue(new Callback<ArrayList<CentreListResponse>>() {
//            @Override
//            public void onResponse(Call<ArrayList<CentreListResponse>> call, Response<ArrayList<CentreListResponse>> response) {
//                if(response.body().get(0).getStatus().equals("found")){
//                    searchResult.postValue(response.body().get(0).getCentres());
//                    searchStatus=2;
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<CentreListResponse>> call, Throwable t) {
//                searchStatus=1;
//            }
//        });
//        return searchResult;
//    }


    @Override
    protected void onCleared() {
        super.onCleared();
//        disposeCentreDataSourceFactory.getStatus().removeObserver(observer);
        disposable.dispose();
    }
}
