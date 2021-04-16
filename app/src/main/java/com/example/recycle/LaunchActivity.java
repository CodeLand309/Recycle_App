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
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString("Image", "Image");
//        editor.putInt("User ID", 1);
//        editor.putInt("Log in Status", 0);
//        editor.apply();
        String image = sp.getString("Image", "");
        String name = sp.getString("Name","");
        Log.d("IMAGE: ", image);
        Log.d("Name: ", name);
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
            register.putExtra("Phone Number", phone);
            startActivity(register);
            finish();
        }
    }
}