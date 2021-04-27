package com.example.recycle.SubActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import com.example.recycle.MainUI.MainActivity;
import com.example.recycle.MainUI.SellFragment;
import com.example.recycle.MainUI.User;
import com.example.recycle.R;
import com.example.recycle.RegisterActivity;
import com.example.recycle.RetrofitFolder.RestApiInterface;
import com.example.recycle.RetrofitFolder.RestClient;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadProduct extends AppCompatActivity {

    private EditText Product, Description, Year, Price;
    private Button Upload, Save, Cancel;
    private ImageView Image;
    private String name, product, description, date, image, Result;
    private int year=0, price=0, user_id, flag=0;
    private static final int PICK_IMAGE = 777;
    private SharedPreferences sp;
    private Uri imageURI;
    private Bitmap bitmap;
    private RestApiInterface restApiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                product = Product.getText().toString();
                description = Description.getText().toString();
                year = Integer.parseInt(Year.getText().toString());
                price = Integer.parseInt(Price.getText().toString());
                Calendar calender = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                date = dateFormat.format(calender.getTime());
                sp = getSharedPreferences("Credentials", Context.MODE_PRIVATE);
                name = sp.getString("Name", "");
                user_id = sp.getInt("User ID", 0);
                Log.d("Name: ", product);
                Log.d("Description: ", description);
                Log.d("Year", year+"");
                Log.d("Price: ", price+"");
                Log.d("DATE: ", date);
                if(flag==1)
                    image = imageToString();
                if(!(product.equals("")) && !(description.equals("")) && !(date.equals("")) && year!=0 && price!=0 && flag!=0){

                    restApiInterface = RestClient.getRetrofit().create(RestApiInterface.class);

                    Call<JsonElement> call = restApiInterface.addProduct(product, description, year, price, date, image, name, user_id);
                    call.enqueue(new Callback<JsonElement>() {
                        @Override
                        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                            if (!response.isSuccessful()) {
                                Result = "Code: " + response.code();
                                Toast.makeText(UploadProduct.this, "There was Some Error", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Toast.makeText(UploadProduct.this, "Product Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            JsonObject jsonObject = response.body().getAsJsonObject();
                            String content = jsonObject.get("status").getAsString();
                            Toast.makeText(UploadProduct.this, content, Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(UploadProduct.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<JsonElement> call, Throwable t) {
                            Result = t.getMessage();
                            Toast.makeText(UploadProduct.this, "Could Not Upload Product", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    Toast.makeText(UploadProduct.this, "Enter All Fields", Toast.LENGTH_SHORT).show();
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
                Intent i = new Intent(UploadProduct.this, MainActivity.class);
                startActivity(i);
                finish();
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
                flag = 1;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String imageToString(){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }
}