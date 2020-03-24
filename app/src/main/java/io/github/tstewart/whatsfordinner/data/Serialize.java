package io.github.tstewart.whatsfordinner.data;

import android.content.Context;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import io.github.tstewart.whatsfordinner.R;
import io.github.tstewart.whatsfordinner.user.UserData;

/**
 * Serialize
 * Serializes user data to a file.
 * Created by Thomas Stewart https://github.com/tstewart
 */
public class Serialize {

    /**
     * @param context Application context. Required to establish file stream.
     * @param userData User data to be serialized.
     * @throws IOException If a connection could not be established to a file stream.
     */
    public static void serializeUser(Context context, UserData userData) throws IOException {
        String fileName = context.getResources().getString(R.string.user_data_file_location);

        FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(userData);
        os.close();
        fos.close();
    }

}
