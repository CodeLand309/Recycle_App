package com.example.recycle.SubActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recycle.Model.User;
import com.example.recycle.R;
import com.example.recycle.Network.RestApiInterface;
import com.example.recycle.Network.RestClient;
import com.squareup.picasso.Picasso;

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
    private Button Dial;
    private String url = RestClient.BASE_URL + "user_image/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Dial = findViewById(R.id.call_user);
        Name = findViewById(R.id.username);
        Phone = findViewById(R.id.userphone);
        Address = findViewById(R.id.useraddress);
        Image = findViewById(R.id.profile_image);

        user_id = Integer.parseInt(getIntent().getStringExtra("User ID"));
        restApiInterface = RestClient.getRetrofit().create(RestApiInterface.class);

        Dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + phone));
                startActivity(dialIntent);
            }
        });

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
                url = url + phone + "/" + name;
                Picasso.get().load(url).fit().into(Image);

            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ContactActivity.this, "Cannot Access Server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}