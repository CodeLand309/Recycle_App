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
import android.widget.TextView;
import android.widget.Toast;

import com.example.recycle.MainUI.MainActivity;
import com.example.recycle.R;
import com.example.recycle.RetrofitFolder.RestApiInterface;
import com.example.recycle.RetrofitFolder.RestClient;
import com.example.recycle.ServerErrorActivity;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoProvider;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeProfile extends AppCompatActivity {

    private EditText Address, Age;
    private TextView Name, Phone;
    private Button Upload, Save, Cancel;
    private ImageView Image;
    private String name, address, phone, image, Result, url = RestClient.BASE_URL + "user_image/";
    private int age=0, user_id, flag=0;
    private static final int PICK_IMAGE = 777;
    private SharedPreferences sp;
    private JSONObject data;
    private Uri imageURI;
    private Bitmap bitmap;
    private RestApiInterface restApiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);
        Image = findViewById(R.id.profile_image);
        Upload = findViewById(R.id.new_upload);
        Save = findViewById(R.id.save_data);
        Cancel = findViewById(R.id.cancel);
        Name = findViewById(R.id.my_name);
        Phone = findViewById(R.id.my_phone);
        Age = findViewById(R.id.new_age);
        Address = findViewById(R.id.new_address);

        SharedPreferences sp = getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        user_id = sp.getInt("User ID", 0);
        name = sp.getString("Name", "");
        age = sp.getInt("Age", 0);
        image = sp.getString("Image", "");
        address = sp.getString("Address", "");
        phone = sp.getString("Phone Number", "");
        url = url + phone + "/" + name;

        Name.setText(name);
        Phone.setText(phone);
        Age.setText(age+"");
        Address.setText(address+"");
        Log.d("Image", image);

        Picasso.get().load(url).placeholder(R.drawable.profile).fit().into(Image);
        flag=1;
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                age = Integer.parseInt(Age.getText().toString());
                address = Address.getText().toString();
                if(flag==2)
                    image = imageToString();
                if(!(address.equals("")) && age!=0 && flag!=0){

                    restApiInterface = RestClient.getRetrofit().create(RestApiInterface.class);

                    Call<JsonElement> call = restApiInterface.editUser(name, user_id, age, address, phone, image, flag);
                    call.enqueue(new Callback<JsonElement>() {
                        @Override
                        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                            if (!response.isSuccessful()) {
                                Result = "Code: " + response.code();
                                Toast.makeText(ChangeProfile.this, Result, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            JsonObject jsonObject = response.body().getAsJsonObject();
                            String status = jsonObject.get("status").getAsString();
                            int new_age = jsonObject.get("Age").getAsInt();
                            String new_address = jsonObject.get("Address").getAsString();
                            Toast.makeText(ChangeProfile.this, status, Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putInt("Age", age);
                            editor.putString("Address", address);
                            editor.commit();
                            Intent i = new Intent(ChangeProfile.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<JsonElement> call, Throwable t) {
                            Result = t.getMessage();
                            Intent i = new Intent(ChangeProfile.this, ServerErrorActivity.class);
                            startActivity(i);
                        }
                    });
                }
                else{
                    Toast.makeText(ChangeProfile.this, "Enter All Fields", Toast.LENGTH_SHORT).show();
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
                Intent i = new Intent(ChangeProfile.this, MainActivity.class);
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
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }
}