package io.github.tstewart.whatsfordinner.async;

import android.os.AsyncTask;

import org.json.JSONObject;

import io.github.tstewart.CalorieLookup.APIRequest;
import io.github.tstewart.CalorieLookup.edamam.EdamamConnection;
import io.github.tstewart.CalorieLookup.error.APICallLimitReachedException;
import io.github.tstewart.CalorieLookup.error.InvalidRequestException;

public class FoodRequestAsync extends AsyncTask<FoodRequestParams, Void, JSONObject> {

    public interface FoodRequestResult {

        void onReceiveResponse(JSONObject response);
    }
    @Override
    protected JSONObject doInBackground(FoodRequestParams... foodRequestParams) {
        EdamamConnection connection = foodRequestParams[0].getEdamamConnection();
        APIRequest apiRequest = foodRequestParams[0].getRequest();

        try {
            return connection.request(apiRequest);
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (APICallLimitReachedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public FoodRequestResult delegate = null;


    public FoodRequestAsync(FoodRequestResult delegate) {
        this.delegate = delegate;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        delegate.onReceiveResponse(jsonObject);
    }

}
