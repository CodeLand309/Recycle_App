package com.example.recycle.SubActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recycle.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class DisposeCenterDetails extends AppCompatActivity {


    private String Centre, centre_name, centre_address, image, phone, url = "http://192.168.29.202:5000/dispose_image/";
    Button Call;
    TextView CentreAddress, CentreName, Phone;
    ImageView CentreImage;
    private JSONObject data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispose_centre_details);

//        list = findViewById(R.id.list);
//        Intent i = getIntent();
//        String temp[] = i.getStringArrayExtra("listViewClickValues");
//        ArrayAdapter<String> adapter2= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, temp);
//        list.setAdapter(adapter2);

        CentreImage = findViewById(R.id.centre_image);
        Call = findViewById(R.id.call);
        CentreName = findViewById(R.id.centre_name);
        CentreAddress = findViewById(R.id.location);
        Phone = findViewById(R.id.centre_phone);
        Centre = getIntent().getStringExtra("Centre");
        try {
            data = new JSONObject(Centre);
            centre_name = data.get("Name").toString();
            centre_address = data.get("Address").toString();
            phone = data.get("Phone").toString();
            image = data.get("Image").toString();
            Log.d("Image", image);

            CentreName.setText(centre_name);
            CentreAddress.setText(centre_address);
            Phone.setText(phone);

            url = url + image;
            Log.d("Image URL", url);
            Picasso.get().load(url).fit().placeholder(R.drawable.profile).into(CentreImage);
            url = "http://192.168.29.202:5000/dispose_image/";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DisposeCenterDetails.this, "clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}