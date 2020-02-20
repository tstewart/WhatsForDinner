package io.github.tstewart.whatsfordinner;

import androidx.appcompat.app.AppCompatActivity;
import io.github.tstewart.CalorieLookup.APIRequest;
import io.github.tstewart.CalorieLookup.Food;
import io.github.tstewart.CalorieLookup.edamam.EdamamConnection;
import io.github.tstewart.CalorieLookup.edamam.EdamamJSONParser;
import io.github.tstewart.CalorieLookup.request.FoodRequest;
import io.github.tstewart.whatsfordinner.async.RequestAsync;
import io.github.tstewart.whatsfordinner.async.RequestParams;
import io.github.tstewart.whatsfordinner.data.Serialize;
import io.github.tstewart.whatsfordinner.user.UserData;
import io.github.tstewart.whatsfordinner.util.ActivityHelper;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class AddFoodActivity extends AppCompatActivity {

    EditText foodRequestInput;
    ListView foodList;

    ArrayList<Food> listItems = new ArrayList<>();

    ArrayAdapter<Food> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        foodRequestInput = findViewById(R.id.enterFoodInput);

        foodList = findViewById(R.id.foodList);
        foodList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        foodList.setOnItemClickListener(this::onItemClicked);

        Button searchButton = findViewById(R.id.btnSearch);
        searchButton.setOnClickListener(this::onSearchButtonClicked);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_selectable_list_item, listItems);
        foodList.setAdapter(adapter);
    }

    private void onSearchButtonClicked(View view) {

        clearListResponses();

        String requestString = foodRequestInput.getText().toString();

        // TODO add proper encoding of request
        if(!requestString.equals("") && requestString.matches("[a-zA-Z ]+")) {
            String appId = getResources().getString(R.string.food_appid);
            String appSecret = getResources().getString(R.string.food_appsecret);
            EdamamConnection connection = new EdamamConnection(appId, appSecret);

            FoodRequest request = new FoodRequest(requestString);
            APIRequest apiRequest = new APIRequest(request);

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();

            new RequestAsync(response -> {
                EdamamJSONParser parser = new EdamamJSONParser();
                try {
                    ArrayList<Food> foods = parser.parseFoodResponse(response);
                    if(foods != null) {
                        for(int i = 0; i < foods.size(); i++) {
                            adapter.add(foods.get(i));
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

    private void clearListResponses() {
        adapter.clear();
        listItems.clear();
    }

    private void onItemClicked(AdapterView<?> adapterView, View view, int position, long id) {

        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            if(which == DialogInterface.BUTTON_POSITIVE) {
                Food selectedFood = listItems.get(position);

                UserData.getInstance().addNutrients(selectedFood.getNutritionalInfo());
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
