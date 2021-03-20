package com.example.recycle.SubActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recycle.LaunchActivity;
import com.example.recycle.R;
import com.example.recycle.RetrofitFolder.RestApiInterface;
import com.example.recycle.RetrofitFolder.RestClient;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProduct extends AppCompatActivity {

    private EditText Product, Description, Year, Price;
    private Button Upload, Save, Cancel;
    private ImageView Image;
    private String product, description, image, Result, ProductJson, url = RestClient.BASE_URL + "product_image/";
    private int year=0, price=0, user_id, product_id, flag=0;
    private static final int PICK_IMAGE = 777;
    private SharedPreferences sp;
    private JSONObject data;
    private Uri imageURI;
    private Bitmap bitmap;
    private RestApiInterface restApiInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_product);
        Product = findViewById(R.id.enter_name);
        Description = findViewById(R.id.type_description);
        Year = findViewById(R.id.type_years);
        Price = findViewById(R.id.type_price);
        Image = findViewById(R.id.product_picture);
        Upload = findViewById(R.id.upload_image);
        Save = findViewById(R.id.save_button);
        Cancel = findViewById(R.id.cancel_button);

        ProductJson = getIntent().getStringExtra("Product");
        try {
            data = new JSONObject(ProductJson);
            product_id = Integer.parseInt(data.get("Product ID").toString());
            description = data.get("Description").toString();
            user_id = Integer.parseInt(data.get("User ID").toString());
            product = data.get("Product Name").toString();
            price = Integer.parseInt(data.get("Price").toString());
            year = Integer.parseInt(data.get("Years").toString());
            image = data.get("Image").toString();
            Log.d("Image", image);

            Product.setText(product);
            Price.setText(price + "");
            Year.setText(year + "");
            Description.setText(description);

            url = url + image;
            Log.d("Image URL", url);
            Picasso.get().load(url).fit().placeholder(R.drawable.profile).into(Image);
            flag=1;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                product = Product.getText().toString();
                description = Description.getText().toString();
                year = Integer.parseInt(Year.getText().toString());
                price = Integer.parseInt(Price.getText().toString());
                Log.d("Name: ", product);
                Log.d("Description: ", description);
                Log.d("Year", year+"");
                Log.d("Price: ", price+"");
                if(flag==2)
                    image = imageToString();
                if(!(product.equals("")) && !(description.equals("")) && year!=0 && price!=0 && flag!=0){

                    restApiInterface = RestClient.getRetrofit().create(RestApiInterface.class);

                    Call<JsonElement> call = restApiInterface.editProduct(product, description, year, price, image, product_id, user_id, flag);
                    call.enqueue(new Callback<JsonElement>() {
                        @Override
                        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                            if (!response.isSuccessful()) {
                                Result = "Code: " + response.code();
                                Toast.makeText(EditProduct.this, Result, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            JsonObject jsonObject = response.body().getAsJsonObject();
                            String content = jsonObject.get("status").getAsString();
                            Toast.makeText(EditProduct.this, content, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<JsonElement> call, Throwable t) {
                            Result = t.getMessage();
                            Toast.makeText(EditProduct.this, Result, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    Toast.makeText(EditProduct.this, "Enter All Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditProduct.this, "Clicked on Cancel Button", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageURI = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageURI);
                Image.setImageBitmap(bitmap);
                flag = 2;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String imageToString(){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();

//                byte[] imageInByte = stream.toByteArray();
//                long lengthbmp = imageInByte.length;

        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }
}
