package io.github.tstewart.whatsfordinner;

import androidx.appcompat.app.AppCompatActivity;
import io.github.tstewart.NutritionCalculator.UserInfo;
import io.github.tstewart.whatsfordinner.user.UserData;
import io.github.tstewart.whatsfordinner.util.ActivityHelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    TextView ageInput;
    RadioGroup genderInput;
    TextView heightInput;
    TextView weightInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(this::onUpdateButtonClicked);

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

    protected void onUpdateButtonClicked(View view) {
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
