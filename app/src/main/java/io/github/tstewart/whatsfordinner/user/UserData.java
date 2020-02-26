package io.github.tstewart.whatsfordinner.user;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import io.github.tstewart.CalorieLookup.Recipe;
import io.github.tstewart.CalorieLookup.nutrients.Nutrient;
import io.github.tstewart.NutritionCalculator.UserInfo;
import io.github.tstewart.NutritionCalculator.UserNutrition;

public class UserData implements Serializable {

    private static UserData instance;

    private HashMap<Class<? extends Nutrient>, Double> nutrientsEaten;
    private int caloriesEaten = 0;

    private UserInfo info;
    private Recipe recentRecipe;

    private UserData() {
        if(nutrientsEaten == null) nutrientsEaten = new HashMap<>();
    }

    public static UserData getInstance() {
        if(instance == null) instance = new UserData();
        return instance;
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        instance = this;
    }

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

    private void addNutrient(Nutrient nutrient) {
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
        // TODO delete
        Method method = null;
        Object userInfo = UserInfo.class;
        try {
            method = ((Class) userInfo).getDeclaredMethod("setUserNutrition", UserNutrition.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        method.setAccessible(true);
        try {
            Object r = method.invoke(userInfo, nutrition);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
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
