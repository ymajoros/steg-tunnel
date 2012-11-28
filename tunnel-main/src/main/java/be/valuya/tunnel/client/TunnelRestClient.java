/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.tunnel.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;

/**
 * Rest client to talk to tunnel server.
 *
 * @author Yannick
 */
public class TunnelRestClient extends TunnelRestClientBase {

    private WebResource tunnelWebResource;
    public volatile static int connectionCount = 0;
    private static final Logger LOG = Logger.getLogger(TunnelRestClient.class.getName());

    public TunnelRestClient(String tunnelServerUrl) {
        Client client = Client.create();
        tunnelWebResource = client.resource(tunnelServerUrl);
    }

    public String connect(String hostname, int port, long ttl) {
        String url = MessageFormat.format("/cn/{0}/{1,number,0}/{2,number,0}", hostname, port, ttl);
        WebResource connectWebResource = tunnelWebResource.path(url);
        String connectionId = getResource(connectWebResource, String.class);
        incrementConnectionCount();
        String message = MessageFormat.format("Connected {0}. Connection count: {1,number,0}", connectionId, connectionCount);
        LOG.log(Level.INFO, message);
        return connectionId;
    }

    public String disconnect(String connectionId) {
        String url = MessageFormat.format("/ko/{0}", connectionId);
        WebResource connectWebResource = tunnelWebResource.path(url);
        put(connectWebResource);
        decrementConnectionCount();
        String message = MessageFormat.format("Disconnected {0}. Connection count: {1,number,0}", connectionId, connectionCount);
        LOG.log(Level.INFO, message);
        return connectionId;
    }

    public void up(String connectionId, byte[] data) {
        String url = MessageFormat.format("/up/{0}", connectionId);
        WebResource upWebResource = tunnelWebResource.path(url);
        Builder builder = upWebResource.accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_JSON_TYPE);
        String message = MessageFormat.format("Up {0}, size= {1,number,0}", connectionId, data.length);
        LOG.log(Level.INFO, message);
        put(builder, data);
    }

    public byte[] down(String connectionId, int maxSize) {
        String url = MessageFormat.format("/dn/{0}/{1,number,0}", connectionId, maxSize);
        WebResource downWebResource = tunnelWebResource.path(url);
        try {
            byte[] byteArray = new byte[0];
            byte[] output = getResource(downWebResource, byteArray.getClass());
            int length = output == null ? -1 : output.length;
            String message = MessageFormat.format("Down {0}, size= {1,number,0}", connectionId, length);
            LOG.log(Level.INFO, message);
            return output;
        } catch (UniformInterfaceException uniformInterfaceException) {
            ClientResponse response = uniformInterfaceException.getResponse();
            Status status = response.getClientResponseStatus();
            if (status == Status.NO_CONTENT) {
                return null;
            } else {
                throw uniformInterfaceException;
            }
        }
    }

    private static void incrementConnectionCount() {
        connectionCount++;
    }

    private static void decrementConnectionCount() {
        connectionCount--;
    }
}
