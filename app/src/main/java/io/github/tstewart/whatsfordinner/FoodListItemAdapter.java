package io.github.tstewart.whatsfordinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

import androidx.annotation.NonNull;
import io.github.tstewart.CalorieLookup.Food;
import io.github.tstewart.CalorieLookup.nutrients.Nutrient;

/**
 * Custom list view to display food data.
 * Created by Thomas Stewart https://github.com/tstewart
 */
class FoodListItemAdapter extends ArrayAdapter<Food> {

    // List of foods in the ListView
    private final ArrayList<Food> objects;

    /**
     * @param context Application context
     * @param resource Resource id of the ListView item
     * @param objects List of foods
     */
    public FoodListItemAdapter(@NonNull Context context, int resource, ArrayList<Food> objects) {
        super(context, resource, objects);
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        Food food = objects.get(position);

        if(v == null) {
            // Create an instance of the ListView item, set the view to be returned as this item.
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_view_food_item, null);
        }

        if(food != null) {
            // Set the list item details to that of the food.
            TextView title = v.findViewById(R.id.food_list_item_name);
            TextView brand = v.findViewById(R.id.food_list_item_brand);
            TextView calories = v.findViewById(R.id.food_list_item_calories);
            TextView nutrients = v.findViewById(R.id.food_list_item_nutrients);

            title.setText("Name: " + food.getFoodName());
            brand.setText("Brand: " + food.getBrandName());
            calories.setText("Calories: " + (int)Math.floor(food.getCalories()));
            nutrients.setText("Nutrients: " + getNutrientsAsString(food.getNutritionalInfo().iterator()));
        }

        return v;
    }

    /**
     * Convert the nutrients in an iterator to a string.
     * @param nutrients Iterator of nutrients to be converted
     * @return String containing the nutrients.
     */
    private String getNutrientsAsString(Iterator<Nutrient> nutrients) {
        StringBuilder nutrientString = new StringBuilder();

        while(nutrients.hasNext()) {
            Nutrient nutrient = nutrients.next();
            nutrientString.append(nutrient.getClass().getSimpleName()).append(": ").append((int)Math.floor(nutrient.getAmount()));

            if(nutrients.hasNext()) nutrientString.append(", ");

        }

        return nutrientString.toString();
    }
}
