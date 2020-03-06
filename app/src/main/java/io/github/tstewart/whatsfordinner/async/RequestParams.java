package io.github.tstewart.whatsfordinner.async;

import io.github.tstewart.CalorieLookup.APIRequest;
import io.github.tstewart.CalorieLookup.Connection;
import io.github.tstewart.CalorieLookup.edamam.EdamamConnection;

public class RequestParams {
    private Connection connection;
    private APIRequest request;

    public RequestParams(Connection connection, APIRequest request) {
        this.connection = connection;
        this.request = request;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public APIRequest getRequest() {
        return request;
    }

    public void setRequest(APIRequest request) {
        this.request = request;
    }
}
