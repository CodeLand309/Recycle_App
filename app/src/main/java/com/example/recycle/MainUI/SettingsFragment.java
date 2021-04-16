package com.example.recycle.MainUI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.recycle.R;
import com.example.recycle.RetrofitFolder.RestApiInterface;
import com.example.recycle.RetrofitFolder.RestClient;
import com.example.recycle.SignUPActivity;
import com.example.recycle.SubActivity.EditProduct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends PreferenceFragmentCompat {

    private Preference report, profile, delete, logout;
    private RestApiInterface restApiInterface;
    private String name, phone, product_id, Result;
    private int id;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        report = findPreference("report_key");
        profile = findPreference("change_pro_key");
        delete = findPreference("delete_key");
        logout = findPreference("log_out_key");
        SharedPreferences sp = getContext().getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        id=sp.getInt("User ID", 0);
        name=sp.getString("Name", "");
        phone=sp.getString("Phone Number", "");
        SharedPreferences.Editor editor = sp.edit();
//        editor.putString("Name", "Kanna");
//        editor.commit();
        profile.setTitle(name);
        report.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            public boolean onPreferenceClick(Preference preference) {
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.envyandroid.com"));
//                startActivity(intent);
//                return false;

                Intent i = new Intent(Intent.ACTION_SEND);
                //i.setType("text/plain"); //use this line for testing in the emulator
                i.setType("message/rfc822"); // use from live device
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"test@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "subject goes here");
                i.putExtra(Intent.EXTRA_TEXT, "body goes here");
                startActivity(Intent.createChooser(i, "Select email application."));
                return false;
            }
        });

        delete.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Delete");
                builder.setMessage("Are You Sure to Delete Account?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
        //                Intent intent = new Intent(getContext(), SignUPActivity.class);
        //                startActivity(intent);
        //                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //                firebase.auth().currentUser.unlink(firebase.auth.PhoneAuthProvider.PROVIDER_ID);

        //                if (user != null) {
        //                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
        //                        @Override
        //                        public void onComplete(@NonNull Task<Void> task) {
        //                            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
        //                                @Override
        //                                public void onComplete(@NonNull Task<Void> task) {
        //                                    if (task.isSuccessful()) {
                        FirebaseAuth.getInstance().getCurrentUser().unlink(PhoneAuthProvider.PROVIDER_ID);
                        restApiInterface = RestClient.getRetrofit().create(RestApiInterface.class);

                        Call<JsonElement> call = restApiInterface.deleteUser(id, phone);
                        call.enqueue(new Callback<JsonElement>() {
                            @Override
                            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                                if (!response.isSuccessful()) {
                                    Result = "Code: " + response.code();
                                    Toast.makeText(getContext(), Result, Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                JsonObject jsonObject = response.body().getAsJsonObject();
                                String content = jsonObject.get("status").getAsString();
                                Toast.makeText(getContext(), content, Toast.LENGTH_SHORT).show();
                                editor.remove("Name");
                                editor.remove("User ID");
                                editor.remove("Address");
                                editor.remove("Phone Number");
                                editor.remove("Gender");
                                editor.remove("Age");
                                editor.remove("Image");
                                editor.putInt("Log in Status", 0);
                                editor.apply();

                                Intent i = new Intent(getContext(), SignUPActivity.class);
                                startActivity(i);
                                getActivity().finish();
                                //                                    }
        //                                }
        //                            });
        //                        }
        //                    });
        //                }
                            }

                            @Override
                            public void onFailure(Call<JsonElement> call, Throwable t) {
                                Result = t.getMessage();
                                Toast.makeText(getContext(), Result, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

                return false;
                    // Prompt the user to re-provide their sign-in credentials
//                if (user != null) {
//                    user.reauthenticate(credential)
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    user.delete()
//                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    if (task.isSuccessful()) {
//                                                        Log.d("TAG", "User account deleted.");
//                                                        startActivity(new Intent(DeleteUser.this, StartActivity.class));
//                                                        Toast.makeText(DeleteUser.this, "Deleted User Successfully,", Toast.LENGTH_LONG).show();
//                                                    }
//                                                }
//                                            });
//                                }
//                            });
//                }
            }
        });
        logout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Log Out");
                builder.setMessage("Are you Sure to Log Out?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signOut();
        //                editor.putString("Name", "");
        //                editor.putInt("User ID", 0);
        //                editor.putString("Address", "");
        //                editor.putString("Gender", "");
        //                editor.putInt("Age", 0);
        //                editor.putString("Image", "");
                        editor.remove("Phone Number");
                        editor.putInt("Log in Status", 1);
                        editor.apply();
                        Intent intent = new Intent(getContext(), SignUPActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

                return false;
            }
        });


//    private ImageView Profile;
//    Button button;
//    private static final int IMAGE_CODE = 1;
//    Uri imageUri;

//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_connection, container, false);
//        Profile = view.findViewById(R.id.profile);
//        button = view.findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent gallery = new Intent();
//                gallery.setType("image/*");
//                gallery.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(gallery, IMAGE_CODE);
//            }
//        });
//        return view;
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == IMAGE_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            imageUri = data.getData();
//            Profile.setImageURI(imageUri);
//        }
//    }
    }

}

