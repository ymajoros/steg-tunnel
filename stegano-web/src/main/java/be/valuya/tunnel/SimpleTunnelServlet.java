/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.tunnel;

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
@WebServlet("/dumbo")
public class SimpleTunnelServlet extends AbstractTunnelServlet {

    @Override
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        ServletInputStream servletInputStream = request.getInputStream();
        ServletOutputStream servletOutputStream = response.getOutputStream();

        Socket socket = createDestSocketFromRequest(request);
        tunnel(socket, servletInputStream, servletOutputStream);
    }
}
