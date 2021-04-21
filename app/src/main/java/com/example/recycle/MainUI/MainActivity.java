package com.example.recycle.MainUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.recycle.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(!isNetworkAvailable()){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(0)).commit();
        }
        else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.notification)
            Toast.makeText(this, "Clicked on Notification", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Here");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                SharedPreferences sp = getSharedPreferences("Current Fragment", Context.MODE_PRIVATE);
                String prev_frag = sp.getString("Name", "");
                if(prev_frag.equals("HomeFragment"))
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                else if(prev_frag.equals("DisposeFragment"))
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DisposeFragment()).commit();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void performSearch(String query) {
        if(!isNetworkAvailable()){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(0)).commit();
        }
        else {
//                    Fragment currentFragment = (Fragment)getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//                    String fragment_name = String.valueOf(currentFragment);
            SharedPreferences sp = getSharedPreferences("Current Fragment", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sp.edit();
            String prev_frag = sp.getString("Name", "");
//                    Log.d("Hello", String.valueOf(currentFragment));
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if(prev_frag.equals("HomeFragment")) {
                SearchFragment_Product searchFragmentProduct = SearchFragment_Product.newInstance();
                Bundle bundle = new Bundle();
                bundle.putString("Search Word", query);
                searchFragmentProduct.setArguments(bundle);
                fragmentTransaction.add(R.id.fragment_container, searchFragmentProduct).commit();
            }
            else if(prev_frag.equals("DisposeFragment")){
                SearchFragment_DisposeCentre searchFragment_disposeCentre = SearchFragment_DisposeCentre.newInstance();
                Bundle bundle = new Bundle();
                bundle.putString("Search Word", query);
                searchFragment_disposeCentre.setArguments(bundle);
                fragmentTransaction.add(R.id.fragment_container, searchFragment_disposeCentre).commit();
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            if(!isNetworkAvailable())
                                selectedFragment = new ConnectionFragment(0);
                            else
                                selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_chat:
                            if(!isNetworkAvailable())
                                selectedFragment = new ConnectionFragment(0);
                            else
                                selectedFragment = new ChatFragment();
                            break;
                        case R.id.nav_sell:
                            if(!isNetworkAvailable())
                                selectedFragment = new ConnectionFragment(0);
                            else
                                selectedFragment = new SellFragment();
                            break;
                        case R.id.nav_dispose:
                            if(!isNetworkAvailable())
                                selectedFragment = new ConnectionFragment(0);
                            else
                                selectedFragment = new DisposeFragment();
                            break;
                        case R.id.nav_settings:
//                            if(!isNetworkAvailable())
//                                selectedFragment = new ConnectionFragment();
//                            else
                                selectedFragment = new SettingsFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };

//    @Override
//    public void onActionPerformed(Bundle bundle) {
//        int actionPerformed = bundle.getInt(FragmentActionListener.ACTION_KEY);
//        switch (actionPerformed){
//            case FragmentActionListener.ACTION_VALUE_COUNTRY_SELECTED: addCountryDescriptionFragment(bundle); break;
//        }
//    }
//    }
}