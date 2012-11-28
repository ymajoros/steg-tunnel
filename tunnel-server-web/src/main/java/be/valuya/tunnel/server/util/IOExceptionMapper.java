/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.tunnel.server.util;

import be.valuya.tunnel.util.TunnelError;
import java.io.IOException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Maps TunnelServerException's to a message.
 *
 * @author Yannick
 */
@Provider
public class IOExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<IOException> {

    @Override
    public Response toResponse(IOException ioException) {
        TunnelError tunnelError = TunnelError.IO;
        return toResponse(ioException, tunnelError);
    }
}
