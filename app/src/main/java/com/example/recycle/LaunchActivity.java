package com.example.recycle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.recycle.MainUI.MainActivity;

public class LaunchActivity extends AppCompatActivity {

    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        sp = getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        if(sp.contains("Name") && sp.contains("Phone Number") && sp.contains("User ID")){
            Intent check = new Intent(LaunchActivity.this, MainActivity.class);
            startActivity(check);
            finish();
        }
        else{
            Intent check = new Intent(LaunchActivity.this, RegisterActivity.class);
            startActivity(check);
            finish();
        }
    }
}