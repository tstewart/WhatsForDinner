package io.github.tstewart.whatsfordinner.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import io.github.tstewart.NutritionCalculator.UserInfo;
import io.github.tstewart.whatsfordinner.data.Serialize;
import io.github.tstewart.whatsfordinner.user.UserData;

public class ActivityHelper {

    public static void launchActivity(Context context, Class<? extends Activity> activityClass) {
        context.startActivity(new Intent(context, activityClass));
    }

    public static void updateUserSettings(Activity context,
                                          TextView ageInput, RadioGroup genderInput,
                                          TextView heightInput, TextView weightInput) throws IllegalArgumentException {

        String ageText = ageInput.getText().toString();
        String heightText = heightInput.getText().toString();
        String weightText = weightInput.getText().toString();

        if(ageText.length() == 0
                || genderInput.getCheckedRadioButtonId() == -1
                || heightText.length() == 0
                || weightText.length() == 0) {
            throw new IllegalArgumentException("Please enter all values");
        }

        int age;
        UserInfo.Gender gender;
        double height;
        double weight;

        try {
            age = Integer.parseInt(ageText);
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Please enter a valid age");
        }

        RadioButton selectedButton = context.findViewById(genderInput.getCheckedRadioButtonId());

        if(selectedButton.getText().toString().equals("Male")) {
            gender = UserInfo.Gender.MALE;
        }
        else {
            gender = UserInfo.Gender.FEMALE;
        }

        try {
            height = Double.parseDouble(heightText);
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Please enter a valid height");
        }

        try {
            weight = Double.parseDouble(weightText);
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Please enter a valid weight");
        }

        UserInfo user = new UserInfo(gender, age, weight, height);
        UserData.getInstance().setInfo(user);

        // Save data to file
        try {
            Serialize.serializeUser(context, UserData.getInstance());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to save user data!", Toast.LENGTH_LONG).show();
        }
    }

}
