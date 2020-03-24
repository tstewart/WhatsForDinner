package io.github.tstewart.whatsfordinner.data;

import android.content.Context;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import io.github.tstewart.whatsfordinner.R;
import io.github.tstewart.whatsfordinner.user.UserData;

/**
 * Deserialize
 * Deserializes user data from a file.
 * Created by Thomas Stewart https://github.com/tstewart
 */
public class Deserialize {

    /**
     * Deserializes user data.
     * @param context Application context
     * @throws IOException Thrown if the file could not be found or opened
     * @throws ClassNotFoundException Thrown if the UserData object could not be instantiated.
     */
    public static void deserializeUser(Context context) throws IOException, ClassNotFoundException {

        String fileName = context.getResources().getString(R.string.user_data_file_location);

        FileInputStream fis = context.openFileInput(fileName);
        ObjectInputStream is = new ObjectInputStream(fis);
        UserData userData = (UserData) is.readObject();
        is.close();
        fis.close();
    }
}
