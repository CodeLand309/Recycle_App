package com.example.recycle.SubActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.recycle.R;
import com.example.recycle.RetrofitFolder.RestClient;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductDetails extends AppCompatActivity {


    private String Product, user_id, product_id, user_name, product_name, description, image, price, year, date, url = RestClient.BASE_URL + "product_image/";
    Button Contact, Buy;
    TextView Description, ProductName, Price, Year, Seller, Date;
    ImageView ProductImage;
    private JSONObject data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        ProductImage = findViewById(R.id.product_image);
        Contact = findViewById(R.id.contact);
        Buy = findViewById(R.id.buy);
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
        Buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent purchase = new Intent(ProductDetails.this, PurchaseActivity.class);
                purchase.putExtra("Product ID", product_id);
                startActivity(purchase);
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