package io.github.tstewart.whatsfordinner.async;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import io.github.tstewart.CalorieLookup.APIRequest;
import io.github.tstewart.CalorieLookup.Connection;
import io.github.tstewart.CalorieLookup.edamam.EdamamConnection;
import io.github.tstewart.CalorieLookup.error.InvalidRequestException;
import io.github.tstewart.CalorieLookup.error.InvalidResponseException;
import io.github.tstewart.CalorieLookup.request.FoodRequest;

/**
 * RequestAsync
 * Class to request JSON from a server asynchronously.
 * Created by Thomas Stewart https://github.com/tstewart
 */
public class RequestAsync extends AsyncTask<RequestParams, Void, JSONObject> {

    // Activity context
    private Context context;

    // Exception if one is thrown while requesting
    private Exception e;

    /**
     * Main constructor for RequestAsync
     * @param context The Activity context, used to alert the user of errors via toasts
     * @param result Delegated result function, called after the result has been obtained from the server
     */
    public RequestAsync(Context context, FoodRequestResult result) {
        this.context = context;
        this.delegate = result;
    }

    // Called on completion of the async task
    public interface FoodRequestResult {
        // Sends the object response back to the caller
        void onReceiveResponse(JSONObject response);
    }

    /**
     * Function that is performed on a different thread.
     * @param requestParams Parameters for the Edamam request
     * @return A JSONObject with the response from the Edamam server.
     */
    @Override
    protected JSONObject doInBackground(RequestParams... requestParams) {
        // Get the parameters from the requestparams object.
        Connection connection = requestParams[0].getConnection();
        APIRequest apiRequest = requestParams[0].getRequest();

        try {
            // Try and return the result from Edamam
            return connection.request(apiRequest);
        } catch (InvalidRequestException | InvalidResponseException e) {
            e.printStackTrace();
            // Error is assigned to be handled when the request has finished executing
            this.e = e;
        }
        // Return null if an error occurs
        return null;
    }

    private FoodRequestResult delegate;


    public RequestAsync(FoodRequestResult delegate) {
        this.delegate = delegate;
    }

    /**
     * Called when the doInBackground function has finished.
     * @param jsonObject JSON object returned from the Edamam server (if exists).
     */
    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        // If an error was found, show it to the user via a toast.
        if(e != null) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        // Delegate function is called
        delegate.onReceiveResponse(jsonObject);
    }


}
