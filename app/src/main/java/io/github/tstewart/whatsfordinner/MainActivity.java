package io.github.tstewart.whatsfordinner;

import androidx.appcompat.app.AppCompatActivity;
import io.github.tstewart.NutritionCalculator.strategies.NutritionCalculationStrategy;
import io.github.tstewart.whatsfordinner.user.UserData;
import io.github.tstewart.whatsfordinner.util.ActivityHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
            System.out.println(UserData.getInstance().getInfo().getUserNutrition().getCaloriesRequired());
            Toast.makeText(this, UserData.getInstance().getNutrition().getCaloriesRequired() + "", Toast.LENGTH_LONG).show();
        }
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

}
