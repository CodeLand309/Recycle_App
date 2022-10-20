package com.example.recycle.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.example.recycle.Fragments.CallbackInterface;
import com.example.recycle.Pagination.ProductDataSource;
import com.example.recycle.R;
import com.example.recycle.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements CallbackInterface {

    SearchView searchView;
    TextView counter;
    int pending_notification;
    ActivityMainBinding activityMainBinding = null;
    NavController navController = null;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        ActivityMainBinding binding = activityMainBinding;
        setContentView(binding.getRoot());

//        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
//        bottomNav.setOnNavigationItemSelectedListener(navListener);

        SharedPreferences sp = getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        id = sp.getInt("User ID",0);

        BottomNavigationView bottomNav = binding.bottomNavigation;
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationUI.setupWithNavController(bottomNav, navController);


        //setSupportActionBar(toolbar);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                if (navDestination.getId()!=R.id.nav_home && navDestination.getId()!=R.id.nav_dispose && !sp.contains("User ID")) {
                    Intent i = new Intent(MainActivity.this, SignUPActivity.class);
                    startActivity(i);
                    navController.popBackStack();
                }else {
                    if (navDestination.getId() == R.id.nav_settings) {
                        showHideErrorMessages(-1);
                    } else {
                        if (!isNetworkAvailable()) {
                            showHideErrorMessages(0);
                        }

                    }
                }
            }
        });

        //        if (!isNetworkAvailable()) {
//            //binding.fragmentContainer.setVisibility(View.INVISIBLE);
//            if(!(navController.getCurrentDestination().getId()==R.id.nav_settings))
//                ModifyFragment(0);
////            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(0)).commit();
//        }

//        else {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
//      }





    }

//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        String fragment_name;
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//        fragment_name = String.valueOf(fragment);
//        outState.putString("Fragment Name", fragment_name);
//    }

