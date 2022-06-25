package com.example.recycle.ViewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ProductsViewModelFactory implements ViewModelProvider.Factory {
    private Integer ID;
    private String type;

    public ProductsViewModelFactory(Integer id, String type){
        ID = id;
        this.type = type;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ProductsViewModel(ID, type);
    }
}
