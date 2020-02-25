package io.github.tstewart.whatsfordinner;

import androidx.appcompat.app.AppCompatActivity;
import io.github.tstewart.CalorieLookup.nutrients.Carbohydrates;
import io.github.tstewart.CalorieLookup.nutrients.Fat;
import io.github.tstewart.CalorieLookup.nutrients.Fiber;
import io.github.tstewart.CalorieLookup.nutrients.Protein;
import io.github.tstewart.NutritionCalculator.strategies.NutritionCalculationStrategy;
import io.github.tstewart.whatsfordinner.user.UserData;
import io.github.tstewart.whatsfordinner.util.ActivityHelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addFood = findViewById(R.id.addFood);
        addFood.setOnClickListener(this::onAddFoodButtonClick);
        
        Button getRecipe = findViewById(R.id.getRecipe);
        getRecipe.setOnClickListener(this::onGetRecipeButtonClick);

        if(UserData.getInstance().getInfo() != null) {
            NutritionCalculationStrategy strategy = new NutritionCalculationStrategy();
            strategy.calculateNutritionalInformation(UserData.getInstance().getInfo());
        }

        if(UserData.getInstance().getRecentRecipe() != null) {
            TextView recipeTitle = findViewById(R.id.recentRecipeTitle);
            TextView recipeLink = findViewById(R.id.recentRecipeLink);

            recipeTitle.setVisibility(View.VISIBLE);
            recipeLink.setText(UserData.getInstance().getRecentRecipe().getRecipeUrl());
        }

        setNutrientRequirements();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setNutrientRequirements();
    }

    private void onGetRecipeButtonClick(View view) {
        ActivityHelper.launchActivity(this, GetRecipeActivity.class);
    }

    private void onAddFoodButtonClick(View view) {
        ActivityHelper.launchActivity(this, AddFoodActivity.class);
    }


    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.topbar_with_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.settings) {
            Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
            MainActivity.this.startActivity(settings);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setNutrientRequirements() {

        UserData instance = UserData.getInstance();

        TextView calories = findViewById(R.id.nutrientCalorie);
        TextView fat = findViewById(R.id.nutrientFat);
        TextView fiber = findViewById(R.id.nutrientFiber);
        TextView carbohydrates = findViewById(R.id.nutrientCarb);
        TextView protein = findViewById(R.id.nutrientProtein);

        calories.setText(String.format(getString(R.string.caloriePlaceholder), instance.getCaloriesEaten(), instance.getNutrition().getCaloriesRequired()));
        fat.setText(String.format(getString(R.string.nutritionPlaceholder), instance.getNutrient(Fat.class), instance.getNutrition().getFatRequired()));
        fiber.setText(String.format(getString(R.string.nutritionPlaceholder), instance.getNutrient(Fiber.class), instance.getNutrition().getFiberRequired()));
        carbohydrates.setText(String.format(getString(R.string.nutritionPlaceholder), instance.getNutrient(Carbohydrates.class), instance.getNutrition().getCarbohydratesRequired()));
        protein.setText(String.format(getString(R.string.nutritionPlaceholder), instance.getNutrient(Protein.class), instance.getNutrition().getProteinRequired()));

    }

}
