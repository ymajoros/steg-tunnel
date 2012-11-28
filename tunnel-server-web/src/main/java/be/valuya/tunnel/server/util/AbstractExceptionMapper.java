/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.tunnel.server.util;

import be.valuya.tunnel.util.TunnelError;
import be.valuya.tunnel.util.TunnelErrorResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.ws.rs.core.Response;

/**
 *
 * @author Yannick
 */
public class AbstractExceptionMapper {

    protected String getStackTraceText(Exception exception) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);
        return stringWriter.toString();
    }

    protected Response toResponse(Exception exception, TunnelError tunnelError) {
        String message = exception.getMessage();
        String stackTraceText = getStackTraceText(exception);
        TunnelErrorResponse tunnelErrorResponse = new TunnelErrorResponse();
        tunnelErrorResponse.setTunnelError(tunnelError);
        tunnelErrorResponse.setMessage(message);
        tunnelErrorResponse.setStackTraceText(stackTraceText);
        return Response.status(502).entity(tunnelErrorResponse).build();
    }
}
