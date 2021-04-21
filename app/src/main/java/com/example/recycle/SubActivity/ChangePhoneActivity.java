package com.example.recycle.SubActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recycle.MainUI.MainActivity;
import com.example.recycle.OTP_Activity;
import com.example.recycle.R;
import com.example.recycle.RegisterActivity;
import com.example.recycle.RetrofitFolder.RestApiInterface;
import com.example.recycle.RetrofitFolder.RestClient;
import com.example.recycle.SignUPActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePhoneActivity extends AppCompatActivity {

    Button Verify, SendOTP, Resend;
    EditText Phone, t1,t2,t3,t4,t5,t6;

    private String phone, result, otp, o1,o2,o3,o4,o5,o6;
    private RestApiInterface restApiInterface;

    FirebaseAuth auth;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private String verificationCode;
    PhoneAuthProvider.ForceResendingToken mResendToken;

    private static long mTimeLeftInMillis = 120000;
    TextView countTime,wait;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);
        Verify = findViewById(R.id.verify_again);
        SendOTP = findViewById(R.id.new_otp);
        Resend = findViewById(R.id.resend_again);
        Phone = findViewById(R.id.new_phone);

        wait=findViewById(R.id.wait);
        countTime=findViewById(R.id.countTime);

        t1 = findViewById(R.id.txt1);
        t2 = findViewById(R.id.txt2);
        t3 = findViewById(R.id.txt3);
        t4 = findViewById(R.id.txt4);
        t5 = findViewById(R.id.txt5);
        t6 = findViewById(R.id.txt6);

        SendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone = Phone.getText().toString();
                if(phone.length() == 10) {
                    StartFirebaseLogin();
                    ChangePhoneNumber(phone);
                }
                else{
                    Toast.makeText(ChangePhoneActivity.this, "Enter Valid Phone Number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber("+91"+phone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(OTP_Activity.this)
                        .setCallbacks(mCallback)
                        .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
                */
                //Toast.makeText(OTP_Activity.this, "hello", Toast.LENGTH_SHORT).show();
                phone = "+91 " + phone;
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phone,                     // Phone number to verify
                        120,                           // Timeout duration
                        TimeUnit.SECONDS,                // Unit of timeout
                        ChangePhoneActivity.this,        // Activity (for callback binding)
                        mCallback,
                        mResendToken);
                Toast.makeText(ChangePhoneActivity.this, "yes", Toast.LENGTH_SHORT).show();
            }
        });

        Verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                o1 = t1.getText().toString();
//                if(c1.length()==1)
//                    e2.requestFocus();
                o2 = t2.getText().toString();
//                if(c2.length()==1)
//                    e3.requestFocus();
                o3 = t3.getText().toString();
//                if(c3.length()==1)
//                    e4.requestFocus();
                o4 = t4.getText().toString();
//                if(c4.length()==1)
//                    e5.requestFocus();
                o5 = t5.getText().toString();
//                if(c5.length()==1)
//                    e6.requestFocus();
                o6 = t6.getText().toString();
                otp = o1+o2+o3+o4+o5+o6;
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
                SigninWithNewPhone(credential);
            }
        });
    }

    private void ChangePhoneNumber(String phone) {
        phone = "+91 " + phone;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,                     // Phone number to verify
                120,                           // Timeout duration
                TimeUnit.SECONDS,                // Unit of timeout
                ChangePhoneActivity.this,        // Activity (for callback binding)
                mCallback);
    }

    private void StartFirebaseLogin() {

        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(ChangePhoneActivity.this,"verification completed",Toast.LENGTH_SHORT).show();
                wait.setVisibility(View.VISIBLE);
                countTime.setVisibility(View.VISIBLE);
                new CountDownTimer(120000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        mTimeLeftInMillis = millisUntilFinished;
                        updateCountDownText();
                    }
                    @Override
                    public void onFinish() {
                        countTime.setText("TimeOut");
                    }
                }.start();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(ChangePhoneActivity.this,"verification failed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                mResendToken = forceResendingToken;
                Toast.makeText(ChangePhoneActivity.this,"Code sent", Toast.LENGTH_SHORT).show();
                wait.setVisibility(View.VISIBLE);
                countTime.setVisibility(View.VISIBLE);
                new CountDownTimer(mTimeLeftInMillis,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        mTimeLeftInMillis = millisUntilFinished;
                        updateCountDownText();
                    }
                    @Override
                    public void onFinish() {
                        countTime.setText("TimeOut");
                    }
                }.start();
            }
        };
    }

    public void updateCountDownText(){
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        countTime.setText(timeLeftFormatted);
    }

    private void SigninWithNewPhone(PhoneAuthCredential credential) {
        auth.getCurrentUser().updatePhoneNumber(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    SharedPreferences sp = getSharedPreferences("Credentials", Context.MODE_PRIVATE);
                    Intent register = new Intent(ChangePhoneActivity.this, MainActivity.class);
                    SharedPreferences.Editor editor = sp.edit();
                    int user_id = sp.getInt("User ID", 0);
                    editor.putString("Phone Number", phone);
                    editor.putInt("Log in Status", 2);
                    editor.apply();

                    restApiInterface = RestClient.getRetrofit().create(RestApiInterface.class);

                    Call<JsonElement> call = restApiInterface.editPhone(phone, user_id);
                    call.enqueue(new Callback<JsonElement>() {
                        @Override
                        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                            if (!response.isSuccessful()) {
                                result = "Code: " + response.code();
                                Toast.makeText(ChangePhoneActivity.this, result, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            JsonObject jsonObject = response.body().getAsJsonObject();
                            String status = jsonObject.get("status").getAsString();
                            Toast.makeText(ChangePhoneActivity.this, status, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<JsonElement> call, Throwable t) {
                            result = t.getMessage();
                            Toast.makeText(ChangePhoneActivity.this, result, Toast.LENGTH_SHORT).show();
                        }
                    });
                    startActivity(register);
                    finish();
                } else {
                    Toast.makeText(ChangePhoneActivity.this,"Incorrect OTP",Toast.LENGTH_SHORT).show();
                }
            }
        });
//        auth.signInWithCredential(credential)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            SharedPreferences sp = getSharedPreferences("Credentials", Context.MODE_PRIVATE);
//                            if(sp.contains("User ID")){
//                                Intent register = new Intent(ChangePhoneActivity.this, MainActivity.class);
//                                FirebaseUser user = task.getResult().getUser();
//                                SharedPreferences.Editor editor = sp.edit();
//                                editor.putString("Phone Number", phone);
//                                editor.putInt("Log in Status", 2);
//                                editor.apply();
//                                startActivity(register);
//                                finish();
//                            }
//                            else {
//                                Intent register = new Intent(ChangePhoneActivity.this, RegisterActivity.class);
//                                SharedPreferences.Editor editor = sp.edit();
//                                editor.putString("Phone Number", phone);
//                                editor.putInt("Log in Status", 1);
//                                editor.apply();
//                                FirebaseUser user = task.getResult().getUser();
//                                register.putExtra("Phone Number", phone);
//                                startActivity(register);
//                                finish();
//                            }
//                        } else {
//                            Toast.makeText(ChangePhoneActivity.this,"Incorrect OTP",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
    }
}