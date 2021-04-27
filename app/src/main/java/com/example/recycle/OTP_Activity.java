package com.example.recycle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recycle.MainUI.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class OTP_Activity extends AppCompatActivity {

    FirebaseAuth auth;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private String verificationCode;
    PhoneAuthProvider.ForceResendingToken mResendToken;

    EditText e1, e2, e3, e4, e5, e6;
    Button verify, resend;
    String  phone, otp, c1,c2,c3,c4,c5,c6;
    private static long mTimeLeftInMillis = 120000;
    TextView countTime,wait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        wait=findViewById(R.id.wait);
        countTime=findViewById(R.id.countTime);
        verify = findViewById(R.id.verify);
        resend = findViewById(R.id.resend);
        e1 = findViewById(R.id.text1);
        e2 = findViewById(R.id.text2);
        e3 = findViewById(R.id.text3);
        e4 = findViewById(R.id.text4);
        e5 = findViewById(R.id.text5);
        e6 = findViewById(R.id.text6);

        e1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length() == 1)
                    e2.requestFocus();
            }
        });

        e2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length() == 1)
                    e3.requestFocus();
                else if(editable.toString().length() == 0)
                    e1.requestFocus();
            }
        });

        e3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length() == 1)
                    e4.requestFocus();
                else if(editable.toString().length() == 0)
                    e2.requestFocus();
            }
        });

        e4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length() == 1)
                    e5.requestFocus();
                else if(editable.toString().length() == 0)
                    e3.requestFocus();
            }
        });

        e5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length() == 1)
                    e6.requestFocus();
                else if(editable.toString().length() == 0)
                    e4.requestFocus();
            }
        });

        e6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length() == 0)
                    e5.requestFocus();
            }
        });

        Intent otp_intent = getIntent();
        phone = otp_intent.getStringExtra("Phone Number");
        StartFirebaseLogin();
        verifyPhone(phone);
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone = "+91 " + phone;
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phone,                     // Phone number to verify
                        120,                           // Timeout duration
                        TimeUnit.SECONDS,                // Unit of timeout
                        OTP_Activity.this,        // Activity (for callback binding)
                        mCallback,
                        mResendToken);
                Toast.makeText(OTP_Activity.this, "yes", Toast.LENGTH_SHORT).show();
            }
        });
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c1 = e1.getText().toString();
                c2 = e2.getText().toString();
                c3 = e3.getText().toString();
                c4 = e4.getText().toString();
                c5 = e5.getText().toString();
                c6 = e6.getText().toString();
                if(c1=="" || c2=="" || c3=="" || c4=="" || c5=="" || c6=="")
                    Toast.makeText(OTP_Activity.this, "Enter All Fields", Toast.LENGTH_SHORT).show();
                otp = c1+c2+c3+c4+c5+c6;
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
                SigninWithPhone(credential);
            }
        });
    }

    private void verifyPhone(String phone) {
        phone = "+91 " + phone;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,                     // Phone number to verify
                120,                           // Timeout duration
                TimeUnit.SECONDS,                // Unit of timeout
                OTP_Activity.this,        // Activity (for callback binding)
                mCallback);
    }

    private void StartFirebaseLogin() {

        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(OTP_Activity.this,"verification completed",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(OTP_Activity.this,"verification failed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                mResendToken = forceResendingToken;
                Toast.makeText(OTP_Activity.this,"Code sent", Toast.LENGTH_SHORT).show();
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

    private void SigninWithPhone(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            SharedPreferences sp = getSharedPreferences("Credentials", Context.MODE_PRIVATE);
                            if(sp.contains("User ID")){
                                Intent register = new Intent(OTP_Activity.this, MainActivity.class);
                                FirebaseUser user = task.getResult().getUser();
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("Phone Number", phone);
                                editor.putInt("Log in Status", 2);
                                editor.commit();
                                startActivity(register);
                                finish();
                            }
                            else {
                                Intent register = new Intent(OTP_Activity.this, RegisterActivity.class);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("Phone Number", phone);
                                editor.putInt("Log in Status", 1);
                                editor.apply();
                                FirebaseUser user = task.getResult().getUser();
                                startActivity(register);
                                finish();
                            }
                        } else {
                            Toast.makeText(OTP_Activity.this,"Incorrect OTP",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}