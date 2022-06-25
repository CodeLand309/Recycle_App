package com.example.recycle.Pagination;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.example.recycle.Model.CentreListResponse;
import com.example.recycle.Model.DataResponse;
import com.example.recycle.Model.ProductsItem;
import com.example.recycle.Network.Repository;

import java.lang.reflect.Type;
import java.util.ArrayList;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDataSource extends PageKeyedDataSource<Integer, ProductsItem> {

    public static int PAGE_SIZE = 4;
    private static int FIRST_PAGE = 1;
    private Integer ID=null;
    private String TYPE;
    private Integer total=0;
    private Observer<Integer> observer;
    //private static int status;

    private Observable<Integer> status = Observable.just(-1);
    private String Search=null;
    public Observable<Integer> getStatus(){
        return status;
    }

    public Integer getSearchResultSize(){
        return total;
    }
//
//    public static void setID(Integer id){
//        ID = id;
//    }

//    public static void setType(String type){
//        TYPE=type;
//        if("Other".equals(type))
//            PAGE_SIZE = 4;
//        else
//            PAGE_SIZE = 2;
//    }

    ProductDataSource(Integer id, String searchText, String type, Observer<Integer> observer){
        ID = id;
        TYPE = type;
        if("Other".equals(type))
            PAGE_SIZE = 4;
        else
            PAGE_SIZE = 2;
        Search = searchText;
        this.observer = observer;
        status.subscribe(observer);
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> loadInitialParams, @NonNull LoadInitialCallback<Integer, ProductsItem> loadInitialCallback) {
        Call<ArrayList<DataResponse>> call;
//        if(ID == null){
//            call = Repository.getRepository().getProductDataNoID(FIRST_PAGE, PAGE_SIZE, TYPE, Search);
//        }else{
//            call = Repository.getRepository().getProductData(FIRST_PAGE, PAGE_SIZE, TYPE, ID, Search);
//        }

        call = Repository.getRepository().getProducts(FIRST_PAGE, PAGE_SIZE, TYPE, ID, Search);
        call.enqueue(new Callback<ArrayList<DataResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<DataResponse>> call, Response<ArrayList<DataResponse>> response) {
                if(call.isExecuted() && response.body() != null && response.body().get(0).getStatus().equals("ok")) {
                    if (Search != null) {
                        if (response.body().get(1).getSearch().equals("found")) {
                            observer.onNext(4);
                            total = response.body().get(2).getSearchResultSize();
                            loadInitialCallback.onResult(response.body().get(3).getItems(), null, 2);
                        } else {
                            observer.onNext(3);
                        }
                    } else {
                        observer.onNext(2);
                        loadInitialCallback.onResult(response.body().get(1).getItems(), null, 2);
                    }
                }else{
                    observer.onNext(1);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<DataResponse>> call, Throwable t) {
                observer.onNext(1);
            }
        });


        //call.enqueue(new Callback<PagedList<DataResponse>>() {
//            @Override
//            public void onResponse(Call<ArrayList<DataResponse>> call, Response<ArrayList<DataResponse>> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<DataResponse>> call, Throwable t) {
//                status = 1;
//            }
//        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> loadParams, @NonNull LoadCallback<Integer, ProductsItem> loadCallback) {
//        Call<ArrayList<DataResponse>> call;
////        if(ID == null){
////            call = Repository.getRepository().getProductDataNoID(loadParams.key, PAGE_SIZE);
////        }else{
////            call = Repository.getRepository().getProductData(loadParams.key, PAGE_SIZE, ID);
////        }
//        call = Repository.getRepository().getProducts(loadParams.key, PAGE_SIZE, TYPE, ID);
//        call.enqueue(new Callback<ArrayList<DataResponse>>() {
//            @Override
//            public void onResponse(Call<ArrayList<DataResponse>> call, Response<ArrayList<DataResponse>> response) {
//                if(response.body()!=null && loadParams.key > 1){
//                    status=2;
//                    loadCallback.onResult(response.body().get(1).getItems(), loadParams.key-1);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<DataResponse>> call, Throwable t) {
//                status = 1;
//            }
//        });
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> loadParams, @NonNull LoadCallback<Integer, ProductsItem> loadCallback) {
        Call<ArrayList<DataResponse>> call;
//        if(ID == null){
//            call = Repository.getRepository().getProductDataNoID(FIRST_PAGE, PAGE_SIZE, TYPE, Search);
//        }else{
//            call = Repository.getRepository().getProductData(FIRST_PAGE, PAGE_SIZE, TYPE, ID, Search);
//        }
        call = Repository.getRepository().getProducts(loadParams.key, PAGE_SIZE, TYPE, ID, Search);
        call.enqueue(new Callback<ArrayList<DataResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<DataResponse>> call, Response<ArrayList<DataResponse>> response) {
                if(call.isExecuted() && response.body()!=null && response.body().get(0).getStatus().equals("ok")) {
                    if(Search!=null) {
                        if (response.body().get(1).getSearch().equals("found")) {
                            observer.onNext(4);
                            loadCallback.onResult(response.body().get(3).getItems(), loadParams.key + 1);
                        }else{
                            observer.onNext(3);
                        }
                    }else{
                        observer.onNext(2);
                        loadCallback.onResult(response.body().get(1).getItems(), loadParams.key + 1);
                    }
                }else{
                    observer.onNext(1);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<DataResponse>> call, Throwable t) {
                observer.onNext(1);
            }
        });
    }

}
