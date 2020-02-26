package io.github.tstewart.whatsfordinner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import io.github.tstewart.CalorieLookup.APIRequest;
import io.github.tstewart.CalorieLookup.Food;
import io.github.tstewart.CalorieLookup.edamam.EdamamConnection;
import io.github.tstewart.CalorieLookup.edamam.EdamamJSONParser;
import io.github.tstewart.CalorieLookup.nutrients.Nutrient;
import io.github.tstewart.CalorieLookup.request.FoodRequest;
import io.github.tstewart.whatsfordinner.async.RequestAsync;
import io.github.tstewart.whatsfordinner.async.RequestParams;
import io.github.tstewart.whatsfordinner.data.Serialize;
import io.github.tstewart.whatsfordinner.user.UserData;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class AddFoodActivity extends AppCompatActivity {

    private EditText foodRequestInput;

    private final ArrayList<Food> listItems = new ArrayList<>();

    private FoodListItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        foodRequestInput = findViewById(R.id.enterFoodInput);

        ListView foodList = findViewById(R.id.foodList);
        foodList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        foodList.setOnItemClickListener((adapterView, view, position, id) -> onItemClicked(position));

        Button searchButton = findViewById(R.id.btnSearch);
        searchButton.setOnClickListener(view -> onSearchButtonClicked());

        adapter = new FoodListItemAdapter(this.getApplicationContext(), R.layout.list_view_food_item, listItems);
        foodList.setAdapter(adapter);
    }

    private void onSearchButtonClicked() {

        clearListResponses();

        String requestString = foodRequestInput.getText().toString();

        // TODO add proper encoding of request
        if(!requestString.equals("") && requestString.matches("[a-zA-Z ]+")) {
            String appId = getResources().getString(R.string.food_appid);
            String appSecret = getResources().getString(R.string.food_appsecret);
            EdamamConnection connection = new EdamamConnection(appId, appSecret);

            if(requestString.equals("iloveyou")) {
                try {
                    ActionBar actionBar = getSupportActionBar();
                    actionBar.setTitle("I love you too! <3");

                    ColorDrawable colorDrawable
                            = new ColorDrawable(Color.parseColor("#FF69B4"));

                    actionBar.setBackgroundDrawable(colorDrawable);
                }
                catch(NullPointerException ignore) {}
                return;
            }
            else if(requestString.equals("null")) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=dQw4w9WgXcQ"));
                startActivity(browserIntent);
            }

            FoodRequest request = new FoodRequest(requestString);
            APIRequest apiRequest = new APIRequest(request);

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();

            new RequestAsync(this, response -> {
                EdamamJSONParser parser = new EdamamJSONParser();
                try {
                    if(response != null) {
                        ArrayList<Food> foods = parser.parseFoodResponse(response);
                        if (foods != null) {
                            for (int i = 0; i < foods.size(); i++) {
                                adapter.add(foods.get(i));
                            }
                            progressDialog.dismiss();
                            adapter.notifyDataSetChanged();
                        }
                    }
                    else {

                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }).execute(new RequestParams(connection, apiRequest));
        }
    }

    private void clearListResponses() {
        adapter.clear();
        listItems.clear();
    }

    private void onItemClicked(int position) {

        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            if(which == DialogInterface.BUTTON_POSITIVE) {
                Food selectedFood = listItems.get(position);

                Iterable<Nutrient> nutrients = selectedFood.getNutritionalInfo();

                UserData.getInstance().addNutrients(nutrients.iterator());
                UserData.getInstance().addCalories((int)Math.floor(selectedFood.getCalories()));

                try {
                    Serialize.serializeUser(this, UserData.getInstance());
                } catch (IOException e) {
                    Toast.makeText(this, "Failed to save new food information to file.", Toast.LENGTH_LONG).show();
                }
                this.finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.addToFoodsConfirmation).setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
}
