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

public class RequestAsync extends AsyncTask<RequestParams, Void, JSONObject> {

    private Context context;
    private Exception e;

    public RequestAsync(Context context, FoodRequestResult result) {
        this.context = context;
        this.delegate = result;
    }

    public interface FoodRequestResult {
        void onReceiveResponse(JSONObject response);
    }
    @Override
    protected JSONObject doInBackground(RequestParams... requestParams) {
        Connection connection = requestParams[0].getConnection();
        APIRequest apiRequest = requestParams[0].getRequest();

        try {
            return connection.request(apiRequest);
        } catch (InvalidRequestException | InvalidResponseException e) {
            e.printStackTrace();
            this.e = e;
        }
        return null;
    }

    private FoodRequestResult delegate;


    public RequestAsync(FoodRequestResult delegate) {
        this.delegate = delegate;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        if(e != null) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        delegate.onReceiveResponse(jsonObject);
    }


}
