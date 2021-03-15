package com.example.recycle.MainUI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.recycle.R;
import com.example.recycle.RetrofitFolder.RestApiInterface;
import com.example.recycle.SignUPActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends PreferenceFragmentCompat {

    private Preference report, profile, delete, logout;
    private RestApiInterface restApiInterface;
    private String name;
    private int id;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        report = (Preference) findPreference("report_key");
        profile = findPreference("change_pro_key");
        delete = findPreference("delete_key");
        logout = findPreference("log_out_key");
        SharedPreferences sp = getContext().getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        id=sp.getInt("User ID", 0);
        name=sp.getString("Name", "");
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
                Intent intent = new Intent(getContext(), SignUPActivity.class);
                startActivity(intent);
//                Call<void> call = restApiInterface.deleteUser(id);
//                call.enqueue(new Callback<void>() {
//                    @Override
//                    public void onResponse(@NonNull Call<void> call, @NonNull Response<void> response) {
//                        ArrayList<ProductsItem> products = response.body().get(1).getItems();
//                        mAdapter = new ProductsAdapter(products, getContext());
//                        Log.d("TAG", "Reached Here3");
//                        mRecyclerView.setAdapter(mAdapter);
//                        Log.d("Tag", String.valueOf(response.body()));
//                        Toast.makeText(getContext(), "First Page Loading", Toast.LENGTH_SHORT).show();
//                        progressBar.setVisibility(View.GONE);
//                    }
//                    @Override
//                    public void onFailure(Call<void> call, Throwable t) {
//                        Toast.makeText(getContext(), "Cannot Access Server", Toast.LENGTH_SHORT).show();
//                    }
//                });
                return false;
            }
        });
        logout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getContext(), SignUPActivity.class);
                startActivity(intent);
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

