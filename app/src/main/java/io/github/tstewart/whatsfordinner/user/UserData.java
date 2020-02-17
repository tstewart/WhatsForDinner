package io.github.tstewart.whatsfordinner.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import io.github.tstewart.CalorieLookup.nutrients.Nutrient;
import io.github.tstewart.NutritionCalculator.UserInfo;
import io.github.tstewart.NutritionCalculator.UserNutrition;

public class UserData implements Serializable {

    private static final UserData instance = new UserData();

    private HashMap<Class<? extends Nutrient>, Double> nutrientsEaten = new HashMap<>();
    private int caloriesEaten = 0;

    private UserInfo info;

    private UserData() { }

    public static UserData getInstance() {
        return instance;
    }

    public UserInfo getInfo() {
        return info;
    }

    public void setInfo(UserInfo info) {
        this.info = info;
    }

    public UserNutrition getNutrition() {
        return info.getUserNutrition();
    }

    public void addNutrient(Nutrient nutrient) {
        if(nutrientsEaten.containsKey(nutrient.getClass())) {
            nutrientsEaten.put(nutrient.getClass(), nutrientsEaten.get(nutrient.getClass()) + nutrient.getAmount());
        }
        else {
            nutrientsEaten.put(nutrient.getClass(), nutrient.getAmount());
        }
    }

    public void addNutrients(ArrayList<Nutrient> nutrients) {
        for (int i = 0; i < nutrients.size(); i++) {
            addNutrient(nutrients.get(i));
        }
    }

    public double getNutrient(Class<? extends Nutrient> nutrientClass) {
        return nutrientsEaten.containsKey(nutrientClass) ? nutrientsEaten.get(nutrientClass) : 0.0;
    }

    public void addCalories(int caloriesEaten) { this.caloriesEaten += caloriesEaten; }

    public void setNutrition(UserNutrition nutrition) {
        info.setUserNutrition(nutrition);
    }

    public HashMap<Class<? extends Nutrient>, Double> getNutrientsEaten() {
        return nutrientsEaten;
    }

    public void setNutrientsEaten(HashMap<Class<? extends Nutrient>, Double> nutrientsEaten) {
        this.nutrientsEaten = nutrientsEaten;
    }

    public int getCaloriesEaten() {
        return caloriesEaten;
    }

    public void setCaloriesEaten(int caloriesEaten) {
        this.caloriesEaten = caloriesEaten;
    }
}
