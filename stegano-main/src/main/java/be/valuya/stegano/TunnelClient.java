/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.stegano;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Yannick
 */
public class TunnelClient {

    private WebResource tunnelWebResource;
    private volatile static int connectionCount = 0;

    public TunnelClient() {
        Client client = Client.create();
        String mainUrl = "http://localhost:8080/tn/w/tn";
        tunnelWebResource = client.resource(mainUrl);
    }

    public String connect(String hostname, int port, long ttl) {
        String url = MessageFormat.format("/cn/{0}/{1,number,0}/{2,number,0}", hostname, port, ttl);
        WebResource connectWebResource = tunnelWebResource.path(url);
        String connectionId = connectWebResource.get(String.class);
        return connectionId;
    }

    public String disconnect(String connectionId) {
        String url = MessageFormat.format("/ko/{0}", connectionId);
        WebResource connectWebResource = tunnelWebResource.path(url);
        connectWebResource.put();
        return connectionId;
    }

    public void up(String connectionId, byte[] data) {
        String url = MessageFormat.format("/up/{0}", connectionId);
        WebResource upWebResource = tunnelWebResource.path(url);
        Builder builder = upWebResource.accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_JSON_TYPE);
        builder.put(data);
    }

    public byte[] down(String connectionId, int maxSize) {
        String url = MessageFormat.format("/dn/{0}/{1,number,0}", connectionId, maxSize);
        WebResource downWebResource = tunnelWebResource.path(url);
        byte[] byteArray = new byte[0];
        try {
            byte[] output = downWebResource.get(byteArray.getClass());
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

    private static void serveAsync(final Socket socket, final String hostname, final int port, final long ttl) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    serve(socket, hostname, port, ttl);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        thread.start();
    }

    private static void serve(Socket socket, String hostname, int port, long ttl) throws IOException {
        final TunnelClient tunnelClient = new TunnelClient();
        final String connectionId = tunnelClient.connect(hostname, port, ttl);
        System.out.println("Connection id:" + connectionId);
        InputStream inputStream = socket.getInputStream();
        final OutputStream outputStream = socket.getOutputStream();

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        byte[] readBytes = tunnelClient.down(connectionId, 1024);
                        if (readBytes == null) {
                            break;
                        }
                        outputStream.write(readBytes);
                    }
                } catch (IOException exception) {
                    throw new RuntimeException(exception);
                } finally {
                    tunnelClient.disconnect(connectionId);
                }
            }
        };
        thread.start();

        try {
            byte[] buffer = new byte[1024];
            while (true) {
                int read = inputStream.read(buffer);
                if (read < 0) {
                    tunnelClient.disconnect(connectionId);
                    break;
                }
                byte[] readBuffer = new byte[read];
                System.arraycopy(buffer, 0, readBuffer, 0, read);
                tunnelClient.up(connectionId, readBuffer);
            }
        } finally {
            tunnelClient.disconnect(connectionId);
        }
        thread.interrupt();
    }

    private static void test2() {
        try {
            String hostname = "music.valuya.be";
            int port = 80;
            long ttl = 1000 * 60 * 5;
            int listenPort = 1859;
            ServerSocket serverSocket = new ServerSocket(listenPort);
            System.out.println("Listening on port " + listenPort);
            while (true) {
                Socket socket = serverSocket.accept();
                serveAsync(socket, hostname, port, ttl);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

    private static void test1() {
        String hostname = "music.valuya.be";
        int port = 80;
        long ttl = 1000 * 60 * 1;
        TunnelClient tunnelClient = new TunnelClient();
        String connectionId = tunnelClient.connect(hostname, port, ttl);
        System.out.println("Connection id:" + connectionId);
        String dataStr =
                "GET / HTTP/1.1\n"
                + "host: www.google.be\n\n";
        Charset defaultCharset = Charset.defaultCharset();
        byte[] data = dataStr.getBytes(defaultCharset);
        tunnelClient.up(connectionId, data);
        byte[] output = tunnelClient.down(connectionId, 1024);
        String outputStr = new String(output, defaultCharset);
        System.out.println(outputStr);
    }

    public static void main(String... args) {
        test2();
    }
}
