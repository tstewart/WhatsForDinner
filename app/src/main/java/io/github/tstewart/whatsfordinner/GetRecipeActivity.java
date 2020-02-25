package io.github.tstewart.whatsfordinner;

import androidx.appcompat.app.AppCompatActivity;
import io.github.tstewart.CalorieLookup.APIRequest;
import io.github.tstewart.CalorieLookup.Food;
import io.github.tstewart.CalorieLookup.Recipe;
import io.github.tstewart.CalorieLookup.edamam.EdamamConnection;
import io.github.tstewart.CalorieLookup.edamam.EdamamJSONParser;
import io.github.tstewart.CalorieLookup.request.FoodRequest;
import io.github.tstewart.CalorieLookup.request.RecipeRequest;
import io.github.tstewart.whatsfordinner.async.RequestAsync;
import io.github.tstewart.whatsfordinner.async.RequestParams;
import io.github.tstewart.whatsfordinner.user.UserData;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;

public class GetRecipeActivity extends AppCompatActivity {

    EditText recipeRequestInput;
    ListView recipeList;

    ArrayList<Recipe> listItems = new ArrayList<>();
    ArrayAdapter<Recipe> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_recipe);

        recipeRequestInput = findViewById(R.id.preferredIngredientInput);

        recipeList = findViewById(R.id.recipeList);
        recipeList.setOnItemClickListener(this::onItemClicked);

        Button searchButton = findViewById(R.id.btnSearch);
        searchButton.setOnClickListener(this::onSearchButtonClicked);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_selectable_list_item, listItems);
        recipeList.setAdapter(adapter);
    }

    private void onItemClicked(AdapterView<?> adapterView, View view, int i, long l) {
        Intent mainActivity = new Intent(this, RecipePopupActivity.class);

        // Convert the recipe to a JSON format so that it can be passed to the new intent
        Gson gson = new Gson();

        Bundle args = new Bundle();
        args.putString("recipe", gson.toJson(listItems.get(i)));

        mainActivity.putExtras(args);

        this.startActivity(mainActivity);
    }

    private void onSearchButtonClicked(View view) {


        String requestString = recipeRequestInput.getText().toString();

        if(!requestString.equals("") && requestString.matches("[a-zA-Z ]+")) {
            String appId = getResources().getString(R.string.recipe_appid);
            String appSecret = getResources().getString(R.string.recipe_appsecret);
            EdamamConnection connection = new EdamamConnection(appId, appSecret);

            RecipeRequest request = new RecipeRequest(requestString, UserData.getInstance().getInfo(), UserData.getInstance().getNutrientsEaten(), UserData.getInstance().getCaloriesEaten());
            APIRequest apiRequest = new APIRequest(request);

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();

            new RequestAsync(response -> {
                EdamamJSONParser parser = new EdamamJSONParser();
                try {
                    ArrayList<Recipe> recipes = parser.parseRecipeResponse(response);
                    if(recipes != null) {
                        for(int i = 0; i < recipes.size(); i++) {
                            adapter.add(recipes.get(i));
                        }
                        progressDialog.dismiss();
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }).execute(new RequestParams(connection, apiRequest));
        }
    }
}
