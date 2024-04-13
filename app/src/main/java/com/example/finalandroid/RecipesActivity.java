package com.example.finalandroid;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecipesActivity extends AppCompatActivity {

    RecyclerView recyclerViewRecipes;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        // Initialize DBHelper
        dbHelper = new DBHelper(this);

        // Initialize RecyclerView
        recyclerViewRecipes = findViewById(R.id.recyclerViewRecipes);
        recyclerViewRecipes.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve all recipes from the database
        List<Recipe> recipes = dbHelper.getAllRecipes();

        // Create a custom adapter to display both name and description
       // RecipeAdapter adapter = new RecipeAdapter(this, recipes);

        // Set the custom adapter to the RecyclerView
       // recyclerViewRecipes.setAdapter(adapter);
    }
}
