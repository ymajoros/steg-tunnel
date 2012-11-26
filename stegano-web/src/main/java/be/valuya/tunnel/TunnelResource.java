/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.tunnel;

import com.sun.jersey.api.NotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * Tunnel Web Service
 *
 * @author Yannick
 */
@Stateless
@Path("/tn")
@Consumes({"application/xml", "application/json"})
@Produces({"application/xml", "application/json"})
public class TunnelResource {

    @EJB
    private ConnectionService connectionService;

    @PUT
    @Path("echo")
    public String echo(String input) {
        return input;
    }

    @GET
    @Path("cn/{host}/{port}/{ttl}")
    public String connect(@PathParam("host") String hostname, @PathParam("port") int port, @PathParam("ttl") long ttl) throws UnknownHostException, IOException {
        PersistentConnection persistentConnection = connectionService.createConnection(hostname, port, ttl);
        return persistentConnection.getId();
    }

    @PUT
    @Path("ko/{id}")
    public void disconnect(@PathParam("id") String id) throws IOException {
        PersistentConnection persistentConnection = connectionService.getConnection(id);
        if (persistentConnection != null) {
            connectionService.disconnect(persistentConnection);
        }
    }

    @PUT
    @Path("up/{id}")
    public void up(@PathParam("id") String id, byte[] input) throws IOException {
        PersistentConnection persistentConnection = getPersistentConnection(id);
        try {
            Socket socket = persistentConnection.getSocket();
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(input);
        } catch (SocketException socketException) {
            connectionService.disconnect(persistentConnection);
        }
    }

    @GET
    @Path("dn/{id}/{max}")
    public byte[] down(@PathParam("id") String id, @PathParam("max") int maxSize) throws IOException {
        PersistentConnection persistentConnection = getPersistentConnection(id);
        Socket socket = persistentConnection.getSocket();
        InputStream inputStream = socket.getInputStream();
        byte[] buffer = new byte[maxSize];
        try {
            int read = inputStream.read(buffer, 0, maxSize);
            if (read < 0) {
                connectionService.disconnect(persistentConnection);
                return null;
            }
            byte[] readBuffer = new byte[read];
            System.arraycopy(buffer, 0, readBuffer, 0, read);
            return readBuffer;
        } catch (SocketException socketException) {
            connectionService.disconnect(persistentConnection);
            return null;
        }
    }

    private PersistentConnection getPersistentConnection(String id) {
        PersistentConnection persistentConnection = connectionService.getConnection(id);
        if (persistentConnection == null) {
            String message = MessageFormat.format("Connection {0}", id);
            throw new NotFoundException(message);
        }
        return persistentConnection;
    }
}
