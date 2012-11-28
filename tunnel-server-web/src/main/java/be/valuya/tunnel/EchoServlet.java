/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.tunnel;

import be.valuya.tunnel.util.StreamUtils;
import java.io.IOException;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;

@WebServlet("/echo")
public class EchoServlet extends AbstractTunnelServlet {

    @Override
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        ServletInputStream inputStream = request.getInputStream();
        ServletOutputStream outputStream = response.getOutputStream();
        AsyncContext asyncContext = request.startAsync();
        StreamUtils.copyStream(inputStream, outputStream);
    }
}
