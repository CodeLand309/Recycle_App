package com.example.recycle.SubActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.recycle.R;
import com.example.recycle.Network.RestClient;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class DisposeCenterDetails extends AppCompatActivity {


    private String Centre, centre_name, centre_address, image, phone, url = RestClient.BASE_URL + "dispose_image/";
    Button Call;
    TextView CentreAddress, CentreName, Phone;
    ImageView CentreImage;
    private JSONObject data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispose_centre_details);

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

            CentreName.setText(centre_name);
            CentreAddress.setText(centre_address);
            Phone.setText(phone);

            url = url + image;
            Picasso.get().load(url).fit().centerInside().placeholder(R.drawable.ic_round_image_24).into(CentreImage);
            url = RestClient.BASE_URL + "dispose_image/";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + phone));
                startActivity(dialIntent);
            }
        });
    }
}