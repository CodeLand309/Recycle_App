package com.example.recycle.SubActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recycle.MainUI.User;
import com.example.recycle.R;
import com.example.recycle.RetrofitFolder.RestApiInterface;
import com.example.recycle.RetrofitFolder.RestClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactActivity extends AppCompatActivity {

    private RestApiInterface restApiInterface;
    private String name, phone, address;
    private int user_id;
    private CircleImageView Image;
    private User user;
    private TextView Name, Phone, Address;
    private Button Call;
    String url = "http://192.168.29.202:5000/user_image/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Call = findViewById(R.id.call_user);
        Name = findViewById(R.id.username);
        Phone = findViewById(R.id.userphone);
        Address = findViewById(R.id.useraddress);
        Image = findViewById(R.id.profile_image);

        user_id = Integer.parseInt(getIntent().getStringExtra("User ID"));
        restApiInterface = RestClient.getRetrofit().create(RestApiInterface.class);

        Call<User> call = restApiInterface.getUserData(user_id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                user = response.body();
                name = user.getUserName();
                phone = user.getPhone();
                address = user.getAddress();

                Name.setText(name);
                Phone.setText(phone);
                Address.setText(address);
                url = url + name;
                Picasso.get().load(url).fit().into(Image);

            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ContactActivity.this, "Cannot Access Server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}