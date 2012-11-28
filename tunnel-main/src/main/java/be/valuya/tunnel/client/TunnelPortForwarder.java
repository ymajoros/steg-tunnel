/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.tunnel.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Port forwarder, listen on a port and transmit input/output to/from tunnel.
 *
 * @author Yannick
 */
public class TunnelPortForwarder {

    private static final Logger LOG = Logger.getLogger(TunnelPortForwarder.class.getName());
    private TunnelClientParams tunnelClientParams;
    private ThreadGroup threadGroup;
    public volatile static int threadNr;

    public TunnelPortForwarder(TunnelClientParams tunnelClientParams) {
        this.tunnelClientParams = tunnelClientParams;
        threadGroup = new TunnelThreadGroup();
    }

    public void startServer() {
        try {
            int listenPort = tunnelClientParams.getListenPort();
            ServerSocket serverSocket = new ServerSocket(listenPort);
            System.out.println("Listening on port " + listenPort);
            while (true) {
                Socket socket = serverSocket.accept();
                serveSingleAsync(socket);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

    private void serveSingleAsync(final Socket socket) {
        String threadId = createThreadId();
        Thread thread = new Thread(threadGroup, threadId) {
            @Override
            public void run() {
                serveSingle(socket);
            }
        };
        thread.start();
    }

    private void serveSingle(Socket socket) {
        String tunnelServerUrl = tunnelClientParams.getTunnelServerUrl();
        String destHostName = tunnelClientParams.getDestHostName();
        int destPort = tunnelClientParams.getDestPort();
        long defaultTTL = tunnelClientParams.getDefaultTTL();
        final TunnelRestClient tunnelRestClient = new TunnelRestClient(tunnelServerUrl);
        final String connectionId = tunnelRestClient.connect(destHostName, destPort, defaultTTL);
        Thread tunnelReadThread = null;
        try {
            tunnelReadThread = startTunnelReadThread(tunnelRestClient, connectionId, socket);
            tunnelWriteWhileData(tunnelRestClient, connectionId, socket);
        } catch (IOException ioException) {
            LOG.log(Level.INFO, "Socket error, " + connectionId, ioException);
        } finally {
            if (tunnelReadThread != null) {
                tunnelReadThread.interrupt();
            }
            tunnelRestClient.disconnect(connectionId);
        }
    }

    private void tunnelWriteWhileData(final TunnelRestClient tunnelClient, final String connectionId, Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();

        byte[] buffer = new byte[1024];
        while (true) {
            int read = read(inputStream, buffer);
            if (read < 0) {
                break;
            }
            byte[] readBuffer = new byte[read];
            System.arraycopy(buffer, 0, readBuffer, 0, read);
            tunnelClient.up(connectionId, readBuffer);
        }
    }

    private Thread startTunnelReadThread(final TunnelRestClient tunnelClient, final String connectionId, Socket socket) {
        Thread thread = new TunnelReadThread(tunnelClient, connectionId, socket);
        thread.start();
        return thread;
    }

    private static String createThreadId() {
        return "tunnel-port-forwarder-" + threadNr++;
    }

    private int read(InputStream inputStream, byte[] buffer) throws IOException {
        try {
            int read = inputStream.read(buffer);
            return read;
        } catch (SocketException socketException) {
            // better check if other thread closed the socket; if not, throw anyway
            return -1;
        }
    }
}
