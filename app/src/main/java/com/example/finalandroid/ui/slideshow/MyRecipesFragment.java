package com.example.finalandroid.ui.slideshow;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalandroid.DBHelper;
import com.example.finalandroid.R;
import com.example.finalandroid.Recipe;
import com.example.finalandroid.RecipeAdapter;
import com.example.finalandroid.SubmitRecipe;

import java.util.List;

public class MyRecipesFragment extends Fragment implements RecipeAdapter.OnRecipeClickListener {

    private RecyclerView recyclerView;
    private RecipeAdapter adapter;
    private DBHelper dbHelper;
    private String loggedInUserId; // Declare loggedInUserId as a member variable

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String LOGGED_IN_USER = "loggedInUser";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_recipes, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewRecipes);
        Button buttonAddRecipe = view.findViewById(R.id.buttonAddRecipe);

        dbHelper = new DBHelper(requireContext());

        // Get the logged-in user's ID
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        loggedInUserId = sharedPreferences.getString(LOGGED_IN_USER, ""); // Assign value to loggedInUserId

        if (loggedInUserId.isEmpty()) {
            // If user is not logged in, disable the button and show a toast
            buttonAddRecipe.setEnabled(false);
            showToast("Please login to enter Recipe");
        } else {
            // Fetch recipes associated with the logged-in user
            List<Recipe> recipes = dbHelper.getRecipesByUserId(loggedInUserId);

            // Initialize and set up the RecyclerView
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            adapter = new RecipeAdapter(requireContext(), recipes, this);
            recyclerView.setAdapter(adapter);

            buttonAddRecipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Navigate to the Add Recipe screen
                    Intent intent = new Intent(requireContext(), SubmitRecipe.class);
                    startActivity(intent);
                }
            });
        }

        return view;
    }

    @Override
    public void onEditClick(Recipe recipe) {
        // Handle edit action here
        // For example, navigate to the edit screen with recipe details
    }

    @Override
    public void onDeleteClick(Recipe recipe) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this recipe?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Call DBHelper method to delete the recipe from the database
                dbHelper.deleteRecipe(recipe.getId());
                // Update the dataset used by the adapter
                List<Recipe> updatedRecipes = dbHelper.getRecipesByUserId(loggedInUserId);
                adapter.updateData(updatedRecipes);
                showToast("Recipe deleted successfully");
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}

