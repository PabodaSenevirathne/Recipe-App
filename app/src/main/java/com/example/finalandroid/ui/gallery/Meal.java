package com.example.finalandroid.ui.gallery;

public class Meal {
    private String mealName;
    private String mealCategory;
    private String mealInstructions;

    public Meal(String mealName, String mealCategory, String mealInstructions) {
        this.mealName = mealName;
        this.mealCategory = mealCategory;
        this.mealInstructions = mealInstructions;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getMealCategory() {
        return mealCategory;
    }

    public void setMealCategory(String mealCategory) {
        this.mealCategory = mealCategory;
    }

    public String getMealInstructions() {
        return mealInstructions;
    }

    public void setMealInstructions(String mealInstructions) {
        this.mealInstructions = mealInstructions;
    }
}
