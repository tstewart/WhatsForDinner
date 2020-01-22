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

public class SignUpActivity extends AppCompatActivity {

    TextView ageInput;
    RadioGroup genderInput;
    TextView heightInput;
    TextView weightInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(this::onSubmitButtonClick);

        ageInput = findViewById(R.id.ageInput);
        genderInput = findViewById(R.id.genderInput);
        heightInput = findViewById(R.id.heightInput);
        weightInput = findViewById(R.id.weightInput);
    }

    public void onSubmitButtonClick(View view) {
        try {
            ActivityHelper.updateUserSettings(this, ageInput, genderInput, heightInput, weightInput);
            Intent mainActivity = new Intent(this, MainActivity.class);
            this.startActivity(mainActivity);
            this.finish();
        }
        catch(IllegalArgumentException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
