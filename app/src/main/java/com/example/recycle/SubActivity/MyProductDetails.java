package com.example.recycle.SubActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recycle.R;
import com.example.recycle.RetrofitFolder.RestApiInterface;
import com.example.recycle.RetrofitFolder.RestClient;
import com.example.recycle.Activities.ServerErrorActivity;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProductDetails extends AppCompatActivity {

    private String Product, Result, user_id, product_id, product_name, description, image, price, year, date, status, url = RestClient.BASE_URL + "product_image/";
    private RestApiInterface restApiInterface;
    Button Mark_Received, Mark_Sold;
    TextView Description, ProductName, Price, Year, Seller, Date, Status;
    ImageView ProductImage;
    private JSONObject data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_product_details);
        ProductImage = findViewById(R.id.product_image);
        Mark_Received = findViewById(R.id.mark_received);
        Mark_Sold = findViewById(R.id.mark_sold);
        ProductName = findViewById(R.id.product_name);
        Price = findViewById(R.id.price);
        Year = findViewById(R.id.years);
        Seller = findViewById(R.id.seller);
        Description = findViewById(R.id.description);
        Date = findViewById(R.id.date);
        Status = findViewById(R.id.status);

        Product = getIntent().getStringExtra("Product");
        try {
            data = new JSONObject(Product);
            user_id = data.get("User ID").toString();
            product_id = data.get("Product ID").toString();
            description = data.get("Description").toString();
            product_name = data.get("Product Name").toString();
            price = data.get("Price").toString();
            year = data.get("Years").toString();
            image = data.get("Image").toString();
            date = data.get("Date").toString();
            status = data.get("Status").toString();
            Log.d("Image", image);

            ProductName.setText(product_name);
            Price.setText(price);
            Year.setText(year);
            Date.setText(date);
            Description.setText(description);

            if(status.equals("1"))
                Status.setText("In Transaction");
            else if(status.equals("0"))
                Status.setText("Unsold");
            url = url + image;
            Log.d("Image URL", url);
            Picasso.get().load(url).fit().centerInside().placeholder(R.drawable.ic_round_image_24).into(ProductImage);
            url = RestClient.BASE_URL + "product_image/";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Mark_Sold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                restApiInterface = RestClient.getRetrofit().create(RestApiInterface.class);

                Call<JsonElement> call = restApiInterface.markSold(Integer.parseInt(product_id), Integer.parseInt(user_id));
                call.enqueue(new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        if (!response.isSuccessful()) {
                            Result = "Code: " + response.code();
                            Toast.makeText(MyProductDetails.this, "There was some Error", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        JsonObject jsonObject = response.body().getAsJsonObject();
                        String content = jsonObject.get("Status").getAsString();
                        String status = jsonObject.get("Product Status").getAsString();
                        if(status.equals("In Transaction"))
                            Status.setText("In Transaction");
                        else if(status.equals("Unsold"))
                            Status.setText("Unsold");
                    }

                    @Override
                    public void onFailure(Call<JsonElement> call, Throwable t) {
                        Result = t.getMessage();
                        Intent i = new Intent(MyProductDetails.this, ServerErrorActivity.class);
                        startActivity(i);
                    }
                });
            }
        });
        Mark_Received.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                restApiInterface = RestClient.getRetrofit().create(RestApiInterface.class);

                Call<JsonElement> call = restApiInterface.markReceived(Integer.parseInt(product_id), Integer.parseInt(user_id));
                call.enqueue(new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        if (!response.isSuccessful()) {
                            Result = "Code: " + response.code();
                            Toast.makeText(MyProductDetails.this, Result, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        JsonObject jsonObject = response.body().getAsJsonObject();
                        String content = jsonObject.get("Status").getAsString();
                    }

                    @Override
                    public void onFailure(Call<JsonElement> call, Throwable t) {
                        Result = t.getMessage();
                        Toast.makeText(MyProductDetails.this, Result, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}