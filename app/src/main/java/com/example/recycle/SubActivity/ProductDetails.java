package com.example.recycle.SubActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recycle.Model.User;
import com.example.recycle.R;
import com.example.recycle.RetrofitFolder.RestApiInterface;
import com.example.recycle.RetrofitFolder.RestClient;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetails extends AppCompatActivity {


    private String Product, user_id, product_id, user_name, product_name, description, image, price, year, date, url = RestClient.BASE_URL + "product_image/";
    String name, phone;
    Button Contact, Chat;
    TextView Description, ProductName, Price, Year, Seller, Date;
    ImageView ProductImage;
    RestApiInterface restApiInterface;

    private JSONObject data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        ProductImage = findViewById(R.id.product_image);
        Contact = findViewById(R.id.contact);
        Chat = findViewById(R.id.chat);
        ProductName = findViewById(R.id.product_name);
        Price = findViewById(R.id.price);
        Year = findViewById(R.id.years);
        Seller = findViewById(R.id.seller);
        Description = findViewById(R.id.description);
        Date = findViewById(R.id.date);
        Product = getIntent().getStringExtra("Product");
        try {
            data = new JSONObject(Product);
            user_id = data.get("User ID").toString();
            product_id = data.get("Product ID").toString();
            user_name = data.get("Name").toString();
            description = data.get("Description").toString();
            product_name = data.get("Product Name").toString();
            price = data.get("Price").toString();
            year = data.get("Years").toString();
            image = data.get("Image").toString();
            date = data.get("Date").toString();
            Log.d("Image", image);

            price = "₹" + price;
            ProductName.setText(product_name);
            Price.setText(price);
            Year.setText(year);
            Seller.setText(user_name);
            Date.setText(date);
            Description.setText(description);

            url = url + image;
            Log.d("Image URL", url);
            Picasso.get().load(url).fit().centerInside().placeholder(R.drawable.ic_round_image_24).into(ProductImage);
            url = RestClient.BASE_URL + "product_image/";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                restApiInterface = RestClient.getRetrofit().create(RestApiInterface.class);

                Call<User> call = restApiInterface.getUserData(Integer.parseInt(user_id));
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                        User user = response.body();
                        name = user.getUserName();
                        phone = user.getPhone();

                        Intent purchase = new Intent(ProductDetails.this, ChatActivity.class);
                        purchase.putExtra("Phone", phone);
                        startActivity(purchase);
                    }
                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(ProductDetails.this, "Cannot Access Server", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        Contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contact = new Intent(ProductDetails.this, ContactActivity.class);
                contact.putExtra("User ID", user_id);
                startActivity(contact);
            }
        });

    }
}