package io.github.tstewart.whatsfordinner;

import androidx.appcompat.app.AppCompatActivity;
import io.github.tstewart.CalorieLookup.APIRequest;
import io.github.tstewart.CalorieLookup.Recipe;
import io.github.tstewart.CalorieLookup.edamam.EdamamConnection;
import io.github.tstewart.CalorieLookup.edamam.EdamamJSONParser;
import io.github.tstewart.CalorieLookup.request.RecipeRequest;
import io.github.tstewart.whatsfordinner.async.RequestAsync;
import io.github.tstewart.whatsfordinner.async.RequestParams;
import io.github.tstewart.whatsfordinner.user.UserData;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Activity to get a recipe using data from your eaten foods.
 * Created by Thomas Stewart https://github.com/tstewart
 */
public class GetRecipeActivity extends AppCompatActivity {

    private EditText recipeRequestInput;

    private final ArrayList<Recipe> listItems = new ArrayList<>();
    private ArrayAdapter<Recipe> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_recipe);

        recipeRequestInput = findViewById(R.id.preferredIngredientInput);

        ListView recipeList = findViewById(R.id.recipeList);
        recipeList.setOnItemClickListener((adapterView, view1, i, l) -> onItemClicked(i));

        Button searchButton = findViewById(R.id.btnSearch);
        searchButton.setOnClickListener(view -> onSearchButtonClicked());

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_selectable_list_item, listItems);
        recipeList.setAdapter(adapter);
    }

    private void onItemClicked(int i) {
        Intent mainActivity = new Intent(this, RecipePopupActivity.class);

        // Convert the recipe to a JSON format so that it can be passed to the new intent
        Gson gson = new Gson();

        Bundle args = new Bundle();
        args.putString("recipe", gson.toJson(listItems.get(i)));

        mainActivity.putExtras(args);

        this.startActivity(mainActivity);
    }

    private void onSearchButtonClicked() {

        // Clear existing responses
        clearListResponses();

        String requestString = recipeRequestInput.getText().toString();

        // If request string is not empty, and only contains accepted characters
        if(!requestString.equals("") && requestString.matches("[a-zA-Z ]+")) {
            String appId = getResources().getString(R.string.recipe_appid);
            String appSecret = getResources().getString(R.string.recipe_appsecret);
            EdamamConnection connection = new EdamamConnection(appId, appSecret);

            RecipeRequest request = new RecipeRequest(requestString, UserData.getInstance().getInfo(), UserData.getInstance().getNutrientsEaten(), UserData.getInstance().getCaloriesEaten());
            APIRequest apiRequest = new APIRequest(request);

            // Show progress dialog whilst contacting Edamam
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();

            new RequestAsync(this, response -> {
                EdamamJSONParser parser = new EdamamJSONParser();
                try {
                    if(response != null) {
                        ArrayList<Recipe> recipes = parser.parseRecipeResponse(response);
                        if (recipes != null && !recipes.isEmpty()) {
                            for (int i = 0; i < recipes.size(); i++) {
                                adapter.add(recipes.get(i));
                            }
                            // Update listview of responses
                            adapter.notifyDataSetChanged();
                        }
                        else {
                            Toast.makeText(this, "No recipes could be found that matched this query or that meet your nutritional requirements", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    progressDialog.dismiss();
                }
            }).execute(new RequestParams(connection, apiRequest));
        }
    }

    // Clear existing responses
    private void clearListResponses() {
        adapter.clear();
        listItems.clear();
    }
}
