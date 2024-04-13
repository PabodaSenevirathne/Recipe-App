package com.example.finalandroid.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalandroid.DBHelper;
import com.example.finalandroid.R;
import com.example.finalandroid.Recipe;
import com.example.finalandroid.RecipeRecyclerViewAdapter;

import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewRecipes;
    private DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerViewRecipes = view.findViewById(R.id.recyclerViewRecipes);
        dbHelper = new DBHelper(getActivity());

        // Retrieve all recipes from the database
        List<Recipe> recipes = dbHelper.getAllRecipes();

        // Create a RecyclerView adapter
//        RecipeRecyclerViewAdapter adapter = new RecipeRecyclerViewAdapter(recipes);

        // Set the adapter to the RecyclerView
//        recyclerViewRecipes.setAdapter(adapter);
        recyclerViewRecipes.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }
}