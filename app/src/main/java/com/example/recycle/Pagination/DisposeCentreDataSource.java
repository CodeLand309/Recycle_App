package com.example.recycle.Pagination;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.example.recycle.Activities.MainActivity;
import com.example.recycle.Model.CentreListResponse;
import com.example.recycle.Model.DisposeCentre;
import com.example.recycle.Network.Repository;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisposeCentreDataSource extends PageKeyedDataSource<Integer, DisposeCentre> {

    public static final int PAGE_SIZE = 9;
    private static final int FIRST_PAGE = 1;
//    private MutableLiveData<Integer> status=new MutableLiveData<>(-1);
    private Integer total=0;
    private Observer<Integer> observer;
//    private Subscriber<Integer> subscriber = new FlowableSubscriber<Integer>() {
//    @Override
//    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Subscription s) {
//
//    }
//
//    @Override
//    public void onNext(Integer integer) {
//
//    }
//
//    @Override
//    public void onError(Throwable t) {
//    }
//
//    @Override
//    public void onComplete() {
//    }
//};

    private Observable<Integer> status = Observable.just(-1);
//    private Integer status=-1;
    private String Search=null;

    public Observable<Integer> getStatus(){
        return status;
    }

    public Integer getSearchResultSize(){
        return total;
    }

//    public LiveData<Integer> getStatus(){
//        return status;
//    }
//
//    public void Integer getStatus(){
//        return status;
//    }

//    public static void setSearch(String search) {
//        Search=search;
//    }

    DisposeCentreDataSource(String searchText, Observer<Integer> observer){
        Search = searchText;
        this.observer = observer;
        status.subscribe(observer);
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams loadInitialParams, @NonNull LoadInitialCallback loadInitialCallback) {
        Repository.getRepository().getCentreData(FIRST_PAGE, PAGE_SIZE, Search).enqueue(new Callback<ArrayList<CentreListResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<CentreListResponse>> call, Response<ArrayList<CentreListResponse>> response) {
                if(call.isExecuted()){
                    if(response.body()!=null && response.body().get(0).getStatus().equals("ok")) {
                        if (Search != null) {
                            if (response.body().get(1).getSearch().equals("found")) {
//                                status = 2;
//                                status.postValue(4);
                                total = response.body().get(2).getSearchResultSize();
                                observer.onNext(4);
                                loadInitialCallback.onResult(response.body().get(3).getCentres(), null, 2);
                            } else {
//                                status = 3;
//                               status.postValue(3);
                                observer.onNext(3);
                            }
                        } else {
//                            status = 2;
//                            status.postValue(2);
                            observer.onNext(2);
                            loadInitialCallback.onResult(response.body().get(1).getCentres(), null, 2);
                        }
                    }
                } else {
//                    status = 1;
//                    status.postValue(1);
                    observer.onNext(1);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CentreListResponse>> call, Throwable t) {
                System.out.println(t.getMessage()+"\n"+t.getLocalizedMessage());
//                status = 4;
//                status.postValue(1);
                observer.onNext(1);
                System.out.println("Failed");
            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> loadParams, @NonNull LoadCallback<Integer, DisposeCentre> loadCallback) {
//        Repository.getRepository().getCentreData(loadParams.key, PAGE_SIZE, Search).enqueue(new Callback<ArrayList<CentreListResponse>>() {
//            @Override
//            public void onResponse(Call<ArrayList<CentreListResponse>> call, Response<ArrayList<CentreListResponse>> response) {
//                if(call.isExecuted()) {
//                    if (response.body() != null && loadParams.key > 1) {
//                        if (Search != null) {
//                            if (response.body().get(1).getSearch().equals("found") && !response.body().get(2).equals("[]")) {
////                                status = 2;
////                                status.postValue(4);
//                                observer.onNext(4);
//                                loadCallback.onResult(response.body().get(2).getCentres(), loadParams.key - 1);
//                            } else {
////                                status = 3;
////                            status.postValue(3);
//                                observer.onNext(3);
//                            }
//                        } else {
////                            status = 2;
////                        status.postValue(2);
//                            observer.onNext(2);
//                            loadCallback.onResult(response.body().get(1).getCentres(), loadParams.key - 1);
//                        }
//                    }
//                }else{
////                    status =1;
////                    status.postValue(1);
//                    observer.onNext(1);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<CentreListResponse>> call, Throwable t) {
////                status = 4;
////                status.postValue(1);
//                observer.onNext(1);
//            }
//        });
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> loadParams, @NonNull final LoadCallback<Integer, DisposeCentre> loadCallback) {
        Repository.getRepository().getCentreData(loadParams.key, PAGE_SIZE, Search).enqueue(new Callback<ArrayList<CentreListResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<CentreListResponse>> call, Response<ArrayList<CentreListResponse>> response) {
                if(call.isExecuted()) {
                    if(response.body()!=null && response.body().get(0).getStatus().equals("ok")){
                        if(Search!=null) {
                            if (response.body().get(1).getSearch().equals("found")) {
//                                status = 2;
//                                status.postValue(4);
                                observer.onNext(4);
                                loadCallback.onResult(response.body().get(3).getCentres(), loadParams.key + 1);
                            } else {
//                                status = 3;
//                                status.postValue(3);
                                observer.onNext(3);
                            }
                        }else {
//                            status = 2;
//                            status.postValue(2);
                            observer.onNext(2);
                            loadCallback.onResult(response.body().get(1).getCentres(), loadParams.key + 1);
                        }
                    }
                } else {
//                    status = 1;
//                    status.postValue(1);
                    observer.onNext(1);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CentreListResponse>> call, Throwable t) {
//                status = 4;
//                status.postValue(1);
                observer.onNext(1);
            }
        });
    }
}
