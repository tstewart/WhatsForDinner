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

/**
 * Activity utility class
 * Created by Thomas Stewart https://github.com/tstewart
 */
public class ActivityHelper {

    /**
     * Launch an activity from the context Activity.
     * @param context Context Activity
     * @param activityClass Activity to be opened.
     */
    public static void launchActivity(Context context, Class<? extends Activity> activityClass) {
        context.startActivity(new Intent(context, activityClass));
    }

    /**
     * Updates the user settings and saves them to file.
     * @param context Application context
     * Age, gender, weight, height inputs
     * @throws IllegalArgumentException If an input value is invalid.
     */
    public static void updateUserSettings(Activity context,
                                          TextView ageInput, RadioGroup genderInput,
                                          TextView heightInput, TextView weightInput) throws IllegalArgumentException {

        String ageText = ageInput.getText().toString();
        String heightText = heightInput.getText().toString();
        String weightText = weightInput.getText().toString();

        // If a value doesn't exist, throw an error
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

        // Try and parse the age, if the input is not a number, return an error.
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

        // Try and parse the height as a double.
        try {
            height = Double.parseDouble(heightText);
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Please enter a valid height");
        }

        // Try and parse the weight as a double.
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
