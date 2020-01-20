package io.github.tstewart.whatsfordinner;

import androidx.appcompat.app.AppCompatActivity;
import io.github.tstewart.NutritionCalculator.UserInfo;
import io.github.tstewart.whatsfordinner.user.UserData;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    TextView ageInput;
    RadioGroup genderInput;
    TextView heightInput;
    TextView weightInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button addFood = findViewById(R.id.submit_button);
        addFood.setOnClickListener(this::onSubmitButtonClick);

        ageInput = findViewById(R.id.ageInput);
        genderInput = findViewById(R.id.genderInput);
        heightInput = findViewById(R.id.heightInput);
        weightInput = findViewById(R.id.weightInput);
    }

    private void onSubmitButtonClick(View view) {

        String ageText = ageInput.getText().toString();
        String heightText = heightInput.getText().toString();
        String weightText = weightInput.getText().toString();

        if(ageText.length() == 0
                || genderInput.getCheckedRadioButtonId() == -1
                || heightText.length() == 0
                || weightText.length() == 0) {
            Toast.makeText(this, "Please enter all values.", Toast.LENGTH_SHORT).show();
            return;
        }

        int age;
        UserInfo.Gender gender;
        double height;
        double weight;

        try {
            age = Integer.parseInt(ageText);
        }
        catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid age.", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedButton = findViewById(genderInput.getCheckedRadioButtonId());

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
            Toast.makeText(this, "Please enter a valid height.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            weight = Double.parseDouble(weightText);
        }
        catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid weight.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            UserInfo user = new UserInfo(gender, age, weight, height);
            Toast.makeText(this, user.getGender() + " " + user.getAge() + " " + user.getHeight() + " " + user.getWeight(), Toast.LENGTH_SHORT).show();
        }
        catch (IllegalArgumentException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
