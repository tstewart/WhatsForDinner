package io.github.tstewart.whatsfordinner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import androidx.appcompat.app.AppCompatActivity;
import io.github.tstewart.CalorieLookup.Recipe;
import io.github.tstewart.whatsfordinner.data.Serialize;
import io.github.tstewart.whatsfordinner.user.UserData;
import io.github.tstewart.whatsfordinner.util.ActivityHelper;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class RecipePopupActivity extends AppCompatActivity {

    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_popup);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            String personJsonString = String.valueOf(bundle.get("recipe"));

            if(personJsonString.length() > 0) {
                recipe = new Gson().fromJson(personJsonString, Recipe.class);

                TextView recipeTitle = findViewById(R.id.recipeTitle);
                TextView recipeLink = findViewById(R.id.recipeLink);
                ImageView recipeIcon = findViewById(R.id.suggestedRecipeImage);

                recipeTitle.setText(recipe.getName());
                recipeLink.setText(recipe.getRecipeUrl());

                // Download image from URL using Android Universal Image Loader
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.init(ImageLoaderConfiguration.createDefault(this.getApplicationContext()));
                imageLoader.displayImage(recipe.getIconUrl(), recipeIcon);
            }
        }

        if(recipe == null) {
            Toast.makeText(this, "Failed to open this recipe!", Toast.LENGTH_LONG).show();
            this.finish();
        }

        Button btnNewRecipe = findViewById(R.id.btnNewRecipe);
        btnNewRecipe.setOnClickListener(view -> onNewRecipeButtonClicked());

        Button btnConfirmRecipe = findViewById(R.id.btnRecipeSuccess);
        btnConfirmRecipe.setOnClickListener(view -> onConfirmRecipeClicked());

    }

    private void onConfirmRecipeClicked() {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            if(which == DialogInterface.BUTTON_POSITIVE) {
                UserData.getInstance().clearAllNutrients();
                UserData.getInstance().setRecentRecipe(recipe);
                try {
                    Serialize.serializeUser(this, UserData.getInstance());
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to save information information to file.", Toast.LENGTH_LONG).show();
                }

                ActivityHelper.launchActivity(this, MainActivity.class);
                this.finish();

            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.addRecipeConfirmation).setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void onNewRecipeButtonClicked() {
        this.finish();
    }
}
