package io.github.tstewart.whatsfordinner.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class ActivityHelper {

    public static void launchActivity(Context context, Class<? extends Activity> activityClass) {
        context.startActivity(new Intent(context, activityClass));
    }

}
