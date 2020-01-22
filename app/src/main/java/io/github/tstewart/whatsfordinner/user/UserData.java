package io.github.tstewart.whatsfordinner.user;

import java.io.Serializable;

import io.github.tstewart.NutritionCalculator.UserInfo;
import io.github.tstewart.NutritionCalculator.UserNutrition;

public class UserData implements Serializable {

    static final UserData instance = new UserData();

    private UserInfo info;
    private UserNutrition nutrition;

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
        return nutrition;
    }

    public void setNutrition(UserNutrition nutrition) {
        this.nutrition = nutrition;
    }
}
