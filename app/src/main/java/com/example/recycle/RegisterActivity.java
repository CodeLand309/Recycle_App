package com.example.recycle;

import androidx.annotation.NonNull;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.recycle.MainUI.MainActivity;
import com.example.recycle.MainUI.User;
import com.example.recycle.RetrofitFolder.RestApiInterface;
import com.example.recycle.RetrofitFolder.RestClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    Button Register, Upload;
    EditText name, age, address;
    Spinner gender;
    private CircleImageView profile;
    private static final int PICK_IMAGE = 777;
    private FirebaseUser user;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private int Age=0, key=0, flag=0;
    private String Name, Phone, Address, Gender, Profile;
    private SharedPreferences sp;
    Uri imageURI;
    Bitmap bitmap;
    String Result;

    private RestApiInterface restApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Register=findViewById(R.id.register);
        Upload = findViewById(R.id.upload);
        profile = (CircleImageView) findViewById(R.id.profile_image);
        gender = findViewById(R.id.spinner);
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        address = findViewById(R.id.address);

        List<String> categories = new ArrayList<String>();
        categories.add("Select Gender");
        categories.add("Female");
        categories.add("Male");
        categories.add("Other");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        gender.setAdapter(dataAdapter);

        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String Item = adapterView.getItemAtPosition(i).toString();
                if(Item == "Female") {
                    key=1;
                    Gender = "Female";
                }
                else if(Item == "Male") {
                    key=1;
                    Gender = "Male";
                }
                else if(Item == "Other"){
                    key=1;
                    Gender = "Other";
                }
                else{
                    key=0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                key = 0;
                Toast.makeText(RegisterActivity.this, "Choose Value", Toast.LENGTH_SHORT).show();
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(key!=0) {
                    SharedPreferences sp = getSharedPreferences("Credentials", Context.MODE_PRIVATE);
                    Phone = sp.getString("Phone Number","");
//                    Phone = "9895054781";
                    Name = name.getText().toString();
                    Address = address.getText().toString();
                    Age = Integer.parseInt(age.getText().toString());
                    Profile = imageToString();

                    if(!(Name.equals("")) && !(Address.equals("")) && !(Phone.equals("")) && Age!=0 && flag!=0){

                        restApiInterface = RestClient.getRetrofit().create(RestApiInterface.class);

                        Call<User> call = restApiInterface.registerUser(Name, Gender, Address, Profile, Phone, Age);
                        call.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (!response.isSuccessful()) {
                                    Result = "Code: " + response.code();
                                    Toast.makeText(RegisterActivity.this, Result, Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                User user = response.body();
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("Name", user.getUserName());
                                editor.putInt("User ID", user.getUserID());
                                editor.putString("Gender", user.getGender());
                                editor.putString("Address", user.getAddress());
                                editor.putString("Phone Number", user.getPhone());
                                editor.putString("Image", user.getImage());
                                editor.putInt("Age", user.getAge());
                                editor.putInt("Log in Status", 2);
                                editor.apply();

                                HashMap<String,Object> chatUser =new HashMap<>();
                                chatUser.put("Name", Name);
                                chatUser.put("Phone", Phone);
                                chatUser.put("Image", user.getImage());
                                chatUser.put("ID", user.getUserID());
                                database = FirebaseDatabase.getInstance();
                                myRef = database.getReference("Users").child(Phone);

                                myRef.setValue(chatUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                                            i.putExtra("Fragment", 1);
                                            startActivity(i);
                                            finish();
                                        }
                                    }
                                });

//                                Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
//                                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
//                                startActivity(i);
//                                finish();
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Result = t.getMessage();
                                Toast.makeText(RegisterActivity.this, Result, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else{
                        Toast.makeText(RegisterActivity.this, "Enter All Fields", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Choose Gender", Toast.LENGTH_SHORT).show();
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageURI = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageURI);
                profile.setImageBitmap(bitmap);
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

//                byte[] imageInByte = stream.toByteArray();
//                long lengthbmp = imageInByte.length;

        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }
}