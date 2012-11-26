/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.tunnel;

import be.valuya.stegano.StreamUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;

/**
 *
 * @author Yannick
 */
public abstract class AbstractTunnelServlet extends HttpServlet {

    protected Socket createDestSocketFromRequest(ServletRequest request) throws IOException, NumberFormatException {
        String destHostStr = request.getParameter("dest_host");
        String destPortStr = request.getParameter("dest_port");
        int destPort = Integer.parseInt(destPortStr);
        Socket socket = new Socket(destHostStr, destPort);
        return socket;
    }

    protected void tunnel(Socket socket, InputStream inputStream, OutputStream outputStream) throws IOException {
        InputStream targetTunnelInputStream = socket.getInputStream();
        OutputStream targetTunnelOutputStream = socket.getOutputStream();

        Thread tunnelOutputCopyStreamThread = null;
        try {
            tunnelOutputCopyStreamThread = StreamUtils.copyStreamAsync(targetTunnelInputStream, outputStream);
            // copy tunnel output (an InputStream, something we read from) to unencoded, to-obfuscate output
            // copy decoded input to tunnel input (an OutputStream, something we write to!)
            StreamUtils.copyStream(inputStream, targetTunnelOutputStream);
        } finally {
            if (tunnelOutputCopyStreamThread != null) {
                tunnelOutputCopyStreamThread.interrupt();
            }
        }
    }
}
