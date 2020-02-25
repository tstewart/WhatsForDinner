package io.github.tstewart.whatsfordinner.async;

import io.github.tstewart.CalorieLookup.APIRequest;
import io.github.tstewart.CalorieLookup.edamam.EdamamConnection;

public class RequestParams {
    private EdamamConnection edamamConnection;
    private APIRequest request;

    public RequestParams(EdamamConnection edamamConnection, APIRequest request) {
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
