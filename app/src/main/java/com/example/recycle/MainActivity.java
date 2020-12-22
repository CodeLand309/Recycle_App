package com.example.recycle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText Phone;
    Button OTP, later;
    String phone_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Phone=findViewById(R.id.phone);
        OTP=findViewById(R.id.verify);
        later=findViewById(R.id.later);
        OTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone_no = Phone.getText().toString();
                if(phone_no.length()==10 && !phone_no.isEmpty()) {
                    Intent otp_intent = new Intent(MainActivity.this, OTP_Activity.class);
                    otp_intent.putExtra("Phone Number", phone_no);
                    startActivity(otp_intent);
                }
                else{
                    Toast.makeText(MainActivity.this, "Enter Valid Phone Number", Toast.LENGTH_SHORT).show();
                }
            }
        });
        later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent later_intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(later_intent);
            }
        });
    }
}