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
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Activity to add a food to your eaten foods.
 * Created by Thomas Stewart https://github.com/tstewart
 */
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

        // Set the list view to the custom food list view to display the food information correctly.
        adapter = new FoodListItemAdapter(this.getApplicationContext(), R.layout.list_view_food_item, listItems);
        foodList.setAdapter(adapter);
    }

    private void onSearchButtonClicked() {

        // Clear any existing responses
        clearListResponses();

        String requestString = foodRequestInput.getText().toString();

        // If request string exists and only contains letters/spaces.
        if(!requestString.equals("") && requestString.matches("[a-zA-Z ]+")) {
            String appId = getResources().getString(R.string.food_appid);
            String appSecret = getResources().getString(R.string.food_appsecret);
            EdamamConnection connection = new EdamamConnection(appId, appSecret);

            // Easter egg response, intended as a joke for testers of the application.
            // Opens a link to Rick Astley - Never Gonna Give You Up
            if(requestString.equals("null") || requestString.equals("undefined")) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=dQw4w9WgXcQ"));
                startActivity(browserIntent);
                return;
            }

            FoodRequest request = new FoodRequest(requestString);
            APIRequest apiRequest = new APIRequest(request);

            // Display progress dialog while the app is searching for a JSON response.
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();

            new RequestAsync(this, response -> {
                EdamamJSONParser parser = new EdamamJSONParser();
                try {
                    if(response != null) {
                        // Parse JSON response
                        ArrayList<Food> foods = parser.parseFoodResponse(response);

                        if (foods != null && !foods.isEmpty()) {
                            for (int i = 0; i < foods.size(); i++) {
                                adapter.add(foods.get(i));
                            }
                            // Update the list with the new values
                            adapter.notifyDataSetChanged();
                        }
                        else {
                            Toast.makeText(this, "No foods could be found that matched this query.", Toast.LENGTH_SHORT).show();
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

    /**
     * Clear all items in the listView
     */
    private void clearListResponses() {
        adapter.clear();
        listItems.clear();
    }

    /**
     * Called when an item in the listview is clicked
     * @param position Position in the list
     */
    private void onItemClicked(int position) {

        final EditText input = new EditText(this);
        // Only allow number input
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setRawInputType(Configuration.KEYBOARD_12KEY);

        // Open dialog box, call listener on response.
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            if(which == DialogInterface.BUTTON_POSITIVE) {
                Food selectedFood = listItems.get(position);

                Iterator<Nutrient> nutrients = selectedFood.getNutritionalInfo().iterator();
                // Nutrients are added to this list, multiplied by the quantity.
                ArrayList<Nutrient> quantitativeNutrients = new ArrayList<>();

                String inputText = input.getText().toString();
                float quantity;

                try {
                    if(inputText.isEmpty()) quantity = 1;
                    else quantity = Float.parseFloat(inputText);
                }
                // If the quantity is invalid, alert the user
                catch(NumberFormatException e) {
                    Toast.makeText(this, "Please enter a valid quantity.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // If quantity is greater than 100, reject the request.
                if(quantity > 100) {
                    Toast.makeText(this, "Please enter a quantity lower than 100.", Toast.LENGTH_SHORT).show();
                    return;
                }

                while(nutrients.hasNext()) {
                        Nutrient nutrient = nutrients.next();
                        // Multiply nutrients by quantity
                        nutrient.setAmount(nutrient.getAmount() * quantity);
                        quantitativeNutrients.add(nutrient);
                }

                UserData.getInstance().addNutrients(quantitativeNutrients);
                UserData.getInstance().addCalories((int)Math.floor(selectedFood.getCalories() * quantity));

                try {
                    // Serialize user data to file.
                    Serialize.serializeUser(this, UserData.getInstance());
                } catch (IOException e) {
                    Toast.makeText(this, "Failed to save new food information to file.", Toast.LENGTH_LONG).show();
                }
                this.finish();
            }
        };

        // Alert dialog to be shown to the user.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.selectQuantity).setPositiveButton("Ok", dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener)
                .setView(input).show();
    }
}
