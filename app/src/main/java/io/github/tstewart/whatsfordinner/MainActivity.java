package io.github.tstewart.whatsfordinner;

import androidx.appcompat.app.AppCompatActivity;
import io.github.tstewart.CalorieLookup.nutrients.Carbohydrates;
import io.github.tstewart.CalorieLookup.nutrients.Fat;
import io.github.tstewart.CalorieLookup.nutrients.Fiber;
import io.github.tstewart.CalorieLookup.nutrients.Protein;
import io.github.tstewart.NutritionCalculator.strategies.NutritionCalculationStrategy;
import io.github.tstewart.whatsfordinner.user.UserData;
import io.github.tstewart.whatsfordinner.util.ActivityHelper;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Launcher activity to access the features of the application.
 * Created by Thomas Stewart https://github.com/tstewart
 */
public class MainActivity extends AppCompatActivity {

    // Get preferences, and packageId to determine if this is the first time the app has been run from the user's phone
    SharedPreferences prefs = null;
    private final String packageId = "io.github.tstewart.whatsfordinner";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences(packageId, MODE_PRIVATE);

        setContentView(R.layout.activity_main);

        Button addFood = findViewById(R.id.addFood);
        addFood.setOnClickListener(view -> onAddFoodButtonClick());
        
        Button getRecipe = findViewById(R.id.getRecipe);
        getRecipe.setOnClickListener(view -> onGetRecipeButtonClick());

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

        // Calculate and show the nutrient requirements of the user.
        setNutrientRequirements();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setNutrientRequirements();

        // If the value firstrun is true, then show a welcome message.
        if (prefs.getBoolean("firstrun", true)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.first_welcome_msg)).show();

            // Set firstrun to false, so this welcome message never appears again.
            prefs.edit().putBoolean("firstrun", false).apply();
        }
    }

    private void onGetRecipeButtonClick() {
        ActivityHelper.launchActivity(this, GetRecipeActivity.class);
    }

    private void onAddFoodButtonClick() {
        ActivityHelper.launchActivity(this, AddFoodActivity.class);
    }


    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.topbar_with_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle settings button click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.settings) {
            Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
            MainActivity.this.startActivity(settings);
        }
        return super.onOptionsItemSelected(item);
    }

    // Set the user's required nutrients in the textviews of the main menu.
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
