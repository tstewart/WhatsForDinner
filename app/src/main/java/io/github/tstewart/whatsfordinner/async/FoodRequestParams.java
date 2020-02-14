package io.github.tstewart.whatsfordinner.async;

import io.github.tstewart.CalorieLookup.APIRequest;
import io.github.tstewart.CalorieLookup.edamam.EdamamConnection;

public class FoodRequestParams {
    private EdamamConnection edamamConnection;
    private APIRequest request;

    public FoodRequestParams(EdamamConnection edamamConnection, APIRequest request) {
        this.edamamConnection = edamamConnection;
        this.request = request;
    }

    public EdamamConnection getEdamamConnection() {
        return edamamConnection;
    }

    public void setEdamamConnection(EdamamConnection edamamConnection) {
        this.edamamConnection = edamamConnection;
    }

    public APIRequest getRequest() {
        return request;
    }

    public void setRequest(APIRequest request) {
        this.request = request;
    }
}
