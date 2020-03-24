package io.github.tstewart.whatsfordinner;

import android.Manifest;
import android.annotation.SuppressLint;

import androidx.fragment.app.FragmentActivity;
import io.github.tstewart.whatsfordinner.data.Deserialize;
import io.github.tstewart.whatsfordinner.user.UserData;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;

/**
 * Activity to show attribution and allow the app to load data in the background
 * Created by Thomas Stewart https://github.com/tstewart
 */
public class IntroActivity extends FragmentActivity {

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check for external write permissions
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    // If permission granted by user, deserialize user data.
                    if (granted) {
                        try {
                            File file = getFileStreamPath(getResources().getString(R.string.user_data_file_location));

                            if(file.canRead()) {
                                Deserialize.deserializeUser(this);
                            }
                            else {
                                // If file doesn't exist or cant be read, show an error.
                                // This is not sent as a toast to the user as there are cases wherein the user data does not exist.
                                // E.g. Clearing cache, or on first start.
                                System.err.println("User data file does not exist.");
                            }
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // If write permission is not granted, remind the user that it is required.
                        Toast.makeText(this, "Write access is requiured to save user data.", Toast.LENGTH_LONG).show();
                    }
                });

        setContentView(R.layout.activity_intro);


        // Number of milliseconds the intro will be displayed for.
        final long INTRO_DISPLAY_LENGTH = 3000;

        // Handled once the time has allotted.
        new Handler().postDelayed(() -> {
            // If user data exists, take the user to the Main Activity
            if(UserData.getInstance().getInfo() != null) {
                Intent mainActivity = new Intent(IntroActivity.this, MainActivity.class);
                IntroActivity.this.startActivity(mainActivity);
            }
            // Else, take the user to the sign up page to input their details.
            else {
                Intent signUpIntent = new Intent(IntroActivity.this, SignUpActivity.class);
                IntroActivity.this.startActivity(signUpIntent);
            }
            IntroActivity.this.finish();
        }, INTRO_DISPLAY_LENGTH);
    }
}