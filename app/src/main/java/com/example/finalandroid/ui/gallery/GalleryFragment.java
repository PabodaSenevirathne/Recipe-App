package com.example.finalandroid.ui.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalandroid.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    private RecyclerView recyclerView;
    private MealAdapter mealAdapter;
    private List<Meal> mealList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        recyclerView = root.findViewById(R.id.recyclerView);

        // Ensure the layout inflated correctly
        if (root == null) {
            Log.e("GalleryFragment", "Fragment layout inflation failed!");
            return null;
        }

        // Ensure the RecyclerView is found
        if (recyclerView == null) {
            Log.e("GalleryFragment", "RecyclerView not found in layout!");
            return null;
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



        // Initialize mealList and mealAdapter
        mealList = new ArrayList<>();
        mealAdapter = new MealAdapter(mealList);
        recyclerView.setAdapter(mealAdapter);

        // Make the API request
        makeApiRequest();

        return root;
    }

    private void makeApiRequest() {
        new Thread(() -> {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL("https://www.themealdb.com/api/json/v1/1/random.php");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                // Update the UI with the API response
                updateUI(response.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void updateUI(String responseData) {
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            JSONArray mealsArray = jsonObject.getJSONArray("meals");

            for (int i = 0; i < mealsArray.length(); i++) {
                JSONObject mealObject = mealsArray.getJSONObject(i);
                String mealName = mealObject.getString("strMeal");
                String mealCategory = mealObject.getString("strCategory");
                String mealInstructions = mealObject.getString("strInstructions");

                Meal meal = new Meal(mealName, mealCategory, mealInstructions);
                mealList.add(meal);
            }

            // Notify the adapter that the data set has changed
            requireActivity().runOnUiThread(() -> mealAdapter.notifyDataSetChanged());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
