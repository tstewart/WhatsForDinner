package io.github.tstewart.whatsfordinner.user;

import java.io.Serializable;
import java.util.ArrayList;

import io.github.tstewart.CalorieLookup.nutrients.Nutrient;
import io.github.tstewart.NutritionCalculator.UserInfo;
import io.github.tstewart.NutritionCalculator.UserNutrition;

public class UserData implements Serializable {

    private static final UserData instance = new UserData();

    private ArrayList<Nutrient> nutrientsEaten = new ArrayList<>();
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

    public void addNutrient(Nutrient nutrient) { this.nutrientsEaten.add(nutrient); }

    public void addNutrients(ArrayList<Nutrient> nutrients) {
        nutrientsEaten.addAll(nutrients);
    }

    public void addCalories(int caloriesEaten) { this.caloriesEaten += caloriesEaten; }

    public void setNutrition(UserNutrition nutrition) {
        info.setUserNutrition(nutrition);
    }
}
