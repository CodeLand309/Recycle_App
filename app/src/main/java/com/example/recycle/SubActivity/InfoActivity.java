package com.example.recycle.SubActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.recycle.R;

public class InfoActivity extends AppCompatActivity {

    private Button Electronic, Plastic, More;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Electronic = findViewById(R.id.electronic);
        Plastic = findViewById(R.id.plastic);
        More = findViewById(R.id.more_about_waste);
        Electronic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InfoActivity.this, InfoActivity_Electronic.class));
            }
        });
        Plastic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InfoActivity.this, InfoActivity_Plastic.class));
            }
        });
        More.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.dtmskips.co.uk/blog/types-of-waste/"));
                startActivity(i);
            }
        });
    }
}