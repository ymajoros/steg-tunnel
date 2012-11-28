/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.tunnel.client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author Yannick
 */
public class TunnelReadThread extends Thread {

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
            OutputStream outputStream = socket.getOutputStream();
            while (true) {
                byte[] readBytes = null;
                readBytes = tunnelClient.down(connectionId, 1024);
                if (readBytes == null) {
                    socket.close();
                    break;
                }
                outputStream.write(readBytes);
            }
        } catch (IOException exception) {
            System.out.println("Read error");
        }
    }
}
