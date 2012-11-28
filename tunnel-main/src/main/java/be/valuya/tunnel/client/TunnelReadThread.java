/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.tunnel.client;

import be.valuya.tunnel.util.TunnelServerException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Yannick
 */
public class TunnelReadThread extends Thread {

    private static final Logger LOG = Logger.getLogger(TunnelReadThread.class.getName());
    private final TunnelRestClient tunnelClient;
    private final String connectionId;
    private final Socket socket;

    public TunnelReadThread(TunnelRestClient tunnelClient, String connectionId, Socket socket) {
        this.tunnelClient = tunnelClient;
        this.connectionId = connectionId;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            try {
                OutputStream outputStream = socket.getOutputStream();
                while (true) {
                    byte[] readBytes = null;
                    readBytes = tunnelClient.down(connectionId, 1024);
                    if (readBytes == null) {
                        socket.close();
                        tunnelClient.disconnect(connectionId);
                        break;
                    }
                    outputStream.write(readBytes);
                }
            } catch (TunnelServerException tunnelServerException) {
                String errorMessage = tunnelServerException.getMessage();
                String message = MessageFormat.format("Server read error for {0}: {1}", connectionId, errorMessage);
                LOG.log(Level.WARNING, message);
                socket.close();
                tunnelClient.disconnect(connectionId);
            }
        } catch (IOException ioException) {
            LOG.log(Level.WARNING, "Read error for " + connectionId, ioException);
        }
    }
}
