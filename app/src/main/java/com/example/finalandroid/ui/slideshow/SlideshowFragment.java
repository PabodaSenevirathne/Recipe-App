package com.example.finalandroid.ui.slideshow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.finalandroid.DBHelper;
import com.example.finalandroid.SubmitRecipe;
import com.example.finalandroid.R;
import com.example.finalandroid.RegistrationActivity;
import com.example.finalandroid.databinding.FragmentSlideshowBinding;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private EditText usernameEditText, passwordEditText;
    private Button signInButton, signUpButton; // Add signUpButton
    private DBHelper dbHelper;
    private SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String LOGGED_IN_USER = "loggedInUser";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment using data binding
        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        usernameEditText = root.findViewById(R.id.username1);
        passwordEditText = root.findViewById(R.id.password1);
        signInButton = root.findViewById(R.id.btnSignIn1);
        signUpButton = root.findViewById(R.id.btnSignUp1); // Find signUpButton
        dbHelper = new DBHelper(requireContext());
        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the RegistrationActivity
                startActivity(new Intent(requireActivity(), RegistrationActivity.class));
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = usernameEditText.getText().toString();
                String pass = passwordEditText.getText().toString();

                if (user.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(requireContext(), "Please enter all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean checkUserPass = dbHelper.checkUsernamePassword(user, pass);
                    if (checkUserPass) {
                        saveLoggedInUser(user);
                        Toast.makeText(requireContext(), "Sign in successful", Toast.LENGTH_SHORT).show();
                        startActivity(SubmitRecipe.class);
                    } else {
                        Toast.makeText(requireContext(), "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return root;
    }

    private void saveLoggedInUser(String username) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOGGED_IN_USER, username);
        editor.apply();
    }

    private void startActivity(Class<?> cls) {
        startActivity(new Intent(requireActivity(), cls));
        requireActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Release the binding when the fragment is destroyed
        binding = null;
    }
}
