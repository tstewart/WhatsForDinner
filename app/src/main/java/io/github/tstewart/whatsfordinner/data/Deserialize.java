package io.github.tstewart.whatsfordinner.data;

import android.content.Context;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import io.github.tstewart.whatsfordinner.R;
import io.github.tstewart.whatsfordinner.user.UserData;

public class Deserialize {

    public static void deserializeUser(Context context) throws IOException, ClassNotFoundException {

        String fileName = context.getResources().getString(R.string.user_data_file_location);

        FileInputStream fis = context.openFileInput(fileName);
        ObjectInputStream is = new ObjectInputStream(fis);
        UserData userData = (UserData) is.readObject();
        is.close();
        fis.close();
    }
}
