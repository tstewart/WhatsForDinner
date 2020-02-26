package io.github.tstewart.whatsfordinner;

import android.Manifest;
import android.annotation.SuppressLint;

import androidx.fragment.app.FragmentActivity;
import io.github.tstewart.whatsfordinner.data.Deserialize;
import io.github.tstewart.whatsfordinner.user.UserData;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;


public class IntroActivity extends FragmentActivity {

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        try {
                            File file = getFileStreamPath(getResources().getString(R.string.user_data_file_location));

                            if(file.canRead()) {
                                Deserialize.deserializeUser(this);
                            }
                            else {
                                System.err.println("User data file does not exist.");
                            }
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(this, "Write access is requiured to save user data.", Toast.LENGTH_LONG).show();
                    }
                });

        setContentView(R.layout.activity_intro);



        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        long INTRO_DISPLAY_LENGTH = 3000;
        new Handler().postDelayed(() -> {
            /* Create an Intent that will start the Menu-Activity. */
            if(UserData.getInstance().getInfo() != null) {
                Intent mainActivity = new Intent(IntroActivity.this, MainActivity.class);
                IntroActivity.this.startActivity(mainActivity);
            }
            else {
                Intent signUpIntent = new Intent(IntroActivity.this, SignUpActivity.class);
                IntroActivity.this.startActivity(signUpIntent);
            }
            IntroActivity.this.finish();
        }, INTRO_DISPLAY_LENGTH);
    }
}