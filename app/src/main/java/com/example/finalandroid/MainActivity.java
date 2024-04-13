package com.example.finalandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText username, password, repassword;
    Button signup, signin;

    DBHelper DB;
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String LOGGED_IN_USER = "loggedInUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        repassword = findViewById(R.id.repassword);
        signup = findViewById(R.id.btnSignUp);
        signin = findViewById(R.id.btnSignIn);
        DB = new DBHelper(this);
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        // Check if the user is already logged in
        String loggedInUser = sharedPreferences.getString(LOGGED_IN_USER, null);
        if (loggedInUser != null) {
            // If user is already logged in, redirect to HomeActivity
            Intent intent = new Intent(MainActivity.this, SubmitRecipe.class);
            startActivity(intent);
            finish(); // finish the current activity to prevent going back to it
        }

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();

                if (user.equals("") || pass.equals("") || repass.equals("")) {
                    Toast.makeText(MainActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (pass.equals(repass)) {
                        boolean checkuser = DB.checkUsernamePassword(user, pass);

                        if (!checkuser) {
                            boolean insert = DB.insertData(user, pass);
                            if (insert) {
                                Toast.makeText(MainActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                // Save logged-in user to SharedPreferences
                                saveLoggedInUser(user);
                                // Redirect to HomeActivity
                                Intent intent = new Intent(getApplicationContext(), SubmitRecipe.class);
                                startActivity(intent);
                                finish(); // finish the current activity to prevent going back to it
                            } else {
                                Toast.makeText(MainActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "User Already Exists! Please Sign in", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Password not matching", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Check if the user is already logged in
        String loggedInUser = sharedPreferences.getString(LOGGED_IN_USER, null);
        if (loggedInUser != null) {
            // If user is already logged in, do nothing when back button is pressed
            // Optionally, you can show a Toast indicating that pressing back is disabled
            Toast.makeText(this, "Back button disabled while logged in", Toast.LENGTH_SHORT).show();
        } else {
            // If user is not logged in, proceed with default back button behavior
            super.onBackPressed();
        }
    }

    private void saveLoggedInUser(String username) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOGGED_IN_USER, username);
        editor.apply();
    }
}