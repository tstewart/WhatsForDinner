package io.github.tstewart.whatsfordinner.async;

import io.github.tstewart.CalorieLookup.APIRequest;
import io.github.tstewart.CalorieLookup.Connection;
import io.github.tstewart.CalorieLookup.edamam.EdamamConnection;

/**
 * RequestParams
 * Class to contain the connection and request objects for RequestAsyc
 * Created by Thomas Stewart https://github.com/tstewart
 */
public class RequestParams {
    // Connection object, contains the API url and the function to request a JSON response
    private Connection connection;
    // Request to be parsed when the connection function is called
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
