package com.example.finalandroid;

import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button btnlogin, btnSignUp; // Add button for Sign up

    DBHelper DB;

    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String LOGGED_IN_USER = "loggedInUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username1);
        password = findViewById(R.id.password1);
        btnlogin = findViewById(R.id.btnSignIn1);
        btnSignUp = findViewById(R.id.btnSignUp1); // Initialize the Sign up button
        DB = new DBHelper(this);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                if (user.equals("") || pass.equals("")) {
                    Toast.makeText(LoginActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean checkuserpass = DB.checkUsernamePassword(user, pass);
                    if (checkuserpass) {
                        // Save logged-in user to SharedPreferences
                        saveLoggedInUser(user);
                        Toast.makeText(LoginActivity.this, "Sign in successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), SubmitRecipe.class);
                        startActivity(intent);
                        finish(); // Finish LoginActivity to prevent going back to it
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to the registration activity when Sign up button is clicked
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the sign-in page by overriding the back button press
        // Optional: Show a Toast indicating that pressing back is disabled
        Toast.makeText(this, "Back button disabled", Toast.LENGTH_SHORT).show();
        // Call super.onBackPressed() to maintain default back button behavior when necessary
        super.onBackPressed();
    }

    private void saveLoggedInUser(String username) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOGGED_IN_USER, username);
        editor.apply();
    }
}
