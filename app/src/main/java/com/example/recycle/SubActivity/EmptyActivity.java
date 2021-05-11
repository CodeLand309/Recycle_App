package com.example.recycle.SubActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.recycle.Activities.MainActivity;
import com.example.recycle.R;

public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(EmptyActivity.this, MainActivity.class);
        startActivity(i);
    }
}