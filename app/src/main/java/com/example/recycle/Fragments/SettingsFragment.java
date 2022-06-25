package com.example.recycle.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.recycle.Activities.MainActivity;
import com.example.recycle.R;
import com.example.recycle.Network.RestApiInterface;
import com.example.recycle.Network.RestClient;
import com.example.recycle.Activities.SignUPActivity;
import com.example.recycle.SubActivity.ChangePhoneActivity;
import com.example.recycle.SubActivity.ChangeProfile;
import com.example.recycle.SubActivity.History;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  SettingsFragment extends PreferenceFragmentCompat {

    private Preference report, profile, history, help, delete, logout, change_phone;
    private RestApiInterface restApiInterface;
    private String name, phone, Result;
    private int id;

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
        menu.removeItem(R.id.search);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        setHasOptionsMenu(true);
        report = findPreference("report_key");
        profile = findPreference("change_pro_key");
        delete = findPreference("delete_key");
        logout = findPreference("log_out_key");
        history = findPreference("history_key");
        help = findPreference("help_key");
        change_phone = findPreference("change_phone_key");
        SharedPreferences sp = getContext().getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        id=sp.getInt("User ID", 0);
        name=sp.getString("Name", "");
        phone=sp.getString("Phone Number", "");
        SharedPreferences.Editor editor = sp.edit();
        profile.setTitle(name);

        profile.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if(((MainActivity)getActivity()).isNetworkAvailable()){
                    Intent i = new Intent(getActivity(), ChangeProfile.class);
                    startActivity(i);
                }
                else{
                    ((MainActivity) getActivity()).showHideErrorMessages(0);
                    //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(0)).commit();
                }
                return false;
            }
        });

        change_phone.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Change Phone Number");
                builder.setMessage("Are You Sure to Change Phone Number?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(((MainActivity)getActivity()).isNetworkAvailable()) {
                            Intent intent = new Intent(getActivity(), ChangePhoneActivity.class);
                            startActivity(intent);
                        }else{
                            ((MainActivity) getActivity()).showHideErrorMessages(0);
                            //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(0)).commit();
                        }
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

        history.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if(((MainActivity)getActivity()).isNetworkAvailable()){
                    Intent i = new Intent(getActivity(), History.class);
                    startActivity(i);
                }else{
                    ((MainActivity) getActivity()).showHideErrorMessages(0);
                    //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(0)).commit();
                }
                return false;
            }
        });

        help.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent i = new Intent(Intent.ACTION_SEND);
                //i.setType("text/plain"); //use this line for testing in the emulator
                i.setType("message/rfc822"); // use from live device
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"CodeLand309@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Subject Goes here (Give a Title)");
                i.putExtra(Intent.EXTRA_TEXT, "Body Goes Here (Enter your doubts here)");
                startActivity(Intent.createChooser(i, "Select email application."));
                return false;
            }
        });

        report.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            public boolean onPreferenceClick(Preference preference) {
                Intent i = new Intent(Intent.ACTION_SEND);
                //i.setType("text/plain"); //use this line for testing in the emulator
                i.setType("message/rfc822"); // use from live device
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"CodeLand309@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Subject Goes here (Give a Title)");
                i.putExtra(Intent.EXTRA_TEXT, "Body Goes Here (Type the problem)");
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
                        if(((MainActivity)getActivity()).isNetworkAvailable()) {
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
                                }

                                @Override
                                public void onFailure(Call<JsonElement> call, Throwable t) {
                                    Result = t.getMessage();
                                    Toast.makeText(getContext(), Result, Toast.LENGTH_SHORT).show();
                                    ((MainActivity) getActivity()).showHideErrorMessages(1);
                                    //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(1)).commit();
                                }
                            });
                        }
                        else{
                            ((MainActivity) getActivity()).showHideErrorMessages(0);
                            //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(0)).commit();
                        }
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
        logout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Log Out");
                builder.setMessage("Are you Sure to Log Out?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(((MainActivity)getActivity()).isNetworkAvailable()) {
                            FirebaseAuth.getInstance().signOut();
                            editor.remove("Phone Number");
                            editor.putInt("Log in Status", 1);
                            editor.apply();
                            Intent intent = new Intent(getContext(), SignUPActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                        else{
                            ((MainActivity) getActivity()).showHideErrorMessages(0);
                            //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(0)).commit();
                        }
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
    }
//    public boolean isNetworkAvailable() {
//        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(getResources().getColor(R.color.customWhite));
    }
}

