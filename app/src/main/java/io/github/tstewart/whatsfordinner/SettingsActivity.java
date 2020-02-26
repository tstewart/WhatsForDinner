package io.github.tstewart.whatsfordinner;

import androidx.appcompat.app.AppCompatActivity;
import io.github.tstewart.CalorieLookup.Food;
import io.github.tstewart.NutritionCalculator.UserInfo;
import io.github.tstewart.whatsfordinner.data.Serialize;
import io.github.tstewart.whatsfordinner.user.UserData;
import io.github.tstewart.whatsfordinner.util.ActivityHelper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class SettingsActivity extends AppCompatActivity {

    private TextView ageInput;
    private RadioGroup genderInput;
    private TextView heightInput;
    private TextView weightInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(view -> onUpdateButtonClicked());

        Button resetNutritionButton = findViewById(R.id.reset_nutrition_button);
        resetNutritionButton.setOnClickListener(view -> onResetButtonClicked());

        ageInput = findViewById(R.id.ageInput);
        genderInput = findViewById(R.id.settings_genderInput);
        heightInput = findViewById(R.id.heightInput);
        weightInput = findViewById(R.id.weightInput);

        // Fill user information with current values
        UserInfo userInfo = UserData.getInstance().getInfo();
        ageInput.setText(Integer.toString(userInfo.getAge()));

        if(userInfo.getGender() == UserInfo.Gender.MALE) {
            RadioButton radioButton = findViewById(R.id.settings_gender_male);
            radioButton.setChecked(true);
        }
        else {
            RadioButton radioButton = findViewById(R.id.settings_gender_female);
            radioButton.setChecked(true);
        }

        heightInput.setText(Double.toString(userInfo.getHeight()));
        weightInput.setText(Double.toString(userInfo.getWeight()));
    }

    private void onResetButtonClicked() {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            if(which == DialogInterface.BUTTON_POSITIVE) {
                UserData.getInstance().clearAllNutrients();
                this.finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.resetNutritionConfirmation).setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void onUpdateButtonClicked() {
        try {
            ActivityHelper.updateUserSettings(this, ageInput, genderInput, heightInput, weightInput);
            Toast.makeText(this, "Information successfully updated!", Toast.LENGTH_SHORT).show();

            Intent mainActivity = new Intent(this, MainActivity.class);
            this.startActivity(mainActivity);
            this.finish();
        }
        catch(IllegalArgumentException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
