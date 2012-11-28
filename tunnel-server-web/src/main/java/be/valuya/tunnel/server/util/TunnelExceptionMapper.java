/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.tunnel.server.util;

import be.valuya.tunnel.util.TunnelServerException;
import be.valuya.tunnel.util.TunnelError;
import be.valuya.tunnel.util.TunnelErrorResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Maps TunnelServerException's to a message.
 *
 * @author Yannick
 */
@Provider
public class TunnelExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<TunnelServerException> {

    @Override
    public Response toResponse(TunnelServerException tunnelServerException) {
        TunnelError tunnelError = tunnelServerException.getTunnelError();
        return toResponse(tunnelServerException, tunnelError);
    }
}
