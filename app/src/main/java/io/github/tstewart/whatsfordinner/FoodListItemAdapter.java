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

class FoodListItemAdapter extends ArrayAdapter<Food> {

    private final ArrayList<Food> objects;

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
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_view_food_item, null);
        }

        if(food != null) {
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
