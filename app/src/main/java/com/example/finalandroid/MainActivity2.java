package com.example.finalandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.finalandroid.databinding.ActivityMain2Binding;
import com.google.android.material.navigation.NavigationView;

public class MainActivity2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMain2Binding binding;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Menu navMenu;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain2.toolbar);

        drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Define top-level destinations for navigation
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main2);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Setup ActionBarDrawerToggle
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, binding.appBarMain2.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // Set navigation item click listener
        navigationView.setNavigationItemSelectedListener(this);

        // Show the hamburger menu icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set toolbar navigation click listener to handle navigation drawer opening/closing
        binding.appBarMain2.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        // Get navigation menu
        navMenu = navigationView.getMenu();

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE);

        // Update navigation menu based on login status
        updateNavigationMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity2, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main2);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_login) {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main2);
            navController.navigate(R.id.nav_slideshow);
        } else if (id == R.id.nav_logout) {
            // Logout action
            clearLoggedInUser();
            Toast.makeText(this, "You have been logged out!", Toast.LENGTH_SHORT).show();
            // Redirect to MainActivity
            Intent intent = new Intent(this, MainActivity2.class);
            startActivity(intent);
            finish();
        }
        if (id == R.id.nav_gallery) {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main2);
            navController.navigate(R.id.nav_gallery);
        } else if (id == R.id.nav_my_recipes) {
            // Check if user is logged in before navigating to My Recipes
            String loggedInUserId = sharedPreferences.getString(MainActivity.LOGGED_IN_USER, "");
            if (!loggedInUserId.isEmpty()) {
                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main2);
                navController.navigate(R.id.nav_my_recipes);
            } else {
                // Prompt user to log in
                // You can show a toast or a dialog here
            }
        } else if (id == R.id.nav_home) {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main2);
            navController.navigate(R.id.nav_home);
        }
        // Close the navigation drawer after the item is selected
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateNavigationMenu() {
        String loggedInUserId = sharedPreferences.getString(MainActivity.LOGGED_IN_USER, "");

        MenuItem loginItem = navMenu.findItem(R.id.nav_login);
        MenuItem logoutItem = navMenu.findItem(R.id.nav_logout);
        MenuItem myRecipesItem = navMenu.findItem(R.id.nav_my_recipes);

        if (loggedInUserId.isEmpty()) {
            // User is not logged in, show the "Login" item and hide the "Logout" item and "My Recipes" item
            loginItem.setVisible(true);
            logoutItem.setVisible(false);
            myRecipesItem.setVisible(false);
        } else {
            // User is logged in, hide the "Login" item and show the "Logout" item and "My Recipes" item
            loginItem.setVisible(false);
            logoutItem.setVisible(true);
            myRecipesItem.setVisible(true);
        }
    }

    private void clearLoggedInUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(MainActivity.LOGGED_IN_USER);
        editor.apply();
    }
}
