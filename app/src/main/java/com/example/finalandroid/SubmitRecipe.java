package com.example.finalandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class SubmitRecipe extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String LOGGED_IN_USER = "loggedInUser";

    EditText editTextRecipeName, editTextRecipeDescription;
    Button btnLogout, btnSubmitRecipe;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_recipe);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        // Initialize DBHelper
        dbHelper = new DBHelper(this);

        // Retrieve logged-in user from SharedPreferences
        String loggedInUser = getLoggedInUser();
        if (loggedInUser != null) {
            Toast.makeText(this, "Logged in user: " + loggedInUser, Toast.LENGTH_SHORT).show();
        }

        // Initialize views
        editTextRecipeName = findViewById(R.id.editTextRecipeName);
        editTextRecipeDescription = findViewById(R.id.editTextRecipeDescription);
        btnLogout = findViewById(R.id.btnLogout);
        btnSubmitRecipe = findViewById(R.id.btnSubmitRecipe);

        // Set onClickListener for logout button
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearLoggedInUser();
                Toast.makeText(SubmitRecipe.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SubmitRecipe.this, MainActivity2.class);
                startActivity(intent);
                finish(); // Finish current activity to prevent going back to it with back button
            }
        });

        // Set onClickListener for submit recipe button
        btnSubmitRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String recipeName = editTextRecipeName.getText().toString().trim();
                String recipeDescription = editTextRecipeDescription.getText().toString().trim();
                String userId = getLoggedInUser(); // Get user ID of logged-in user

                if (userId != null && !recipeName.isEmpty() && !recipeDescription.isEmpty()) {
                    // Insert recipe into database with user ID
                    boolean success = dbHelper.insertRecipe(userId, recipeName, recipeDescription);
                    if (success) {
                        Toast.makeText(SubmitRecipe.this, "Recipe submitted successfully", Toast.LENGTH_SHORT).show();
                        // Clear EditText fields after submission
                        editTextRecipeName.setText("");
                        editTextRecipeDescription.setText("");

                        startActivity(new Intent(SubmitRecipe.this, MainActivity2.class));
                    } else {
                        Toast.makeText(SubmitRecipe.this, "Failed to submit recipe", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SubmitRecipe.this, "Please enter both recipe name and description", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getLoggedInUser() {
        return sharedPreferences.getString(LOGGED_IN_USER, null);
    }

    private void clearLoggedInUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(LOGGED_IN_USER);
        editor.apply();
    }
}
