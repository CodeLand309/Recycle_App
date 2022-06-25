package com.example.recycle.Fragments;

import android.view.View;

import androidx.fragment.app.Fragment;

public interface CallbackInterface {

    boolean isNetworkAvailable();
    boolean isAnyErrorMessageShowing();
    void showHideErrorMessages(int action);
    void hideKeyboard();
    void showSnackbar(View view, String ms);

}
