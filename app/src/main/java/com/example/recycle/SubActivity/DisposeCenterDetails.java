package com.example.recycle.SubActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.recycle.R;

public class DisposeCenterDetails extends AppCompatActivity {

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispose_center_details);

//        list = findViewById(R.id.list);
//        Intent i = getIntent();
//        String temp[] = i.getStringArrayExtra("listViewClickValues");
//        ArrayAdapter<String> adapter2= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, temp);
//        list.setAdapter(adapter2);
    }
}