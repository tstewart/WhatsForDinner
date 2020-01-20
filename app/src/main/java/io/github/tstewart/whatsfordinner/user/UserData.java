package io.github.tstewart.whatsfordinner.user;

import java.io.Serializable;

import io.github.tstewart.NutritionCalculator.UserInfo;
import io.github.tstewart.NutritionCalculator.UserNutrition;

public class UserData implements Serializable {
    private UserInfo info;
    private UserNutrition nutrition;

    public UserData(UserInfo info, UserNutrition nutrition) {
        this.info = info;
        this.nutrition = nutrition;
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
