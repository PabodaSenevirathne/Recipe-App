package com.example.finalandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
    private Context mContext;
    private List<Recipe> mRecipes;

    private OnRecipeClickListener mListener;

    public RecipeAdapter(Context context, List<Recipe> recipes, OnRecipeClickListener listener) {
        mContext = context;
        mRecipes = recipes;
        mListener = listener;
    }

    public RecipeAdapter(List<Recipe> recommendedRecipes) {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe currentRecipe = mRecipes.get(position);
        holder.textViewRecipeName.setText(currentRecipe.getName());
        holder.textViewRecipeDescription.setText(currentRecipe.getDescription());
        // Set click listeners for edit and delete buttons
        holder.buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onEditClick(currentRecipe);
            }
        });

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDeleteClick(currentRecipe);
            }
        });
    }

    @Override
    public int getItemCount() {

        if (mRecipes != null) {
            return mRecipes.size();
        } else {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewRecipeName;
        TextView textViewRecipeDescription;

        Button buttonEdit;
        Button buttonDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewRecipeName = itemView.findViewById(R.id.textViewRecipeName);
            textViewRecipeDescription = itemView.findViewById(R.id.textViewRecipeDescription);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
    // Interface for click listeners
    public interface OnRecipeClickListener {
        void onEditClick(Recipe recipe);
        void onDeleteClick(Recipe recipe);
    }
    // Method to update dataset and refresh RecyclerView
    public void updateData(List<Recipe> updatedRecipes) {
        mRecipes.clear();
        mRecipes.addAll(updatedRecipes);
        notifyDataSetChanged();
    }
}
