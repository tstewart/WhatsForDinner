package io.github.tstewart.whatsfordinner;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import io.github.tstewart.whatsfordinner.user.UserData;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;


public class IntroActivity extends Activity {

    final long INTRO_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intro);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
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