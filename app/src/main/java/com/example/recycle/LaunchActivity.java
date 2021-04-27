package com.example.recycle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.recycle.MainUI.MainActivity;

public class LaunchActivity extends AppCompatActivity {

    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        sp = getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        if(sp.getInt("Log in Status", 0)==2)
        {
            Intent check = new Intent(LaunchActivity.this, MainActivity.class);
            startActivity(check);
            finish();
        }
        else if(!sp.contains("Phone Number")){
            Intent register = new Intent(LaunchActivity.this, SignUPActivity.class);
            startActivity(register);
            finish();
        }
        else{
            String phone = sp.getString("Phone Number","");
            Intent register = new Intent(LaunchActivity.this, RegisterActivity.class);
            startActivity(register);
            finish();
        }
    }
}