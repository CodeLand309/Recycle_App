package com.example.recycle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.recycle.MainUI.MainActivity;

public class SignUPActivity extends AppCompatActivity {
    EditText Phone;
    Button OTP, later;
    String phone_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Phone=findViewById(R.id.phone);
        OTP=findViewById(R.id.verify);
        later=findViewById(R.id.sign_in_later);
        OTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone_no = Phone.getText().toString();
                if(phone_no.length() == 10) {
                    Intent otp_intent = new Intent(SignUPActivity.this, OTP_Activity.class);
                    otp_intent.putExtra("Phone Number", phone_no);
                    startActivity(otp_intent);
                    finish();
                }
                else{
                    Toast.makeText(SignUPActivity.this, "Enter Valid Phone Number", Toast.LENGTH_SHORT).show();
                }
            }
        });
        later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent later_intent = new Intent(SignUPActivity.this, MainActivity.class);
                startActivity(later_intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "This Operation not Supported", Toast.LENGTH_SHORT).show();
    }
}