package com.example.recycle.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recycle.Fragments.ChatFragment;
import com.example.recycle.Fragments.ConnectionFragment;
import com.example.recycle.Fragments.DisposeFragment;
import com.example.recycle.Fragments.HomeFragment;
import com.example.recycle.Fragments.NotFoundFragment;
import com.example.recycle.Fragments.SearchFragment_DisposeCentre;
import com.example.recycle.Fragments.SearchFragment_Product;
import com.example.recycle.Fragments.SellFragment;
import com.example.recycle.Fragments.SettingsFragment;
import com.example.recycle.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    SearchView searchView;
    TextView counter;
    int pending_notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (!isNetworkAvailable()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(0)).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String fragment_name;
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        fragment_name = String.valueOf(fragment);
        outState.putString("Fragment Name", fragment_name);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String fragment_name = savedInstanceState.getString("Fragment Name");
        if(fragment_name.startsWith("HomeFragment") || fragment_name.startsWith("SearchFragment_Product"))
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        else if(fragment_name.startsWith("ChatFragment"))
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChatFragment()).commit();
        else if(fragment_name.startsWith("SellFragment"))
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SellFragment()).commit();
        else if(fragment_name.startsWith("DisposeFragment") || fragment_name.startsWith("SearchFragment_DisposeCentre"))
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DisposeFragment()).commit();
        else if(fragment_name.startsWith("SettingsFragment"))
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
        else if(fragment_name.startsWith("ConnectionFragment")) {
            if (!isNetworkAvailable()) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(0)).commit();
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(1)).commit();
            }
        }
        else
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotFoundFragment()).commit();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.notification)
//            Toast.makeText(this, "Clicked on Notification", Toast.LENGTH_SHORT).show();
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem search = menu.findItem(R.id.search);
        MenuItem notification = menu.findItem(R.id.notification);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        pending_notification = notificationManager.getActiveNotifications().length;
        if(pending_notification == 0)
            notification.setActionView(null);
        else{
            notification.setActionView(R.layout.notification_badge);
            View view = notification.getActionView();
            counter = view.findViewById(R.id.count);
            counter.setText(String.valueOf(pending_notification));
        }

        searchView = (SearchView) search.getActionView();
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
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    search.collapseActionView();
                    SharedPreferences sp = getSharedPreferences("Current Fragment", Context.MODE_PRIVATE);
                    String prev_frag = sp.getString("Name", "");
                    if (prev_frag.equals("HomeFragment"))
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                    else if (prev_frag.equals("DisposeFragment"))
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DisposeFragment()).commit();
                }
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void performSearch(String query) {
        if (!isNetworkAvailable()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(0)).commit();
        } else {
            SharedPreferences sp = getSharedPreferences("Current Fragment", Context.MODE_PRIVATE);
            String prev_frag = sp.getString("Name", "");
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (prev_frag.equals("HomeFragment")) {
                SearchFragment_Product searchFragmentProduct = SearchFragment_Product.newInstance();
                Bundle bundle = new Bundle();
                bundle.putString("Search Word", query);
                searchFragmentProduct.setArguments(bundle);
                fragmentTransaction.add(R.id.fragment_container, searchFragmentProduct).commit();
            } else if (prev_frag.equals("DisposeFragment")) {
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
                searchView.setIconified(true);
                searchView.setIconified(true);
                Fragment selectedFragment = null;
                SharedPreferences sp = getSharedPreferences("Credentials", Context.MODE_PRIVATE);
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        if (!isNetworkAvailable())
                            selectedFragment = new ConnectionFragment(0);
                        else
                            selectedFragment = new HomeFragment();
                        break;
                    case R.id.nav_chat:
                        if (!isNetworkAvailable())
                            selectedFragment = new ConnectionFragment(0);
                        else {
                            if (sp.contains("User ID")) {
                                selectedFragment = new ChatFragment();
                            } else {
                                Intent i = new Intent(MainActivity.this, RegisterActivity.class);
                                startActivity(i);
                            }
                        }
                        break;
                    case R.id.nav_sell:
                        if (!isNetworkAvailable())
                            selectedFragment = new ConnectionFragment(0);
                        else {
                            if (sp.contains("User ID")) {
                                selectedFragment = new SellFragment();
                            } else {
                                Intent i = new Intent(MainActivity.this, RegisterActivity.class);
                                startActivity(i);
                            }
                        }
                        break;
                    case R.id.nav_dispose:
                        if (!isNetworkAvailable())
                            selectedFragment = new ConnectionFragment(0);
                        else
                            selectedFragment = new DisposeFragment();
                        break;
                    case R.id.nav_settings:
                        if (sp.contains("User ID")) {
                            selectedFragment = new SettingsFragment();
                        } else {
                            Intent i = new Intent(MainActivity.this, RegisterActivity.class);
                            startActivity(i);
                        }
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                return true;
            }
        };
}