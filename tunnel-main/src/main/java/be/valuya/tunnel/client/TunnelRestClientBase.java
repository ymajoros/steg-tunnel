/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.tunnel.client;

import be.valuya.tunnel.util.TunnelError;
import be.valuya.tunnel.util.TunnelErrorResponse;
import be.valuya.tunnel.util.TunnelServerException;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

/**
 *
 * @author Yannick
 */
public class TunnelRestClientBase {

    protected <T> T getResource(WebResource connectWebResource, Class<T> resourceClass) {
        ClientResponse clientResponse = connectWebResource.get(ClientResponse.class);
        return handleResponse(clientResponse, resourceClass);
    }

    protected <U> void put(Builder builder, U requestEntity) {
        ClientResponse clientResponse = builder.put(ClientResponse.class, requestEntity);
        handleResponse(clientResponse, null);
    }

    protected <T, U> T put(Builder builder, Class<T> resourceClass, U requestEntity) {
        ClientResponse clientResponse = builder.put(ClientResponse.class, requestEntity);
        return handleResponse(clientResponse, resourceClass);
    }

    protected void put(WebResource connectWebResource) throws ClientHandlerException, UniformInterfaceException {
        ClientResponse clientResponse = connectWebResource.put(ClientResponse.class);
        handleResponse(clientResponse, null);
    }

    private <T> T handleResponse(ClientResponse clientResponse, Class<T> resourceClass) throws RuntimeException, ClientHandlerException, TunnelServerException, UniformInterfaceException {
        ClientResponse.Status clientResponseStatus = clientResponse.getClientResponseStatus();
        switch (clientResponseStatus) {
            case OK:
                if (resourceClass == null) {
                    return null;
                }
                T entity = clientResponse.getEntity(resourceClass);
                return entity;
            case NO_CONTENT:
                return null;
        }
        if (clientResponseStatus.getStatusCode() == 502) {
            TunnelErrorResponse tunnelErrorResponse = clientResponse.getEntity(TunnelErrorResponse.class);
            TunnelError tunnelError = tunnelErrorResponse.getTunnelError();
            String message = tunnelErrorResponse.getMessage();
            TunnelServerException tunnelServerException = new TunnelServerException(tunnelError, message);
            throw tunnelServerException;
        }
        throw new RuntimeException("Error, got http status: " + clientResponseStatus);
    }
}
