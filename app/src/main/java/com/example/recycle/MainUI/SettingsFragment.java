package com.example.recycle.MainUI;

import android.content.Intent;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.recycle.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        Preference report = (Preference) findPreference("report_key");
        report.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            public boolean onPreferenceClick(Preference preference) {
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.envyandroid.com"));
//                startActivity(intent);
//                return false;

                    Intent i = new Intent(Intent.ACTION_SEND);
                    //i.setType("text/plain"); //use this line for testing in the emulator
                    i.setType("message/rfc822") ; // use from live device
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{"test@gmail.com"});
                    i.putExtra(Intent.EXTRA_SUBJECT,"subject goes here");
                    i.putExtra(Intent.EXTRA_TEXT,"body goes here");
                    startActivity(Intent.createChooser(i, "Select email application."));
                return false;
            }


        });
    }



//    private ImageView Profile;
//    Button button;
//    private static final int IMAGE_CODE = 1;
//    Uri imageUri;

//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_settings, container, false);
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