//    @Override
//    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        String fragment_name = savedInstanceState.getString("Fragment Name");
//        if(fragment_name.startsWith("HomeFragment") || fragment_name.startsWith("SearchFragment_Product"))
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
//        else if(fragment_name.startsWith("ChatFragment"))
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChatFragment()).commit();
//        else if(fragment_name.startsWith("SellFragment"))
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SellFragment()).commit();
//        else if(fragment_name.startsWith("DisposeFragment") || fragment_name.startsWith("SearchFragment_DisposeCentre"))
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DisposeFragment()).commit();
//        else if(fragment_name.startsWith("SettingsFragment"))
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
//        else if(fragment_name.startsWith("ConnectionFragment")) {
//            if (!isNetworkAvailable()) {
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(0)).commit();
//            } else {
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(1)).commit();
//            }
//        }
//        else
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotFoundFragment()).commit();
//    }

    @Override
    public boolean isNetworkAvailable() {
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
//        MenuItem search = menu.findItem(R.id.search);
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

//        searchView = (SearchView) search.getActionView();
//        searchView.setQueryHint("Search Here");
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                performSearch(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if (!b) {
//                    search.collapseActionView();
//                    SharedPreferences sp = getSharedPreferences("Current Fragment", Context.MODE_PRIVATE);
//                    String prev_frag = sp.getString("Name", "");
//                    if (prev_frag.equals("HomeFragment"))
//                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
//                    else if (prev_frag.equals("DisposeFragment"))
//                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DisposeFragment()).commit();
//                }
//            }
//        });
        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        return super.onPrepareOptionsMenu(menu);
//
//    }

    private void performSearch(String query) {
//        if (!isNetworkAvailable()) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConnectionFragment(0)).commit();
//        } else {
//            SharedPreferences sp = getSharedPreferences("Current Fragment", Context.MODE_PRIVATE);
//            String prev_frag = sp.getString("Name", "");
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            if (prev_frag.equals("HomeFragment")) {
//                SearchFragment_Product searchFragmentProduct = SearchFragment_Product.newInstance();
//                Bundle bundle = new Bundle();
//                bundle.putString("Search Word", query);
//                searchFragmentProduct.setArguments(bundle);
//                fragmentTransaction.add(R.id.fragment_container, searchFragmentProduct).commit();
//            } else if (prev_frag.equals("DisposeFragment")) {
//                SearchFragment_DisposeCentre searchFragment_disposeCentre = SearchFragment_DisposeCentre.newInstance();
//                Bundle bundle = new Bundle();
//                bundle.putString("Search Word", query);
//                searchFragment_disposeCentre.setArguments(bundle);
//                fragmentTransaction.add(R.id.fragment_container, searchFragment_disposeCentre).commit();
//            }
//        }

//        if(isNetworkAvailable()) {
//            if (isAnyErrorMessageShowing())
//                showHideErrorMessages(-1);
//            if(navController.getCurrentDestination().getId()==R.id.fragment_home){
//                Bundle bundle = new Bundle();
//                bundle.putString("Search Word", query);
//                NavArgument argument = new NavArgument.Builder().build();
//                argument.putDefaultValue("Search Bundle", bundle);
//                //navController.getCurrentDestination().addArgument();
//                navController.getCurrentDestination().addInDefaultArgs(bundle);
//                SearchFragment_Product searchFragmentProduct = SearchFragment_Product.newInstance();

//                searchFragmentProduct.setArguments(bundle);
//            }
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(networkBroadcast);
    }

    //    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
//        new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                searchView.setIconified(true);
//                searchView.setIconified(true);
//                Fragment selectedFragment = null;
//                SharedPreferences sp = getSharedPreferences("Credentials", Context.MODE_PRIVATE);
//                switch (item.getItemId()) {
//                    case R.id.nav_home:
//                        if (!isNetworkAvailable())
//                            selectedFragment = new ConnectionFragment(0);
//                        else
//                            selectedFragment = new HomeFragment();
//                        break;
//                    case R.id.nav_chat:
//                        if (!isNetworkAvailable())
//                            selectedFragment = new ConnectionFragment(0);
//                        else {
//                            if (sp.contains("User ID")) {
//                                selectedFragment = new ChatFragment();
//                            } else {
//                                Intent i = new Intent(MainActivity.this, RegisterActivity.class);
//                                startActivity(i);
//                            }
//                        }
//                        break;
//                    case R.id.nav_sell:
//                        if (!isNetworkAvailable())
//                            selectedFragment = new ConnectionFragment(0);
//                        else {
//                            if (sp.contains("User ID")) {
//                                selectedFragment = new SellFragment();
//                            } else {
//                                Intent i = new Intent(MainActivity.this, RegisterActivity.class);
//                                startActivity(i);
//                            }
//                        }
//                        break;
//                    case R.id.nav_dispose:
//                        if (!isNetworkAvailable())
//                            selectedFragment = new ConnectionFragment(0);
//                        else
//                            selectedFragment = new DisposeFragment();
//                        break;
//                    case R.id.nav_settings:
//                        if (sp.contains("User ID")) {
//                            selectedFragment = new SettingsFragment();
//                        } else {
//                            Intent i = new Intent(MainActivity.this, RegisterActivity.class);
//                            startActivity(i);
//                        }
//                        break;
//                }
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
//                return true;
//            }
//        };

    public void showProgress(){
        activityMainBinding.progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress(){
        activityMainBinding.progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean isAnyErrorMessageShowing(){
        if(activityMainBinding.connContainer.getVisibility()==View.VISIBLE)
            return true;
        return false;
    }

    @Override
    public void showHideErrorMessages(int action){
        if (action == -1 || action==2 || action==4){
            activityMainBinding.connContainer.setVisibility(View.GONE);
        }
        else {
            activityMainBinding.connContainer.setVisibility(View.VISIBLE);
            if(action == 0) {
                activityMainBinding.no.setText(R.string.NoInternetConnection);
                activityMainBinding.errorImage.setImageResource(R.drawable.no_connection);
                activityMainBinding.errorAction.setText(R.string.NoInternetAction);
            }else if (action == 1) {
                activityMainBinding.no.setText(R.string.ServerError);
                activityMainBinding.errorImage.setImageResource(R.drawable.server_error);
                activityMainBinding.errorAction.setText(R.string.ServerErrorHint);
            } else if(action==3) {
                activityMainBinding.no.setText(R.string.NoSearchResult);
                activityMainBinding.errorImage.setImageResource(R.drawable.not_found);
                activityMainBinding.errorAction.setText(R.string.NoSearchHint);
            }
        }
    }

    @Override
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void showSnackbar(View view, String msg) {
        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(getColor(R.color.customGrey));
        snackbar.setTextColor(getColor(R.color.customWhite));
        View snackView = snackbar.getView();
//        if(navController.getCurrentDestination().getId()==R.id.fragment_dispose)
//            snackbar.setAnchorView();
//        else
            snackbar.setAnchorView(activityMainBinding.bottomNavigation);
        TextView snackText = snackView.findViewById(com.google.android.material.R.id.snackbar_text);
        snackText.setAllCaps(true);
//        snackText.setTextAppearance(R.style.TextAppearance_AppCompat_Caption);
        snackText.setTextSize(15);
        snackbar.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}