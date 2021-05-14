package com.example.recycle.SubActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recycle.Activities.MainActivity;
import com.example.recycle.R;
import com.example.recycle.RetrofitFolder.RestApiInterface;
import com.example.recycle.RetrofitFolder.RestClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePhoneActivity extends AppCompatActivity {

    Button Verify, SendOTP, Resend;
    EditText Phone, t1,t2,t3,t4,t5,t6;

    private String old_phone, phone, result, otp, o1,o2,o3,o4,o5,o6;
    private RestApiInterface restApiInterface;

    private FirebaseAuth auth;

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

        t1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length() == 1)
                    t2.requestFocus();
            }
        });

        t2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length() == 1)
                    t3.requestFocus();
                else if(editable.toString().length() == 0)
                    t1.requestFocus();
            }
        });

        t3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length() == 1)
                    t4.requestFocus();
                else if(editable.toString().length() == 0)
                    t2.requestFocus();
            }
        });

        t4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length() == 1)
                    t5.requestFocus();
                else if(editable.toString().length() == 0)
                    t3.requestFocus();
            }
        });

        t5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length() == 1)
                    t6.requestFocus();
                else if(editable.toString().length() == 0)
                    t4.requestFocus();
            }
        });

        t6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length() == 0)
                    t5.requestFocus();
            }
        });

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

                phone = "+91 " + phone;
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phone,                     // Phone number to verify
                        120,                           // Timeout duration
                        TimeUnit.SECONDS,                // Unit of timeout
                        ChangePhoneActivity.this,        // Activity (for callback binding)
                        mCallback,
                        mResendToken);
            }
        });

        Verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                o1 = t1.getText().toString();
                o2 = t2.getText().toString();
                o3 = t3.getText().toString();
                o4 = t4.getText().toString();
                o5 = t5.getText().toString();
                o6 = t6.getText().toString();
                if(o1.equals("") || o2.equals("") || o3.equals("") || o4.equals("") || o5.equals("") || o6.equals(""))
                    Toast.makeText(ChangePhoneActivity.this, "Enter All Fields", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ChangePhoneActivity.this,"Verification Completed",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ChangePhoneActivity.this,"Verification Failed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                mResendToken = forceResendingToken;
                Toast.makeText(ChangePhoneActivity.this,"OTP has been Sent from Server", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(ChangePhoneActivity.this, "There was some Error", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            JsonObject jsonObject = response.body().getAsJsonObject();
                            String status = jsonObject.get("status").getAsString();
                            Toast.makeText(ChangePhoneActivity.this, status, Toast.LENGTH_SHORT).show();

                            old_phone = sp.getString("Phone Number","");
                            String name = sp.getString("Name", "");
                            int id = sp.getInt("User ID", 0);

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                            reference.child(old_phone).removeValue();

                            HashMap<String,Object> chatUser = new HashMap<>();
                            chatUser.put("Name", name);
                            chatUser.put("Phone", phone);
                            chatUser.put("Image", "image");
                            chatUser.put("ID", id);

                            reference.child(phone).setValue(chatUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent register = new Intent(ChangePhoneActivity.this, MainActivity.class);
                                        startActivity(register);
                                        finish();
                                    }
                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<JsonElement> call, Throwable t) {
                            result = t.getMessage();
                            Toast.makeText(ChangePhoneActivity.this, "There was Some Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(ChangePhoneActivity.this,"Incorrect OTP",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}