/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.tunnel;

import be.valuya.stegano.SteganoInputStream;
import be.valuya.stegano.SteganoOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;

/**
 *
 * @author Yannick
 */
@WebServlet("/piednez")
public class SteganoTunnelServlet extends AbstractTunnelServlet {

    @Override
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        ServletInputStream servletInputStream = request.getInputStream();
        SteganoInputStream steganoInputStream = new SteganoInputStream(servletInputStream);

        ServletOutputStream servletOutputStream = response.getOutputStream();
        SteganoOutputStream steganoOutputStream = new SteganoOutputStream(servletOutputStream);

        Socket socket = createDestSocketFromRequest(request);

        tunnel(socket, steganoInputStream, steganoOutputStream);
    }
}
