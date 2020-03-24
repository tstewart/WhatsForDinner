package io.github.tstewart.whatsfordinner.user;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import io.github.tstewart.CalorieLookup.Recipe;
import io.github.tstewart.CalorieLookup.nutrients.Nutrient;
import io.github.tstewart.NutritionCalculator.UserInfo;
import io.github.tstewart.NutritionCalculator.UserNutrition;

/**
 * Stores user data as a singleton.
 * Created by Thomas Stewart https://github.com/tstewart
 */
public class UserData implements Serializable {

    // Instance of this class
    private static UserData instance;

    // Nutrients eaten already by the user.
    // Stored as a hashmap with references to the nutrient class and amount (in double)
    private HashMap<Class<? extends Nutrient>, Double> nutrientsEaten;

    // Calories already eaten by the user.
    private int caloriesEaten = 0;

    // User info (i.e. age, gender, weight, height)
    private UserInfo info;

    // The most recent recipe selected by the user on this app.
    // This is stored for users to access recipes again after closing the recipe lookup activity.
    private Recipe recentRecipe;

    // Private constructor, only callable to create singleton instance.
    private UserData() {
        if(nutrientsEaten == null) nutrientsEaten = new HashMap<>();
    }


    public static UserData getInstance() {
        // If singleton hasn't been instantiated
        if(instance == null) instance = new UserData();
        return instance;
    }

    // Used in serializing the object.
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
    }

    // Used in deserializing the object.
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        instance = this;
    }

    // Used in serializing the object.
    public Object readResolve() {
        nutrientsEaten = getNutrientsEaten();
        caloriesEaten = getCaloriesEaten();
        info = getInfo();
        return getInstance();
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

    /**
     * Add a nutrient to the HashMap
     * If it already exists, the amount is appended to the existing amount
     * @param nutrient Nutrient to be added
     */
    private void addNutrient(Nutrient nutrient) {
        // If the nutrient already exists in the HashMap
        if(nutrientsEaten.containsKey(nutrient.getClass())) {
            nutrientsEaten.put(nutrient.getClass(), nutrientsEaten.get(nutrient.getClass()) + nutrient.getAmount());
        }
        else {
            nutrientsEaten.put(nutrient.getClass(), nutrient.getAmount());
        }
    }

    // Add a list of nutrients to the HashMap
    public void addNutrients(ArrayList<Nutrient> nutrients) {
        for (int i = 0; i < nutrients.size(); i++) {
            addNutrient(nutrients.get(i));
        }
    }

    // Add a list of nutrients to the HashMap (in iterator format)
    public void addNutrients(Iterator<Nutrient> nutrients) {
        while(nutrients.hasNext()) {
            addNutrient(nutrients.next());
        }
    }

    // Get nutrient from the HashMap
    // If doesn't exist, return 0.
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

    public void clearAllNutrients() {
        this.nutrientsEaten.clear();
        this.caloriesEaten = 0;
    }

    public void setRecentRecipe(Recipe recipe) {
        this.recentRecipe = recipe;
    }

    public Recipe getRecentRecipe() {
        return recentRecipe;
    }
}
